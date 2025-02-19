package ajprogramming.TouchMouse.Keyboard.KeyArrays;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class LowCasedLettersVKCodes  {
    private static Map<String, Integer> charsToVKCodes = new HashMap<>();

     static {
        for (char c = 'a'; c <= 'z'; c++) {
            try {
                System.out.println("INside LOW leteer " + String.valueOf(c));
                charsToVKCodes.put(String.valueOf(c), (int) KeyEvent.class.getField("VK_" + String.valueOf(c).toUpperCase()).get(null));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("HASHMAP LOW: " + charsToVKCodes.values().toString());

    }

    public static int getVKCode(String keyChar) {
        return LowCasedLettersVKCodes.charsToVKCodes.getOrDefault(keyChar, -1);
    }
}
