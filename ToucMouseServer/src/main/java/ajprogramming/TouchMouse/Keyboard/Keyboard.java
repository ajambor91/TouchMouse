package ajprogramming.TouchMouse.Keyboard;

import ajprogramming.TouchMouse.Keyboard.KeyArrays.*;
import ajprogramming.TouchMouse.Network.KeyboardMessageBuffer;
import ajprogramming.TouchMouse.Network.Messages.KeyboardKey;
import ajprogramming.TouchMouse.Network.Messages.UDPMessage;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Keyboard extends Thread {

    private final KeyboardMessageBuffer messageBuffer;
    private final boolean running = true;
    private Robot robot;

    public Keyboard(KeyboardMessageBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
        this.init();
    }

    private void init() {
        try {
            this.robot = new Robot();

        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (this.running) {
            UDPMessage udpMessage = this.messageBuffer.getMessage();

            if (udpMessage != null) {
                this.clickKey(udpMessage);
            } else {
                try {
                    synchronized (this) {
                        this.wait();

                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void clickKey(UDPMessage udpMessage) {
        String charKey = ((KeyboardKey) udpMessage.getAction()).getKeyCode();
        int keyEvent = -1;
        keyEvent = LowCasedLettersVKCodes.getVKCode(charKey);
        if (keyEvent > -1) {
            this.robot.keyPress(keyEvent);
            this.robot.keyRelease(keyEvent);
            return;
        }

        keyEvent = CapitalLettersVKCodes.getVKCode(charKey);
        if (keyEvent > -1) {
            this.robot.keyPress(KeyEvent.VK_SHIFT);
            this.robot.keyPress(keyEvent);
            this.robot.keyRelease(keyEvent);
            this.robot.keyRelease(KeyEvent.VK_SHIFT);
            return;
        }

        keyEvent = CombinedChars.getVKCode(charKey);
        if (keyEvent > -1) {
            this.robot.keyPress(KeyEvent.VK_SHIFT);
            this.robot.keyPress(keyEvent);
            this.robot.keyRelease(keyEvent);
            this.robot.keyRelease(KeyEvent.VK_SHIFT);
            return;
        }
        keyEvent = DiactricChars.getVKCode(charKey);

        if (keyEvent > -1) {
            this.robot.keyPress(KeyEvent.VK_ALT_GRAPH);
            this.robot.keyPress(keyEvent);
            this.robot.keyRelease(keyEvent);
            this.robot.keyRelease(KeyEvent.VK_ALT_GRAPH);
            return;
        }

        keyEvent = SpecialCharsToVkCodes.getVKCode(charKey);
        if (keyEvent > -1) {
            this.robot.keyPress(keyEvent);
            this.robot.keyRelease(keyEvent);
        }

    }


}
