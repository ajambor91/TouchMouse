package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.AppConfig;
import ajprogramming.TouchMouse.Menus.MainElementsOptions.MainFrameOptions;
import ajprogramming.TouchMouse.Menus.OptionsFrame.OptionsDIalog;
import ajprogramming.TouchMouse.Mouse.IMouse;
import ajprogramming.TouchMouse.Mouse.MouseHandler;
import ajprogramming.TouchMouse.Network.NetworkService;
import ajprogramming.TouchMouse.Utils.LoggerEx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.Objects;

/**
 *
 * @author Adam
 */
public class MainFrame extends JFrame {
    private final static Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    private static MainFrame instance;
    private static Point point;
    private final MouseListPane mouseListPane;
    private final MouseHandler mouseHandler;
    private final MainFrameOptions mainFrameOptions;
    private OptionsDIalog optionsDIalog;
    private final LoggerEx loggerEx;
    private MainFrame() {
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.loggerEx.info("Initialized app main window");
        Image trayIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/icons/icon_connected_win.png"))).getImage();
        this.setIconImage(trayIcon);
        this.mainFrameOptions = new MainFrameOptions();
        if (MainFrame.point == null) {
            int screenWidth = MainFrame.screenDimension.width;
            int screenHeight = MainFrame.screenDimension.height;

            int xPos = screenWidth - this.mainFrameOptions.getWidth() - 20;
            int yPos = screenHeight - this.mainFrameOptions.getHeight() - 50;
            MainFrame.point = new Point(xPos, yPos);
        }
        this.mouseHandler = NetworkService.getInstance().getMouseHandler();
        this.mouseHandler.setMainFrame(this);
        this.mouseListPane = new MouseListPane(this, this.mouseHandler.getMice());
        this.startWindow();
        this.setBackground(Color.WHITE);
        this.addListeners();

    }

    public static MainFrame getInstance() {
        if (MainFrame.instance == null) {
            MainFrame.instance = new MainFrame();

        }
        MainFrame.instance.setState(JFrame.NORMAL);
        return MainFrame.instance;
    }

    public static void removeInstance() {
        if (MainFrame.instance instanceof MainFrame) {
            MainFrame.instance = null;
        }
    }

    public static Point getPoint() {
        return MainFrame.point;
    }

    public void showOptions() {
        if (this.optionsDIalog == null) {
            this.optionsDIalog = new OptionsDIalog(this);

        } else {
            this.optionsDIalog.setVisible(true);
        }
    }

    public void closeOptions() {
        this.optionsDIalog = null;
    }

    public void refreshMousesList() {
        this.mouseListPane.changeItems(this.mouseHandler.getMice());
    }

    private void startWindow() {
        this.setupWindow();
        this.setVisible(true);
    }

    public void onDisconnect(IMouse mouse) {
        this.mouseHandler.disconnectMouse(mouse);
    }
    public void onRemove(IMouse mouse) {this.mouseHandler.removeMouse(mouse);}
    private void setupWindow() {

        this.loggerEx.info("Set main window option");

        Dimension appDimension = new Dimension(this.mainFrameOptions.getWidth(), this.mainFrameOptions.getHeight());
        this.setLocation(MainFrame.point);
        this.setSize(appDimension);
        this.setMaximumSize(appDimension);
        this.setResizable(false);
        this.setTitle(AppConfig.getInstance().getAppName());
        this.add(this.mouseListPane);

    }


    private void addListeners() {
        this.addWindowListener(new WindowAdapter() {


            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setVisible(false);
                instance = null;
            }
        });
    }

}
