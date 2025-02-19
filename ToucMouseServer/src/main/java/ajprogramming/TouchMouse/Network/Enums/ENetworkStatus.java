package ajprogramming.TouchMouse.Network.Enums;

public enum ENetworkStatus {
    INITIALIZED("initialized"),
    RUNNING("running"),
    DISCONNECTED("disconnected");

    private final String status;

    ENetworkStatus(String status) {
        this.status = status;
    }

    public String ENetworkStatus() {
        return this.status;
    }
}
