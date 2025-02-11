package aj.phone.client.NetworkModule.UDP;


import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import aj.phone.client.NetworkModule.Enums.EMouseTouch;
import aj.phone.client.NetworkModule.Enums.EMouseTouchType;
import aj.phone.client.NetworkModule.Message.MessageCreator;
import aj.phone.client.NetworkModule.Message.Touch;
import aj.phone.client.NetworkModule.Message.UDPMessage;
import aj.phone.client.NetworkModule.NetworkModule;


public class UDPClient extends Thread {
    private DatagramSocket datagramSocket;
    private final MessageBuffer messageBuffer;
    private UDPMessage message;
    private final NetworkModule networkModule;

    public UDPClient(NetworkModule networkModule) {
        this.messageBuffer = new MessageBuffer();
        this.networkModule = networkModule;
    }

    public void stopService() {

        this.datagramSocket.close();
        this.interrupt();
    }

    public void setMessage(UDPMessage message) {
        if (message.getAction() instanceof Touch &&
                ((Touch) message.getAction()).getClick() == EMouseTouch.SINGLE_LPM &&
                ((Touch) message.getAction()).getClickType() == EMouseTouchType.UP
        ) {
            this.messageBuffer.putLastMsg(message);
        } else {
            this.messageBuffer.put(message);
        }
    }


    @Override
    public void run() {
        try {
            this.datagramSocket = new DatagramSocket();
            InetAddress ipAddr = InetAddress.getByName(this.networkModule.getHostAddress());
            Log.d("UDP", String.format("UDP initialized with host adrress: %s", this.networkModule.getHostAddress()));
            while (true) {
                sleep(10);
                if (!this.messageBuffer.isEmpty()) {
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
        return messageCreator.bytefyBufferMessage();
    }

}


