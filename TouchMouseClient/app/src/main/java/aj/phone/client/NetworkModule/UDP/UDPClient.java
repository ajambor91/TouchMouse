package aj.phone.client.NetworkModule.UDP;


import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import aj.phone.client.NetworkModule.Message.MessageCreator;
import aj.phone.client.NetworkModule.Message.UDPMessage;
import aj.phone.client.NetworkModule.NetworkService;


public class UDPClient extends Thread {
    private final MessageBuffer messageBuffer;
    private final NetworkService networkService;
    private DatagramSocket datagramSocket;
    private UDPMessage message;

    public UDPClient(NetworkService networkService) {
        this.messageBuffer = new MessageBuffer();
        this.networkService = networkService;
    }

    public void stopService() {
        if (datagramSocket != null) {
            this.datagramSocket.close();
        }
        this.interrupt();
    }

    public void setMessage(UDPMessage message) {
        Log.d("Keyboard", "In udp client msg " + message.getAction());
        this.messageBuffer.put(message);

    }


    @Override
    public void run() {
        try {
            this.datagramSocket = new DatagramSocket();
            InetAddress ipAddr = InetAddress.getByName(this.networkService.getHostAddress());
            Log.d("UDP", String.format("UDP initialized with host adrress: %s", this.networkService.getHostAddress()));
            while (true) {
                sleep(10);
                if (!this.messageBuffer.isEmpty()) {
                    Log.d("Keyboard", "UDP MEssage");
                    byte[] msg = this.parseMessage(this.messageBuffer.getMessage());
                    DatagramPacket send_packet = new DatagramPacket(msg, msg.length, ipAddr, 9123);
                    this.datagramSocket.send(send_packet);
                    synchronized (this) {

                        this.wait();

                    }
                }
            }
        } catch (IOException e) {
            Log.d("UDP", String.format("UDP Error, %s", e.getMessage()));
        } catch (InterruptedException e) {
            if (isInterrupted()) {
                Log.d("UDP", "Thread is interrupted");
            } else {
                this.interrupt();
            }
        }
    }

    private byte[] parseMessage(UDPMessage[] message) {
        MessageCreator messageCreator = new MessageCreator(message);
        Log.d("Keyboard", messageCreator.jsonfyBufferedMessage());
        return messageCreator.bytefyBufferMessage();
    }

}


