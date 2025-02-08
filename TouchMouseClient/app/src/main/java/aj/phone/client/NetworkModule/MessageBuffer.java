package aj.phone.client.NetworkModule;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import aj.phone.client.NetworkModule.Message.UDPMessage;

public class MessageBuffer {

    private final int maxSize;
    private final LinkedList<UDPMessage> buffer;
    private boolean isActionFinished;

    public MessageBuffer() {
        this.maxSize = 10;
        this.isActionFinished = false;
        this.buffer = new LinkedList<>();
    }

    public boolean isEmpty() {
        return this.buffer.isEmpty();
    }

    public synchronized void put(UDPMessage message) {
        this.buffer.add(message);
        if (this.buffer.size() == this.maxSize) {
            this.isActionFinished = true;
        }

    }

    public synchronized void putLastMsg(UDPMessage message) {
        if (!this.isActionFinished) {
            this.isActionFinished = true;
            this.buffer.add(message);

        }

    }


    public UDPMessage[] getMessage() {
        LinkedList<UDPMessage> msg = (LinkedList<UDPMessage>) this.buffer.clone();
        this.buffer.clear();
        UDPMessage[] udpMessage = new UDPMessage[msg.size()];
        AtomicInteger integer = new AtomicInteger(0);
        this.isActionFinished = false;
        msg.forEach(udp -> {
            udpMessage[integer.getAndIncrement()] = udp;
        });
        return udpMessage;
    }

}
