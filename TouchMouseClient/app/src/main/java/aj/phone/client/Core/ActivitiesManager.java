package aj.phone.client.Core;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

import aj.phone.client.Activities.BaseActivity;
import aj.phone.client.Activities.ConnectionActivity.ConnectionActivity;
import aj.phone.client.Activities.ConnectionActivity.ConnectionRefreshFragment;
import aj.phone.client.Activities.TouchPadActivity.TouchPadActivity;
import aj.phone.client.R;


public class ActivitiesManager {

    private static ActivitiesManager instance;
    private BaseActivity currentActivity;

    private ActivitiesManager() {
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

    public boolean isOnConnectingOrTouchpad() {
        Log.d("TCP ACTIVITY", String.format("Is connection activity %b", this.currentActivity instanceof ConnectionActivity));
        return this.currentActivity instanceof ConnectionActivity || this.currentActivity instanceof TouchPadActivity;
    }

    public void runActivity(Class<?> activityToOpen) {
        if (this.currentActivity.getClass() != activityToOpen) {
            Log.d("ACT_CHANGE", "Running new activity");
            Intent intent = new Intent(this.currentActivity, activityToOpen);
            this.currentActivity.startActivity(intent);
        }

    }

    public void runActivityWithScreen(Class<?> activityToOpen) {
        if (activityToOpen != this.currentActivity.getClass()) {
            Log.d("ACT_CHANGE", "Changing activity");
            Intent intent = new Intent(this.currentActivity, activityToOpen);
            intent.putExtra("frg", R.id.refresh_fragment);
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
