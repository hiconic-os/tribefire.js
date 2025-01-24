import { async, attr, eval_, hc, reason, reflection, service, util } from "@dev.hiconic/tf.js_hc-js-api";
import { DispatchableRequest, Neutral, ServiceRequest } from "@dev.hiconic/gm_service-api-model";
import { Reason } from "@dev.hiconic/gm_gm-core-api";
import { UnsupportedOperation } from "@dev.hiconic/gm_essential-reason-model";


export import Maybe = reason.Maybe;
export import EntityType = reflection.EntityType;
export import JsEvalContext = eval_.JsEvalContext;
export import Evaluator = eval_.Evaluator;
export import AttributeContext = attr.AttributeContext;
export import AttributeContexts = attr.AttributeContexts;
export import TypeSafeAttribute = attr.TypeSafeAttribute;
export import TypeSafeAttributeEntry = attr.TypeSafeAttributeEntry;

export { Neutral, ServiceRequest };

export type ServiceProcessorFunction<SR extends ServiceRequest, R> =
    (context: service.ServiceRequestContext, request: SR) => Promise<Maybe<R>>;

export interface ServiceProcessor<SR extends ServiceRequest, R> {
    process(context: service.ServiceRequestContext, request: SR): Promise<Maybe<R>>;
}

export interface ServiceProcessorBinder {
    /** Binds a service processsor for given ServiceRequest type */
    bind<T extends ServiceRequest>(
        requestType: EntityType<T>, processor: ServiceProcessor<T, any>): ServiceProcessorBinding;
    /** Binds a service processsor for given ServiceRequest type */
    bindFunction<T extends ServiceRequest>(
        requestType: EntityType<T>, processor: ServiceProcessorFunction<T, any>): ServiceProcessorBinding;

    /** Binds a service processsor for given ServiceRequest type and serviceId. */
    bindDispatching<T extends DispatchableRequest>(
        requestType: EntityType<T>, serviceId: string, processor: ServiceProcessor<T, any>): ServiceProcessorBinding;
    /** Binds a service processsor for given ServiceRequest type and serviceId. */
    bindDispatchingFunction<T extends DispatchableRequest>(
        requestType: EntityType<T>, serviceId: string, processor: ServiceProcessorFunction<T, any>): ServiceProcessorBinding;
}

export interface ServiceProcessorBinding {
    unbind(): void;
}

export class LocalEvaluator extends eval_.EmptyEvaluator<ServiceRequest> implements ServiceProcessorBinder {
    readonly #processors = util.newDenotationMap<ServiceRequest, ServiceProcessor<ServiceRequest, any>>();
    readonly #dispatchingRegistries = new Map<string, DispatchingRegistry>();

    eval<T>(request: ServiceRequest): JsEvalContext<T> {
        return new LocalEvalContext(this, request);
    }

    /*
     * ServiceProcessorBinder
     */

    bindFunction<T extends ServiceRequest>(
        denotationType: EntityType<T>,
        processor: ServiceProcessorFunction<T, any>
    ): ServiceProcessorBinding {

        return this.bind(denotationType, this.serviceProcessor(processor));
    }

    bind<T extends ServiceRequest>(
        denotationType: EntityType<T>,
        processor: ServiceProcessor<T, any>
    ): ServiceProcessorBinding {

        this.#processors.put(denotationType, processor);

        return {
            unbind: () => this.#processors.remove(denotationType)
        }
    }

    bindDispatchingFunction<T extends DispatchableRequest>(
        denotationType: EntityType<T>,
        serviceId: string,
        processor: (context: service.ServiceRequestContext, request: T) => Promise<Maybe<any>>
    ): ServiceProcessorBinding {

        return this.bindDispatching(denotationType, serviceId, this.serviceProcessor(processor));
    }

    bindDispatching<T extends DispatchableRequest>(
        denotationType: EntityType<T>,
        serviceId: string,
        processor: ServiceProcessor<T, any>
    ): ServiceProcessorBinding {

        let dispatchingRegistry = this.acquireDispatchingRegistry(serviceId);
        if (!dispatchingRegistry)
            this.#dispatchingRegistries.set(serviceId, dispatchingRegistry = new DispatchingRegistry());

        dispatchingRegistry.bind(denotationType, processor);

        return {
            unbind: () => {
                dispatchingRegistry.processors.remove(denotationType);
                if (dispatchingRegistry.processors.isEmpty())
                    this.#dispatchingRegistries.delete(serviceId);
            }
        }
    }

    private serviceProcessor<SR extends ServiceRequest, R>(
        lambda: ServiceProcessorFunction<SR, R>
    ): ServiceProcessor<SR, R> {
        return { process: (ctx, req) => lambda(ctx, req) }
    }

    private acquireDispatchingRegistry(serviceId: string): DispatchingRegistry {
        let dispatchingRegistry = this.#dispatchingRegistries.get(serviceId);
        if (!dispatchingRegistry)
            this.#dispatchingRegistries.set(serviceId, dispatchingRegistry = new DispatchingRegistry());
        return dispatchingRegistry;
    }

    resolveProcessor(request: ServiceRequest): Maybe<ServiceProcessor<ServiceRequest, any>> {
        let result: ServiceProcessor<ServiceRequest, any> | null = null;
        if (DispatchableRequest.isInstance(request))
            result = this.resolveDispatchingProcessor(request as DispatchableRequest);

        if (!result)
            result = this.#processors.find(request);

        return result ? Maybe.complete(result) : Maybe.empty(this.noProcessorFoundFor(request));
    }

