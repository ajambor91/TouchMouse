package ajprogramming.TouchMouse;

import ajprogramming.TouchMouse.Mouse.IMouse;
import ajprogramming.TouchMouse.Utils.XML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class AppConfig {
    private final String appName = "ToucMouse";
    private final String appDataPath = System.getProperty("user.home") + "\\AppData\\Local\\TouchMouse";
    private XML xml;
    private static AppConfig instance;

    private AppConfig() {
        this.initialize();

    }


    public static AppConfig getInstance() {
        if (AppConfig.instance == null) {
            AppConfig.instance = new AppConfig();
            AppConfig.instance.initializeXML();

        }
        return AppConfig.instance;
    }

    public String getAppName() {return this.appName;}
    public HashMap<String, IMouse> getMice() {
        return this.xml.getMice();
    }

    public String getAppDataPath() {
        return this.appDataPath;
    }

    private void initialize() {
        try {
            if (Files.notExists(Path.of(this.appDataPath))) {
                Files.createDirectory(Path.of(this.appDataPath));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void initializeXML() {
        this.xml = new XML();

    }

}
