package ajprogramming.TouchMouse.Network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Adam
 */
public class UDPServerConfig {

    public static UDPServerConfig instance;
    public final int PORT = 9123;
    public final int BUFFER_SIZE = 1024;
    public String hostName;

    private UDPServerConfig() {
        this.init();
    }

    public static UDPServerConfig getInstance() {
        if (instance == null) {
            instance = new UDPServerConfig();
        }
        return instance;
    }

    private void init() {
        try {
            this.hostName = this.getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(UDPServerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }


}
