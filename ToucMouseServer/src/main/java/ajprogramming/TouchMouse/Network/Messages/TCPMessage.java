package ajprogramming.TouchMouse.Network.Messages;

import ajprogramming.TouchMouse.Network.Enums.TCPMessageTypeEnum;

public class TCPMessage implements INetworkMessage {
    private String mouseAddress;
    private String mouseId;
    private String sessionId;
    private TCPMessageTypeEnum type;
    private String hostAddress;
    private String appName;
    private String mouseName;
    public String getMouseId() {
        return this.mouseId;
    }

    public void setMouseId(String mouseId) {
        this.mouseId = mouseId;
    }

    @Override
    public String getHostname() {
        return "";
    }

    @Override
    public void setHostname(String hostname) {

    }


    public String getHostAddress() {
        return this.hostAddress;
    }

    public void setHostAddress(String address) {
        this.hostAddress = address;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public TCPMessage(){}

    public TCPMessageTypeEnum getType() {
        return this.type;
    }

    public void setType(TCPMessageTypeEnum type) {
        this.type = type;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMouseName() {
        return this.mouseName;
    }

    public void setMouseName(String mouseName) {
        this.mouseName = mouseName;
    }

    public void setMouseAddress(String mouseAddress) {
        this.mouseAddress = mouseAddress;
    }


    public String getMouseAddress() {
        return this.mouseAddress;
    }
}
