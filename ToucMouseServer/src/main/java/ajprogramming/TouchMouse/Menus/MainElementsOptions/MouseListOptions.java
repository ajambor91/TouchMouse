package ajprogramming.TouchMouse.Menus.MainElementsOptions;

import ajprogramming.TouchMouse.Menus.Shared.IElementOptions;

import java.awt.*;

public class MouseListOptions implements IElementOptions {
    private final int width = 300;
    private final int height = 200;
    private final Color color = new Color(64, 64, 64);

    public int getWidth() {
        return this.width;
    }

    public Color getListColor() {
        return this.color;
    }
    public int getHeight() {
        return this.height;
    }
}
