package ajprogramming.TouchMouse.Mouse;

import ajprogramming.TouchMouse.Network.Enums.EMouseTouch;
import ajprogramming.TouchMouse.Network.Enums.EMouseTouchType;
import ajprogramming.TouchMouse.Network.Enums.UDPMessageTypeEnum;
import ajprogramming.TouchMouse.Network.MessageBuffer;
import ajprogramming.TouchMouse.Network.Messages.Move;
import ajprogramming.TouchMouse.Network.Messages.Scroll;
import ajprogramming.TouchMouse.Network.Messages.Touch;
import ajprogramming.TouchMouse.Network.Messages.UDPMessage;
import ajprogramming.TouchMouse.Utils.LoggerEx;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * @author Adam
 */
public class MouseMove extends Thread {
    private final boolean running = true;
    private final Robot robot;
    private final MessageBuffer messageBuffer;
    private final LoggerEx loggerEx;

    public MouseMove(MessageBuffer messageBuffer) throws Exception {
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.loggerEx.info("Initialized mouse move");
        this.robot = new Robot();
        this.messageBuffer = messageBuffer;
    }

    private void moveMouse(int moveX, int moveY) {
        Point start = MouseInfo.getPointerInfo().getLocation();
        int startX = start.x, startY = start.y;
        robot.mouseMove(startX + moveX, startY + moveY);
    }


    @Override
    public void run() {
        this.loggerEx.info("Mouse move started");

        while (running) {
            UDPMessage udpMessage = this.messageBuffer.getMessage();
            if (udpMessage != null) {
                if (udpMessage.getType() == UDPMessageTypeEnum.MOVE) {
                    Move move = (Move) udpMessage.getAction();
                    this.moveMouse(move.getX(), move.getY());
                } else if (udpMessage.getType() == UDPMessageTypeEnum.TOUCH) {
                    Touch touch = (Touch) udpMessage.getAction();
                    this.mouseClick(touch);
                } else if (udpMessage.getType() == UDPMessageTypeEnum.SCROLL) {
                    Scroll scroll = (Scroll) udpMessage.getAction();
                    this.mouseScroll(scroll);
                }

            } else {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        this.loggerEx.warning("Mouse move interrupted", e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }


    public void mouseClick(Touch touch) {
        if (touch.getClick() == EMouseTouch.SINGLE_LPM) {
            if (touch.getClickType() == EMouseTouchType.DOWN) {
                this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            } else if (touch.getClickType() == EMouseTouchType.UP) {
                this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        } else if (touch.getClick() == EMouseTouch.SINGLE_PPM) {
            if (touch.getClickType() == EMouseTouchType.DOWN) {
                this.robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            } else if (touch.getClickType() == EMouseTouchType.UP) {
                this.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            }
        }
    }

    public void mouseScroll(Scroll scroll) {
        this.robot.mouseWheel(scroll.getLineScroll());
    }
}
