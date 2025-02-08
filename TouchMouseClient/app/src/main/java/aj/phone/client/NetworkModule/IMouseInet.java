package aj.phone.client.NetworkModule;

import aj.phone.client.IHost;

public interface IMouseInet extends IHost {
    String getAddress();

    void setAddress(String address);

    String getMouseName();

    void setMouseName(String mouseName);

    String getSessionId();

    String getAppId();

    void setAppId(String appId);
}
