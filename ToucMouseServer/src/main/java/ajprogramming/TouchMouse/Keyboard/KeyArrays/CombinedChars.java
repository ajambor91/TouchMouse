package ajprogramming.TouchMouse.Keyboard.KeyArrays;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class CombinedChars {
    private static final Map<String, Integer> charsToVKCodes = new HashMap<>();

    static {
        charsToVKCodes.put("@", KeyEvent.VK_2);
        charsToVKCodes.put("#", KeyEvent.VK_3);
        charsToVKCodes.put("$", KeyEvent.VK_4);
        charsToVKCodes.put("%", KeyEvent.VK_5);
        charsToVKCodes.put("^", KeyEvent.VK_6);
        charsToVKCodes.put("&", KeyEvent.VK_7);
        charsToVKCodes.put("*", KeyEvent.VK_8);
        charsToVKCodes.put("(", KeyEvent.VK_9);
        charsToVKCodes.put(")", KeyEvent.VK_0);
        charsToVKCodes.put("_", KeyEvent.VK_MINUS);
        charsToVKCodes.put("+", KeyEvent.VK_EQUALS);
        charsToVKCodes.put("{", KeyEvent.VK_OPEN_BRACKET);
        charsToVKCodes.put("}", KeyEvent.VK_CLOSE_BRACKET);
        charsToVKCodes.put("|", KeyEvent.VK_BACK_SLASH);
        charsToVKCodes.put(":", KeyEvent.VK_SEMICOLON);
        charsToVKCodes.put("\"", KeyEvent.VK_QUOTE);
        charsToVKCodes.put("<", KeyEvent.VK_COMMA);
        charsToVKCodes.put(">", KeyEvent.VK_PERIOD);
        charsToVKCodes.put("?", KeyEvent.VK_SLASH);
    }

    public static int getVKCode(String keyChar) {
        return CombinedChars.charsToVKCodes.getOrDefault(keyChar, -1);
    }
}
