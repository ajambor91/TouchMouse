package ajprogramming.TouchMouse.Menus;

import javax.swing.*;
import java.awt.*;

public class MouseListLabel extends JLabel {
    private final MouseListLabelOptions mouseListLabelOptions;
    private final MouseListPane mouseListPane;
    public MouseListLabel(MouseListPane mouseListPane) {
        super("Connected mice");
        this.mouseListLabelOptions = new MouseListLabelOptions();
        this.mouseListPane = mouseListPane;
        this.initialize();
    }

    private void initialize() {
        this.setText("Connected touches");
        this.setPreferredSize(new Dimension(this.mouseListLabelOptions.getWidth(), this.mouseListLabelOptions.getHeight()));
        this.setHorizontalAlignment(SwingConstants.LEFT);
        this.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.setVisible(true);
        this.mouseListPane.add(this);
    }
}
