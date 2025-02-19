package aj.phone.client.Activities.TouchPadActivity;

import android.content.Context;
import android.os.Bundle;

import javax.inject.Inject;

import aj.phone.client.Activities.BaseActivity;
import aj.phone.client.Core.DIModule;
import aj.phone.client.databinding.ActivityTouchpadBinding;

public class TouchPadActivity extends BaseActivity {
    @Inject
    public DIModule diModule;
    private ActivityTouchpadBinding binding;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTouchpadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
