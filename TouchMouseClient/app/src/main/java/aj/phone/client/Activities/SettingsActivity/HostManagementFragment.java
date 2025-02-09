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
import androidx.fragment.app.FragmentTransaction;
import aj.phone.client.R;

import javax.inject.Inject;

import aj.phone.client.Core.DIModule;
import aj.phone.client.Core.HostManager;
import aj.phone.client.IHost;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.databinding.HostManagementFragmentBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HostManagementFragment extends Fragment {
    @Inject
    public DIModule diModule;
    private NetworkModule networkModule;
    private Button backButton;
    private TextView hostnameText;
    private Button removeButton;

    private Button disconnectButton;
    private final IHost currentHost;
    private HostManager hostManager;
    public HostManagementFragment(IHost host) {
        this.currentHost = host;

    }
    private HostManagementFragmentBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.networkModule = this.diModule.getNetworkModule();

        Log.d("HOST", "Host management initialized");
        this.hostManager = new HostManager(this.currentHost);
        binding = HostManagementFragmentBinding.inflate(inflater, container, false);
        this.hostnameText = binding.managementHostaddr;
        this.backButton = binding.managementBackBtn;
        this.removeButton = binding.managementRemoveBtn;
        this.disconnectButton = binding.managementDisconnectBtn;

        this.hostnameText.setText(String.format("IP Address: %s",this.currentHost.getHostAddress()));
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                back();
            }
        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initButtons();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void back() {
        FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment mainSettings = new SettingsFragment();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, mainSettings).commit();
        }
    }


    private void initButtons() {
        this.diconnectHost();
        this.removeHost();
        this.onBackButton();
    }

    private void diconnectHost() {
        this.disconnectButton.setOnClickListener(e -> {
            this.hostManager.disconnectHost();
        });
    }

    private void removeHost() {
        this.removeButton.setOnClickListener(e -> {
            this.hostManager.removeHost();
        });
    }

    private void onBackButton() {
        this.backButton.setOnClickListener(e -> {
            this.back();
        });
    }

}