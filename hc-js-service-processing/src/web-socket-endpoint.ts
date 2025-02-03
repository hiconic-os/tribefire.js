import { eval_, util } from "@dev.hiconic/tf.js_hc-js-api";
import { ServiceRequest } from "@dev.hiconic/gm_service-api-model";
import { ReconnectingWebSocket, WsReconnectionParams } from "./reconnecting-web-socket.js";

export type WsServiceEndpointProps = WsReconnectionParams & {
    sessionId?: string;

    /** This is called immediately after the P */
    onChannelIdAssigned?: (channelId: string) => void;
}

export class WebSocketServiceEndpoint {

    readonly #evaluator: eval_.Evaluator<ServiceRequest>;
    readonly #url: string;
    readonly #clientId: string;
    readonly #optionalProps: WsServiceEndpointProps;

    #webSocket?: ReconnectingWebSocket;

    #channelId?: string;

    constructor(evaluator: eval_.Evaluator<ServiceRequest>, url: string, clientId: string, optionalProps: WsServiceEndpointProps = {}) {
        this.#evaluator = evaluator;
        this.#url = url;
        this.#clientId = clientId;
        this.#optionalProps = optionalProps;
    }

    /**
     * Warning:  messing with this object's even handlers can break the underlying reconnecting mechanism of this endpoint.
     * Make sure you know what you are doing, alrgiht?
     */
    webSocket(): ReconnectingWebSocket | undefined {
        return this.#webSocket;
    }

    channelId(): string | undefined {
        return this.#channelId;
    }

    close() {
        if (this.#webSocket) {
            this.#webSocket.close();
            this.#webSocket = undefined;
        }
    }

    // returns the first channelId
    async openWebSocketWithChannelId(): Promise<string> {
        const wsUrl = this.wsUrl();
        this.logDebug("WS URL: " + wsUrl);

        this.#webSocket = new ReconnectingWebSocket(wsUrl, this.#optionalProps);

        let promiseHandled = false;
        let resolve: any
        let reject: any;

        const promise = new Promise<string>((res, rej) => {
            resolve = res;
            reject = rej;
        })

        const channelIdFromFirstMessageResolvingHandler: ((ev: MessageEvent) => any) = messageEvent => {
            if (promiseHandled) {
                this.logDebug("Unexpected: WebSocket has already been established or failed.");
                return;
            }

            this.#channelId = messageEvent.data;
            this.logDebug("Established WebSocket connection with channelId: " + this.#channelId);

            this.#webSocket!.onmessage = messageEvent => this.handleWsMessage(messageEvent)

            if (!promiseHandled)
                resolve(this.#channelId!);
            else
                promiseHandled = true;

            this.#optionalProps?.onChannelIdAssigned?.(this.#channelId!);
        }


        this.#webSocket.onopen = () => {
            this.logDebug("WebSocket connection established to: " + wsUrl);
            this.#webSocket!.onmessage = channelIdFromFirstMessageResolvingHandler;
        }

        this.#webSocket.onerror = error => {
            console.error("Error with WebSocket connection to: " + wsUrl, error);
            if (!promiseHandled) {
                reject(error);
                promiseHandled = true;
            }
        }

        this.#webSocket.onclose = closeEvent => {
            this.logDebug(`Lost WebSocket connection to: ${wsUrl}. Reason: ${closeEvent.reason}`);
            if (!promiseHandled) {
                reject(closeEvent.reason);
                promiseHandled = true;
            }
        }


        this.#webSocket.onmessage = channelIdFromFirstMessageResolvingHandler

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

    private logDebug(...message: any[]) {
        if (this.#optionalProps.debug)
            console.log("[WebSocketEndpoint] ", ...message);
    }
}

