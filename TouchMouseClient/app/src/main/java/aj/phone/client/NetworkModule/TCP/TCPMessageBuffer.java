package aj.phone.client.NetworkModule.TCP;

import java.net.Socket;
import java.util.LinkedList;

import aj.phone.client.NetworkModule.Message.TCPMessage;

public class TCPMessageBuffer {
    private final LinkedList<TCPMessage> tcpMessages;

    public boolean isEmpty() {
        return this.tcpMessages.isEmpty();
    }
    public TCPMessageBuffer() {
        this.tcpMessages = new LinkedList<>();
    }

    public void addMessage(TCPMessage message) {
        this.tcpMessages.add(message);
    }

    public TCPMessage getMessage() {
        return this.tcpMessages.poll();
    }
}
