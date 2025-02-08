
package ajprogramming.TouchMouse.Network;

import ajprogramming.TouchMouse.Mouse.Mouse;
import ajprogramming.TouchMouse.Mouse.MouseHandler;
import ajprogramming.TouchMouse.Network.Messages.UDPMessage;
import ajprogramming.TouchMouse.Network.Utils.NetworkUtils;
import ajprogramming.TouchMouse.Utils.LoggerEx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam
 */
public class UDPService extends Thread {
    private volatile boolean run = true;
    private final MouseHandler mouseHandler;
    private final LoggerEx loggerEx;
    private DatagramSocket datagramSocket;
    public UDPService(MouseHandler mouseHandler) {
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.mouseHandler = mouseHandler;
        this.loggerEx.info("UDP service initialized");

    }

    @Override()
    public void run() {
        this.loggerEx.info("UDP service started");

        try {
            final ArrayList<InetAddress> interfaces = NetworkUtils.listAllOwnAddresses();
            this.datagramSocket= new DatagramSocket(UDPServerConfig.getInstance().PORT);
            DatagramPacket receivedPacket
                    = new DatagramPacket(new byte[UDPServerConfig.getInstance().BUFFER_SIZE], 1024);
            while (this.run) {
                this.datagramSocket.receive(receivedPacket);

                int length = receivedPacket.getLength();
                InetAddress address = receivedPacket.getAddress();

                if (!this.isOwnInterface(address, interfaces)) {
                    ActionCreator actionCreator = new ActionCreator(receivedPacket.getData(), length);
                    List<UDPMessage> message = actionCreator.getAction();
                    if (message != null) {
                        this.mouseHandler.onAction(actionCreator.getAction());
                    }


                }
                sleep(5);
            }
        } catch (IOException ex) {
            this.loggerEx.warning("UDP Service warning", ex.getMessage());
        } catch (InterruptedException e) {
            this.loggerEx.warning("UDP Service interrupted", e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        if (this.datagramSocket != null) {
            this.datagramSocket.disconnect();
            this.datagramSocket.close();
            this.datagramSocket = null;
        }
        this.run = false;
        this.loggerEx.info("UDP service disconnected");
        this.interrupt();
    }

    private boolean isOwnInterface(InetAddress address, ArrayList<InetAddress> inetAddressArrayList) {
        return inetAddressArrayList.stream().anyMatch(addr -> addr.getHostAddress().equals(address.getHostAddress()));
    }
    
}
