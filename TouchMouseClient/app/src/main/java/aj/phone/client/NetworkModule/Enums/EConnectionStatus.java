package aj.phone.client.NetworkModule.Enums;

public enum EConnectionStatus {
    BROADCAST_TIMEOUT("broadcast-timeout"),
    INITIALIZED("initialized"),
    LISTEN("listen"),
    CONNECTED("connected"),
    DISCONNECTED("disconnected"),
    DISCONNECTED_HOST("diconnected-host"),
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
