package com.twotoasters.androidwearhelpers.library.sensor;

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
    protected int sensorType;
    protected Sensor sensor;

    public SensorTrackerBase(Context context, int sensorType) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensorType = sensorType;
        this.sensor = sensorManager.getDefaultSensor(sensorType);
    }

    public boolean register() throws InvalidSensorTypeException {
        return register(READING_RATE_TENTH_OF_SECOND);
    }

    public boolean register(int pollingRate) throws InvalidSensorTypeException {
        if (sensor == null) throw new InvalidSensorTypeException(sensorType);
        if (!isRegistered) {
            sensorManager.registerListener(this, sensor, pollingRate);
            isRegistered = true;
            onRegister();
            return true;
        }
        return false;
    }

    public boolean unregister() {
        if (isRegistered) {
            sensorManager.unregisterListener(this);
            isRegistered = false;
            onUnregister();
            return true;
        }
        return false;
    }

    /**
     * Called immediately after a listener has been registered successfully for a Sensor. Perform any
     * post-registration logic here.
     */
    protected abstract void onRegister();

    /**
     * Called immediately after a listener has been unregistered for a Sensor. This is a good place to
     * do some cleanup or reinitialization.
     */
    protected abstract void onUnregister();

    @Override
    public abstract void onSensorChanged(SensorEvent event);

    @Override
    public abstract void onAccuracyChanged(Sensor sensor, int i);

    /**
     * Thrown when attempting to register a listener for a Sensor that does not exist or cannot
     * be found.
     */
    public static class InvalidSensorTypeException extends Exception {

        public InvalidSensorTypeException(int sensorType) {
            super("Sensor of type " + sensorType + " was not found on this device.");
        }

    }

}
