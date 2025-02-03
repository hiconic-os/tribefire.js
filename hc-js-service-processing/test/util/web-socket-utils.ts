export type PromiseResolve<T> = (value: T | PromiseLike<T>) => void;

export class WebSocketAwaiters {
    openAwaiter = new Awaiter<void>();
    messageAwaiter = new Awaiter<void>();
    closeAwaiter = new Awaiter<void>();
    errorAwaiter = new Awaiter<void>();
}

/**
 * This can be used to give a name to a Promise, await it on one place and resolve it on another.
 * It's useful to bind it to event handler, thus easily checking values of events in tests.
 * 
 * @example:
 * myValueAwaiter= new Awaiter<string>()
 *
 * testInitialization() {
 *   someObject.onValueChanged = (value: string) => myValueAwaiter.resolve(value)
 * }
 *
 * testExecution() {
 *   triggerSomething(); // this leads to 
 *   const str1 = await myValueAwaiter.await();
 *   // expect str1
 *
 *   triggerSomethingElse();
 *   const str2 = await myValueAwaiter.await();
 *   // expect str2
 * }
 */
export class Awaiter<T> {
    promise: Promise<T>;
    resolve: PromiseResolve<T>;

    constructor() {
        [this.promise, this.resolve] = newPromise();
    }

    async await(): Promise<T> {
        const result = await this.promise;
        [this.promise, this.resolve] = newPromise();
        return result;
    }
}

function newPromise<T>(): [Promise<T>, PromiseResolve<T>] {
    let resolveCallback: PromiseResolve<T>;
    const promise = new Promise<T>((resolve) => resolveCallback = resolve);
    return [promise, resolveCallback!];
}