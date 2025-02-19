package ajprogramming.TouchMouse;

import ajprogramming.TouchMouse.Keyboard.KeyArrays.*;
import ajprogramming.TouchMouse.Menus.MainFrame;
import ajprogramming.TouchMouse.Mouse.MouseMove;
import ajprogramming.TouchMouse.Network.NetworkService;
import ajprogramming.TouchMouse.Tray.Tray;
import ajprogramming.TouchMouse.Utils.App;
import ajprogramming.TouchMouse.Utils.FilesTools;

import javax.swing.*;

/**
 * @author Adam
 */
public class TouchMouse {
    private static TouchMouse touchMouse;
    private final App app;
    private final NetworkService networkService;
    private final Tray tray;
    private MainFrame mainFrame;
    private MouseMove mouseMove;

    private TouchMouse() {
        this.app = new App();
//        SpecialCharsToVkCodes.initialize();
//            CombinedChars.initialize();
//            LowCasedLettersVKCodes.initialize();
//            CapitalLettersVKCodes.initialize();
//            DiactricChars.initialize();
        this.tray = Tray.getInstance();
        this.networkService = NetworkService.getInstance();
    }

    public static void main(String[] args) {
        AppConfig.getInstance();
        new FilesTools().initializeLogFile();
        TouchMouse.touchMouse = new TouchMouse();
        TouchMouse.touchMouse.runApp();


    }

    public void runApp() {
        boolean isAppAlive = this.app.isAlive();
        if (isAppAlive) {
            JOptionPane.showMessageDialog(null, "Application is running", "Application is runnig", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        this.createMenu();
        this.networkService.runBroadcast();
        this.networkService.runServer();

    }

    private void createMenu() {
        this.tray.createMenu();
    }
}
