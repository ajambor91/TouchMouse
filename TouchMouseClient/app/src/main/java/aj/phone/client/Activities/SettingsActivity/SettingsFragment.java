package aj.phone.client.Activities.SettingsActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import aj.phone.client.Core.HostManager;
import aj.phone.client.IHost;
import aj.phone.client.R;
import aj.phone.client.databinding.SettingsFragmentBinding;

public class SettingsFragment extends Fragment {

    private Button manageHostBtn;
    private Button changeNameBtn;
    private @NonNull SettingsFragmentBinding binding;
    private HostListAdapter hostListAdapter;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        this.manageHostBtn = rootView.findViewById(R.id.button_manage_host);
        this.changeNameBtn = rootView.findViewById(R.id.button);
        this.hostListAdapter = new HostListAdapter();
        recyclerView.setAdapter(this.hostListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    private void addButtonActions() {
        this.manageHostBtn.setOnClickListener(e -> {
            Log.d("Settings", "Clicked move to hostManagement");
            FragmentManager fm =  requireActivity().getSupportFragmentManager();
            if (this.hostListAdapter.getCurrentHost() != null){
                Log.d("Settings", "Creating host management fragment");
                Fragment hostManagementFragment = new HostManagementFragment(this.hostListAdapter.getCurrentHost());
                FragmentTransaction ft = fm.beginTransaction().replace(R.id.fragmentContainerView, hostManagementFragment);
                ft.commit();
            }
        });

    }

}