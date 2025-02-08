package aj.phone.client.NetworkModule.Enums;

public enum EMouseTouchType {
    DOWN("down"),
    UP("up");

    private final String gest;

    EMouseTouchType(String messageType) {
        this.gest = messageType;
    }

    public String EMouseTouch() {
        return this.gest;
    }
}
