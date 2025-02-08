package ajprogramming.TouchMouse.Tray;

import ajprogramming.TouchMouse.Menus.MainFrame;
import ajprogramming.TouchMouse.Network.Enums.ENetworkStatus;
import ajprogramming.TouchMouse.Network.NetworkService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Tray {
    private static Tray instance;
    private final NetworkService networkService;
    private final PopupMenu popup = new PopupMenu();
    private final Image connectedState;
    private final HashMap<String, MenuItem> menuItemsList = new HashMap<>();
    private final HashMap<String, String> textsForMenus = new HashMap<>();
    private TrayIcon tray;

    private Tray() {
        this.addTexts();
        this.networkService = NetworkService.getInstance();
        this.connectedState = Toolkit.getDefaultToolkit().createImage("src/main/resources/icon_connected_win.png");
    }

    public static Tray getInstance() {
        if (Tray.instance == null) {
            Tray.instance = new Tray();
        }
        return Tray.instance;
    }

    public void createMenu() {
        if (SystemTray.isSupported()) {
            final SystemTray systemTray = SystemTray.getSystemTray();

            this.menuItemsList.put("disconnect", this.createMenuItem("disconnect", "Disconnect", this.createActionListenerDisconnect()));
            this.menuItemsList.put("broadcast", this.createMenuItem("broadcast", "Stop Broadcast", this.createActionListenerStopBroadcast()));
            this.menuItemsList.put("exit", this.createMenuItem("exit", "Exit", this.createActionListener()));

            for (MenuItem menu : this.menuItemsList.values()) {
                this.popup.add(menu);
            }
            Image trayIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/icons/icon_connected_win.png"))).getImage();
            this.tray = new TrayIcon(trayIcon, "PhoneMouse", this.popup);
            try {
                systemTray.add(this.tray);
                this.tray.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            createActionListenerForManuallyAddPhone();
                        }
                    }
                });
            } catch (AWTException ex) {
                Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void showMessage(String title, String message) {
        this.showMessage(title, message, TrayIcon.MessageType.INFO);
    }

    public void showMessage(String title, String message, TrayIcon.MessageType messageType) {
        this.tray.displayMessage(title, message, messageType);
    }

    private MenuItem createMenuItem(String key, String name, ActionListener listener) {
        final MenuItem menu = new MenuItem();
        menu.setLabel(name);
        menu.setName(key);
        menu.addActionListener(listener);
        return menu;

    }

    private ActionListener createActionListener() {
        final ActionListener listener = (ActionEvent e) -> {
            System.exit(0);
        };
        return listener;
    }

    private ActionListener createActionListenerDisconnect() {
        final ActionListener listener = (ActionEvent e) -> {
            MenuItem menuItem = this.menuItemsList.get("disconnect");
            if (this.networkService.status() != ENetworkStatus.DISCONNECTED) {
                menuItem.setLabel(this.textsForMenus.get("reconnect"));
                this.networkService.disconnect();

            } else {
                menuItem.setLabel(this.textsForMenus.get("disconnect"));
                this.networkService.reconnectAfterForce();

            }
        };
        return listener;
    }

    private ActionListener createActionListenerStopBroadcast() {
        final ActionListener listener = (ActionEvent e) -> {
            MenuItem item = this.menuItemsList.get("broadcast");
            if (NetworkService.getInstance().isBroadcastingInterrupted()) {
                item.setLabel(this.textsForMenus.get("stopBroadcasting"));
                this.networkService.runBroadcast();
            } else {
                item.setLabel(this.textsForMenus.get("startBroadcasting"));
                this.networkService.stopBroadcast();
            }

        };
        return listener;
    }

    private void createActionListenerForManuallyAddPhone() {
        MainFrame mainFrame = MainFrame.getInstance();

    }

    private void addTexts() {
        this.textsForMenus.put("exit", "Exit");
        this.textsForMenus.put("disconnect", "Disconnect");
        this.textsForMenus.put("reconnect", "Reconnect");
        this.textsForMenus.put("startBroadcasting", "Start Broadcasting");
        this.textsForMenus.put("stopBroadcasting", "Stop Broadcasting");

    }
}
