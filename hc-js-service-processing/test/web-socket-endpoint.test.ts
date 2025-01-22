import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { Client, Server } from 'mock-socket';
import { WebSocketServiceEndpoint } from '../src/web-socket-endpoint.js';
import { LocalEvaluator } from '../src/evaluator.js';
import { eval_ } from '@dev.hiconic/tf.js_hc-js-api';
import { ServiceRequest } from '@dev.hiconic/gm_service-api-model';
import { WebSocketAwaiter } from './util/web-socket-utils.js';

const SERVER_URL = "ws://localhost:420";
const TEST_CHANNEL_ID = "CHANNEL_ID"

describe('Reconnecting Web Socket tests', () => {

    let server: PushingWebSocketServer;
    let evaluator: eval_.Evaluator<ServiceRequest>;
    let endpoint: WebSocketServiceEndpoint;

    const wsAwaiter = new WebSocketAwaiter();

    beforeEach(async () => {
        console.log("Setting Up Server");
        server = new PushingWebSocketServer(SERVER_URL);

        console.log("Setting Up Client");
        evaluator = new LocalEvaluator();
        endpoint = new WebSocketServiceEndpoint(evaluator, SERVER_URL, "testClientId");

        console.log("Connecting Client");
        const channelId = await endpoint.openWebSocketWithChannleId();
        expect(channelId).toBe(TEST_CHANNEL_ID);

        endpoint.webSocket()!.onmessage = (event) => {
            console.log(`Client received: ${event.data}`);
            wsAwaiter.resolve4Message!()
        }

        endpoint.webSocket()!.onclose = () => {
            console.log(`Client received: close`);
            wsAwaiter.resolve4Close!()
        }
    })

    afterEach(async () => {
        console.log("Shutting down client.");
        endpoint.close();
        console.log("Shutting down server.");
        server.close();

        // if we don't wait here, the next test's will client will immediately be notified with onclose
        console.log("Waiting for client to close.");
        await wsAwaiter.awaitClientClose();
        console.log("Client closed.");
    })

    it('tests message echoed', async () => {
        console.log("Waiting for channelId to be established.");

        // if no channelId is set yet, we wait for the message to be handled
        if (!endpoint.channelId1())
            await wsAwaiter.awaitClientMessage();

        expect(endpoint.channelId1()).toEqual(TEST_CHANNEL_ID);
    })
})


///////

class PushingWebSocketServer {

    server: Server;
    serverSocket: Client | undefined;

    constructor(url: string) {
        this.server = new Server(url);

        this.server.on("connection", (socket) => {
            console.log("[Server] Client connected");
            this.serverSocket = socket;

            console.log("[Server] Sending channel id: " + TEST_CHANNEL_ID);
            socket.send(TEST_CHANNEL_ID);
        });
    }

    /**
     * Closes the server and disconnects all clients.
     */
    close() {
        this.server.stop();
        console.log("Server stopped.");
    }
}
