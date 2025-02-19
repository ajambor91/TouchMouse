package ajprogramming.TouchMouse.Network.Messages;

import ajprogramming.TouchMouse.Network.UDPAction;

public class Scroll implements UDPAction {
    private int lineScroll = 0;

    public int getLineScroll() {
        return this.lineScroll;
    }

    public void setLineScroll(int lineScroll) {
        this.lineScroll = lineScroll;
    }
}
