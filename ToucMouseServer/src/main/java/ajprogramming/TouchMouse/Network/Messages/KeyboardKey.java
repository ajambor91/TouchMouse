package ajprogramming.TouchMouse.Network.Messages;

import ajprogramming.TouchMouse.Network.UDPAction;

public class KeyboardKey implements UDPAction {

    private String keyCode;

    public String getKeyCode() {
        return this.keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }
}
