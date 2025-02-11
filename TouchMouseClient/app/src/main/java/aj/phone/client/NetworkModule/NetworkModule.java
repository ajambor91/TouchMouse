package aj.phone.client.NetworkModule;

import android.util.Log;

import java.io.IOException;

import aj.phone.client.Activities.ConnectionActivity.ConnectionActivity;
import aj.phone.client.Activities.TouchPadActivity.TouchPadActivity;
import aj.phone.client.Core.ActivitiesManager;
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

public class NetworkModule extends MouseInet {

    private final TCPMessageBuffer tcpMessageBuffer;
    private static NetworkModule instance;

    private boolean connected = false;
    private ActivitiesManager activitiesManager;
    private UDPClient udpClient;
    private TCPClient tcpClient;
    private BroadcastListener broadcastListener;

    private NetworkModule() {
        this.tcpMessageBuffer = new TCPMessageBuffer();
        this.initializeSelfObject();
        this.tcpClient = new TCPClient(this, this.tcpMessageBuffer);
        this.udpClient = new UDPClient(this);
        this.broadcastListener = new BroadcastListener(this);
    }

    public static NetworkModule getInstance() {
        if (NetworkModule.instance == null) {
            NetworkModule.instance = new NetworkModule();
        }
        return NetworkModule.instance;
    }

    public void setActivitiesManager(ActivitiesManager activitiesManager) {
        this.activitiesManager = activitiesManager;
    }

    public void runTCP(BroadcastMessage message) {
        Log.d("TCP", String.format("Run TCP for message appName: %s with self appName: %s", message.getAppName(), this.getAppName()));
        if (!this.connected && message.getAppName().equals(this.getAppName())) {
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
        try {
            this.tcpMessageBuffer.addMessage((TCPMessage) messageCreator.getMessage());
            synchronized (this.tcpClient.getTcpSender()) {
                this.tcpClient.getTcpSender().notify();
            }

        } catch (Exception e) {
            Log.e("Disconnect", "Disconnect error",e);
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        MessageCreator messageCreator = this.createMessageCreator(TCPMessageTypeEnum.DISCONNECT);
        try {
            this.tcpMessageBuffer.addMessage((TCPMessage) messageCreator.getMessage());
            synchronized (this.tcpClient.getTcpSender()) {
                this.tcpClient.getTcpSender().notify();
            }

        } catch (Exception e) {
            Log.e("Disconnect", "Disconnect error",e);
            throw new RuntimeException(e);
        }

    }

    public void setTouchUDPMessage(int scrollLine) {
        Log.d("UDP", "Creating message");
        Scroll scroll = new Scroll();
        scroll.setLineScroll(scrollLine);
;        MessageCreator messageCreator = this.createMessageCreator(UDPMessageTypeEnum.SCROLL, scroll);
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

        MessageCreator messageCreator = this.createMessageCreator( UDPMessageTypeEnum.MOVE,  move);
        synchronized (this.udpClient) {
            this.udpClient.setMessage((UDPMessage) messageCreator.getMessage());
            this.udpClient.notify();
        }


    }

    public void reconnect() {
        this.setDisconnected();
        this.udpClient.interrupt();
        this.tcpClient = new TCPClient(this, this.tcpMessageBuffer);
        this.udpClient = new UDPClient(this);
        this.broadcastListener = new BroadcastListener(this);
        this.broadcastListener.start();

    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected() {
        this.connected = true;
    }

    public void setDisconnected() {
        this.connected = false;
    }

    public void onDisconnectOrFail() {

        try {
            if (activitiesManager.isOnSettings()) {
                return;
            }
            this.udpClient.stopService();
            this.broadcastListener.interrupt();
            this.tcpClient.stopService();
            if (this.getConnectionStatus() == EConnectionStatus.INITIALIZED) {
                this.setConnectionStatus(EConnectionStatus.FAIL);
            } else {
                this.setConnectionStatus(EConnectionStatus.DISCONNECTED);
            }
            this.activitiesManager.runActivityWithScreen(ConnectionActivity.class);
        } catch (IOException e) {
            Log.e("NETWORK", "Cannot interrupt TCP", e);
            throw new RuntimeException(e);
        }

    }

    public void processNewConnection() {
        this.runUDP();
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
    private void runUDP() {
        Log.d("UDP", "Initilizing UDP");

        if (this.connected) {
            Log.d("UDP", "Starting UDP");
            this.udpClient.start();
        }
    }
}
