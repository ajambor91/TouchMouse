package ajprogramming.TouchMouse.Network.Enums;

public enum MessageTypes {
    BROADCAST("broadcast"),
    TCP("tcp"),
    UDP("UDP");

    private final String messageType;

    MessageTypes(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return this.messageType;
    }
}
