package aj.phone.client.Core;

import android.util.Log;
import android.view.MotionEvent;

import aj.phone.client.NetworkModule.Enums.EEventStage;
import aj.phone.client.NetworkModule.Enums.EMouseTouch;
import aj.phone.client.NetworkModule.Enums.EMouseTouchType;
import aj.phone.client.NetworkModule.NetworkModule;


public class MouseMove {
    private static MouseMove instance;
    private EEventStage eventStage = EEventStage.INITIALIZED;
    private NetworkModule networkModule;
    private float deltaX = 0;
    private float deltaY = 0;
    private float startY = 0;
    private float startX = 0;
    private long touchStart = 0;
    private boolean lpmButton = false;
    private boolean isPPMDown = false;
    private boolean isLPMDown = false;


    public static MouseMove getInstance() {
        if (MouseMove.instance == null) {
            MouseMove.instance = new MouseMove();
        }
        return MouseMove.instance;
    }

    public void setNetworkModule(NetworkModule networkModule) {
        this.networkModule = networkModule;
    }

    public boolean runMouse(MotionEvent event) {

        if (this.eventStage == EEventStage.FINISHED || this.eventStage == EEventStage.INITIALIZED) {
            this.eventStage = EEventStage.STARTED;

        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                this.touchStart = System.currentTimeMillis();
                startX = event.getX();
                startY = event.getY();
                Log.d("Touch", String.format("Touch DOWNDetected touch, with clicks count: %s, LPM status: %b", event.getPointerCount(), this.isLPMDown));
                return true;
            case MotionEvent.ACTION_MOVE:
                this.onMove(event);
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                this.onPPMDown(event);
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                this.onPPMUp(event);
                return true;
            case MotionEvent.ACTION_UP:
                this.onLPMUp(event);
                return true;

        }
        return true;
    }

    private void onLPMDown(MotionEvent event) {
        if (this.eventStage == EEventStage.MOVING) {
            return;
        }
        Log.d("Touch", String.format("Detected inside Motion single click stage %s, lpm %b", this.eventStage.getStage(), this.isLPMDown));

        if (event.getPointerCount() == 1 && !this.isLPMDown) {
            this.isLPMDown = true;
            this.eventStage = EEventStage.PROGRESS;
            Log.d("Touch", String.format("Down mowe y: %s, x: %s", event.getY(), event.getX()));
            this.networkModule.setTouchUDPMessage(EMouseTouch.SINGLE_LPM, EMouseTouchType.DOWN);

            Log.d("Touch", "Detected single click");
        }
    }

    private void onPPMDown(MotionEvent event) {
        if (event.getPointerCount() > 1 && this.eventStage == EEventStage.PPM_INIT) {

            Log.d("Touch", "Detected double touch");
            this.isPPMDown = true;
            this.isLPMDown = false;
            this.networkModule.setTouchUDPMessage(EMouseTouch.SINGLE_PPM, EMouseTouchType.DOWN);
        }
    }

    private boolean onMove(MotionEvent event) {

        deltaX = event.getX() - startX;
        deltaY = event.getY() - startY;

        startX = event.getX();
        startY = event.getY();
        long touchDuration = System.currentTimeMillis() - touchStart;

        Log.d("Touch", "Moving: X=" + deltaX + ", Y=" + deltaY + " time: " + touchDuration);
        if (event.getPointerCount() > 1) {
            if (deltaY > 1) {
                this.eventStage = EEventStage.SCROLL;
                this.networkModule.setTouchUDPMessage(1);
                Log.d("Touch", "Detected double ACTION MOVE");
                return true;
            } else if (deltaY < -1) {
                this.eventStage = EEventStage.SCROLL;
                this.networkModule.setTouchUDPMessage(-1);
                Log.d("Touch", "Detected double ACTION MOVE");
                return true;
            }

        }
        if (event.getPointerCount() == 1 && Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) {

            this.networkModule.setTouchUDPMessage((int) deltaX, (int) deltaY);

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
            this.networkModule.setTouchUDPMessage(EMouseTouch.SINGLE_PPM, EMouseTouchType.UP);
            this.networkModule.setTouchUDPMessage(EMouseTouch.SINGLE_LPM, EMouseTouchType.UP);
            this.isPPMDown = false;
            this.eventStage = EEventStage.FINISHED;

        }

        lpmButton = false;
    }

    private void onLPMUp(MotionEvent event) {
        Log.d("Touch", String.format("Flags status, stage: %s, lpmStatus: %b", this.eventStage.getStage(), this.lpmButton));
        if (EEventStage.PROGRESS == this.eventStage || EEventStage.MOVING == this.eventStage && this.isLPMDown) {
            Log.d("Touch", "Touch screen");

        }
        this.networkModule.setTouchUDPMessage(EMouseTouch.SINGLE_LPM, EMouseTouchType.UP);
        lpmButton = false;
        this.isLPMDown = false;
        this.eventStage = EEventStage.FINISHED;

    }

}