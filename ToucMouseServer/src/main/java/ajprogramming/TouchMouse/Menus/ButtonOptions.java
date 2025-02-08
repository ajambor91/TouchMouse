package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.Menus.Shared.IElementOptions;

public class ButtonOptions implements IElementOptions {

    private int height = 30;
    private int width = 150;
    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
