package ajprogramming.TouchMouse.Utils;

import javax.swing.*;

public class Platform {
    private EPlatform platform;

    public Platform() {
        this.initialize();
    }

    public EPlatform getPlatform() {
        return this.platform;
    }

    private void initialize() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith(EPlatform.WINDOWS.getOs())) {
            this.platform = EPlatform.WINDOWS;
        } else if (osName.toLowerCase().startsWith(EPlatform.LINUX.getOs())) {
            this.platform = EPlatform.LINUX;
        } else {
            JOptionPane.showMessageDialog(null, "OS is not supporting", "OS is not supporting", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}