    private resolveDispatchingProcessor(request: DispatchableRequest): ServiceProcessor<ServiceRequest, any> | null {
        if (!request.serviceId)
            return null;

        const registry = this.#dispatchingRegistries.get(request.serviceId);
        if (!registry)
            return null;

        return registry.processors.find(request);
    }

    private noProcessorFoundFor(request: ServiceRequest): Reason {
        let msg = "No ServiceProcessor found for request of type [" + request.EntityType().getTypeSignature() + "]";
        if (DispatchableRequest.isInstance(request)) {
            const dr = request as DispatchableRequest;
            if (dr.serviceId)
                msg += " with serviceId [" + (request as DispatchableRequest).serviceId + "]";
            else
                msg += ", a DispatchableRequest with no serviceId (maybe that's the problem?)";
        }

        msg += " -- Request: " + request;

        return reason.build(UnsupportedOperation).text(msg).toReason();
    }
}

class DispatchingRegistry {
    readonly processors = util.newDenotationMap<ServiceRequest, ServiceProcessor<ServiceRequest, any>>();

    bind<T extends ServiceRequest>(
        denotationType: EntityType<T>,
        processor: ServiceProcessor<ServiceRequest, any>
    ): void {

        this.processors.put(denotationType, processor);
    }
}

// Just some boilerplate, wihout this TSC complains about overrides for methods from JsEvalContext
interface LocalEvalContextHelper<T> extends eval_.EmptyEvalContext<T>, JsEvalContext<T> { }
class LocalEvalContextHelper<T> { }

class LocalEvalContext<T> extends LocalEvalContextHelper<T> {

    readonly #localEvaluator: LocalEvaluator;
    readonly #request: ServiceRequest;
    readonly #mapAttrContext = new attr.MapAttributeContext(null!);

    constructor(evaluator: LocalEvaluator, request: ServiceRequest) {
        super();
        this.#localEvaluator = evaluator;
        this.#request = request;
    }

    override async andGet(): Promise<T> {
        const maybe = await this.andGetReasoned();
        return maybe.get();
    }

    override andGetReasoned(): Promise<Maybe<T>> {
        const maybeProcessor = this.#localEvaluator.resolveProcessor(this.#request);

        if (!maybeProcessor.isSatisfied())
            return Promise.resolve(maybeProcessor.cast());

        const processor = maybeProcessor.get();
        const context = this.newServiceRequestContext();

        return processor.process(context, this.#request);
    }

    override getReasoned(callback: async.AsyncCallback<Maybe<T>>): void {
        this.andGetReasoned()
            .then(value => callback.onSuccess(value))
            .catch(e => callback.onFailure(e))
    }

    override get(callback: async.AsyncCallback<T>): void {
        this.andGet()
            .then(value => callback.onSuccess(value))
            .catch(e => callback.onFailure(e))
    }

    private newServiceRequestContext(): service.ServiceRequestContext {
        const context = new service.StandardServiceRequestContext(this.parentAttributeContext(), this.#localEvaluator);
        context.setEvaluator(this.#localEvaluator);

        for (const entry of this.streamAttributes().iterable())
            context.setAttribute(entry.attribute(), entry.value());

        return context;
    }

    private parentAttributeContext(): AttributeContext {
        const pac = eval_.ParentAttributeContextAspect.$.find(this);
        return pac.isPresent() ? pac.get() as AttributeContext : AttributeContexts.peek();
    }

    override getReasonedSynchronous(): Maybe<T> {
        throw new Error("Synchronous evaluation is not supported!");
    }

    override getSynchronous(): T {
        throw new Error("Synchronous evaluation is not supported!");
    }

    override setAttribute<A extends TypeSafeAttribute<V>, V>(attribute: hc.Class<A>, value: V): void {
        this.#mapAttrContext.setAttribute(attribute, value);
    }

    override findAttribute<A extends TypeSafeAttribute<V>, V>(attribute: hc.Class<A>): hc.Optional<V> {
        return this.#mapAttrContext.findAttribute(attribute);
    }

    override getAttribute<A extends TypeSafeAttribute<V>, V>(attribute: hc.Class<A>): V {
        return this.#mapAttrContext.getAttribute(attribute);
    }

    override findOrDefault<A extends TypeSafeAttribute<V>, V>(attribute: hc.Class<A>, defaultValue: V): V {
        return this.#mapAttrContext.findOrDefault(attribute, defaultValue);
    }

    override findOrNull<A extends TypeSafeAttribute<V>, V>(attribute: hc.Class<A>): V {
        return this.#mapAttrContext.findOrNull(attribute);
    }

    override findOrSupply<A extends TypeSafeAttribute<V>, V>(attribute: hc.Class<A>, defaultValueSupplier: hc.Supplier<V>): V {
        return this.#mapAttrContext.findOrSupply(attribute, defaultValueSupplier);
    }

    override streamAttributes(): hc.Stream<TypeSafeAttributeEntry> {
        return this.#mapAttrContext.streamAttributes();
    }

}

// for local-only case (for now just push notifications) we set a RequestedEndpointAspect to imply no fallback via remotifier is desired
// otherwise, build an evaluator that tries locally first, if no processor found, either return reason (in case push notification) or


