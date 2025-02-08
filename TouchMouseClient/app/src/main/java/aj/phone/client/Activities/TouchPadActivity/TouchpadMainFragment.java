package aj.phone.client.Activities.TouchPadActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import aj.phone.client.Core.DIModule;
import aj.phone.client.Core.MouseMove;
import aj.phone.client.NetworkModule.NetworkModule;
import aj.phone.client.R;
import aj.phone.client.databinding.ActivityTouchpadBinding;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class TouchpadMainFragment extends Fragment {

    @Inject
    public DIModule diModule;
    private GestureDetector detector;
    private ActivityTouchpadBinding binding;

    private MouseMove mouseMove;
    private NetworkModule networkModule;

    public TouchpadMainFragment() throws Exception {
        super(R.layout.touchpad_fragment_main);

    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.mouseMove = this.diModule.getMouseMove();
        this.networkModule = this.diModule.getNetworkModule();
        this.mouseMove.setNetworkModule(this.networkModule);
        Log.d("TOUCHPAD", "Initialized touchpad fragment");
        View view = inflater.inflate(R.layout.touchpad_fragment_main, container, false);
        return view.getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        NetworkModule networkModuleL = this.networkModule;
        MouseMove move = this.mouseMove;
        Log.d("TOUCHPAD", "Adding event into screen");
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return move.runMouse(event);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}