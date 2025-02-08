package aj.phone.client.NetworkModule;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import aj.phone.client.NetworkModule.Enums.MessageTypes;
import aj.phone.client.NetworkModule.Message.BroadcastMessage;
import aj.phone.client.NetworkModule.Message.MessageCreator;

public class BroadcastListener extends Thread {

    private final int timeout = 25000;
    private final NetworkModule networkModule;

    public BroadcastListener(NetworkModule networkModule) {
        this.networkModule = networkModule;
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        Log.d("BROADCAST", "Initializing broadcast");
        try (DatagramSocket datagramSocket = new DatagramSocket(9123)) {
            socket = datagramSocket;
            datagramSocket.setSoTimeout(this.timeout);
            byte[] receivedData = new byte[1472];
            DatagramPacket datagramPacket = new DatagramPacket(receivedData, receivedData.length);

            datagramSocket.receive(datagramPacket);
            MessageCreator messageCreator = new MessageCreator(new String(datagramPacket.getData(), 0, datagramPacket.getLength(), StandardCharsets.UTF_8), MessageTypes.BROADCAST);
            this.networkModule.runTCP((BroadcastMessage) messageCreator.getMessage());
        } catch (SocketTimeoutException socketTimeoutException) {
            Log.d("BROADCAST", "Cannot received broadcast data");
            socket.close();
            this.networkModule.onDisconnectOrFail();


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
