package aj.phone.client.Activities.ConnectionActivity;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import aj.phone.client.Activities.BaseActivity;
import aj.phone.client.Core.ActivitiesManager;
import aj.phone.client.R;
import aj.phone.client.databinding.ConnectionActivityBinding;

public class ConnectionActivity extends BaseActivity {

    @Inject
    public ActivitiesManager activitiesManager;

    private ConnectionActivityBinding binding;

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ConnectionActivityBinding.inflate(getLayoutInflater());
        Log.d("APP_MAIN", "Initialized main activity");
        try {
            Bundle intentFragment = getIntent().getExtras();
            if (intentFragment != null) {
                int fragment = intentFragment.getInt("frg");
                Log.d("APP_DEBUG", String.format("Displays main app screen %s", fragment));

                if (fragment == R.id.refresh_fragment) {
                    this.initReconnect();
                    return;
                }
                Log.d("APP_DEBUG", "No fragment");
            }
            this.initMainApp();
        } catch (Exception e) {
            Log.e("APP", "On create error", e);
        }


    }

    private void initReconnect() throws Exception {
        Log.d("APP_MAIN", "Displays reconnect screen");
        this.changeScreen(R.id.fragmentContainerView, ConnectionRefreshFragment.class);
        setContentView(binding.getRoot());

    }

    private void initMainApp() {
        Log.d("APP_MAIN", "Displays main app screen");
        this.networkService.initialize();
        setContentView(binding.getRoot());

    }

}