package ajprogramming.TouchMouse.Keyboard.KeyArrays;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class SpecialCharsToVkCodes {
    private static final Map<String, Integer> charsToVKCodes = new HashMap<>();

    static {
        charsToVKCodes.put("BCKSPC", KeyEvent.VK_BACK_SPACE);
        charsToVKCodes.put("SPC", KeyEvent.VK_SPACE);
        charsToVKCodes.put("RTN", KeyEvent.VK_ENTER);
        charsToVKCodes.put(".", KeyEvent.VK_PERIOD);
        charsToVKCodes.put(",", KeyEvent.VK_COMMA);
        charsToVKCodes.put("[", KeyEvent.VK_OPEN_BRACKET);
        charsToVKCodes.put("]", KeyEvent.VK_CLOSE_BRACKET);
        charsToVKCodes.put("`", KeyEvent.VK_BACK_QUOTE);
        charsToVKCodes.put("'", KeyEvent.VK_QUOTE);
        charsToVKCodes.put("\"", KeyEvent.VK_QUOTEDBL);
        charsToVKCodes.put("\\", KeyEvent.VK_BACK_SLASH);
        charsToVKCodes.put("/", KeyEvent.VK_SLASH);

    }

    public static int getVKCode(String keyChar) {
        return SpecialCharsToVkCodes.charsToVKCodes.getOrDefault(keyChar, -1);
    }
}


