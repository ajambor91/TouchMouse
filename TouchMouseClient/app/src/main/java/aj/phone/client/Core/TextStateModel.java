package aj.phone.client.Core;

import androidx.lifecycle.ViewModel;

public class TextStateModel extends ViewModel {

    private boolean isInputExists;
    private String textState;

    public String getTextState() {
        return this.textState;
    }

    public void setTextState(String textState) {
        this.textState = textState;
    }

    public boolean isInputExists() {
        return this.isInputExists;
    }

    public void setInputExists(boolean inputExists) {
        this.isInputExists = inputExists;
    }

    public boolean isEmpty() {
        return textState == null || textState.isEmpty();
    }
}
