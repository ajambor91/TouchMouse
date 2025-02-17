package ajprogramming.TouchMouse.Keyboard;

import ajprogramming.TouchMouse.AppConfig;
import ajprogramming.TouchMouse.Network.KeyboardMessageBuffer;
import ajprogramming.TouchMouse.Network.MessageBuffer;
import ajprogramming.TouchMouse.Network.Messages.KeyboardKey;
import ajprogramming.TouchMouse.Network.Messages.UDPMessage;
import ajprogramming.TouchMouse.Utils.EPlatform;

import java.awt.*;

public class Keyboard extends Thread{

    private volatile boolean running = true;
    private Robot robot;
    private final KeyboardMessageBuffer messageBuffer;
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
            if (!this.messageBuffer.isEmpty()) {
                this.clickKey(this.messageBuffer.getMessage());
            } else  {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void clickKey(UDPMessage udpMessage) {
        this.robot.keyPress(((KeyboardKey) udpMessage.getAction()).getKeyCode());
    }
}
