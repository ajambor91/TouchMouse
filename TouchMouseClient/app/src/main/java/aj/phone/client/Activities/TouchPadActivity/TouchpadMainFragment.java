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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import javax.inject.Inject;

import aj.phone.client.Core.DIModule;
import aj.phone.client.Core.MouseMove;
import aj.phone.client.NetworkModule.NetworkService;
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
    private NetworkService networkService;

    public TouchpadMainFragment() throws Exception {
        super(R.layout.touchpad_fragment_main);

    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.mouseMove = this.diModule.getMouseMove();
        this.networkService = this.diModule.getNetworkModule();
        this.mouseMove.setNetworkModule(this.networkService);
        Log.d("TOUCHPAD", "Initialized touchpad fragment");
        View view = inflater.inflate(R.layout.touchpad_fragment_main, container, false);
        return view.getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        this.addTouchEvent(view);

        Log.d("TOUCHPAD", "Adding event into screen");

    }

    private void showKeyboard() {
        Log.d("KEYBOARD", "Activity listener");

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        KeyboardInputFragment keyboardInputFragment = new KeyboardInputFragment();
        FragmentTransaction fr = fragmentManager.beginTransaction();
        fr.add(R.id.fragmentContainerView, keyboardInputFragment).commit();
    }

    private void addTouchEvent(View view) {
        mouseMove.setActionListener(v -> {
            this.showKeyboard();
        });

        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return mouseMove.runMouse(v, event);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("KEYBOARD", "Destroy view");
        binding = null;
    }

}