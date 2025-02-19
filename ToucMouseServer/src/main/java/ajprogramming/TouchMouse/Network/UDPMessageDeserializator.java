package ajprogramming.TouchMouse.Network;

import ajprogramming.TouchMouse.Network.Enums.UDPMessageTypeEnum;
import ajprogramming.TouchMouse.Network.Messages.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UDPMessageDeserializator extends JsonDeserializer<INetworkMessage> {
    @Override
    public INetworkMessage deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode root = mapper.readTree(jsonParser);
        UDPMessage udpMessage = new UDPMessage();
        JsonNode typeNode = root.get("type");
        if (typeNode == null) {
            throw new IOException("Cannot found 'messageTypeEnum'");
        }
        UDPMessageTypeEnum type = UDPMessageTypeEnum.valueOf(typeNode.asText());
        JsonNode actionNode = root.get("action");
        if (actionNode == null) {
            throw new IOException("No action field");
        }
        JsonParser objectActionNode = actionNode.traverse(jsonParser.getCodec());

        JsonNode idNode = root.get("mouseId");
        String id = idNode.asText();
        JsonNode sessionIdNode = root.get("sessionId");
        String sessionId = sessionIdNode.asText();

        JsonNode hostAddressNode = root.get("hostAddress");
        String hostAddress = hostAddressNode.asText();

        UDPAction udpAction = null;
        switch (type) {
            case UDPMessageTypeEnum.MOVE:
                udpAction = objectActionNode.readValueAs(Move.class);
                break;
            case UDPMessageTypeEnum.TOUCH:
                udpAction = objectActionNode.readValueAs(Touch.class);
                break;
            case SCROLL:
                udpAction = objectActionNode.readValueAs(Scroll.class);
                break;
            case KEYBOARD:
                udpAction = objectActionNode.readValueAs(KeyboardKey.class);
                break;
            default:
                throw new IOException("Invalid UDPAction type: " + type);
        }

        MessageCreator messageCreator = new MessageCreator(
                id,
                sessionId,
                hostAddress,
                null,
                null,
                udpAction,
                type
        );
        return messageCreator.getMessage();
    }
}
