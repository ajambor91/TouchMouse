package ajprogramming.TouchMouse.Mouse;

import ajprogramming.TouchMouse.Keyboard.Keyboard;
import ajprogramming.TouchMouse.Network.Enums.EConnectionStatus;
import ajprogramming.TouchMouse.Network.Enums.MessageTypes;
import ajprogramming.TouchMouse.Network.Enums.TCPMessageTypeEnum;
import ajprogramming.TouchMouse.Network.Enums.UDPMessageTypeEnum;
import ajprogramming.TouchMouse.Network.IHost;
import ajprogramming.TouchMouse.Network.KeyboardMessageBuffer;
import ajprogramming.TouchMouse.Network.MessageBuffer;
import ajprogramming.TouchMouse.Network.MessageCreator;
import ajprogramming.TouchMouse.Network.Messages.TCPMessage;
import ajprogramming.TouchMouse.Network.Messages.UDPMessage;
import ajprogramming.TouchMouse.Tray.Tray;
import ajprogramming.TouchMouse.Utils.LoggerEx;
import ajprogramming.TouchMouse.Utils.XML;

import java.io.*;
import java.net.Socket;

public class Mouse extends Thread implements IMouse {

    private final MouseHandler mouseHandler;
    private final IHost host;
    private final LoggerEx loggerEx;
    private Keyboard keyboard;
    private boolean isConnected = false;
    private MouseMove mouseMove;
    private String mouseAddress;
    private final String sessionId;
    private Socket tcpSocket;
    private String mouseName;
    private String mouseId;
    private EConnectionStatus connectionStatus;
    private final MessageBuffer messageBuffer;
    private KeyboardMessageBuffer keyboardMessageBuffer;

    public Mouse(Socket tcpSocket, MouseHandler mouseHandler, TCPMessage tcpMessage, IHost host) {
        try {
            this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
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

    public EConnectionStatus getConnectionStatus() {
        return this.connectionStatus;
    }

    public String getMouseId() {
        return this.mouseId;
    }

    public void setMouseId(String mouseId) {
        this.mouseId = mouseId;
    }

    public IHost getHost() {
        return this.host;
    }

    public String getMouseName() {
        return this.mouseName;
    }

    public void setMouseName(String mouseName) {
        this.mouseName = mouseName;
    }

    public String getMouseAddress() {
        return this.mouseAddress;
    }

    public void setMouseAddress(String mouseAddress) {
        this.mouseAddress = mouseAddress;
    }

    public String getMouseID() {
        return this.mouseId;
    }

    public void addMsg(UDPMessage udpMessage) {
        if (udpMessage.getType() == UDPMessageTypeEnum.KEYBOARD) {

            this.keyboardMessageBuffer.put(udpMessage);
            synchronized (this.keyboard) {
                this.keyboard.notify();
            }
        } else {
            this.messageBuffer.put(udpMessage);
            synchronized (this.mouseMove) {
                this.mouseMove.notify();
            }
        }

    }

    @Override
    public void run() {
        this.listeningTCP();
    }

    @Override
    public String toString() {
        return String.format("%s, ip: %s, status: %s", this.getMouseName(), this.getMouseAddress(), this.isConnected ? "Connected" : "Disconnected");
    }

    public void disconnectMouse() {
        Tray.getInstance().showMessage("Mouse disconnected", String.format("Mouse %s disconnected", this.mouseName));
        this.disconnect();
    }

    public void removeMouse() {
        Tray.getInstance().showMessage("Mouse removed", String.format("Mouse %s removed", this.mouseName));
        this.disconnect();
    }

    private void listeningTCP() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.tcpSocket.getInputStream()))) {
            this.tcpSocket.setKeepAlive(true);
            String incomingMessage;
            this.isConnected = true;
            while ((incomingMessage = reader.readLine()) != null) {
                MessageCreator messageCreator = new MessageCreator(incomingMessage, MessageTypes.TCP);
                this.loggerEx.info("Mouse received TCP message", messageCreator.jsonfyMessage());
                this.processMessage((TCPMessage) messageCreator.getMessage());
                this.isConnected = true;
            }
            if (this.isInterrupted()) {
                this.wait();
            }
            this.connectionStatus = EConnectionStatus.FAIL;
            this.isConnected = false;
            this.interrupt();
        } catch (IOException | IllegalMonitorStateException | InterruptedException e) {
            this.connectionStatus = EConnectionStatus.FAIL;
            this.loggerEx.info("Mouse disconnecting");
        }
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

    private void sendTCPMessage(TCPMessage tcpMessage) {
        try {
            OutputStream outputStream = this.tcpSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            MessageCreator messageCreator = new MessageCreator(tcpMessage);
            writer.println(messageCreator.jsonfyMessage());
        } catch (IOException e) {
            this.connectionStatus = EConnectionStatus.FAIL;

        }
    }

    private void processMessage(TCPMessage tcpMessage) {
        switch (tcpMessage.getType()) {
            case DISCONNECT:
                this.disconnectMouse();
                break;
            case NAME_CHANGE:
                this.changeName(tcpMessage);
                break;
            case KEYBOARD_SHOW:
                this.setKeyboard();
                break;
            case KEYBOARD_HIDE:
                this.clearKeyboard();
                break;
            default:
                this.sendTCPMessage(tcpMessage);
                break;
        }
    }

    private void clearKeyboard() {
        this.keyboard.interrupt();
        this.keyboard = null;
        this.keyboardMessageBuffer = null;

    }

    private void setKeyboard() {
        this.keyboardMessageBuffer = new KeyboardMessageBuffer();
        this.keyboard = new Keyboard(this.keyboardMessageBuffer);
        this.keyboard.start();
    }

    private void changeName(TCPMessage tcpMessage) {
        this.setMouseName(tcpMessage.getMouseName());
        XML xml = new XML();
        xml.changeMouseName(this);
        this.mouseHandler.overrideMouse(this);
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

    }

    private void disconnect() {
        try {
            this.isConnected = false;
            this.connectionStatus = EConnectionStatus.FAIL;
            this.loggerEx.info("Destroying socket");
            this.sendDisconnectMessage();
            this.stopKeyboard();
            this.stopMouseMove();
            this.tcpSocket.close();
            this.tcpSocket = null;
            this.interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void stopMouseMove() {
        this.mouseMove.interrupt();
        this.mouseMove = null;
    }

    private void stopKeyboard() {
        this.keyboard.interrupt();
        this.keyboard = null;
    }


}
