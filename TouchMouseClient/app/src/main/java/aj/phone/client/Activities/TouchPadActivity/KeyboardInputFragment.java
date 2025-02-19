package aj.phone.client.Activities.TouchPadActivity;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import aj.phone.client.Core.DIModule;
import aj.phone.client.Core.Keyboard;
import aj.phone.client.NetworkModule.Enums.EFunctionalKey;
import aj.phone.client.NetworkModule.Enums.TCPMessageTypeEnum;
import aj.phone.client.NetworkModule.NetworkService;
import aj.phone.client.R;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class KeyboardInputFragment extends Fragment {

    private final TouchpadMainFragment touchpadMainFragment;
    @Inject
    public DIModule diModule;
    private NetworkService networkService;
    private Keyboard keyboard;
    private EditText keyboardInput;

    public KeyboardInputFragment(TouchpadMainFragment touchpadMainFragment) {
        this.touchpadMainFragment = touchpadMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard_input, container, false);
        this.keyboard = this.diModule.getKeyboard();
        this.networkService = this.diModule.getNetworkModule();
        this.keyboard.setNetworkService(this.networkService);
        this.setKeyboardInput(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sendKeyboardShowMessage();
        this.addKeyboardEvent();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.touchpadMainFragment.setTextState(String.valueOf(this.keyboardInput.getText()));
    }

    @Override
    public void onResume() {
        super.onResume();
        keyboardInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.keyboardInput, InputMethodManager.SHOW_IMPLICIT);
    }

    public void setInputFocus() {
        keyboardInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.keyboardInput, InputMethodManager.SHOW_IMPLICIT);
    }

    private void sendKeyboardShowMessage() {
        this.networkService.setTCPMessage(TCPMessageTypeEnum.KEYBOARD_SHOW);
    }

    private void addKeyboardEvent() {
        keyboardInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EFunctionalKey keyCodeEnum = EFunctionalKey.fromKey(keyCode);
                if (keyCodeEnum == EFunctionalKey.RTN) {
                    keyboardInput.setText("");
                }
                if (keyCodeEnum != null) {
                    keyboard.processKeyEvent(keyCodeEnum.getKeyValue());
                } else if (
                        EBackEvent.BACK.getEventType() == keyCode
                ) {
                    destroyKeyboard();
                }
                return true;
            }
        });
        keyboardInput.addTextChangedListener(new TextWatcher() {
            private String beforeText = null;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (beforeText.length() < s.length()) {
                    String result = s.toString().replace(beforeText, "");
                    if (result.equals(" ")) {
                        result = EFunctionalKey.SPC.getKeyValue();
                    }
                    keyboard.processKeyEvent(result);
                } else if (beforeText.length() > s.length()) {
                    keyboard.processKeyEvent(EFunctionalKey.BCKSPC.getKeyValue());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.equals(beforeText)) {
                    beforeText = s.toString();
                }
            }
        });
        this.keyboardInput.post(() -> {
            this.setInputFocus();
        });
    }

    private void destroyKeyboard() {
        this.touchpadMainFragment.destroyTextFragment();
    }

    private void setKeyboardInput(View view) {
        this.keyboardInput = view.findViewById(R.id.keyboard_input);
    }

}