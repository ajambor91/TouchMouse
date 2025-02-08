package aj.phone.client.NetworkModule.Enums;

public enum BroadcastMessageTypeEnum {
    BROADCAST("BROADCAST");

    private final String messageType;

    BroadcastMessageTypeEnum(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return this.messageType;
    }
}
