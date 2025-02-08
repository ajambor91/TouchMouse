package ajprogramming.TouchMouse.Menus;

import ajprogramming.TouchMouse.Mouse.IMouse;
import ajprogramming.TouchMouse.Utils.LoggerEx;

import java.util.HashMap;

public class MouseListModel {
    private IMouse[] data;
    private LoggerEx loggerEx;
    public MouseListModel(HashMap<String, IMouse> map) {
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.loggerEx.info("Initializing MouseListModel");
        this.initializeData(map);
    }

    private void initializeData(HashMap<String, IMouse> map) {
        this.loggerEx.info("Initializing MouseListModel data");

        data = new IMouse[map.size()];
        int row = 0;
        for (IMouse mouse : map.values()) {
            data[row] = mouse;
            row++;
        }
    }

    public IMouse[] getData() {
        return this.data;
    }

}
