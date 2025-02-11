package ajprogramming.TouchMouse.Menus.MainElementsOptions;

import ajprogramming.TouchMouse.Menus.Shared.IElementOptions;

import java.awt.*;

public class ButtonOptions implements IElementOptions {

    private int height = 30;
    private int width = 150;
    private Color backgroundColor = new Color(64, 64, 64);

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }
    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
