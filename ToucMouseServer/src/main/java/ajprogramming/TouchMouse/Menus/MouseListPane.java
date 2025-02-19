package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.Menus.MainElementsOptions.MouseListPaneOptions;
import ajprogramming.TouchMouse.Menus.Shared.DefaultPane;
import ajprogramming.TouchMouse.Mouse.IMouse;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MouseListPane extends DefaultPane {
    private final MainFrame mainFrame;
    private final HashMap<String, IMouse> miceHashMap;
    private final MouseListPaneOptions mouseListPaneOptions;
    private ButtonsPane buttonsPane;
    private MouseList mouseList;
    private ButtonsSettingsPane buttonsSettingsPane;
    private JLabel jLabel;

    public MouseListPane(MainFrame mainFrame, HashMap<String, IMouse> miceHashMap) {
        this.mouseListPaneOptions = new MouseListPaneOptions();
        this.mainFrame = mainFrame;
        this.miceHashMap = miceHashMap;
        this.initialize();

    }

    private void initialize() {
        this.jLabel = new MouseListLabel(this);
        this.mouseList = new MouseList(this, this.miceHashMap);
        this.buttonsPane = new ButtonsPane(this);
        this.buttonsSettingsPane = new ButtonsSettingsPane(this);
        this.setSize(new Dimension(this.mouseListPaneOptions.getWidth(), this.mouseListPaneOptions.getHeight()));


        this.setPreferredSize(new Dimension(this.mouseListPaneOptions.getWidth(), this.mouseListPaneOptions.getHeight()));
        this.mainFrame.add(this);


    }

    public MainFrame getMainFrame() {
        return this.mainFrame;
    }

    public ButtonsPane getButtonPane() {
        return this.buttonsPane;
    }

    public void changeItems(HashMap<String, IMouse> mouseToChange) {
        this.mouseList.setListData(new MouseListModel(mouseToChange).getData());
    }


    public MouseList getMouseList() {
        return this.mouseList;
    }

    public void onDisconnectEvent() {
        this.mainFrame.onDisconnect((IMouse) this.mouseList.getSelectedValue());
    }

    public void onRemoveEvent() {
        this.mainFrame.onRemove((IMouse) this.mouseList.getSelectedValue());
    }


}
