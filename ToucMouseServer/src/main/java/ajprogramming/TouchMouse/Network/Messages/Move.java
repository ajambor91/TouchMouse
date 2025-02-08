package ajprogramming.TouchMouse.Network.Messages;

import ajprogramming.TouchMouse.Network.UDPAction;

public class Move implements UDPAction {
    private int x;
    private int y;

    public Move() {
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
