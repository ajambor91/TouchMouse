package ajprogramming.TouchMouse.Network.Enums;

public enum EConnectionStatus {
    INITIALIZED("initialized"),
    CONNECTED("connected"),
    DISCONNECTED("disconnected"),
    FAIL("failed"),
    RECONNECTING("reconnecting");
    private final String status;

    EConnectionStatus(String messageType) {
        this.status = messageType;
    }

    public String EConnectionStatus() {
        return this.status;
    }
}
