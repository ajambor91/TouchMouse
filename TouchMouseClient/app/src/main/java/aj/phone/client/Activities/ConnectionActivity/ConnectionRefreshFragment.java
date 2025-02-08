package aj.phone.client.Activities.ConnectionActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import aj.phone.client.Core.ActivitiesManager;
import aj.phone.client.Core.DIModule;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.R;
import aj.phone.client.databinding.ConnectionFragmentRefreshBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConnectionRefreshFragment extends Fragment {
    @Inject
    public DIModule diModule;
    private NetworkModule networkModule;
    private ActivitiesManager activitiesManager;
    private ConnectionFragmentRefreshBinding binding;

    public ConnectionRefreshFragment() throws Exception {

    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d("REFRESH", "Initializing reconnecting screen");
        View view = inflater.inflate(R.layout.connection_fragment_refresh, container, false);
        this.networkModule = this.diModule.getNetworkModule();
        this.activitiesManager = this.diModule.getActivitiesManager();
        Log.d("REFRESH", "Get activities manager");


        binding = ConnectionFragmentRefreshBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button refreshButton = view.findViewById(R.id.refresh_button);

        Log.d("REFRESH", "Found reconnect button");

        refreshButton.setOnClickListener(b -> {
            Log.d("REFRESH", "Reconnect button: adding action");

            this.reconnect();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void reconnect() {
        Log.d("REFRESH", "Refreshing");
        this.activitiesManager.runScreen(R.id.fragmentContainerView, ConnectionMainFragment.class);
        this.networkModule.reconnect();
    }

}