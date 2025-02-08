package aj.phone.client.XML;

import android.util.Log;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import aj.phone.client.AppData;
import aj.phone.client.IAppData;
import aj.phone.client.IHost;

public class XML {
    private HashMap<String, IHost> hostHashMap;
    private IAppData appData;

    public XML() {
        this.initialize();
        this.createAppData();
    }

    public void onFirstRun() {
        try {
            if (!XMLUtils.checkIsDocumentExists()) {
                this.createIfNotExists();
            }
            XMLUtils.initializeConfigOnFirstRun();

        } catch (Exception e) {
            Log.e("XML", "Cannot initialize config", e);
            throw new RuntimeException(e);
        }
    }

    public void addHost(IHost host) {
        if (this.hostHashMap == null) {
            this.initialize();
        }
        Log.d("XML", String.format("Adding host with IP %s", host.getHostAddress()));
        try {
            if (this.hostHashMap.get(host.getHostAddress()) == null) {
                XMLUtils.addHost(host);
                this.hostHashMap = XMLUtils.getHosts();
            }
        } catch (Exception e) {
            Log.e("XML", String.format("Error when adding host %s", e.getMessage()), e);
        }

    }

    public HashMap<String, IHost> getHosts() {
        return this.hostHashMap;
    }

    private void initialize() {
        try {
            if (!XMLUtils.checkIsDocumentExists()) {
                this.createIfNotExists();

            }
            this.hostHashMap = XMLUtils.getHosts();
        } catch (Exception e) {
            Log.e("XML", String.format("Error when xml initialization %s", e.getMessage(), e));
            this.hostHashMap = new HashMap<>();
        }

    }

    public void changeMouseName(String name) {
        try {
            XMLUtils.setOption(XMLUtils.mouseNameTag, name);
            String appName = XMLUtils.getOption(XMLUtils.mouseNameTag, XMLUtils.mouseNameTag);
            this.appData.setMouseName(appName);
        } catch (Exception e) {
            Log.e("XML", "Cannot change mouse name", e);
        }
    }

    public String getMouseName() {
        try {
            if (this.appData != null) {
                return this.appData.getMouseName();
            }
            return XMLUtils.getOption(XMLUtils.mouseNameTag, XMLUtils.mouseNameTag);
        } catch (Exception e) {
            Log.e("XML", "Cannot get mouse name", e);
            return XMLUtils.defaultMouseName;
        }
    }

    public String getAppId() {
        try {
            if (this.appData != null) {
                return this.appData.getAppId();
            }
            return XMLUtils.getOption(XMLUtils.uniqueTag, XMLUtils.defaultOptionAttr);
        } catch (Exception e) {
            Log.e("XML", "Cannot get app id", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getAppName() {
        try {
            if (this.appData != null) {
                return this.appData.getAppName();
            }
            return XMLUtils.getOption(XMLUtils.appNameTag, XMLUtils.defaultOptionAttr);
        } catch (Exception e) {
            Log.e("XML", "Cannot get app name", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private void createAppData() {
        this.appData = new AppData();
        try {
            String mouseName = XMLUtils.getOption(XMLUtils.mouseNameTag, XMLUtils.mouseNameTag);
            String appId = XMLUtils.getOption(XMLUtils.uniqueTag, XMLUtils.defaultOptionAttr);
            String appName = XMLUtils.getOption(XMLUtils.appNameTag, XMLUtils.defaultOptionAttr);
            this.appData.setAppId(appId);
            this.appData.setMouseName(mouseName);
            this.appData.setAppName(appName);
        } catch (Exception e) {
            Log.e("XML", "Cannot creating app data", e);
            this.appData = null;
        }

    }

    private void createIfNotExists() throws ParserConfigurationException, TransformerException {
        XMLUtils.createDocumentDir();
        XMLUtils.createDocumentList();
    }


}
