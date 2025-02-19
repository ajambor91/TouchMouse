package ajprogramming.TouchMouse.Network.Messages;

public interface INetworkMessage {
    String getMouseId();

    void setMouseId(String mouseId);

    String getHostname();

    void setHostname(String hostname);

    String getHostAddress();

    void setHostAddress(String hostAddress);

    String getSessionId();

    void setSessionId(String sessionId);

    String getAppName();

    void setAppName(String appName);

    String getMouseName();

    void setMouseName(String mouseName);

    String getMouseAddress();

    void setMouseAddress(String mouseAddress);

}
