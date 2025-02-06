import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { Client, Server } from 'mock-socket';
import { WebSocketServiceEndpoint } from '../src/web-socket-endpoint.js';
import { LocalEvaluator } from '../src/evaluator.js';
import { eval_ } from '@dev.hiconic/tf.js_hc-js-api';
import { ServiceRequest } from '@dev.hiconic/gm_service-api-model';
import { Awaiter, WebSocketAwaiters } from './util/awaiters.js';

const SERVER_URL = "ws://localhost:420";
const TEST_CHANNEL_ID = "CHANNEL_ID"
const FIRST_TEST_CHANNEL_ID = TEST_CHANNEL_ID+ "-1"

describe('Reconnecting Web Socket tests', () => {

    let server: PushingWebSocketServer;
    let evaluator: eval_.Evaluator<ServiceRequest>;
    let endpoint: WebSocketServiceEndpoint;

    let beforeWasOk: boolean;

    const wsAwaiters = new WebSocketAwaiters();
    const channelIdAwaiter = new Awaiter<string>();

    const onChannelIdAssigned = (channelId: string) => {
        console.log("[ChannelId Awaiter] onChannelIdAssigned: " + channelId);
        channelIdAwaiter.resolve(channelId);
    }

    const currentWebSocket = () => endpoint.webSocket()?.webSocket()!;

    beforeEach(async () => {
        beforeWasOk = false;

        console.log("[Before] Setting Up Server");
        server = new PushingWebSocketServer(SERVER_URL);

        console.log("[Before] Setting Up Client");
        evaluator = new LocalEvaluator();
        endpoint = new WebSocketServiceEndpoint(evaluator, SERVER_URL, "testClientId", { onChannelIdAssigned, debug: true });

        console.log("[Before] Connecting Client");
        await endpoint.openWebSocketWithChannelId();

        endpoint.webSocket()!.onmessage = (event) => {
            console.log(`[Client] received: ${event.data}`);
            wsAwaiters.messageAwaiter.resolve()
        }

        endpoint.webSocket()!.onclose = () => {
            console.log(`[Client] received: close`);
            wsAwaiters.closeAwaiter.resolve()
        }

        beforeWasOk = true;
    })

    afterEach(async () => {
        if (!beforeWasOk)
            return;

        console.log("[After] Shutting down client.");
        endpoint.close();
        console.log("[After] Shutting down server.");
        server.close();

        // if we don't wait here, the next test's will client will immediately be notified with onclose
        console.log("[After] Waiting for client to close.");
        await wsAwaiters.closeAwaiter.awaitNext();
        console.log("[After] Client closed.");
    })

    it('tests channelId received as first message', async () => {
        console.log("[Test] Waiting for channelId to be established.");

        const channelId = await channelIdAwaiter.awaitNext();
        expect(channelId).toEqual(FIRST_TEST_CHANNEL_ID);

        console.log("[Test] Channel ID: ", endpoint.channelId());
        expect(endpoint.channelId()).toEqual(FIRST_TEST_CHANNEL_ID);
    })

    it('tests channelId received as first message after reconnect due to close', async () => {
        console.log("[Test] Waiting for channelId to be established.");

        const channelId = await channelIdAwaiter.awaitNext();
        expect(channelId).toEqual(FIRST_TEST_CHANNEL_ID);

        console.log("[Test] Channel ID: ", endpoint.channelId());
        expect(endpoint.channelId()).toEqual(FIRST_TEST_CHANNEL_ID);

        // Now let's close the underlying WebSocket
        // ReconnectingWebSocket will reconnect, and we get a new pushChannelId
        console.log("[Test] Closing WebSocket: ", endpoint.channelId());
        currentWebSocket().close()

        console.log("[Test] Waiting for new message.");
        const newChannelId = await channelIdAwaiter.awaitNext();
        expect(newChannelId).toEqual(TEST_CHANNEL_ID + "-2");

        // Let's do it again fo good measure
        console.log("[Test] Closing WebSocket: ", endpoint.channelId());
        currentWebSocket().close()

        console.log("[Test] Waiting for new message.");
        const newChannelId2 = await channelIdAwaiter.awaitNext();
        expect(newChannelId2).toEqual(TEST_CHANNEL_ID + "-3");
    })

    it('tests channelId received as first message after reconnect due to error', async () => {
        console.log("[Test] Waiting for channelId to be established.");

        const channelId = await channelIdAwaiter.awaitNext();
        expect(channelId).toEqual(FIRST_TEST_CHANNEL_ID);

        console.log("[Test] Channel ID: ", endpoint.channelId());
        expect(endpoint.channelId()).toEqual(FIRST_TEST_CHANNEL_ID);

        // Now let's close the underlying WebSocket
        // ReconnectingWebSocket will reconnect, and we get a new pushChannelId
        console.log("[Test] Closing WebSocket: ", endpoint.channelId());
        currentWebSocket().close()

        console.log("[Test] Waiting for new message.");
        const newChannelId = await channelIdAwaiter.awaitNext();
        expect(newChannelId).toEqual(TEST_CHANNEL_ID + "-2");

        // Let's do it again fo good measure
        console.log("[Test] Closing WebSocket: ", endpoint.channelId());

        currentWebSocket().onerror!(new Event("Intentional Error To Test Reconnect"));
        currentWebSocket().close()

        console.log("[Test] Waiting for new message.");
        const newChannelId2 = await channelIdAwaiter.awaitNext();
        expect(newChannelId2).toEqual(TEST_CHANNEL_ID + "-3");
    })
})


///////

class PushingWebSocketServer {

    server: Server;
    serverSocket: Client | undefined;
    counter = 1

    constructor(url: string) {
        this.server = new Server(url);

        this.server.on("connection", (socket) => {
            console.log("[Server] Client connected");
            this.serverSocket = socket;

            const channelId = TEST_CHANNEL_ID + "-"+ this.counter++;
            console.log("[Server] Sending channel id: " + channelId);
            socket.send(channelId);
        });
    }

    /**
     * Closes the server and disconnects all clients.
     */
    close() {
        this.server.stop();
        console.log("[Server] Server stopped.");
    }
}
