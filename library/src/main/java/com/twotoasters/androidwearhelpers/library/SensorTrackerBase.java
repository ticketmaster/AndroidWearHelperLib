package com.twotoasters.androidwearhelpers.library;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class SensorTrackerBase implements SensorEventListener {

    public static final int READING_RATE_TENTH_OF_SECOND = 100000;
    public static final int READING_RATE_HUNDREDTH_OF_SECOND = 10000;

    protected boolean isRegistered;
    protected SensorManager sensorManager;
    protected Sensor sensor;

    public SensorTrackerBase(Context context, int sensorType) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
    }

    public boolean register() throws InvalidSensorTypeException {
        return register(READING_RATE_TENTH_OF_SECOND);
    }

    public boolean register(int pollingRate) throws InvalidSensorTypeException {
        if (sensor == null) throw new InvalidSensorTypeException();
        if (!isRegistered) {
            sensorManager.registerListener(this, sensor, pollingRate);
            isRegistered = true;
            return true;
        }
        return false;
    }

    public boolean unregister() {
        if (isRegistered) {
            sensorManager.unregisterListener(this);
            isRegistered = false;
            return true;
        }
        return false;
    }

    @Override
    public abstract void onSensorChanged(SensorEvent event);

    @Override
    public abstract void onAccuracyChanged(Sensor sensor, int i);

    public static class InvalidSensorTypeException extends Exception { }

}
