package ajprogramming.TouchMouse.Keyboard.KeyArrays;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class CapitalLettersVKCodes  {
    private static Map<String, Integer> charsToVKCodes = new HashMap<>();

     static {
        for (char c = 'A'; c <= 'Z'; c++) {
            try {
                System.out.println("INside capital leteer " + c);
                charsToVKCodes.put(String.valueOf(c).toUpperCase(), (int) KeyEvent.class.getField("VK_" + c).get(null));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("HASHMAP CAPITASL: " + charsToVKCodes.values().toString());

    }

    public static int getVKCode(String keyChar) {
        return CapitalLettersVKCodes.charsToVKCodes.getOrDefault(keyChar, -1);
    }
}
