package aj.phone.client.Activities.SettingsActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import aj.phone.client.Activities.ConnectionActivity.ConnectionActivity;
import aj.phone.client.Activities.TouchPadActivity.TouchPadActivity;
import aj.phone.client.Core.ActivitiesManager;
import aj.phone.client.Core.DIModule;
import aj.phone.client.IHost;
import aj.phone.client.NetworkModule.Enums.EConnectionStatus;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.R;
import aj.phone.client.Utils.Config;
import aj.phone.client.databinding.SettingsFragmentBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class SettingsFragment extends Fragment {

    @Inject
    public DIModule diModule;
    private NetworkModule networkModule;
    private ActivitiesManager activitiesManager;
    private Config config;
    private EditText mouseName;
    private Button backBtn;
    private Button manageHostBtn;
    private Button changeNameBtn;
    private @NonNull SettingsFragmentBinding binding;
    private HostListAdapter hostListAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.activitiesManager = this.diModule.getActivitiesManager();
        this.networkModule = this.diModule.getNetworkModule();
        this.addActionOnBack();
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        this.setElements(rootView);
        this.addHostsRecyclerView(rootView);
        Log.d("Settings", "Settings fragment initialization");

        return rootView;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Settings", "Settings view created");
        this.addButtonActions();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void setElements(View view) {
        this.mouseName = view.findViewById(R.id.mouseName);
        this.config = Config.getInstance();
        this.mouseName.setText(this.config.getMouseName());
        this.manageHostBtn = view.findViewById(R.id.button_manage_host);
        this.backBtn = view.findViewById(R.id.settings_back_btn);
        this.changeNameBtn = view.findViewById(R.id.button);
    }

    private void changeName() {
        String newName = this.mouseName.getText().toString();
        Log.d("Settings", String.format("New mouse name: %s", newName));
        Config.getInstance().changeMouseName(newName);
        this.networkModule.changeName(newName);
    }

    private void addActionOnBack() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                back();
            }
        });
    }

    private void onChangeName() {
        this.mouseName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) { // Obsługa "Enter"
                    String newName = mouseName.getText().toString();
                    networkModule.changeName(newName);
                    return true;
                }
                return false;
            }
        });
        this.changeNameBtn.setOnClickListener(e -> {
            this.changeName();
        });

    }

    private void addButtonActions() {
        this.onChangeName();
        this.onBack();
        this.onManageHost();
    }


    private void addHostsRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        this.hostListAdapter = new HostListAdapter();
        recyclerView.setAdapter(this.hostListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void onManageHost() {
        this.manageHostBtn.setOnClickListener(e -> {
            Log.d("Settings", "Clicked move to hostManagement");
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            if (this.hostListAdapter.getCurrentHost() != null) {
                Log.d("Settings", "Creating host management fragment");
                IHost host = this.hostListAdapter.getCurrentHost();
                if (this.hostListAdapter.getCurrentHost().getHostAddress().equals(this.networkModule.getHostAddress())) {
                    host = this.networkModule;
                }
                Fragment hostManagementFragment = new HostManagementFragment(host);
                FragmentTransaction ft = fm.beginTransaction().replace(R.id.settings_container_view, hostManagementFragment);
                ft.commit();
            }
        });
    }

    private void onBack() {
        this.backBtn.setOnClickListener(e -> {
            back();
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

