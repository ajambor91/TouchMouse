package ajprogramming.TouchMouse.Network.Enums;

public enum TCPMessageTypeEnum {
    CONNECTION("connection"),
    NAME_CHANGE("name-change"),
    DISCONNECT("disconnect"),
    RECONNECT("reconnect"),
    RECONNECT_ANSWER("reconnect-answer");


    private final String messageType;

    TCPMessageTypeEnum(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return this.messageType;
    }
}
