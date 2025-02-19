package ajprogramming.TouchMouse.Keyboard.KeyArrays;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class DiactricChars {
    protected static Map<String, Integer> charsToVKCodes = new HashMap<>();

     static {
        System.out.println("LOW CASED LETTERS");

        charsToVKCodes.put("ą", KeyEvent.VK_A);
        charsToVKCodes.put("ć", KeyEvent.VK_C);
        charsToVKCodes.put("ę", KeyEvent.VK_E);
        charsToVKCodes.put("ł", KeyEvent.VK_L);
        charsToVKCodes.put("ń", KeyEvent.VK_N);
        charsToVKCodes.put("ó", KeyEvent.VK_O);
        charsToVKCodes.put("ś", KeyEvent.VK_S);
        charsToVKCodes.put("ź", KeyEvent.VK_Z);
        charsToVKCodes.put("ż", KeyEvent.VK_Z);

        charsToVKCodes.put("Ą", KeyEvent.VK_A); // SHIFT + A
        charsToVKCodes.put("Ć", KeyEvent.VK_C); // SHIFT + C
        charsToVKCodes.put("Ę", KeyEvent.VK_E); // SHIFT + E
        charsToVKCodes.put("Ł", KeyEvent.VK_L); // SHIFT + L
        charsToVKCodes.put("Ń", KeyEvent.VK_N); // SHIFT + N
        charsToVKCodes.put("Ó", KeyEvent.VK_O); // SHIFT + O
        charsToVKCodes.put("Ś", KeyEvent.VK_S); // SHIFT + S
        charsToVKCodes.put("Ź", KeyEvent.VK_Z); // SHIFT + Z
        charsToVKCodes.put("Ż", KeyEvent.VK_Z); // SHIFT + Z

        // Inne popularne znaki diakrytyczne (Europa)
        charsToVKCodes.put("à", KeyEvent.VK_A); // ALTGR + A
        charsToVKCodes.put("è", KeyEvent.VK_E); // ALTGR + E
        charsToVKCodes.put("ì", KeyEvent.VK_I); // ALTGR + I
        charsToVKCodes.put("ò", KeyEvent.VK_O); // ALTGR + O
        charsToVKCodes.put("ù", KeyEvent.VK_U); // ALTGR + U

        charsToVKCodes.put("ä", KeyEvent.VK_A); // ALTGR + ¨ + A
        charsToVKCodes.put("ö", KeyEvent.VK_O); // ALTGR + ¨ + O
        charsToVKCodes.put("ü", KeyEvent.VK_U); // ALTGR + ¨ + U

        charsToVKCodes.put("â", KeyEvent.VK_A); // ALTGR + ^ + A
        charsToVKCodes.put("ê", KeyEvent.VK_E); // ALTGR + ^ + E
        charsToVKCodes.put("î", KeyEvent.VK_I); // ALTGR + ^ + I
        charsToVKCodes.put("ô", KeyEvent.VK_O); // ALTGR + ^ + O
        charsToVKCodes.put("û", KeyEvent.VK_U); // ALTGR + ^ + U

    }

    public static int getVKCode(String keyChar) {
        return DiactricChars.charsToVKCodes.getOrDefault(keyChar, -1);
    }

}
