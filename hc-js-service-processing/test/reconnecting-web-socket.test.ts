import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { Server } from 'mock-socket';
import { ReconnectingWebSocket } from '../src/reconnecting-web-socket.js';
import { WebSocketAwaiter } from './util/web-socket-utils.js';


describe('Reconnecting Web Socket tests', () => {

    const SERVER_URL = "ws://localhost:420";

    let server: EchoingWebSocketServer;
    let client: ReconnectingWebSocket;

    let events: string[]

    const wsAwaiter = new WebSocketAwaiter();

    beforeEach(() => {
        events = [] as string[]
        console.log("Setting Up Server");
        server = new EchoingWebSocketServer(SERVER_URL);

        console.log("Setting Up Client");
        client = new ReconnectingWebSocket(SERVER_URL, { debug: true, firstReconnectTimeoutMs: 1 });

        console.log("Setting Up Event Handlers");
        client.onopen = () => {
            wsAwaiter.resolve4Open!()
            onClientEvent("open");
        }
        client.onmessage = (event) => {
            wsAwaiter.resolve4Message!()
            onClientEvent(event.data);
        }
        client.onclose = () => {
            wsAwaiter.resolve4Close!()
            onClientEvent("close");
        }
        client.onerror = () => {
            wsAwaiter.resolve4Error!()
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
        await wsAwaiter.awaitClientClose();
        console.log("Client closed.");
    })


    it('receives an echo from the server', async () => {
        await wsAwaiter.awaitClientOpen();
        client.send("Hello");

        await wsAwaiter.awaitClientMessage();
        expect(events).toEqual(["open", "Echo: Hello"]);
    })

    it('receives an echo from the server (again)', async () => {
        await wsAwaiter.awaitClientOpen();
        client.send("Hello");

        await wsAwaiter.awaitClientMessage();
        expect(events).toEqual(["open", "Echo: Hello"]);
    })

    it('reconnects when server closes connection', async () => {
        await wsAwaiter.awaitClientOpen();
        client.send("close");

        await wsAwaiter.awaitClientClose();
        expect(events).toEqual(["open", "close"]);

        await wsAwaiter.awaitClientOpen();
        expect(events).toEqual(["open", "close", "open"]);
    })

    it('reconnects when client connection has an error', async () => {
        await wsAwaiter.awaitClientOpen();
        client.webSocket().onerror!(new Event("Intentional Test Error"));

        console.log("[Test] Waiting for client error.");
        await wsAwaiter.awaitClientError();
        expect(events).toEqual(["open", "error"]);

        console.log("[Test] Waiting for client to reopen.");
        await wsAwaiter.awaitClientOpen();
        expect(events).toEqual(["open", "error", "open"]);
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


