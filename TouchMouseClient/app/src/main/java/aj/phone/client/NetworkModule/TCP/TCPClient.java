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
import aj.phone.client.NetworkModule.NetworkService;
import aj.phone.client.Utils.Config;

public class TCPClient extends Thread {


    private final TCPMessageBuffer tcpMessageBuffer;

    private final NetworkService networkService;
    private volatile boolean running = true;
    private TCPSender tcpSender;
    private Socket socket;

    public TCPClient(NetworkService networkService, TCPMessageBuffer tcpMessageBuffer) {
        this.networkService = networkService;
        this.tcpMessageBuffer = tcpMessageBuffer;
    }

    @Override
    public void run() {
        this.socket = new Socket();

        while (this.running) {
            try {
                this.socket.connect(new InetSocketAddress(this.networkService.getHostAddress(), 9123), 5000);
                if (this.tcpSender == null) {
                    this.tcpSender = new TCPSender(socket, this.tcpMessageBuffer);

                }

                Log.d("TCP", "Connected to host");
                this.listenForMessage(this.socket);
            } catch (SocketException socketException) {
                if (this.networkService.getConnectionStatus() != EConnectionStatus.DISCONNECTED) {
                    this.networkService.processTCPSocketException();
                }
                this.running = false;

            } catch (IOException e) {
                this.running = false;
                Log.d("TCP", "Cannot connect");

            }
        }

    }

    public void stopService() throws IOException {
        if (this.socket != null) {
            this.socket.close();
        }
        if (this.tcpSender != null) {
            this.tcpSender.stopService();
        }

        this.interrupt();
    }

    public TCPSender getTcpSender() {
        Log.d("TCP Sender", "Get TCPSender");
        return this.tcpSender;
    }

    private void initConnection() throws IOException {
        MessageCreator messageCreator = null;
        if (this.networkService.getConnectionStatus() != EConnectionStatus.DISCONNECTED) {
            Log.d("TCP", "Creating welcome message");
            messageCreator = new MessageCreator(this.networkService.getAppId(), null, this.networkService.getHostAddress(), this.networkService.getAddress(), this.networkService.getMouseName(), this.networkService.getAppName(), TCPMessageTypeEnum.CONNECTION);
        } else {
            Log.d("TCP", "Creating reconnect message");
            messageCreator = new MessageCreator(this.networkService.getAppId(), null, this.networkService.getHostAddress(), this.networkService.getAddress(), this.networkService.getMouseName(), this.networkService.getAppName(), TCPMessageTypeEnum.RECONNECT);
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
                Config.getInstance().addHost(this.networkService);
                this.processMessage((TCPMessage) messageCreator.getMessage());
            }
            Log.d("TCP", "Socket closed");
        } catch (SocketException socketException) {
            Log.d("TCP", "Socket closed");

            if (this.networkService.getConnectionStatus() != EConnectionStatus.DISCONNECTED) {
                Log.d("TCP", "Socket closed");

                this.networkService.processHostDisconnect();
            }

        }
    }

    private void processMessage(TCPMessage tcpMessage) {
        Log.d("TCP", String.format("Processing TCP message with action %s", tcpMessage.getType().getMessageType()));
        if (tcpMessage.getType() == TCPMessageTypeEnum.CONNECTION) {
            Log.d("TCP", "Processing connection message");
            this.networkService.processNewConnection();
        } else if (tcpMessage.getType() == TCPMessageTypeEnum.DISCONNECT) {
            Log.d("TCP", "Processing disconnection message");
            this.networkService.processDisconnectMessage();
        } else if (tcpMessage.getType() == TCPMessageTypeEnum.RECONNECT_ANSWER) {
            Log.d("TCP", "Processing connection message");
            this.networkService.processReconnecting();
        }

    }
}
