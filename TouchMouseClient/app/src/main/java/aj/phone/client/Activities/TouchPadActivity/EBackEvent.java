package aj.phone.client.Activities.TouchPadActivity;

public enum EBackEvent {

    BACK(4);
    private final int eventType;

    EBackEvent(int eventType) {
        this.eventType = eventType;
    }

    public int getEventType() {
        return this.eventType;
    }
}
