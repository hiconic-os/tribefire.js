export type PromiseResolve<T> = (value: T | PromiseLike<T>) => void;



export class WebSocketAwaiter {

    promise4Open: Promise<void>;
    promise4Message: Promise<void>;
    promise4Close: Promise<void>;
    promise4Error: Promise<void>;

    resolve4Open: PromiseResolve<void>;
    resolve4Message: PromiseResolve<void>;
    resolve4Close: PromiseResolve<void>;
    resolve4Error: PromiseResolve<void>;

    constructor() {
        [this.promise4Open, this.resolve4Open] = newPromise();
        [this.promise4Message, this.resolve4Message] = newPromise();
        [this.promise4Close, this.resolve4Close] = newPromise();
        [this.promise4Error, this.resolve4Error] = newPromise();
    }

    async awaitClientOpen(): Promise<void> {
        await this.promise4Open!;
        [this.promise4Open, this.resolve4Open] = newPromise();
    }

    async awaitClientMessage(): Promise<void> {
        await this.promise4Message!;
        [this.promise4Message, this.resolve4Message] = newPromise();
    }

    async awaitClientClose(): Promise<void> {
        await this.promise4Close!;
        [this.promise4Close, this.resolve4Close] = newPromise();
    }

    async awaitClientError(): Promise<void> {
        await this.promise4Error!;
        [this.promise4Error, this.resolve4Error] = newPromise();
    }

}

function newPromise(): [Promise<void>, PromiseResolve<void>] {
    let resolveCallback: PromiseResolve<void>;
    const promise = new Promise<void>((resolve) => resolveCallback = resolve);
    return [promise, resolveCallback!];
}


