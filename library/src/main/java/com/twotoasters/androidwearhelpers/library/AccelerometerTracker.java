package com.twotoasters.androidwearhelpers.library;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.opengl.Matrix;

public class AccelerometerTracker extends SensorTrackerBase {

    private static final float ACCEL_HIGH_BOUND = 9.0f;
    private static final float ACCEL_LOW_BOUND = 1.0f;
    private static final float FLICK_TO_NEUTRAL_DIFF = 4.5f;

    //Accelerometer normalization variables
    private float[] gravity;
    private float[] linear_acceleration;

    //Flick listening
    private OnFlickListener onFlickListener;
    private float lastHighestAccel;
    private boolean isFlicking;

    //Sensor change listening
    private OnAccelerationChangedListener onAccelerationChangedListener;

    public AccelerometerTracker(Context context) {
        super(context, Sensor.TYPE_ACCELEROMETER);
    }

    public void setOnFlickListener(OnFlickListener listener) {
        this.onFlickListener = listener;
    }

    public void setOnAccelerationChangedListener(OnAccelerationChangedListener listener) {
        this.onAccelerationChangedListener = listener;
    }

    @Override
    protected void onRegister() {
        gravity = new float[3];
        linear_acceleration = new float[3];
    }

    @Override
    protected void onUnregister() {
        //no-op
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        final float alpha = 0.8f;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = Math.abs(event.values[0] - gravity[0]);
        linear_acceleration[1] = Math.abs(event.values[1] - gravity[1]);
        linear_acceleration[2] = Math.abs(event.values[2] - gravity[2]);

        float magnitude = Matrix.length(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]);

        if (onAccelerationChangedListener != null) {
            onAccelerationChangedListener.onAccelerationChanged(
                    linear_acceleration[0], linear_acceleration[1], linear_acceleration[2], magnitude
            );
        }

        if (magnitude > ACCEL_HIGH_BOUND) {
            //In mid-flick
            isFlicking = true;
        } else if ( isFlicking && lastHighestAccel - magnitude > FLICK_TO_NEUTRAL_DIFF) {
            //Ending a flick
            isFlicking = false;
            if (onFlickListener != null) onFlickListener.onFlick();
        } else if (magnitude < ACCEL_LOW_BOUND) {
            isFlicking = false;
        }

        lastHighestAccel = magnitude;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //no-op
    }

    public interface OnAccelerationChangedListener {
        /**
         * Called when a new acceleration value has been calculated from the sensor data in onSensorChanged.
         * @param x Acceleration on the x-axis
         * @param y Acceleration on the y-axis
         * @param z Acceleration on the z-axis
         * @param magnitude The magnitude of the acceleration vector
         */
        void onAccelerationChanged(float x, float y, float z, float magnitude);
    }

    public interface OnFlickListener {
        /**
         * Called when a "flick" gesture has been recognized. A flick can occur in any direction.
         */
        void onFlick();
    }
}