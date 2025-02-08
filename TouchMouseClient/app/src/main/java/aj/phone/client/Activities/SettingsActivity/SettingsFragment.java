package aj.phone.client.Activities.SettingsActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import aj.phone.client.R;
import aj.phone.client.databinding.SettingsFragmentBinding;

public class SettingsFragment extends Fragment {

    private @NonNull SettingsFragmentBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        HostListAdapter hostListAdapter = new HostListAdapter();
        recyclerView.setAdapter(hostListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("Settings", "Settings fragment initialization");

        return rootView;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Settings", "Settings view created");


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}