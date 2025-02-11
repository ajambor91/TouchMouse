package aj.phone.client.Core;

import android.util.Log;

import aj.phone.client.IHost;
import aj.phone.client.NetworkModule.Host;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.Utils.Config;

public class HostManager {

    private NetworkModule activeHost;
    private Host currentHost;
    private final Config config;

    public HostManager(IHost host) {
        this.config = Config.getInstance();
        if (host instanceof NetworkModule) {
            this.activeHost = (NetworkModule) host;
            Log.d("HOST MANAGER", "Set active host");

            this.currentHost = (Host) host;
        } else if (host instanceof Host) {
            this.activeHost = null;
            this.currentHost = (Host) host;
        }
    }

    public void removeHost() {
        this.disconnectHost();
        if (this.activeHost != null) {
            this.activeHost.disconnect();
        }
        Log.d("HOST MANAGER", "Removing host");
        this.config.removeHost(this.currentHost);
    }


    public void disconnectHost() {
        if (this.activeHost != null) {
            Log.d("HOST MANAGER", "Disconnecting host");
            this.activeHost.disconnect();
        }
    }
}
