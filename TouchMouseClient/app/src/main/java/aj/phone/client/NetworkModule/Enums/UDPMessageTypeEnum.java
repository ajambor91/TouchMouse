package aj.phone.client.NetworkModule.Enums;

public enum UDPMessageTypeEnum {
    MOVE("move"),
    TOUCH("touch"),
    SCROLL("scroll");

    private final String messageType;

    UDPMessageTypeEnum(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return this.messageType;
    }
}
