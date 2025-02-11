package aj.phone.client.Activities.HelpActivity;

import android.os.Bundle;
import android.util.Log;

import aj.phone.client.Activities.BaseActivity;
import aj.phone.client.databinding.ActivityHelpBinding;

public class HelpActivity extends BaseActivity {

    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Help", "Help activity initialization");
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d("Help", "Help activity initialized");
    }
}