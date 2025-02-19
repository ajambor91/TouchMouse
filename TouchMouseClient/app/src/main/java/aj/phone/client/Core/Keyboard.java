package aj.phone.client.Core;

import android.util.Log;

import aj.phone.client.NetworkModule.NetworkService;

public class Keyboard {

    private static Keyboard instance;
    private NetworkService networkService;

    private Keyboard() {

    }

    public static Keyboard getInstance() {
        if (Keyboard.instance == null) {
            Keyboard.instance = new Keyboard();
        }
        return Keyboard.instance;
    }

    public void setNetworkService(NetworkService networkService) {
        this.networkService = networkService;
    }

    public boolean processKeyEvent(String keyCode) {
        if (this.networkService != null) {
            Log.d("Keyboard", "Process key event " + keyCode);
            this.networkService.setKeyUDPMessage(keyCode);
        }
        return true;
    }
}
