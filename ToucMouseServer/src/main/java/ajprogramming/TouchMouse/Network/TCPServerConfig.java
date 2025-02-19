package ajprogramming.TouchMouse.Network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServerConfig {
    public static TCPServerConfig instance;
    public final int PORT = 9123;
    public String hostName;

    private TCPServerConfig() {
        this.init();
    }

    public static TCPServerConfig getInstance() {
        if (instance == null) {
            instance = new TCPServerConfig();
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
