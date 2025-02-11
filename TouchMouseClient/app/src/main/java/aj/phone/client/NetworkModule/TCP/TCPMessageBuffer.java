package aj.phone.client.NetworkModule.TCP;

import java.util.LinkedList;

import aj.phone.client.NetworkModule.Message.TCPMessage;

public class TCPMessageBuffer {
    private final LinkedList<TCPMessage> tcpMessages;

    public TCPMessageBuffer() {
        this.tcpMessages = new LinkedList<>();
    }

    public boolean isEmpty() {
        return this.tcpMessages.isEmpty();
    }

    public void addMessage(TCPMessage message) {
        this.tcpMessages.add(message);
    }

    public TCPMessage getMessage() {
        return this.tcpMessages.poll();
    }
}
