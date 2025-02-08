package ajprogramming.TouchMouse.Network.Messages;


import ajprogramming.TouchMouse.Network.Enums.UDPMessageTypeEnum;
import ajprogramming.TouchMouse.Network.UDPAction;
import ajprogramming.TouchMouse.Network.UDPMessageDeserializator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = UDPMessageDeserializator.class)
public class UDPMessage implements INetworkMessage {
    private String mouseAddress;
    private String mouseId;
    private String sessionId;
    private String appName;
    private UDPMessageTypeEnum type;
    private String mouseName;
    private boolean isSend = false;
    private String hostAddress;
    private UDPAction action;
    private String hostname;
    public UDPMessage() {}

    public String getMouseId() {
        return this.mouseId;
    }

    public void setMouseId(String id) {
        this.mouseId = id;
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

    public UDPMessageTypeEnum getType() {
        return this.type;
    }

    public void setSend(boolean send) {
        this.isSend = true;
    }

    public boolean getSend() {
        return this.isSend;
    }
    public void setType(UDPMessageTypeEnum type) {
        this.type = type;
    }

    public UDPAction getAction() {
        return this.action;
    }

    public void setAction(UDPAction action) {
        this.action = action;
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
