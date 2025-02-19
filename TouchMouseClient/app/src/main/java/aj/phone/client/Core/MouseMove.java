package aj.phone.client.Core;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import aj.phone.client.NetworkModule.Enums.EEventStage;
import aj.phone.client.NetworkModule.Enums.EMouseTouch;
import aj.phone.client.NetworkModule.Enums.EMouseTouchType;
import aj.phone.client.NetworkModule.NetworkService;


public class MouseMove {


    private static MouseMove instance;
    private EEventStage eventStage = EEventStage.INITIALIZED;
    private NetworkService networkService;
    private float deltaX = 0;
    private float deltaY = 0;
    private float startY = 0;
    private float startX = 0;
    private long touchStart = 0;
    private boolean lpmButton = false;
    private boolean isPPMDown = false;
    private boolean isLPMDown = false;
    private KeyboardActionListener keyboardActionListener;

    public static MouseMove getInstance() {
        if (MouseMove.instance == null) {
            MouseMove.instance = new MouseMove();
        }
        return MouseMove.instance;
    }

    public void setNetworkModule(NetworkService networkService) {
        this.networkService = networkService;
    }

    public void setActionListener(KeyboardActionListener listener) {
        this.keyboardActionListener = listener;
    }

    public boolean runMouse(View view, MotionEvent event) {

        if (this.eventStage == EEventStage.FINISHED || this.eventStage == EEventStage.INITIALIZED) {
            this.eventStage = EEventStage.STARTED;

        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                this.touchStart = System.currentTimeMillis();
                startX = event.getX();
                startY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:

                this.onMove(view, event);
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                this.onPPMDown(event);
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                this.onPPMUp(event);
                return true;
            case MotionEvent.ACTION_UP:
                deltaX = event.getX() - startX;
                deltaY = event.getY() - startY;

                if (Math.abs(deltaY) > 300 && Math.abs(deltaY) > 100) {
                    this.keyboardActionListener.onKeyboardRequest(view);

                }
                this.onLPMUp();
                return true;

        }
        return true;
    }

    private void onLPMDown(MotionEvent event) {
        if (this.eventStage == EEventStage.MOVING) {
            return;
        }
        if (event.getPointerCount() == 1 && !this.isLPMDown) {
            this.isLPMDown = true;
            this.eventStage = EEventStage.PROGRESS;
            this.networkService.setTouchUDPMessage(EMouseTouch.SINGLE_LPM, EMouseTouchType.DOWN);

        }
    }

    private void onPPMDown(MotionEvent event) {
        if (event.getPointerCount() > 1 && this.eventStage == EEventStage.PPM_INIT) {
            this.isPPMDown = true;
            this.isLPMDown = false;
            this.networkService.setTouchUDPMessage(EMouseTouch.SINGLE_PPM, EMouseTouchType.DOWN);
        }
    }

    private boolean onMove(View view, MotionEvent event) {
        if (startY > view.getHeight() - 200) {
            return true;
        }
        deltaX = event.getX() - startX;
        deltaY = event.getY() - startY;
        startX = event.getX();
        startY = event.getY();
        long touchDuration = System.currentTimeMillis() - touchStart;
        if (event.getPointerCount() > 1) {
            if (deltaY > 1) {
                this.eventStage = EEventStage.SCROLL;
                this.networkService.setTouchUDPMessage(1);
                return true;
            } else if (deltaY < -1) {
                this.eventStage = EEventStage.SCROLL;
                this.networkService.setTouchUDPMessage(-1);
                return true;
            }

        }
        if (event.getPointerCount() == 1 && Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) {
            this.networkService.setTouchUDPMessage((int) deltaX, (int) deltaY);
            Log.d("Touch", "Moving: X=" + deltaX + ", Y=" + deltaY);
            return true;
        } else if (Math.abs(deltaX) < 0.65 && Math.abs(deltaY) < 0.65 && touchDuration < 50) {
            this.onLPMDown(event);
        } else if (Math.abs(deltaX) < 1.1 && Math.abs(deltaY) < 1.1 && touchDuration > 50) {
            this.eventStage = EEventStage.PPM_INIT;
        }
        return true;
    }

    private void onPPMUp(MotionEvent event) {
        Log.d("Touch", String.format("Detected touches, LPM: %b, PPM: %b", lpmButton, isPPMDown));
        if (this.isPPMDown && this.eventStage == EEventStage.PPM_INIT && event.getPointerCount() > 1) {
            this.networkService.setTouchUDPMessage(EMouseTouch.SINGLE_PPM, EMouseTouchType.UP);
            this.networkService.setTouchUDPMessage(EMouseTouch.SINGLE_LPM, EMouseTouchType.UP);
            this.isPPMDown = false;
            this.eventStage = EEventStage.FINISHED;
        }

        lpmButton = false;
    }

    private void onLPMUp() {

        Log.d("Touch", String.format("Flags status, stage: %s, lpmStatus: %b", this.eventStage.getStage(), this.lpmButton));
        if (EEventStage.PROGRESS == this.eventStage || EEventStage.MOVING == this.eventStage && this.isLPMDown) {
            Log.d("Touch", "Touch screen");

        }
        this.networkService.setTouchUDPMessage(EMouseTouch.SINGLE_LPM, EMouseTouchType.UP);
        lpmButton = false;
        this.isLPMDown = false;
        this.eventStage = EEventStage.FINISHED;

    }

    public interface KeyboardActionListener {
        void onKeyboardRequest(View view);
    }

}