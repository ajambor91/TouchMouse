package ajprogramming.TouchMouse.Network.Messages;

import ajprogramming.TouchMouse.Network.UDPAction;

public class KeyboardKey implements UDPAction {

    private int keyCode;

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}
