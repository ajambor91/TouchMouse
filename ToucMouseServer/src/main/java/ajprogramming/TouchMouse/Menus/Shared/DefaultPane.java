package ajprogramming.TouchMouse.Menus.Shared;

import javax.swing.*;
import java.awt.*;

public abstract class DefaultPane extends JPanel {
    private final DefaultPaneOptions defaultPaneOptions;

    protected DefaultPane(LayoutManager layoutManager) {
        super(layoutManager);
        this.defaultPaneOptions = new DefaultPaneOptions();
        this.initialize();
    }

    protected DefaultPane() {
        this.defaultPaneOptions = new DefaultPaneOptions();
        this.initialize();
    }

    private void initialize() {
        this.setBackground(this.defaultPaneOptions.getBackgroundColor());
    }
}
