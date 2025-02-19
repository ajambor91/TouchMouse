package ajprogramming.TouchMouse.Network;


import ajprogramming.TouchMouse.Network.Enums.BroadcastMessageTypeEnum;
import ajprogramming.TouchMouse.Network.Enums.MessageTypes;
import ajprogramming.TouchMouse.Network.Enums.TCPMessageTypeEnum;
import ajprogramming.TouchMouse.Network.Enums.UDPMessageTypeEnum;
import ajprogramming.TouchMouse.Network.Messages.BroadcastMessage;
import ajprogramming.TouchMouse.Network.Messages.INetworkMessage;
import ajprogramming.TouchMouse.Network.Messages.TCPMessage;
import ajprogramming.TouchMouse.Network.Messages.UDPMessage;
import ajprogramming.TouchMouse.Utils.LoggerEx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

public class MessageCreator {
    private final ObjectMapper objectMapper;

    private INetworkMessage INetworkMessage;

    public MessageCreator() {
        this.objectMapper = new ObjectMapper();
    }

    public MessageCreator(String mouseId, String sessionId, String hostAddress, String hostName, String appName, TCPMessageTypeEnum tcpMessageTypeEnum) {
        this.objectMapper = new ObjectMapper();

        this.initialize(mouseId, sessionId, hostAddress, hostName, appName, tcpMessageTypeEnum);
    }

    public MessageCreator(String mouseId, String sessionId, String hostAddress, String hostName, String appName, UDPAction udpAction, UDPMessageTypeEnum udpMessageTypeEnum) {
        this.objectMapper = new ObjectMapper();

        this.initialize(mouseId, sessionId, hostAddress, hostName, appName, udpAction, udpMessageTypeEnum);
    }


    public MessageCreator(String mouseId, String sessionId, String hostAddress, String hostName, String appName, BroadcastMessageTypeEnum broadcastMessageTypeEnum) {
        this.objectMapper = new ObjectMapper();

        this.initialize(mouseId, sessionId, hostAddress, hostName, appName, broadcastMessageTypeEnum);
    }

    public MessageCreator(TCPMessage message) {
        this.objectMapper = new ObjectMapper();
        this.INetworkMessage = message;
    }

    public MessageCreator(BroadcastMessage message) {
        this.objectMapper = new ObjectMapper();
        this.INetworkMessage = message;
    }

    public MessageCreator(String incomingMessage, MessageTypes messageTypes) {
        this.objectMapper = new ObjectMapper();
        this.initializeIncomingMessage(incomingMessage, messageTypes);
    }


    private void initializeIncomingMessage(String incomingMessage, MessageTypes messageTypes) {
        try {
            switch (messageTypes) {
                case BROADCAST:
                    this.INetworkMessage = this.objectMapper.readValue(incomingMessage, BroadcastMessage.class);
                    break;
                case UDP:
                    this.INetworkMessage = this.objectMapper.readValue(incomingMessage, UDPMessage.class);
                    break;
                case TCP:
                    this.INetworkMessage = this.objectMapper.readValue(incomingMessage, TCPMessage.class);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialize(String mouseId, String sessionId, String hostAddress, String hostName, String appName, BroadcastMessageTypeEnum broadcastMessageTypeEnum) {
        BroadcastMessage broadcastMessage = new BroadcastMessage();
        broadcastMessage.setType(broadcastMessageTypeEnum);
        this.INetworkMessage = broadcastMessage;
        this.setMessage(mouseId, sessionId, hostAddress, hostName, appName);

    }

    private void initialize(String mouseId, String sessionId, String hostAddress, String hostName, String appName, UDPAction udpAction, UDPMessageTypeEnum udpMessageTypeEnum) {

        UDPMessage udpMessage = new UDPMessage();
        udpMessage.setAction(udpAction);
        udpMessage.setType(udpMessageTypeEnum);
        this.INetworkMessage = udpMessage;
        this.setMessage(mouseId, sessionId, hostAddress, hostName, appName);

    }

    private void initialize(String mouseId, String sessionId, String hostAddress, String hostName, String appName, TCPMessageTypeEnum tcpMessageTypeEnum) {
        TCPMessage tcpMessage = new TCPMessage();
        tcpMessage.setType(tcpMessageTypeEnum);
        this.INetworkMessage = tcpMessage;
        this.setMessage(mouseId, sessionId, hostAddress, hostName, appName);

    }

    private void setMessage(String mouseId, String sessionId, String hostAddress, String hostName, String appName) {
        this.INetworkMessage.setAppName(appName);
        this.INetworkMessage.setHostname(hostName);
        this.INetworkMessage.setHostAddress(hostAddress);
        this.INetworkMessage.setMouseId(mouseId);
        this.INetworkMessage.setSessionId(sessionId);
    }

    public INetworkMessage getMessage() {
        return this.INetworkMessage;
    }

    public String jsonfyMessage() {
        try {
            return this.objectMapper.writeValueAsString(this.INetworkMessage);

        } catch (JsonProcessingException e) {
            LoggerEx.getLogger(this.getClass().getName()).warning("Cannot JSON serialize: ", e.getMessage());
        }
        return null;
    }

    public byte[] bytefyMessage() {
        return this.jsonfyMessage().getBytes(StandardCharsets.UTF_8);
    }
}
