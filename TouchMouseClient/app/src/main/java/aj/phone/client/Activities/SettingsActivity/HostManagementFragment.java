package aj.phone.client.Activities.SettingsActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import javax.inject.Inject;

import aj.phone.client.Core.ActivitiesManager;
import aj.phone.client.Core.DIModule;
import aj.phone.client.Core.HostManager;
import aj.phone.client.IHost;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.R;
import aj.phone.client.databinding.HostManagementFragmentBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HostManagementFragment extends Fragment {
    private final IHost currentHost;
    @Inject
    public DIModule diModule;
    private NetworkModule networkModule;
    private Button backButton;
    private TextView hostnameText;
    private Button removeButton;
    private ActivitiesManager activitiesManager;
    private Button disconnectButton;
    private HostManagementFragmentBinding binding;

    private HostManager hostManager;


    public HostManagementFragment(IHost host) {
        this.currentHost = host;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.networkModule = this.diModule.getNetworkModule();
        this.activitiesManager = this.diModule.getActivitiesManager();
        Log.d("HOST", "Host management initialized");
        binding = HostManagementFragmentBinding.inflate(inflater, container, false);
        this.initHostManager();
        this.setElements();
        this.initHostnameText();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.addActionOnBackAction();
        this.initButtons();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initHostManager() {
        this.hostManager = new HostManager(this.currentHost);

    }

    private void setElements() {
        this.hostnameText = binding.managementHostaddr;
        this.backButton = binding.managementBackBtn;
        this.removeButton = binding.managementRemoveBtn;
        this.disconnectButton = binding.managementDisconnectBtn;
    }

    private void initHostnameText() {
        this.hostnameText.setText(String.format("IP Address: %s", this.currentHost.getHostAddress()));

    }

    private void addActionOnBackAction() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                back();
            }
        });
    }

    private void back() {
        FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment mainSettings = new SettingsFragment();
            fragmentManager.beginTransaction().replace(R.id.settings_container_view, mainSettings).commit();
        }
    }


    private void initButtons() {
        this.diconnectHost();
        this.removeHost();
        this.onBackButton();
    }

    private void reconnectHost() {

    }

    private void diconnectHost() {
        if (this.currentHost.isActiveHost()) {
            this.disconnectButton.setOnClickListener(e -> {
                if (disconnectButton.getText().equals("Disconnect host")) {
                    this.hostManager.disconnectHost();
                    this.disconnectButton.setText("Reconnect host");
                } else  {
                    this.hostManager.reconnectHost();
                    this.disconnectButton.setText("Disconnect host");
                }

            });
        } else {
            this.disconnectButton.setEnabled(false);
        }

    }

    private void removeHost() {
        this.removeButton.setOnClickListener(e -> {
            this.hostManager.removeHost();
            back();
        });
    }

    private void onBackButton() {
        this.backButton.setOnClickListener(e -> {
            this.back();
        });
    }

}