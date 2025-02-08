
package ajprogramming.TouchMouse.Network;

import ajprogramming.TouchMouse.Network.Enums.BroadcastMessageTypeEnum;
import ajprogramming.TouchMouse.Network.Messages.BroadcastMessage;
import ajprogramming.TouchMouse.Network.Utils.NetworkUtils;
import ajprogramming.TouchMouse.Utils.LoggerEx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Broadcast extends Thread{
    private volatile boolean running = true;
    private final IHost host;
    private final LoggerEx loggerEx;
    private BroadcastMessage broadcastMessage;

    public Broadcast(IHost host) {
        this.host = host;
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.createBroadcastMessage();
    }
    @Override
    public void run(){
        try {
            final ArrayList<String> dataToSend = new ArrayList<>();
            dataToSend.add("PhoneMouse");
            dataToSend.add(UDPServerConfig.getInstance().hostName);
            final ArrayList<InetAddress> interfaces = NetworkUtils.listAllBroadcastAddresses();
            int length = interfaces.size();
            int i = 0;
            DatagramSocket socket = new DatagramSocket();
            MessageCreator messageCreator = new MessageCreator(this.broadcastMessage);
            while (running) {
               InetAddress address = InetAddress.getByName(interfaces.get(i).getHostAddress());

                i++;
                if (i == length) {
                   i = 0;
                }
                socket.setBroadcast(true);
                byte[] broadcastMsg = messageCreator.bytefyMessage();
                DatagramPacket packet;
                packet = new DatagramPacket(broadcastMsg, broadcastMsg.length, address, 9123);
                socket.send(packet);
                synchronized (this) {
                    this.wait(100);

                }
                
            }
        } catch (IOException | InterruptedException ex) {
            this.loggerEx.warning("Broadcast", ex.getMessage());
            this.running = false;
        }
    }

    private void createBroadcastMessage() {
        MessageCreator messageCreator = new MessageCreator(
                null, this.host.getSessionId(), this.host.getHostAddress(), this.host.getHostname(), "JaTouch", BroadcastMessageTypeEnum.BROADCAST
        );
        this.broadcastMessage = (BroadcastMessage) messageCreator.getMessage();
    }


}
