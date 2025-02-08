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
    public void setAppName(String appName);

    public String getAppName();
    public String getMouseName();

    public void setMouseName(String mouseName);

    public void setMouseAddress(String mouseAddress);

    public String getMouseAddress();

}
