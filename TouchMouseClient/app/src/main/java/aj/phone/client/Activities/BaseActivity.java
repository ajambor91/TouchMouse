package aj.phone.client.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import javax.inject.Inject;

import aj.phone.client.Activities.SettingsActivity.SettingsActivity;
import aj.phone.client.Core.ActivitiesManager;
import aj.phone.client.Core.DIModule;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.R;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class BaseActivity extends AppCompatActivity {

    @Inject
    public DIModule diModule;

    protected NetworkModule networkModule;
    protected ActivitiesManager activitiesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activitiesManager = this.diModule.getActivitiesManager();
        this.activitiesManager.setCurrentActivity(this);

        this.networkModule = this.diModule.getNetworkModule();
        this.networkModule.setActivitiesManager(this.activitiesManager);

    }

    public void changeScreen(int containerViewId, Class<? extends Fragment> fragmentClass) {
        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.setReorderingAllowed(true).replace(containerViewId, fragmentClass, null).commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Menu", String.valueOf(item.getItemId()));
        Log.d("Menu", String.valueOf(R.id.settings));
        if (item.getItemId() == R.id.settings) {
            this.activitiesManager.runActivity(SettingsActivity.class);
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
