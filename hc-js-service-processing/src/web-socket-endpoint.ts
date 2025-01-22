import { eval_, util } from "@dev.hiconic/tf.js_hc-js-api";
import { ServiceRequest } from "@dev.hiconic/gm_service-api-model";

export type WsServiceEndpointProps = {
    sessionId?: string;
    onChannelIdAssigned?: (channelId: string) => void;
}

export class WebSocketServiceEndpoint {

    readonly #evaluator: eval_.Evaluator<ServiceRequest>;
    readonly #url: string;
    readonly #clientId: string;
    readonly #optionalProps: WsServiceEndpointProps;

    #webSocket?: WebSocket;

    #channelId?: string;

    constructor(evaluator: eval_.Evaluator<ServiceRequest>, url: string, clientId: string, optionalProps: WsServiceEndpointProps = {}) {
        this.#evaluator = evaluator;
        this.#url = url;
        this.#clientId = clientId;
        this.#optionalProps = optionalProps;
    }

    webSocket(): WebSocket | undefined {
        return this.#webSocket;
    }

    channelId1(): string | undefined {
        return this.#channelId;
    }

    close() {
        if (this.#webSocket) {
            this.#webSocket.close();
            this.#webSocket = undefined;
        }
    }

    // returns the first channelId
    async openWebSocketWithChannleId(): Promise<string> {
        const wsUrl = this.wsUrl();
        console.log("WS URL: " + wsUrl);

        this.#webSocket = new WebSocket(wsUrl);

        let promiseHandled = false;
        let resolve: any
        let reject: any;

        const promise = new Promise<string>((res, rej) => {
            resolve = res;
            reject = rej;
        })

        this.#webSocket.onopen = () => {
            console.log("WebSocket connection established to: " + wsUrl);
        }
        this.#webSocket.onerror = error => {
            console.error("Error with WebSocket connection to: " + wsUrl, error);
            if (!promiseHandled) {
                reject(error);
                promiseHandled = true;
            }
        }
        this.#webSocket.onclose = closeEvent => {
            console.log(`Lost WebSocket connection to: ${wsUrl}. Reason: ${closeEvent.reason}`);
            if (!promiseHandled) {
                reject(closeEvent.reason);
                promiseHandled = true;
            }
        }
        this.#webSocket.onmessage = messageEvent => {
            if (promiseHandled) {
                console.log("Unexpected: WebSocket has already been established or failed.");
                return;
            }

            this.#channelId = messageEvent.data;
            console.info("Established WebSocket connection with channelId: " + this.#channelId);

            this.#webSocket!.onmessage = messageEvent => this.handleWsMessage(messageEvent)
                
            resolve(this.#channelId!);
            this.#optionalProps?.onChannelIdAssigned?.(this.#channelId!);

            promiseHandled = true;
        }

        return promise;
    }

    private wsUrl(): string {
        const params = new URLSearchParams();
        params.set("clientId", this.#clientId);
        params.set("sendChannelId", "true");
        params.set("accept", "gm/jse");

        if (this.#optionalProps.sessionId)
            params.set("sessionId", this.#optionalProps.sessionId);

        return this.#url + "?" + params.toString();
    }

    private async handleWsMessage(messageEvent: MessageEvent<any>): Promise<void> {
        const entity = await util.decodeJse(messageEvent.data);
        if (ServiceRequest.isInstance(entity)) {
            const maybeResult = await (entity as ServiceRequest).EvalAndGetReasoned(this.#evaluator);
            if (!maybeResult.isSatisfied())
                console.error(
                    "[ERROR] Processing PushNotification failed because: " +
                    maybeResult.whyUnsatisfied().text + ". Request:", entity);
        } else {
            console.log("[WARNING] Received push notification that is not a ServiceRequest: " + entity);
        }
    }
}

