package ajprogramming.TouchMouse.Network;

import ajprogramming.TouchMouse.Network.Messages.UDPMessage;
import ajprogramming.TouchMouse.Utils.LoggerEx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ActionCreator {

    private final ObjectMapper objectMapper;
    private final LoggerEx loggerEx;
    private List<UDPMessage> message;

    public ActionCreator(byte[] message, int length) {
        this.objectMapper = new ObjectMapper();
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.initializeActionByte(message, length);
    }

    public ActionCreator(String message) {
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.objectMapper = new ObjectMapper();
    }

    private void initializeActionByte(byte[] message, int length) {
        String messageFromBytes = new String(message, 0, length, StandardCharsets.UTF_8 );
        this.initializeAction(messageFromBytes);
    }

    private void initializeAction(String message)  {
        try {
            this.message = (List<UDPMessage>) this.objectMapper.readValue(message, new TypeReference<List<UDPMessage>>() {});
        } catch (JsonProcessingException e) {
            this.loggerEx.warning("Action creator, JSON Error", e.getMessage());
        }

    }

    public List<UDPMessage> getAction() {
        return this.message;
    }
}
