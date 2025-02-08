package aj.phone.client.Utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

import aj.phone.client.IHost;
import aj.phone.client.XML.XML;

public class Config {

    private static boolean isCreated = false;
    private static Config instance;
    private XML xml;
    private final String dataPath;
    private final DhcpInfo dhcpInfo;
    private final File dataPathDir;
    private final Context context;

    private Config(Context context, WifiManager manager) {
        this.context = context;
        this.dataPathDir = context.getFilesDir();
        this.dataPath = context.getFilesDir().getAbsolutePath();
        this.dhcpInfo = manager.getDhcpInfo();
    }

    public static void createConfig(Context context, WifiManager manager) {
        if (instance == null && !isCreated) {
            instance = new Config(context, manager);
            isCreated = true;
            instance.xml = new XML();
            instance.xml.onFirstRun();
        } else {
            throw new RuntimeException("Cannot create another config instance");

        }

    }

    public static Config getInstance() {
        return instance;
    }

    public DhcpInfo getDhcpInfo() {
        return dhcpInfo;
    }

    public File getDataPathDir() {
        return dataPathDir;
    }

    public String getDataPath() {
        return dataPath;
    }

    public Context getContext() {
        return context;
    }

    public String getAppName() {
        return this.xml.getAppName();
    }

    public void addHost(IHost host) {
        this.xml.addHost(host);
    }

    public void changeMouseName(String name) {
        this.xml.changeMouseName(name);
    }

    public String getMouseName() {
        return this.xml.getMouseName();
    }

    public String getAppId() {
        return this.xml.getAppId();
    }

    public HashMap<String, IHost> getHosts() {
        return this.xml.getHosts();
    }

    public String getSelfIp() {
        String selfIpAddr = this.convertLongToIp(this.dhcpInfo.ipAddress);

        Log.d("CONFIG", String.format("Self ip address %s", selfIpAddr));
        return selfIpAddr;
    }

    public String convertLongToIp(long ip) {
        return ((ip >> 24) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                (ip & 0xFF);
    }
}
