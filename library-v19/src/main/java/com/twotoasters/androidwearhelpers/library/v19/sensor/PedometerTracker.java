package com.twotoasters.androidwearhelpers.library.v19.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.twotoasters.androidwearhelpers.library.sensor.SensorTrackerBase;

public class PedometerTracker extends SensorTrackerBase {

    public PedometerTracker(Context context) {
        super(context, new int[]{ Sensor.TYPE_STEP_COUNTER, Sensor.TYPE_STEP_DETECTOR });
    }

    @Override
    protected void onRegister() {

    }

    @Override
    protected void onUnregister() {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
