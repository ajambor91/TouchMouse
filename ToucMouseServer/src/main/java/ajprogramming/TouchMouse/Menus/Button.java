package ajprogramming.TouchMouse.Menus;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {

    private final ButtonOptions buttonOptions;
    public Button(String buttonText) {
        super(buttonText);
        this.buttonOptions = new ButtonOptions();
        this.initialize();
    }

    private void initialize() {
        this.setPreferredSize(new Dimension(this.buttonOptions.getWidth(), this.buttonOptions.getHeight()));
    }
}
