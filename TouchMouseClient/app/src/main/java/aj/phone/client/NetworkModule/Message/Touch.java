package aj.phone.client.NetworkModule.Message;

import aj.phone.client.NetworkModule.Enums.EMouseTouch;
import aj.phone.client.NetworkModule.Enums.EMouseTouchType;

public class Touch implements UDPAction {
    private EMouseTouch click;
    private EMouseTouchType clickType;


    public EMouseTouchType getClickType() {
        return this.clickType;
    }

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
