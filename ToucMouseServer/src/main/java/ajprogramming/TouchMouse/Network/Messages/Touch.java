package ajprogramming.TouchMouse.Network.Messages;

import ajprogramming.TouchMouse.Network.Enums.EMouseTouch;
import ajprogramming.TouchMouse.Network.Enums.EMouseTouchType;
import ajprogramming.TouchMouse.Network.UDPAction;

public class Touch implements UDPAction {
    private EMouseTouch click;
    private EMouseTouchType clickType;


    public EMouseTouchType getClickType() {
        return this.clickType;
    };

    public void setClickType(EMouseTouchType clickType) {
        this.clickType = clickType;
    }
    public EMouseTouch getClick() {
        return this.click;
    }

    public void setClick(EMouseTouch click) {
        this.click = click;
    }
}
