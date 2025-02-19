package ajprogramming.TouchMouse.Network;

import ajprogramming.TouchMouse.Network.Messages.UDPMessage;

import java.util.LinkedList;


public class KeyboardMessageBuffer {

    private final LinkedList<UDPMessage> buffer;

    public KeyboardMessageBuffer() {
        this.buffer = new LinkedList<>();
    }

    public boolean isEmpty() {
        return this.buffer.isEmpty();
    }

    public UDPMessage getMessage() {
        return this.buffer.poll();
    }

    public synchronized void put(UDPMessage message) {

        this.buffer.add(message);


    }
}
