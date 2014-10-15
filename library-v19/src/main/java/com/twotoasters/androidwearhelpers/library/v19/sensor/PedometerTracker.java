package com.twotoasters.androidwearhelpers.library.v19.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.twotoasters.androidwearhelpers.library.sensor.SensorTrackerBase;

public class PedometerTracker extends SensorTrackerBase {

    private OnStepCountChangedListener stepCountChangedListener;
    private OnStepDetectedListener stepDetectedListener;

    public PedometerTracker(Context context) {
        super(context, new int[]{ Sensor.TYPE_STEP_COUNTER, Sensor.TYPE_STEP_DETECTOR });
    }

    /**
     * Sets a listener for receiving callbacks when the pedometer's step count changes.
     * @param stepCountChangedListener
     */
    public void setOnStepCountChangedListener(OnStepCountChangedListener stepCountChangedListener) {
        this.stepCountChangedListener = stepCountChangedListener;
    }

    /**
     * Sets a listener for receiving callbacks when the pedometer detects that a step has been taken.
     * @param stepDetectedListener
     */
    public void setOnStepDetectedListener(OnStepDetectedListener stepDetectedListener) {
        this.stepDetectedListener = stepDetectedListener;
    }

    @Override
    protected void onRegister() {
        //no-op
    }

    @Override
    protected void onUnregister() {
        //no-op
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && stepCountChangedListener != null) {
            stepCountChangedListener.onStepCountChanged((int)event.values[0]);
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR && stepDetectedListener != null) {
            stepDetectedListener.onStepDetected();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //no-op
    }

    public interface OnStepCountChangedListener {
        /**
         * Called when the pedometer's step count changes. The step count is equal to the total number
         * of steps taken since the device was last rebooted.
         *
         * @param stepCount
         */
        void onStepCountChanged(int stepCount);
    }

    public interface OnStepDetectedListener {
        /**
         * Called when the pedometer detects that a step has been taken.
         */
        void onStepDetected();
    }
}
