package aj.phone.client.NetworkModule;

import java.util.UUID;

public class MouseInet extends Host implements IMouseInet {

    private String appId;
    private final String sessionId;
    private String address;
    private String mouseName;

    public MouseInet() {
        this.sessionId = String.valueOf(UUID.randomUUID());
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMouseName() {
        return this.mouseName;
    }

    public void setMouseName(String mouseName) {
        this.mouseName = mouseName;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
