package ajprogramming.TouchMouse.Utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerEx {
    private static LoggerEx loggerEx;
    private final FilesTools filesTools;
    private Logger logger;
    private LoggerEx(Logger logger) {
        this.logger = logger;
        this.filesTools = new FilesTools();
    }

    public static LoggerEx getLogger(String name) {
        LoggerEx.loggerEx = new LoggerEx(Logger.getLogger(name));
        return loggerEx;
    }

    public void info(String msg) {
        this.logger.info(msg);
        this.filesTools.saveLog(List.of(this.getTime(), msg));
    }


    public void info(String title, String msg) {
        this.logger.info(String.format("%s %s", title, msg));
        this.filesTools.saveLog(List.of(this.getTime(), title, msg));
    }

    public void warning(String title, String msg) {
        this.filesTools.saveLog(List.of(this.getTime(),title, msg));
    }

    private String getTime() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }


}
