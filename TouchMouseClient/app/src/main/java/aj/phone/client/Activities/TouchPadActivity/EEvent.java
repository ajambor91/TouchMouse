package aj.phone.client.Activities.TouchPadActivity;

public enum EEvent {
    MOVE("move"),
    TOUCH("touch");
    private final String eventType;

    EEvent(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return this.eventType;
    }
}
