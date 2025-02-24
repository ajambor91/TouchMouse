package ajprogramming.TouchMouse.Network;

import ajprogramming.TouchMouse.Mouse.MouseHandler;
import ajprogramming.TouchMouse.Network.Enums.MessageTypes;
import ajprogramming.TouchMouse.Network.Enums.TCPMessageTypeEnum;
import ajprogramming.TouchMouse.Network.Messages.TCPMessage;
import ajprogramming.TouchMouse.Utils.LoggerEx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPService extends Thread {
    private final LoggerEx loggerEx;
    private final TCPServerConfig config;
    private final MouseHandler mouseHandler;
    private volatile boolean running = true;
    private boolean forceDisconnected = false;
    private Socket socket;
    private ServerSocket serverSocket;

    public TCPService(MouseHandler mouseHandler) {
        this.mouseHandler = mouseHandler;
        this.config = TCPServerConfig.getInstance();
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.loggerEx.info("TCP service initialized");

    }

    @Override
    public void run() {
        this.loggerEx.info("TCP service started");

        try {

            this.serverSocket = new ServerSocket(this.config.PORT);
            while (running) {
                this.socket = serverSocket.accept();
                InputStream inputStream = this.socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                MessageCreator messageCreator = new MessageCreator(reader.readLine(), MessageTypes.TCP);
                this.loggerEx.info("Received TCP message", messageCreator.jsonfyMessage());
                TCPMessage message = (TCPMessage) messageCreator.getMessage();
                this.processTCPMessage(message, this.socket);
            }
        } catch (IOException e) {
            this.loggerEx.warning("TCP", e.getMessage());
            this.running = false;
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean getIsForceDisconnected() {
        return this.forceDisconnected;
    }

    public void disconnect() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
            this.running = false;
            this.forceDisconnected = true;
            this.loggerEx.info("TCP disconnected");
            this.interrupt();
        } catch (IOException e) {
            this.loggerEx.warning("TCP disconnect error", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void processTCPMessage(TCPMessage tcpMessage, Socket socket) {
        if (tcpMessage.getType() == TCPMessageTypeEnum.CONNECTION) {
            this.mouseHandler.connectMouse(tcpMessage, socket);
        } else if (tcpMessage.getType() == TCPMessageTypeEnum.RECONNECT) {
            this.mouseHandler.reconnect(tcpMessage, socket);
        }
    }


}
