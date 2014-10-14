package com.twotoasters.androidwearhelpers.library.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class MagneticFieldTracker extends SensorTrackerBase {

    private OnMagneticFieldChangedListener listener;

    public MagneticFieldTracker(Context context) {
        super(context, Sensor.TYPE_MAGNETIC_FIELD);
    }

    /**
     * Sets a listener for receiving callbacks as the magnetometer detects changes in the magnetic field.
     * @param listener
     */
    public void setOnMagneticFieldChangedListener(OnMagneticFieldChangedListener listener) {
        this.listener = listener;
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
        if (listener != null) listener.onMagneticFieldChanged(event.values[0], event.values[1], event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //no-op
    }

    public interface OnMagneticFieldChangedListener {

        /**
         * Called when the magnetic field sensor reading changes. Units are all in micro-Tesla (µT).
         * @param x ambient magnetic field along the X-axis
         * @param y ambient magnetic field along the Y-axis
         * @param z ambient magnetic field along the Z-axis
         */
        void onMagneticFieldChanged(float x, float y, float z);
    }
}
