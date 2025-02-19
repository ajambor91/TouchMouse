package ajprogramming.TouchMouse.Network;

public interface IHost {

    String getHostAddress();

    void setHostAddress(String hostAddress);

    String getHostname();

    void setHostname(String hostname);

    String getSessionId();

    void setSessionId(String sessionId);

    String getAppName();

    void setAppName(String appName);

}
