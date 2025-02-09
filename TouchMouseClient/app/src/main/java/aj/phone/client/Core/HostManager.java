package aj.phone.client.Core;

import aj.phone.client.IHost;
import aj.phone.client.NetworkModule.Host;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.Utils.Config;

public class HostManager {

    private NetworkModule activeHost;
    private Host currentHost;
    private Config config;
    public HostManager(IHost host) {
        this.config = Config.getInstance();
        if (host instanceof NetworkModule) {
            this.activeHost = (NetworkModule) host;
            this.currentHost = (Host) host;
        } else if (host instanceof Host) {
            this.activeHost = null;
            this.currentHost = (Host) host;
        }
    }

    public void removeHost() {
        this.disconnectHost();
        if (this.activeHost !=  null) {
            this.activeHost.disconnect();
        }
        this.config.removeHost(this.currentHost);
    }


    public void disconnectHost() {
        if (this.activeHost != null) {
            this.activeHost.disconnect();
        }
    }
}
