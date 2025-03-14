import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { Server } from 'mock-socket';
import { ReconnectingWebSocket } from '../src/reconnecting-web-socket.js';
import { WebSocketAwaiters } from './util/awaiters.js';


describe('Reconnecting Web Socket tests', () => {

    const SERVER_URL = "ws://localhost:420";

    let server: EchoingWebSocketServer;
    let client: ReconnectingWebSocket;

    let events: string[]

    const wsAwaiters = new WebSocketAwaiters();

    beforeEach(() => {
        events = [] as string[]
        console.log("Setting Up Server");
        server = new EchoingWebSocketServer(SERVER_URL);

        console.log("Setting Up Client");
        client = new ReconnectingWebSocket(SERVER_URL, { debug: true, firstReconnectTimeoutMs: 1 });

        console.log("Setting Up Event Handlers");
        client.onopen = () => {
            wsAwaiters.openAwaiter.resolve()
            onClientEvent("open");
        }
        client.onmessage = (event) => {
            wsAwaiters.messageAwaiter.resolve()
            onClientEvent(event.data);
        }
        client.onclose = () => {
            wsAwaiters.closeAwaiter.resolve()
            onClientEvent("close");
        }
        client.onerror = () => {
            wsAwaiters.errorAwaiter.resolve()
            onClientEvent("error");
        }

        function onClientEvent(event: string) {
            console.log(`Client received: ${event}`);
            events.push(event);
        }

    })

    afterEach(async () => {
        console.log("Shutting down client.");
        client.close();
        console.log("Shutting down server.");
        server.close();

        // if we don't wait here, the next test's will client will immediately be notified with onclose
        console.log("Waiting for client to close.");
        await wsAwaiters.closeAwaiter.awaitNext();
        console.log("Client closed.");
    })


    it('receives an echo from the server', async () => {
        await wsAwaiters.openAwaiter.awaitNext();
        client.send("Hello");

        await wsAwaiters.messageAwaiter.awaitNext();
        expect(events).toEqual(["open", "Echo: Hello"]);
    })

    it('receives an echo from the server (again)', async () => {
        await wsAwaiters.openAwaiter.awaitNext();
        client.send("Hello");

        await wsAwaiters.messageAwaiter.awaitNext();
        expect(events).toEqual(["open", "Echo: Hello"]);
    })

    it('reconnects when server closes connection', async () => {
        await wsAwaiters.openAwaiter.awaitNext();
        client.send("close");

        await wsAwaiters.closeAwaiter.awaitNext();
        expect(events).toEqual(["open", "close"]);

        await wsAwaiters.openAwaiter.awaitNext();
        expect(events).toEqual(["open", "close", "open"]);
    })

    it('reconnects when client connection has an error', async () => {
        await wsAwaiters.openAwaiter.awaitNext();
        client.webSocket().onerror!(new Event("Intentional Test Error"));
        client.webSocket().close()

        console.log("[Test] Waiting for client error.");
        await wsAwaiters.errorAwaiter.awaitNext();
        expect(events).toEqual(["open", "error"]);

        console.log("[Test] Waiting for client to reopen.");
        await wsAwaiters.openAwaiter.awaitNext();
        expect(events).toEqual(["open", "error", "close", "open"]);
    })

})


///////

class EchoingWebSocketServer {
    private server: Server;

    constructor(url: string) {
        this.server = new Server(url);

        this.server.on("connection", (socket) => {
            console.log("[Server] Client connected");

            socket.on("message", (message: string | Blob | ArrayBuffer | ArrayBufferView) => {
                console.log(`[Server] Received: ${message}`);

                if (message === "close") {
                    setTimeout(() => {
                        console.log("[Server] Closing connection as requested.");
                        socket.close();
                    }, 1)

                } else {
                    setTimeout(() => {
                        console.log(`[Server] Echoing: ${message}`);
                        socket.send(`Echo: ${message}`);
                    }, 1)

                }
            });
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


