package ajprogramming.TouchMouse.Utils;


import ajprogramming.TouchMouse.AppConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App {

    private FileLock lock;
    private FileChannel channel;

    public boolean isAlive() {
        try {
            File file = new File(AppConfig.getInstance().getAppDataPath(), "phone_mouse.lock");
            this.channel = new RandomAccessFile(file, "rw").getChannel();
            this.lock = channel.tryLock();
            if (this.lock == null) {
                return true;
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        lock.release();
                        channel.close();
                    } catch (IOException e) {
                    }
                }
            });
            return false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }
    
    
}
