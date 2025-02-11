package ajprogramming.TouchMouse.Menus.OptionsFrame;

import ajprogramming.TouchMouse.Menus.OptionsFrame.OptionsElementsOptions.OptionLabelOptions;

import javax.swing.*;
import java.awt.*;

public class OptionLabel extends JLabel {
    private final OptionLabelOptions optionLabelOptions;
    private final MainOptionsPane optionsPane;
    public OptionLabel(String text, MainOptionsPane optionsPane) {
        super(text);
        this.optionLabelOptions = new OptionLabelOptions();
        this.optionsPane = optionsPane;
        this.initialize();
    }

    private void initialize() {
        Dimension dimension = new Dimension(this.optionLabelOptions.getWidth(), this.optionLabelOptions.getHeight());
        this.setForeground(Color.WHITE);
        this.setPreferredSize(dimension);
        this.setSize(dimension);
        this.setVisible(true);
        this.optionsPane.add(this);
    }
}
