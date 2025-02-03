/**
 * NOTE: We don't expose this via the exports configuration in package.json
 * 
 * If this was meant to be used elsewhere, it should be a standalone package, independent of Hiconic.
 */

export type WsReconnectionParams = {

    /** @default 1000 */
    firstReconnectTimeoutMs?: number,

    /** @default "600_000 (i.e. 10 minutes)" */
    maxReconnectTimeoutMs?: number

    /** @default false */
    debug?: boolean
}

export class ReconnectingWebSocket {

    readonly #url: string;
    #webSocket: WebSocket = null!
    #reconnectParams: WsReconnectionParams;

    #failedBefore = false;
    #wasIntentionallyClosed = false;
    #timeout: number;

    onFirstOpen: (event: Event) => void = () => { };
    onReconnect: () => void = () => { };

    // Events on the underlying WebSocket

    /** onopen event of the underlying WebSocket.
     * Not that this is called not just the fist time, but also after every reconnection.
     *  
     * [MDN Reference](https://developer.mozilla.org/docs/Web/API/WebSocket/open_event)*/
    onopen: ((ev: Event) => any) | null = null;
    /** onclose event of the underlying WebSocket.
     * Not that this is called not just the fist time, but also after every reconnection.
     *  
     * [MDN Reference](https://developer.mozilla.org/docs/Web/API/WebSocket/close_event) */
    onclose: ((ev: CloseEvent) => any) | null = null;

    /** onerror event of the underlying WebSocket 
     * Not that this is called not just the fist time, but also after every reconnection.
     * 
     * [MDN Reference](https://developer.mozilla.org/docs/Web/API/WebSocket/error_event) */
    onerror: ((ev: Event) => any) | null = null;

    /** [MDN Reference](https://developer.mozilla.org/docs/Web/API/WebSocket/message_event) */
    onmessage: ((ev: MessageEvent) => any) | null = null;

    constructor(url: string, reconnectParams: WsReconnectionParams = {}) {
        this.#url = url;
        this.#reconnectParams = {
            firstReconnectTimeoutMs: 1000, // 1 second
            maxReconnectTimeoutMs: 10 * 60 * 1000, // 10 minutes
            debug: false,
            ...reconnectParams
        };
        this.#timeout = this.#reconnectParams.firstReconnectTimeoutMs!;

        this.reconnect();
    }

    send(data: string | ArrayBufferLike | Blob | ArrayBufferView): void {
        this.#webSocket.send(data);
    }

    close(code?: number, reason?: string) {
        this.#wasIntentionallyClosed = true;
        this.#webSocket.close(code, reason);
    }

    webSocket(): WebSocket {
        return this.#webSocket;
    }

    private reconnect() {
        this.#webSocket = new WebSocket(this.#url);

        this.#webSocket.onopen = (event) => {
            this.logDebug(`Opened WebSocket connection to: ${this.#url}`);
            this.onopen?.(event);
            this.#failedBefore = false;
            // reset reconnect timeout after a successful connection
            this.#timeout = this.#reconnectParams.firstReconnectTimeoutMs!;
        }
        this.#webSocket.onerror = error => {
            this.logDebug('Error:', error);
            this.onerror?.(error);
        }

        this.#webSocket.onclose = closeEvent => {
            this.logDebug(`WebSocket connection to: ${this.#url} was closed. Reason: `, closeEvent.reason);
            this.onclose?.(closeEvent);

            // if this was closed by calling the close() method,we are obviously not gonna try to reconnect.
            if (this.#wasIntentionallyClosed)
                return;

            if (this.#failedBefore) {
                this.#timeout = Math.min(2 * this.#timeout, this.#reconnectParams.maxReconnectTimeoutMs!);
            } else {
                this.#failedBefore = true;
            }

            this.logDebug(`Will try to reconnect in ${this.#timeout} ms...`);
            setTimeout(() => this.reconnect(), this.#timeout);
        }

        this.#webSocket.onmessage = messageEvent => {
            this.onmessage?.(messageEvent);
        }
    }

    private logDebug(...message: any[]) {
        if (this.#reconnectParams.debug)
            console.log("[Reconnecting WebSocket] ", ...message);
    }
}