package aj.phone.client.NetworkModule;

import android.util.Log;

import java.io.IOException;

import aj.phone.client.Activities.ConnectionActivity.ConnectionActivity;
import aj.phone.client.Activities.TouchPadActivity.TouchPadActivity;
import aj.phone.client.Core.ActivitiesManager;
import aj.phone.client.IHost;
import aj.phone.client.NetworkModule.Enums.EConnectionStatus;
import aj.phone.client.NetworkModule.Enums.EMouseTouch;
import aj.phone.client.NetworkModule.Enums.EMouseTouchType;
import aj.phone.client.NetworkModule.Enums.TCPMessageTypeEnum;
import aj.phone.client.NetworkModule.Enums.UDPMessageTypeEnum;
import aj.phone.client.NetworkModule.Message.BroadcastMessage;
import aj.phone.client.NetworkModule.Message.MessageCreator;
import aj.phone.client.NetworkModule.Message.Move;
import aj.phone.client.NetworkModule.Message.Scroll;
import aj.phone.client.NetworkModule.Message.TCPMessage;
import aj.phone.client.NetworkModule.Message.Touch;
import aj.phone.client.NetworkModule.Message.UDPAction;
import aj.phone.client.NetworkModule.Message.UDPMessage;
import aj.phone.client.NetworkModule.TCP.TCPClient;
import aj.phone.client.NetworkModule.TCP.TCPMessageBuffer;
import aj.phone.client.NetworkModule.UDP.UDPClient;
import aj.phone.client.Utils.Config;

public class NetworkService extends MouseInet {

    private static NetworkService instance;
    private TCPMessageBuffer tcpMessageBuffer;
    private ActivitiesManager activitiesManager;
    private UDPClient udpClient;
    private TCPClient tcpClient;
    private BroadcastListener broadcastListener;

    private NetworkService() {
        this.tcpMessageBuffer = new TCPMessageBuffer();
        this.initializeSelfObject();
        this.tcpClient = new TCPClient(this, this.tcpMessageBuffer);
        this.udpClient = new UDPClient(this);
        this.broadcastListener = new BroadcastListener(this);
    }

    public static NetworkService getInstance() {
        if (NetworkService.instance == null) {
            NetworkService.instance = new NetworkService();
        }
        return NetworkService.instance;
    }

    public void setActivitiesManager(ActivitiesManager activitiesManager) {
        this.activitiesManager = activitiesManager;
    }

