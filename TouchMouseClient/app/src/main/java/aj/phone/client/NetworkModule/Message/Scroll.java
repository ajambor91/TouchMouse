package aj.phone.client.NetworkModule.Message;

public class Scroll implements UDPAction {
    private int lineScroll = 0;

    public int getLineScroll() {
        return this.lineScroll;
    }

    public void setLineScroll(int lineScroll) {
        this.lineScroll = lineScroll;
    }
}
