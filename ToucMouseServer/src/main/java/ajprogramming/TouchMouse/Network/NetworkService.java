package ajprogramming.TouchMouse.Network;

import ajprogramming.TouchMouse.AppConfig;
import ajprogramming.TouchMouse.Mouse.MouseHandler;
import ajprogramming.TouchMouse.Network.Enums.ENetworkStatus;
import ajprogramming.TouchMouse.Utils.LoggerEx;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class NetworkService extends Host {
    private ENetworkStatus networkStatus;
    private static NetworkService instance;
    private final MouseHandler mouseHandler;
    private UDPService udpService;
    private TCPService tcpService;
    private Broadcast broadcast;
    private LoggerEx loggerEx;
    private NetworkService() {
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.initializeHost();
        this.mouseHandler = new MouseHandler(this);
        this.tcpService = new TCPService(this.mouseHandler);
        this.udpService = new UDPService(this.mouseHandler);
        this.broadcast = new Broadcast(this);
        this.loggerEx.info("Network service initialized");


    }

    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }

    public void runServer() {
        this.tcpService.start();
        this.udpService.start();
        this.networkStatus = ENetworkStatus.RUNNING;
        this.loggerEx.info("Network service running");


    }


    public void runBroadcast() {
        this.broadcast.start();
        this.loggerEx.info("Network service, Broadcast started");

    }

    public void stopBroadcast() {
        this.broadcast.interrupt();
        this.broadcast = new Broadcast(this);
        this.loggerEx.info("Network service, Broadcast stopped");

    }

    public void disconnect() {
        this.mouseHandler.disconnectAllMouse();
        this.tcpService.disconnect();
        this.udpService.disconnect();
        this.networkStatus = ENetworkStatus.DISCONNECTED;
        this.loggerEx.info("Network service disconnected");


    }

    public void reconnectAfterForce() {
        if (this.tcpService.getIsForceDisconnected()) {
            this.tcpService = new TCPService(this.mouseHandler);
            this.udpService = new UDPService(this.mouseHandler);
            this.tcpService.start();
            this.udpService.start();
            this.networkStatus = ENetworkStatus.RUNNING;
            this.loggerEx.info("Network service reconnect");

        }
    }
    public ENetworkStatus status() {
        return this.networkStatus;
    }

    public MouseHandler getMouseHandler() {
        return this.mouseHandler;
    }

    public boolean isBroadcastingInterrupted() {
        return this.broadcast.isInterrupted();
    }

    private void initializeHost() {
        try {
            this.networkStatus = ENetworkStatus.INITIALIZED;
            InetAddress inet = InetAddress.getLocalHost();
            this.setHostname(inet.getHostName());
            this.setHostAddress(String.valueOf(inet.getHostAddress()));
            this.setSessionId(String.valueOf(UUID.randomUUID()));
            this.setAppName(AppConfig.getInstance().getAppName());
            this.loggerEx.info("Network service host initialized");

        } catch (UnknownHostException e) {
            this.loggerEx.warning("Network service, Host initialization", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
