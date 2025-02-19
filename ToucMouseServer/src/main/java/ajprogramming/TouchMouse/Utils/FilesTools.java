package ajprogramming.TouchMouse.Utils;

import ajprogramming.TouchMouse.AppConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FilesTools {
    private final AppConfig appConfig;
    private final String logFileName = "log";

    public FilesTools() {
        this.appConfig = AppConfig.getInstance();
    }

    public void initializeLogFile() {
        try {
            if (Files.notExists(Path.of(this.appConfig.getAppDataPath(), this.logFileName))) {
                Files.createFile(Path.of(this.appConfig.getAppDataPath(), this.logFileName));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveLog(List<String> data) {
        try {
            if (Files.exists(Path.of(this.appConfig.getAppDataPath(), this.logFileName))) {
                Files.write(Path.of(this.appConfig.getAppDataPath(), this.logFileName), data, StandardOpenOption.APPEND

                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
