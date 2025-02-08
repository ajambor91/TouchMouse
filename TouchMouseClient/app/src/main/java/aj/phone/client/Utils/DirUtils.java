package aj.phone.client.Utils;

import java.io.File;

public class DirUtils {
    private static final File pathDir = Config.getInstance().getDataPathDir();

    public static boolean createDir(String name) {
        System.out.println("CRT_DIR_CRT_DIR" + " " + pathDir + name);
        final File dir = new File(pathDir, name);
        dir.mkdirs();
        System.out.println("MK_DIR_MK_DIR_MK_DIR_MK_DIR_" + dir.isDirectory());

        return dir.isDirectory();
    }
}
