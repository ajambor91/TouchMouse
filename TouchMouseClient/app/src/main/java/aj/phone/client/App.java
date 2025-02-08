package aj.phone.client;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import aj.phone.client.Utils.Config;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class App extends Application {

    private static App instance;
    public Context context;

    public static App getInstance() {
        Log.d("INIT", "Application started");
        if (instance != null) {
            instance = new App();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance != null) {
            instance = this;
        }
        this.context = this.getApplicationContext();
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        Config.createConfig(context, wifi);

        if (instance != null) {
            Log.d("APP_INIT", "Context set");
        }

    }


}
