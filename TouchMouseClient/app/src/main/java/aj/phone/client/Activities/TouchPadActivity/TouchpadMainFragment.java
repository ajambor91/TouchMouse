package aj.phone.client.Activities.TouchPadActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import aj.phone.client.Core.DIModule;
import aj.phone.client.Core.MouseMove;
import aj.phone.client.Core.TextStateModel;
import aj.phone.client.NetworkModule.NetworkService;
import aj.phone.client.R;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class TouchpadMainFragment extends Fragment {
    @Inject
    public DIModule diModule;
    private KeyboardInputFragment keyboardInputFragment;

    private MouseMove mouseMove;
    private TextStateModel textStateModel;
    private NetworkService networkService;

    public TouchpadMainFragment() {
        super(R.layout.touchpad_fragment_main);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.textStateModel = new ViewModelProvider(this).get(TextStateModel.class);
        this.mouseMove = this.diModule.getMouseMove();
        this.networkService = this.diModule.getNetworkModule();
        this.mouseMove.setNetworkModule(this.networkService);
        View view = inflater.inflate(R.layout.touchpad_fragment_main, container, false);
        return view.getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.addTouchEvent(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!this.textStateModel.isEmpty() && this.textStateModel.isInputExists()) {
            Log.d("Cycle Touch show keyboard", "On start");
            this.showKeyboard();
        }
    }

    public void setTextState(String textState) {
        Log.d("Cycle Touch show keyboard", "Set text state");
        this.textStateModel.setTextState(textState);
    }

    public void destroyTextFragment() {
        this.textStateModel.setInputExists(false);
        this.textStateModel.setTextState(null);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(this.keyboardInputFragment).commit();
        this.keyboardInputFragment = null;
    }

    private void showKeyboard() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fr = fragmentManager.beginTransaction();
        fr.show(keyboardInputFragment).commit();
        this.keyboardInputFragment.setInputFocus();
    }


    private void createKeyboardFragment() {
        this.textStateModel.setInputExists(true);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        this.keyboardInputFragment = new KeyboardInputFragment(this);
        FragmentTransaction fr = fragmentManager.beginTransaction();
        fr.add(R.id.fragmentContainerView, keyboardInputFragment).commit();
    }

    private void addTouchEvent(View view) {
        mouseMove.setActionListener(v -> {
            if (!this.textStateModel.isInputExists()) {
                this.createKeyboardFragment();
            } else {
                this.showKeyboard();
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return mouseMove.runMouse(v, event);
            }
        });
    }
}