package ajprogramming.TouchMouse.Menus.OptionsFrame.OptionsElementsOptions;

import ajprogramming.TouchMouse.Menus.Shared.IElementOptions;

import java.awt.*;


public class DataTextFieldOptions implements IElementOptions {

    private final int width = 300;
    private final int height = 30;
    private final Color backgroundColor = new Color(64, 64, 64);

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
