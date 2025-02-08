package aj.phone.client.Activities.SettingsActivity;

import android.os.Bundle;

import androidx.navigation.ui.AppBarConfiguration;

import aj.phone.client.Activities.BaseActivity;
import aj.phone.client.databinding.ActivitySettingsBinding;

public class SettingsActivity extends BaseActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}