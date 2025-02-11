package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.Menus.MainElementsOptions.ButtonsSettingsPaneOptions;
import ajprogramming.TouchMouse.Menus.Shared.DefaultPane;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsSettingsPane extends DefaultPane {
    private final ButtonsSettingsPaneOptions buttonsSettingsPaneOptions;
    private final MouseListPane mouseListPane;
    private Button optionsButton;

    public ButtonsSettingsPane(MouseListPane mouseListPane) {
        super(new BorderLayout());
        this.mouseListPane = mouseListPane;
        this.buttonsSettingsPaneOptions = new ButtonsSettingsPaneOptions();
        this.initialize();

    }

    private void initialize() {
        this.optionsButton = new Button("Options");
        this.setPreferredSize(new Dimension(this.buttonsSettingsPaneOptions.getWidth(), this.buttonsSettingsPaneOptions.getHeight()));

        this.add(this.optionsButton, BorderLayout.SOUTH);
        this.optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mouseListPane.getMainFrame().showOptions();
            }
        });
        this.setVisible(true);
        this.mouseListPane.add(this);
    }

    private void openOptions() {
        this.mouseListPane.getMainFrame().showOptions();
    }
}