    public void reconnectSpecifiedHost(IHost host) {
        try {
            if (this.getConnectionStatus() == EConnectionStatus.CONNECTED ||
                    this.getConnectionStatus() == EConnectionStatus.RECONNECTING ||
                    this.getConnectionStatus() == EConnectionStatus.LISTEN) {
                this.udpClient.stopService();
                this.tcpClient.stopService();
            }
            this.tcpMessageBuffer = new TCPMessageBuffer();
            TCPMessage tcpMessage = (TCPMessage) this.createMessageCreator(TCPMessageTypeEnum.RECONNECT).getMessage();
            this.tcpMessageBuffer.addMessage(tcpMessage);
            this.setHostAddress(host.getHostAddress());
            this.udpClient = new UDPClient(this);
            this.tcpClient = new TCPClient(this, this.tcpMessageBuffer);
            this.tcpClient.start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void runTCP(BroadcastMessage message) {
        Log.d("TCP", String.format("Run TCP for message appName: %s with self appName: %s", message.getAppName(), this.getAppName()));
        if (message.getAppName().equals(this.getAppName())) {
            TCPMessage tcpMessage = (TCPMessage) this.createMessageCreator(TCPMessageTypeEnum.CONNECTION).getMessage();
            this.tcpMessageBuffer.addMessage(tcpMessage);
            String hostAddress = message.getHostAddress();
            this.setHostAddress(hostAddress);
            this.tcpClient.start();
        }
    }

    public void setTouchUDPMessage(EMouseTouch mouseTouch, EMouseTouchType mouseTouchType) {
        Log.d("UDP", "Creating message");
        Touch touch = new Touch();
        touch.setClickType(mouseTouchType);
        touch.setClick(mouseTouch);
        MessageCreator messageCreator = this.createMessageCreator(UDPMessageTypeEnum.TOUCH, touch);
        synchronized (this.udpClient) {
            this.udpClient.setMessage((UDPMessage) messageCreator.getMessage());
            this.udpClient.notify();
        }
    }

    public void changeName(String newName) {
        this.setMouseName(newName);
        MessageCreator messageCreator = this.createMessageCreator(TCPMessageTypeEnum.NAME_CHANGE);
        if (this.getConnectionStatus() != EConnectionStatus.CONNECTED) {
            return;
        }
        try {
            this.tcpMessageBuffer.addMessage((TCPMessage) messageCreator.getMessage());
            synchronized (this.tcpClient.getTcpSender()) {
                this.tcpClient.getTcpSender().notify();
            }
        } catch (Exception e) {
            Log.e("Disconnect", "Disconnect error", e);
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        if (this.tcpClient != null && this.tcpMessageBuffer != null && this.tcpClient.getTcpSender() != null) {
            MessageCreator messageCreator = this.createMessageCreator(TCPMessageTypeEnum.DISCONNECT);
            try {
                Log.d("TCP", "Disconnect and adding message to buffer");
                this.tcpMessageBuffer.addMessage((TCPMessage) messageCreator.getMessage());
                synchronized (this.tcpClient.getTcpSender()) {
                    this.tcpClient.getTcpSender().notify();
                }

            } catch (Exception e) {
                Log.e("Disconnect", "Disconnect error", e);
                throw new RuntimeException(e);
            }
        }
    }

    public void setTouchUDPMessage(int scrollLine) {
        Log.d("UDP", "Creating message");
        Scroll scroll = new Scroll();
        scroll.setLineScroll(scrollLine);
        MessageCreator messageCreator = this.createMessageCreator(UDPMessageTypeEnum.SCROLL, scroll);
        synchronized (this.udpClient) {
            this.udpClient.setMessage((UDPMessage) messageCreator.getMessage());
            this.udpClient.notify();
        }
    }

    public void setTouchUDPMessage(int moveX, int moveY) {
        Log.d("UDP", "Creating message");
        Move move = new Move();
        move.setX(moveX);
        move.setY(moveY);
        MessageCreator messageCreator = this.createMessageCreator(UDPMessageTypeEnum.MOVE, move);
        synchronized (this.udpClient) {
            this.udpClient.setMessage((UDPMessage) messageCreator.getMessage());
            this.udpClient.notify();
        }
    }

    public void reconnect() {
        this.tcpMessageBuffer = new TCPMessageBuffer();
        this.udpClient.interrupt();
        this.tcpClient = new TCPClient(this, this.tcpMessageBuffer);
        this.udpClient = new UDPClient(this);
        this.broadcastListener = new BroadcastListener(this);
        this.broadcastListener.start();
    }

    public void onBroadcastTimeout() {
        this.setConnectionStatus(EConnectionStatus.BROADCAST_TIMEOUT);
        this.stopServices();
        Log.d("NETWORK", "On broadcast timeout");
        this.conditionallyRunConnectionActivity();
    }

    public void processTCPSocketException() {
        this.setConnectionStatus(EConnectionStatus.FAIL);
        this.stopServices();
        Log.d("NETWORK", "On tcp socket exception");
        this.conditionallyRunConnectionActivity();
    }

    public void processDisconnectMessage() {
        this.setConnectionStatus(EConnectionStatus.DISCONNECTED);
        Log.d("NETWORK", "On message disconnect");
        this.stopServices();
        this.conditionallyRunConnectionActivity();
    }

    public void processHostDisconnect() {
        this.setConnectionStatus(EConnectionStatus.DISCONNECTED_HOST);
        Log.d("NETWORK", "On message disconnect");
        this.stopServices();
        this.conditionallyRunConnectionActivity();
    }

    public void processReconnecting() {
        this.setConnectionStatus(EConnectionStatus.RECONNECTING);
        this.processConnection();
    }

    public void processNewConnection() {
        this.setConnectionStatus(EConnectionStatus.CONNECTED);
        this.processConnection();
    }

    private void conditionallyRunConnectionActivity() {
        if (this.activitiesManager.isOnConnectingOrTouchpad()) {
            this.activitiesManager.runActivityWithScreen(ConnectionActivity.class);
        }
    }

    private void processConnection() {
        this.udpClient.start();
        this.activitiesManager.runActivity(TouchPadActivity.class);
    }

    public void initialize() {
        if (this.getConnectionStatus() == EConnectionStatus.INITIALIZED ||
                this.getConnectionStatus() == EConnectionStatus.DISCONNECTED ||
                this.getConnectionStatus() == EConnectionStatus.FAIL) {
            Log.d("NETWORK", "Initialized network");
            this.broadcastListener.start();
            this.setConnectionStatus(EConnectionStatus.LISTEN);
        }
    }

    private void stopServices() {
        try {

            this.udpClient.stopService();
            this.broadcastListener.interrupt();
            this.tcpClient.stopService();

        } catch (IOException e) {
            Log.e("NETWORK", "Cannot interrupt TCP", e);
            throw new RuntimeException(e);
        }
    }

    private void initializeSelfObject() {
        Log.d("NETWORK", "Initializing network module");
        this.setAddress(Config.getInstance().getSelfIp());
        this.setMouseName(Config.getInstance().getMouseName());
        this.setAppId(Config.getInstance().getAppId());
        this.setAppName(Config.getInstance().getAppName());
        this.setConnectionStatus(EConnectionStatus.INITIALIZED);
        this.setActiveHost(true);
    }

    private MessageCreator createMessageCreator(UDPMessageTypeEnum udpMessageTypeEnum, UDPAction action) {
        return new MessageCreator(
                this.getAppId(),
                this.getSessionId(),
                this.getHostAddress(),
                Config.getInstance().getSelfIp(),
                this.getMouseName(),
                this.getAppName(),
                action,
                udpMessageTypeEnum
        );
    }

    private MessageCreator createMessageCreator(TCPMessageTypeEnum tcpMessageTypeEnum) {
        return new MessageCreator(
                this.getAppId(),
                this.getSessionId(),
                this.getHostAddress(),
                this.getAddress(),
                this.getMouseName(),
                this.getAppName(),
                tcpMessageTypeEnum
        );
    }
}
