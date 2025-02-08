package ajprogramming.TouchMouse.Menus.Shared;

import javax.swing.*;

public class DefaultScrollPane extends JScrollPane{
    private final DefaultPaneOptions defaultPaneOptions;
    public DefaultScrollPane(DefaultPane pane) {
        super(pane);
        this.defaultPaneOptions = new DefaultPaneOptions();
        this.initialize();
    }

    private void initialize() {
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setBackground(this.defaultPaneOptions.getBackgroundColor()
        );
    }
}
