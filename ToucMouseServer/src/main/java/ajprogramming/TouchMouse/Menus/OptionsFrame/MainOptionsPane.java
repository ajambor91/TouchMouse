package ajprogramming.TouchMouse.Menus.OptionsFrame;

import ajprogramming.TouchMouse.Menus.Shared.DefaultPane;
import ajprogramming.TouchMouse.Menus.Shared.DefaultScrollPane;
import ajprogramming.TouchMouse.Network.Utils.NetworkUtils;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainOptionsPane extends DefaultPane {
    private final MainOptionsPaneOptions mainPaneOptions;
    private final OptionsDIalog optionsDIalog;

    public MainOptionsPane(OptionsDIalog optionsDIalog) {
        this.optionsDIalog = optionsDIalog;
        this.mainPaneOptions = new MainOptionsPaneOptions();
        this.initialize();
    }


    private void initialize() {
        try {
            this.setBorder(new EmptyBorder(50, 10, 10, 10));

            Dimension dimension = new Dimension(this.mainPaneOptions.getWidth(), this.mainPaneOptions.getHeight());
            this.setMaximumSize(dimension);
            this.setPreferredSize(dimension);
            this.setLayout(new FlowLayout());
            OptionLabel appName = new OptionLabel("Application name:", this);
            DataTextField appNameField = new DataTextField("TouchMouse:", this);
            OptionLabel hostname = new OptionLabel("Hostname:", this);
            DataTextField hostnameField = new DataTextField(NetworkUtils.getHostname(), this);
            OptionLabel interfaces = new OptionLabel("Interfaces:", this);
            this.setInterfaces();
            OptionLabel appVersion = new OptionLabel("Application version: ", this);
            DataTextField appVersionField = new DataTextField("0.1.0", this);
            this.setVisible(true);
            DefaultScrollPane defaultScrollPane = new DefaultScrollPane(this);

            this.optionsDIalog.add(defaultScrollPane);
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }


    }

    private void setInterfaces() throws SocketException {
        ArrayList<InetAddress> addresses = NetworkUtils.listAllOwnAddresses();
        addresses.forEach(inet -> {
            DataTextField inetField = new DataTextField(inet.getHostAddress(), this);
        });

    }
}
