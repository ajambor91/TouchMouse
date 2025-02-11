package ajprogramming.TouchMouse.Menus.OptionsFrame;

import ajprogramming.TouchMouse.Menus.OptionsFrame.OptionsElementsOptions.DataTextFieldOptions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DataTextField extends JTextField {
    private final DataTextFieldOptions dataTextFieldOptions;
    private final MainOptionsPane mainOptionsPane;

    public DataTextField(String value, MainOptionsPane mainOptionsPane) {
        super(value);
        this.dataTextFieldOptions = new DataTextFieldOptions();
        this.mainOptionsPane = mainOptionsPane;
        this.initialize();
    }

    private void initialize() {
        Dimension dimension = new Dimension(this.dataTextFieldOptions.getWidth(), this.dataTextFieldOptions.getHeight());
        this.setBackground(this.dataTextFieldOptions.getBackgroundColor());
        this.setForeground(Color.WHITE);
        this.setSize(dimension);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.setPreferredSize(dimension);
        this.setEditable(false);
        this.mainOptionsPane.add(this);
    }
}
