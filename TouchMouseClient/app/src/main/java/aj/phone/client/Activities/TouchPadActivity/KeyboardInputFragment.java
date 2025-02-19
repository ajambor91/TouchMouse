package aj.phone.client.Activities.TouchPadActivity;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import aj.phone.client.NetworkModule.Enums.TCPMessageTypeEnum;
import aj.phone.client.NetworkModule.Message.EFunctionalKey;
import aj.phone.client.NetworkModule.NetworkService;
import aj.phone.client.R;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class KeyboardInputFragment extends Fragment {

    @Inject
    public DIModule diModule;

    private NetworkService networkService;
    private Keyboard keyboard;
    private EditText keyboardInput;


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

    private void sendKeyboardShowMessage() {
        this.networkService.setTCPMessage(TCPMessageTypeEnum.KEYBOARD_SHOW);
    }

    private void addKeyboardEvent() {
        Log.d("Keayboard", "Adding keyboard events");
        keyboardInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                assert EFunctionalKey.fromKey(keyCode) != null;
                keyboard.processKeyEvent(EFunctionalKey.fromKey(keyCode).getKeyValue());
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
            Log.d("Keyboard ", "Focusing on edit text");
            keyboardInput.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(this.keyboardInput, InputMethodManager.SHOW_IMPLICIT);
            Log.d("Keyboard ", "Keyboard view");
        });
    }

    private void setKeyboardInput(View view) {
        this.keyboardInput = view.findViewById(R.id.keyboard_input);
    }


}