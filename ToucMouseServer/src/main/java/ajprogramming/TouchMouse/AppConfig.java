package ajprogramming.TouchMouse;

import ajprogramming.TouchMouse.Mouse.IMouse;
import ajprogramming.TouchMouse.Utils.EPlatform;
import ajprogramming.TouchMouse.Utils.Platform;
import ajprogramming.TouchMouse.Utils.XML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class AppConfig {
    private static AppConfig instance;
    private final String appName = "ToucMouse";
    private final String appDataPath = System.getProperty("user.home") + "\\AppData\\Local\\TouchMouse";
    private final String appDataPathLinux = System.getProperty("user.home") + "/.local/share/TouchMouse";
    private XML xml;
    private Platform platform;

    private AppConfig() {

    }


    public static AppConfig getInstance() {
        if (AppConfig.instance == null) {
            AppConfig.instance = new AppConfig();
            AppConfig.instance.initalizePlatform();
            AppConfig.instance.initialize();
            AppConfig.instance.initializeXML();

        }
        return AppConfig.instance;
    }

    public Platform getPlatform() {
        return this.platform;
    }

    public String getAppName() {
        return this.appName;
    }

    public HashMap<String, IMouse> getMice() {
        return this.xml.getMice();
    }

    public String getAppDataPath() {
        return this.platform.getPlatform() == EPlatform.WINDOWS ? this.appDataPath : this.appDataPathLinux;
    }

    private void initialize() {
        try {
            if (Files.notExists(Path.of(this.getAppDataPath()))) {
                Files.createDirectory(Path.of(this.getAppDataPath()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeXML() {
        this.xml = new XML();

    }

    private void initalizePlatform() {
        this.platform = new Platform();
    }

}
