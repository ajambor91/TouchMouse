package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.Menus.MainElementsOptions.ButtonsPaneOptions;
import ajprogramming.TouchMouse.Menus.Shared.DefaultPane;

import javax.swing.*;
import java.awt.*;

public class ButtonsPane extends DefaultPane {
    private final MouseListPane mouseListPane;
    private final ButtonsPaneOptions buttonsPaneOptions;
    private JButton disconnectButton;
    private JButton removeButton;

    public ButtonsPane(MouseListPane mouseListPane) {
        this.mouseListPane = mouseListPane;

        this.buttonsPaneOptions = new ButtonsPaneOptions();
        this.initialize();
        this.initializeButtons();

    }

    private void onRemove() {
        this.mouseListPane.onRemoveEvent();
    }

    private void onDisconnect() {
        this.mouseListPane.onDisconnectEvent();
    }

    public void toggleButton(boolean enabled) {
        this.removeButton.setEnabled(enabled);
        this.disconnectButton.setEnabled(enabled);
        this.setVisible(true);
        this.mouseListPane.add(this);
    }

    private void initialize() {
        this.setPreferredSize(new Dimension(this.buttonsPaneOptions.getWidth(), this.buttonsPaneOptions.getHeight()));
    }

    private void initializeButtons() {
        this.removeButton = new Button("Remove selected");
        this.disconnectButton = new Button("Disconnect selected");
        this.add(this.disconnectButton);
        this.disconnectButton.setVisible(true);
        this.add(this.removeButton);
        this.removeButton.setVisible(true);
        this.toggleButton(false);
        this.removeButton.addActionListener(e -> onRemove());
        this.disconnectButton.addActionListener(e -> onDisconnect());
    }
}
