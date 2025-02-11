package aj.phone.client.NetworkModule.Enums;

public enum TCPMessageTypeEnum {
    CONNECTION("connection"),
    DISCONNECT("disconnect"),

    RECONNECT("reconnect"),
    NAME_CHANGE("name-change"),
    RECONNECT_ANSWER("reconnect-answer");


    private final String messageType;

    TCPMessageTypeEnum(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return this.messageType;
    }
}
