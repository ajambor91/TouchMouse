package aj.phone.client.Core;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.HashMap;

import aj.phone.client.Activities.BaseActivity;
import aj.phone.client.Activities.ConnectionActivity.ConnectionRefreshFragment;
import aj.phone.client.Activities.SettingsActivity.SettingsActivity;
import aj.phone.client.R;


public class ActivitiesManager {

    private static ActivitiesManager instance;
    private final HashMap<String, BaseActivity> activities;
    private BaseActivity previousActivity;
    private BaseActivity currentActivity;

    private ActivitiesManager() {
        this.activities = new HashMap<>();
    }

    public static ActivitiesManager getInstance() {
        if (ActivitiesManager.instance == null) {
            ActivitiesManager.instance = new ActivitiesManager();
        }
        return ActivitiesManager.instance;
    }

    public void setCurrentActivity(BaseActivity activity) {
        this.currentActivity = activity;
    }

    public boolean isOnSettings() {
        return this.currentActivity instanceof SettingsActivity;
    }

    public void runActivity(Class<?> activityToOpen) {
        if (this.currentActivity.getClass() != activityToOpen) {
            Log.d("ACT_CHANGE", "Running new activity");
            Intent intent = new Intent(this.currentActivity, activityToOpen);
            this.previousActivity = this.currentActivity;
            this.currentActivity.startActivity(intent);
        }

    }

    public BaseActivity getPreviousActivity() {
        return this.previousActivity;
    }

    public void previousActivity() {
        if (this.previousActivity != null) {
            Intent intent = new Intent(this.currentActivity, this.previousActivity.getClass());
            this.currentActivity.startActivity(intent);
        }
    }

    public void runActivityWithScreen(Class<?> activityToOpen) {
        if (activityToOpen != this.currentActivity.getClass()) {
            Log.d("ACT_CHANGE", "Changing activity");
            Intent intent = new Intent(this.currentActivity, activityToOpen);
            intent.putExtra("frg", 1);
            this.previousActivity = this.currentActivity;
            this.currentActivity.startActivity(intent);
        } else {
            Log.d("ACT_CHANGE", "Changing screen instead activity");
            this.currentActivity.changeScreen(R.id.fragmentContainerView, ConnectionRefreshFragment.class);
        }
    }


    public void runScreen(int containerViewId, Class<? extends Fragment> fragmentClass) {
        this.currentActivity.changeScreen(containerViewId, fragmentClass);
    }

}
