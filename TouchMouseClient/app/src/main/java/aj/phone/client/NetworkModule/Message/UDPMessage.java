package aj.phone.client.NetworkModule.Message;


import aj.phone.client.NetworkModule.Enums.UDPMessageTypeEnum;

public class UDPMessage implements NetworkMessage {
    private String id;

    private String sessionId;

    private UDPMessageTypeEnum type;
    private String address;

    private UDPAction action;

    public UDPMessage() {
    }

    public String getMouseId() {
        return this.id;
    }

    public void setMouseId(String id) {
        this.id = id;
    }

    public String getHostAddress() {
        return this.address;
    }

    public void setHostAddress(String address) {
        this.address = address;
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


    public void setType(UDPMessageTypeEnum type) {
        this.type = type;
    }

    public UDPAction getAction() {
        return this.action;
    }

    public void setAction(UDPAction action) {
        this.action = action;
    }

}
