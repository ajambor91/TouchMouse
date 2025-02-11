package ajprogramming.TouchMouse.Menus.OptionsFrame;

import ajprogramming.TouchMouse.Menus.MainFrame;
import ajprogramming.TouchMouse.Menus.OptionsFrame.OptionsElementsOptions.OptionsDialogOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

public class OptionsDIalog extends JDialog {
    private final OptionsDialogOptions optionsDialog;
    private final MainFrame mainFrame;
    private final MainOptionsPane mainOptionsPane;
    public OptionsDIalog(MainFrame mainFrame) {
        super(mainFrame, "Info");
        this.mainFrame = mainFrame;
        this.optionsDialog = new OptionsDialogOptions();
        this.mainOptionsPane = new MainOptionsPane(this);
        this.initialize();
    }

    private void initialize() {
        this.setSize(this.optionsDialog.getWidth(), this.optionsDialog.getHeight());
        this.setMaximumSize(new Dimension(this.optionsDialog.getHeight(), this.optionsDialog.getHeight()));
        this.setResizable(false);
        this.setLocation(MainFrame.getPoint());
        this.setVisible(true);
        this.addListeners();
    }

    private void addListeners() {
        this.addWindowListener(new WindowAdapter() {


            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
                mainFrame.closeOptions();
            }
        });
    }
}
