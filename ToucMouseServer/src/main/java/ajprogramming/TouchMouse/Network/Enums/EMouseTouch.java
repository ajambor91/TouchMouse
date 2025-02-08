package ajprogramming.TouchMouse.Network.Enums;

public enum EMouseTouch {
    SINGLE_LPM("single-lpm"),
    SINGLE_PPM("single-ppm");

    private final String gest;

    EMouseTouch(String messageType) {
        this.gest = messageType;
    }

    public String EMouseTouch() {
        return this.gest;
    }
}
