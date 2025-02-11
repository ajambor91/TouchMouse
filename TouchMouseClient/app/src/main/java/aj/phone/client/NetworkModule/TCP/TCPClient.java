package aj.phone.client.NetworkModule.TCP;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import aj.phone.client.NetworkModule.Enums.EConnectionStatus;
import aj.phone.client.NetworkModule.Enums.MessageTypes;
import aj.phone.client.NetworkModule.Enums.TCPMessageTypeEnum;
import aj.phone.client.NetworkModule.Message.MessageCreator;
import aj.phone.client.NetworkModule.Message.TCPMessage;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.Utils.Config;

public class TCPClient extends Thread {
    private final TCPMessageBuffer tcpMessageBuffer;
    private final int maxTries = 10;
    private final boolean connected = false;
    private final int currentTry = 0;
    private final NetworkModule networkModule;
    private volatile boolean running = true;
    private TCPSender tcpSender;
    private Socket socket;

    public TCPClient(NetworkModule networkModule, TCPMessageBuffer tcpMessageBuffer) {
        this.networkModule = networkModule;
        this.tcpMessageBuffer = tcpMessageBuffer;
    }

    @Override
    public void run() {
        this.socket = new Socket();

        while (this.running) {
            Log.d("TCP", String.format("Trying connect %s", this.maxTries));
            try {
                this.socket.connect(new InetSocketAddress(this.networkModule.getHostAddress(), 9123), 5000);
                Log.d("TCP", "Connected to host");
                this.tcpSender = new TCPSender(socket, this.tcpMessageBuffer);
                this.networkModule.setConnected();
                this.initConnection();
                this.listenForMessage(this.socket);
            } catch (SocketException socketException) {
                if (this.networkModule.isConnected()) {
                    this.running = false;
                    this.networkModule.onDisconnectOrFail();
                }
            } catch (IOException e) {
                this.running = false;
                Log.d("TCP", "Cannot connect");

            }
        }
        if (!this.networkModule.isConnected()) {

            this.networkModule.onDisconnectOrFail();
        }

    }

    public void stopService() throws IOException {
        this.socket.close();
        this.interrupt();
    }

    public TCPSender getTcpSender() {
        return this.tcpSender;
    }

    private void initConnection() throws IOException {
        MessageCreator messageCreator = null;
        if (this.networkModule.getConnectionStatus() != EConnectionStatus.DISCONNECTED) {
            Log.d("TCP", "Creating welcome message");
            messageCreator = new MessageCreator(this.networkModule.getAppId(), null, this.networkModule.getHostAddress(), this.networkModule.getAddress(), this.networkModule.getMouseName(), this.networkModule.getAppName(), TCPMessageTypeEnum.CONNECTION);
        } else {
            Log.d("TCP", "Creating reconnect message");
            messageCreator = new MessageCreator(this.networkModule.getAppId(), null, this.networkModule.getHostAddress(), this.networkModule.getAddress(), this.networkModule.getMouseName(), this.networkModule.getAppName(), TCPMessageTypeEnum.RECONNECT);
        }

        TCPMessage tcpMessage = (TCPMessage) messageCreator.getMessage();
        this.tcpMessageBuffer.addMessage(tcpMessage);
        synchronized (this.tcpSender) {
            this.tcpSender.notify();

        }
    }

    private void listenForMessage(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        try {
            Log.d("TCP", "Initializing TCP listening");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                Log.d("TCP", String.format("Received message: %s", message));
                MessageCreator messageCreator = new MessageCreator(message, MessageTypes.TCP);
                Log.d("TCP", String.format("Converted message: %s", messageCreator.jsonfyMessage()));
                Config.getInstance().addHost(this.networkModule);
                this.processMessage((TCPMessage) messageCreator.getMessage());
            }
        } catch (SocketException socketException) {
            this.networkModule.onDisconnectOrFail();

        }
    }

    private void processMessage(TCPMessage tcpMessage) {
        Log.d("TCP", String.format("Processing TCP message with action %s", tcpMessage.getType().getMessageType()));
        if (tcpMessage.getType() == TCPMessageTypeEnum.CONNECTION) {
            Log.d("TCP", "Processing connection message");
            this.networkModule.setConnectionStatus(EConnectionStatus.CONNECTED);
            this.networkModule.processNewConnection();
        } else if (tcpMessage.getType() == TCPMessageTypeEnum.DISCONNECT) {
            Log.d("TCP", "Processing disconnection message");
            this.networkModule.setConnectionStatus(EConnectionStatus.DISCONNECTED);
            this.networkModule.onDisconnectOrFail();
        } else if (tcpMessage.getType() == TCPMessageTypeEnum.RECONNECT_ANSWER) {
            Log.d("TCP", "Processing connection message");
            this.networkModule.setConnectionStatus(EConnectionStatus.CONNECTED);
            this.networkModule.processNewConnection();
        }

    }
}
