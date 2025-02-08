package ajprogramming.TouchMouse.Mouse;

import ajprogramming.TouchMouse.Network.Enums.EConnectionStatus;
import ajprogramming.TouchMouse.Network.Enums.MessageTypes;
import ajprogramming.TouchMouse.Network.Enums.TCPMessageTypeEnum;
import ajprogramming.TouchMouse.Network.IHost;
import ajprogramming.TouchMouse.Network.MessageBuffer;
import ajprogramming.TouchMouse.Network.MessageCreator;
import ajprogramming.TouchMouse.Network.Messages.TCPMessage;
import ajprogramming.TouchMouse.Network.Messages.UDPMessage;
import ajprogramming.TouchMouse.Tray.Tray;

import java.io.*;
import java.net.Socket;

public class Mouse extends Thread implements IMouse{
    private final MouseHandler mouseHandler;
    private final IHost host;
    private boolean isConnected = false;
    private MouseMove mouseMove;
    private  String mouseAddress;
    private  String sessionId;
    private  Socket tcpSocket;
    private String mouseName;
    private  String mouseId;
    private EConnectionStatus connectionStatus;
    private MessageBuffer messageBuffer;

    public Mouse(Socket tcpSocket, MouseHandler mouseHandler, TCPMessage tcpMessage, IHost host) {
        try {
            this.messageBuffer = new MessageBuffer();
            this.mouseMove = new MouseMove(this.messageBuffer);
            this.mouseMove.start();
            this.host = host;
            this.mouseHandler = mouseHandler;
            this.mouseId = tcpMessage.getMouseId();
            this.mouseAddress = tcpMessage.getMouseAddress();
            this.mouseName = tcpMessage.getAppName();
            this.sessionId = tcpMessage.getSessionId();
            this.tcpSocket = tcpSocket;
            this.sendInitializationTCPMessage();
            Tray.getInstance().showMessage("Mouse added", String.format("New mouse %s added", this.mouseName));
            this.connectionStatus = EConnectionStatus.CONNECTED;

        } catch (IOException e) {
            this.connectionStatus = EConnectionStatus.FAIL;
            throw new RuntimeException(e);
        } catch (Exception e) {
            this.connectionStatus = EConnectionStatus.FAIL;
            throw new RuntimeException(e);
        }

    }

    public void addMsg(UDPMessage udpMessage) {
        this.messageBuffer.put(udpMessage);
        synchronized (this.mouseMove) {
            this.mouseMove.notify();
        }
    }

    public EConnectionStatus getConnectionStatus() {
        return this.connectionStatus;
    }

    public void reconnect(Socket socket) {

        try {
            this.tcpSocket = socket;

            this.sendReconnectAnswerTCPMessage();

                this.mouseMove = new MouseMove(this.messageBuffer);
                this.mouseMove.start();

        } catch (IOException e) {
            this.connectionStatus = EConnectionStatus.FAIL;
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String getMouseId() {
        return this.mouseId;
    }

    public void run() {
        this.listeningTCP();
    }


    public String getMouseName() {
        return this.mouseName;
    }

    public void setMouseName(String mouseName) {
            this.mouseName = mouseName;
    }

    public void setMouseAddress(String mouseAddress) {
        this.mouseAddress = mouseAddress;
    }

    public void disconnectMouse() {
        Tray.getInstance().showMessage("Mouse disconnected", String.format("Mouse %s disconnected", this.mouseName));
        this.disconnect();
    }

    public void removeMouse() {
        Tray.getInstance().showMessage("Mouse removed", String.format("Mouse %s removed", this.mouseName));
        this.disconnect();
    }

    public void removeMouseLFromList() {
        Tray.getInstance().showMessage("Mouse removed", String.format("Mouse %s removed", this.mouseName));
    }

    public IHost getHost() {
        return this.host;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    private void listeningTCP() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.tcpSocket.getInputStream()));) {
            this.tcpSocket.setKeepAlive(true);
            String incomingMessage;
            this.isConnected = true;
            while ((incomingMessage = reader.readLine()) != null) {
                MessageCreator messageCreator = new MessageCreator(incomingMessage, MessageTypes.TCP);
                this.processTCPMessage((TCPMessage) messageCreator.getMessage());
                this.isConnected = true;
            }
            this.connectionStatus = EConnectionStatus.FAIL;
            this.isConnected = false;
                this.interrupt();



        } catch (IOException | IllegalMonitorStateException e) {
            this.disconnectMouse();

            throw new RuntimeException(e);
        }
    }


    public String getMouseAddress() {
        return this.mouseAddress;
    }

    public String getMouseID() {
        return this.mouseId;
    }

    public void setMouseId(String mouseId) {
        this.mouseId = mouseId;
    }

    @Override
    public String toString() {
        return String.format("%s, ip: %s, status: %s", this.getMouseName(), this.getMouseAddress(), this.isConnected ? "Connected" : "Disconnected");
    }
    private void processTCPMessage(TCPMessage tcpMessage) {
        this.processMessage(tcpMessage);
    }

    private void sendInitializationTCPMessage() throws IOException {
        OutputStream outputStream = this.tcpSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);
        MessageCreator messageCreator = new MessageCreator(
                this.getMouseId(),
                this.getHost().getSessionId(),
                this.getHost().getHostAddress(),
                this.getHost().getHostname(),
                this.getHost().getAppName(),
                TCPMessageTypeEnum.CONNECTION
        );
        writer.println(messageCreator.jsonfyMessage());
    }
    private void sendReconnectAnswerTCPMessage() throws IOException {
        OutputStream outputStream = this.tcpSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);
        MessageCreator messageCreator = new MessageCreator(
                this.getMouseId(),
                this.getHost().getSessionId(),
                this.getHost().getHostAddress(),
                this.getHost().getHostname(),
                this.getHost().getAppName(),
                TCPMessageTypeEnum.RECONNECT_ANSWER
        );
        writer.println(messageCreator.jsonfyMessage());
        this.isConnected = true;
        this.connectionStatus = EConnectionStatus.CONNECTED;
    }

    private void sendTCPMessage(TCPMessage tcpMessage) {
        try {
            OutputStream outputStream = this.tcpSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            MessageCreator messageCreator = new MessageCreator(tcpMessage);
            writer.println(messageCreator.jsonfyMessage());
        } catch (IOException e) {
            this.connectionStatus = EConnectionStatus.FAIL;
            throw new RuntimeException(e);
        }

    }

    private void processMessage(TCPMessage tcpMessage) {
            this.sendTCPMessage(tcpMessage);
    }


    private void sendDisconnectMessage() {

        MessageCreator messageCreator = new MessageCreator(
                this.getMouseId(),
                this.getHost().getSessionId(),
                this.getHost().getHostAddress(),
                this.getHost().getHostname(),
                this.getHost().getAppName(),
                TCPMessageTypeEnum.DISCONNECT
        );
        this.sendTCPMessage((TCPMessage) messageCreator.getMessage());
        this.mouseMove.interrupt();
    }

    private void disconnect() {
        this.sendDisconnectMessage();
        this.interrupt();

        this.connectionStatus = EConnectionStatus.FAIL;
        this.isConnected = false;
    }


}
