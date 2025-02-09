package aj.phone.client.Core;

import aj.phone.client.IHost;
import aj.phone.client.Utils.Config;

public class HostManager {
    private IHost host;
    private Config config;
    public HostManager(IHost host) {

    }

    public void removeHost() {
        this.config.removeHost(this.host);
    }
}
