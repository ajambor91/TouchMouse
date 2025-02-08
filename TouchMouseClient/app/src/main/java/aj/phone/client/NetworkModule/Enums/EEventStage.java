package aj.phone.client.NetworkModule.Enums;

public enum EEventStage {

    INITIALIZED("initialized"),
    PPM_INIT("ppm-initialized"),
    STARTED("started"),
    PROGRESS("progresss"),
    MOVING("moving"),
    SCROLL("SCROLL"),
    FINISHED("finished");

    private final String stage;

    EEventStage(String stage) {
        this.stage = stage;
    }

    public String getStage() {
        return this.stage;
    }
}
