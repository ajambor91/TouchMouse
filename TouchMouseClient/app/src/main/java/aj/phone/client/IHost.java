package aj.phone.client;

import aj.phone.client.NetworkModule.Enums.EConnectionStatus;

public interface IHost {
    String getName();

    void setName(String name);

    boolean isActiveHost();
    void setActiveHost(boolean isActive);
    String getHostAddress();

    void setHostAddress(String hostAdress);

    String getAppName();

    void setAppName(String appName);

    boolean isDefault();

    EConnectionStatus getConnectionStatus();

    void setConnectionStatus(EConnectionStatus connectionStatus);

    void setIsDefault(boolean isDefault);

}


