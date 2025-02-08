package aj.phone.client.Activities.ConnectionActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import aj.phone.client.Activities.BaseActivity;
import aj.phone.client.Core.ActivitiesManager;
import aj.phone.client.Core.DIModule;
import aj.phone.client.R;
import aj.phone.client.databinding.ConnectionActivityBinding;

public class ConnectionActivity extends BaseActivity {

    @Inject
    public ActivitiesManager activitiesManager;
    @Inject
    public DIModule diModule;
    private ConnectionActivityBinding binding;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ConnectionActivityBinding.inflate(getLayoutInflater());


        Log.d("APP_MAIN", "Initialized main activity");
        Bundle intentFragment = getIntent().getExtras();

        if (intentFragment != null) {
            int fragment = intentFragment.getInt("frg");
            Log.d("APP_DEBUG", String.format("Displays main app screen %s", fragment));

            switch (fragment) {
                case 1:
                    try {
                        this.initReconnect();
                    } catch (Exception e) {
                        Log.d("APP_DEBUG", e.getMessage());
                        this.initMainApp();
                    }
                    break;
            }
        } else {
            Log.d("APP_DEBUG", "No fragment");

            this.initMainApp();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }

    private void initReconnect() throws Exception {
        Log.d("APP_MAIN", "Displays reconnect screen");
        this.changeScreen(R.id.fragmentContainerView, ConnectionRefreshFragment.class);
        setContentView(binding.getRoot());

    }

    private void initMainApp() {
        Log.d("APP_MAIN", "Displays main app screen");
        this.networkModule.initialize();
        setContentView(binding.getRoot());

    }

}