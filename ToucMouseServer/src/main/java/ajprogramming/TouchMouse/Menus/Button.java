package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.Menus.MainElementsOptions.ButtonOptions;

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
        this.setBackground(this.buttonOptions.getBackgroundColor());
        this.setForeground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        this.setPreferredSize(new Dimension(this.buttonOptions.getWidth(), this.buttonOptions.getHeight()));
    }
}
