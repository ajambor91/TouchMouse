package aj.phone.client.Activities.HelpActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import aj.phone.client.Activities.ConnectionActivity.ConnectionActivity;
import aj.phone.client.Activities.TouchPadActivity.TouchPadActivity;
import aj.phone.client.Core.ActivitiesManager;
import aj.phone.client.Core.DIModule;
import aj.phone.client.NetworkModule.Enums.EConnectionStatus;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.R;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HelpFragment extends Fragment {

    @Inject
    public DIModule diModule;

    private NetworkModule networkModule;
    private ActivitiesManager activitiesManager;
    private Button backBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Help", "Help fragment on create");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert container != null;
        this.activitiesManager = this.diModule.getActivitiesManager();
        this.networkModule = this.diModule.getNetworkModule();
        View view = inflater.inflate(R.layout.help_fragment, container, false);
        this.backBtn = view.findViewById(R.id.help_back_btn);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Settings", "Settings view created");
        this.addButtonAction();
        this.addActionOnBack();


    }

    private void addButtonAction() {
        this.onBack();
    }

    private void onBack() {
        this.backBtn.setOnClickListener(e -> {
            back();
        });
    }

    private void addActionOnBack() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                back();
            }
        });
    }

    private void back() {
        if (this.networkModule.getConnectionStatus() == EConnectionStatus.CONNECTED) {
            this.activitiesManager.runActivity(TouchPadActivity.class);

        } else if (this.networkModule.getConnectionStatus() == EConnectionStatus.FAIL || this.networkModule.getConnectionStatus() == EConnectionStatus.DISCONNECTED){
            this.activitiesManager.runActivityWithScreen(ConnectionActivity.class);

        } else  {
            this.activitiesManager.runActivity(ConnectionActivity.class);

        }
    }
}