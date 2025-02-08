package aj.phone.client;

public class AppData implements IAppData {

    private String appId;
    private String appName;

    private String mouseName;

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String name) {
        this.appName = name;
    }

    public String getMouseName() {
        return this.mouseName;
    }

    public void setMouseName(String mouseName) {
        this.mouseName = mouseName;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
