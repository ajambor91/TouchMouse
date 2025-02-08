package aj.phone.client.NetworkModule.Message;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

import aj.phone.client.NetworkModule.Enums.BroadcastMessageTypeEnum;
import aj.phone.client.NetworkModule.Enums.MessageTypes;
import aj.phone.client.NetworkModule.Enums.TCPMessageTypeEnum;
import aj.phone.client.NetworkModule.Enums.UDPMessageTypeEnum;

public class MessageCreator {
    private final ObjectMapper objectMapper;
    private NetworkMessage networkMessage;
    private UDPMessage[] udpMessages;

    public MessageCreator(TCPMessage message) {
        this.objectMapper = new ObjectMapper();
        this.networkMessage = message;
    }

    public MessageCreator(UDPMessage message) {
        this.objectMapper = new ObjectMapper();
        this.networkMessage = message;
    }

    public MessageCreator(UDPMessage[] message) {
        this.objectMapper = new ObjectMapper();
        this.udpMessages = message;
    }


    public MessageCreator(String id, String sessionId, String hostAddress, String mouseAddress, String mouseName, String appName, TCPMessageTypeEnum tcpMessageTypeEnum) {
        this.objectMapper = new ObjectMapper();
        TCPMessage message = new TCPMessage();
        message.setType(tcpMessageTypeEnum);
        this.networkMessage = message;
        this.initialize(id, sessionId, hostAddress, mouseAddress, mouseName, appName);
    }

    public MessageCreator(String id, String sessionId, String hostAddress, String mouseAddress, String mouseName, String appName, BroadcastMessageTypeEnum broadcastMessageTypeEnum) {
        BroadcastMessage broadcastMessage = new BroadcastMessage();
        broadcastMessage.setType(broadcastMessageTypeEnum);
        this.networkMessage = broadcastMessage;
        this.objectMapper = new ObjectMapper();
        this.initialize(id, sessionId, hostAddress, mouseAddress, mouseName, appName);
    }


    public MessageCreator(String id, String sessionId, String hostAddress, String mouseAddress, String mouseName, String appName, UDPAction action, UDPMessageTypeEnum udpMessageTypeEnum) {

        this.objectMapper = new ObjectMapper();

        this.initialize(id, action, udpMessageTypeEnum);
    }

    public MessageCreator(String incomingMessage, MessageTypes messageTypes) {
        this.objectMapper = new ObjectMapper();
        this.initializeIncomingMessage(incomingMessage, messageTypes);
    }

    private void initializeIncomingMessage(String incomingMessage, MessageTypes messageTypes) {
        try {
            switch (messageTypes) {
                case BROADCAST:
                    this.networkMessage = this.objectMapper.readValue(incomingMessage, BroadcastMessage.class);
                    break;
                case TCP:
                    this.networkMessage = this.objectMapper.readValue(incomingMessage, TCPMessage.class);
                    break;
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialize(String id, String sessionId, String hostAddress, String mouseAddress, String mouseName, String appName) {
        this.networkMessage.setHostAddress(hostAddress);
        this.networkMessage.setMouseId(id);
        if (this.networkMessage instanceof TCPMessage) {
            ((TCPMessage) this.networkMessage).setMouseName(mouseName);
            ((TCPMessage) this.networkMessage).setAppName(appName);
            ((TCPMessage) this.networkMessage).setMouseAddress(mouseAddress);
            ((TCPMessage) this.networkMessage).setSessionId(sessionId);
        } else if (this.networkMessage instanceof BroadcastMessage) {
            ((BroadcastMessage) this.networkMessage).setMouseName(mouseName);
            ((BroadcastMessage) this.networkMessage).setAppName(appName);
            ((BroadcastMessage) this.networkMessage).setMouseAddress(mouseAddress);
            ((BroadcastMessage) this.networkMessage).setSessionId(sessionId);
        }

    }

    private void initialize(String id, UDPAction udpAction, UDPMessageTypeEnum udpMessageTypeEnum) {
        UDPMessage udpMessage = new UDPMessage();
        udpMessage.setType(udpMessageTypeEnum);
        udpMessage.setAction(udpAction);
        this.networkMessage = udpMessage;
        this.networkMessage.setMouseId(id);

    }

    public NetworkMessage getMessage() {
        return this.networkMessage;
    }

    public String jsonfyMessage() {
        try {
            return this.objectMapper.writeValueAsString(this.networkMessage);

        } catch (JsonProcessingException e) {
            Log.d("JSON", String.format("Cannot convert message to JSON, error %s", e.getMessage()));
        }
        return "";
    }

    public String jsonfyBufferedMessage() {
        try {
            return this.objectMapper.writeValueAsString(this.udpMessages);

        } catch (JsonProcessingException e) {
            Log.d("JSON", String.format("Cannot convert message to JSON, error %s", e.getMessage()));
        }
        return "";
    }

    public byte[] bytefyMessage() {
        return this.jsonfyMessage().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] bytefyBufferMessage() {
        return this.jsonfyBufferedMessage().getBytes(StandardCharsets.UTF_8);
    }


}
