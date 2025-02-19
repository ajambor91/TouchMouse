package aj.phone.client.NetworkModule.Enums;

public enum TCPMessageTypeEnum {
    CONNECTION("connection"),
    DISCONNECT("disconnect"),
    KEYBOARD_SHOW("keyboard_show"),

    KEYBOARD_HIDE("keyboard_hide"),
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
