package ajprogramming.TouchMouse.Keyboard.KeyArrays;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class LowCasedLettersVKCodes {
    private static final Map<String, Integer> charsToVKCodes = new HashMap<>();

    static {
        for (char c = 'a'; c <= 'z'; c++) {
            try {
                charsToVKCodes.put(String.valueOf(c), (int) KeyEvent.class.getField("VK_" + String.valueOf(c).toUpperCase()).get(null));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static int getVKCode(String keyChar) {
        return LowCasedLettersVKCodes.charsToVKCodes.getOrDefault(keyChar, -1);
    }
}
