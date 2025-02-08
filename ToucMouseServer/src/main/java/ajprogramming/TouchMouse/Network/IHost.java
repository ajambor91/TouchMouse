package ajprogramming.TouchMouse.Network;

public interface IHost {

    public void setHostAddress(String hostAddress);

    public String getHostAddress();

    public void setHostname(String hostname);

    public String getHostname();

    public void setSessionId(String sessionId);
    public String getSessionId();

    public String getAppName();

    public void setAppName(String appName);

}
