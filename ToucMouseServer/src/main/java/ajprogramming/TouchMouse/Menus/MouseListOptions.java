package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.Menus.Shared.IElementOptions;

public class MouseListOptions implements IElementOptions {
    private final int width = 300;
    private final int height = 200;

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
