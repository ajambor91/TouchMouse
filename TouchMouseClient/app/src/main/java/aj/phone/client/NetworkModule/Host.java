package aj.phone.client.NetworkModule;

import aj.phone.client.IHost;
import aj.phone.client.NetworkModule.Enums.EConnectionStatus;

public class Host implements IHost {

    private EConnectionStatus connectionStatus;
    private String appName;
    private String hostAdress;
    private String name;
    private boolean isDefault;
    private String sessionId;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EConnectionStatus getConnectionStatus() {
        return this.connectionStatus;
    }

    public void setConnectionStatus(EConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getHostAddress() {
        return this.hostAdress;
    }

    public void setHostAddress(String hostAdress) {
        this.hostAdress = hostAdress.replace("/", "");

    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
