package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.Menus.MainElementsOptions.MouseListOptions;
import ajprogramming.TouchMouse.Mouse.IMouse;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MouseList extends JList {

    private final MouseListOptions mouseListOptions;
    private final MouseListPane jPanel;
    private final HashMap<String, IMouse> mouse;

    public MouseList(MouseListPane jPanel, HashMap<String, IMouse> mouse) {
        super(new MouseListModel(mouse).getData());
        this.mouseListOptions = new MouseListOptions();
        this.mouse = mouse;
        this.jPanel = jPanel;
        this.initialize();
    }

    private void initialize() {
        this.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.setBackground(this.mouseListOptions.getListColor());
        this.setForeground(Color.WHITE);
        this.setPreferredSize(new Dimension(this.mouseListOptions.getWidth(), this.mouseListOptions.getHeight()));
        this.setVisible(true);
        this.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                jPanel.getButtonPane().toggleButton(getSelectedIndex() != -1);
            }
        });
        this.jPanel.add(this);

    }

}
