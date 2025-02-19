package ajprogramming.TouchMouse.Keyboard.KeyArrays;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class CombinedChars {
    private static Map<String, Integer> charsToVKCodes = new HashMap<>();

     static {
        charsToVKCodes.put("@", KeyEvent.VK_2); // SHIFT + 2
        charsToVKCodes.put("#", KeyEvent.VK_3); // SHIFT + 3
        charsToVKCodes.put("$", KeyEvent.VK_4); // SHIFT + 4
        charsToVKCodes.put("%", KeyEvent.VK_5); // SHIFT + 5
        charsToVKCodes.put("^", KeyEvent.VK_6); // SHIFT + 6
        charsToVKCodes.put("&", KeyEvent.VK_7); // SHIFT + 7
        charsToVKCodes.put("*", KeyEvent.VK_8); // SHIFT + 8
        charsToVKCodes.put("(", KeyEvent.VK_9); // SHIFT + 9
        charsToVKCodes.put(")", KeyEvent.VK_0); // SHIFT + 0
        charsToVKCodes.put("_", KeyEvent.VK_MINUS); // SHIFT + -
        charsToVKCodes.put("+", KeyEvent.VK_EQUALS); // SHIFT + =
        charsToVKCodes.put("{", KeyEvent.VK_OPEN_BRACKET); // SHIFT + [
        charsToVKCodes.put("}", KeyEvent.VK_CLOSE_BRACKET); // SHIFT + ]
        charsToVKCodes.put("|", KeyEvent.VK_BACK_SLASH); // SHIFT + \
        charsToVKCodes.put(":", KeyEvent.VK_SEMICOLON); // SHIFT + ;
        charsToVKCodes.put("\"", KeyEvent.VK_QUOTE); // SHIFT + '
        charsToVKCodes.put("<", KeyEvent.VK_COMMA); // SHIFT + ,
        charsToVKCodes.put(">", KeyEvent.VK_PERIOD); // SHIFT + .
        charsToVKCodes.put("?", KeyEvent.VK_SLASH); // SHIFT + /
    }

    public static int getVKCode(String keyChar) {
        return CombinedChars.charsToVKCodes.getOrDefault(keyChar, -1);
    }
}
