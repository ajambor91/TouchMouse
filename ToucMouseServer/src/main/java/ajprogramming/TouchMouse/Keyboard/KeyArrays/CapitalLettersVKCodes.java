package ajprogramming.TouchMouse.Keyboard.KeyArrays;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class CapitalLettersVKCodes {
    private static final Map<String, Integer> charsToVKCodes = new HashMap<>();

    static {
        for (char c = 'A'; c <= 'Z'; c++) {
            try {
                charsToVKCodes.put(String.valueOf(c).toUpperCase(), (int) KeyEvent.class.getField("VK_" + c).get(null));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static int getVKCode(String keyChar) {
        return CapitalLettersVKCodes.charsToVKCodes.getOrDefault(keyChar, -1);
    }
}
