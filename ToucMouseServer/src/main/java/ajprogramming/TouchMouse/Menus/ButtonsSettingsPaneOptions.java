package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.Menus.Shared.IElementOptions;

public class ButtonsSettingsPaneOptions implements IElementOptions {

    private int height = 230;
    private int width = 300;
    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
