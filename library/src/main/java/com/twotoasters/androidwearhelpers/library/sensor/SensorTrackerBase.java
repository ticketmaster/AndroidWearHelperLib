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
    protected int[] sensorTypes;
    protected Sensor[] sensors;

    public SensorTrackerBase(Context context, int sensorType) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensorTypes = new int[] {sensorType};
        this.sensors = new Sensor[] {sensorManager.getDefaultSensor(sensorType)};
    }

    public SensorTrackerBase(Context context, int[] sensorTypes) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensorTypes = sensorTypes;
        this.sensors = new Sensor[sensorTypes.length];
        for (int i = 0; i < sensorTypes.length; i++) {
            sensors[i] = sensorManager.getDefaultSensor(sensorTypes[i]);
        }
    }

    /**
     * Registers listeners and begins tracking data for the sensor types specified in the constructor.
     * This method uses the default polling rate.
     *
     * @return true if registration was successful, false if any of the Sensors are invalid or the tracker
     *              is already registered
     * @throws InvalidSensorTypeException if a specified Sensor types does not exist or is not present on the device
     * @throws IllegalArgumentException if no Sensor types are specified
     */
    public boolean register() throws InvalidSensorTypeException, IllegalArgumentException {
        return register(READING_RATE_TENTH_OF_SECOND);
    }

    /**
     * Registers listeners and begins tracking data for the sensor types specified in the constructor.
     *
     * @param pollingRate The rate (in ms) at which Sensor events will be reported, if possible
     * @return true if registration was successful, false if any of the Sensors are invalid or the tracker
     *              is already registered
     * @throws InvalidSensorTypeException if a specified Sensor types does not exist or is not present on the device
     * @throws IllegalArgumentException if no Sensor types are specified
     */
    public boolean register(int pollingRate) throws InvalidSensorTypeException, IllegalArgumentException {
        if (!isRegistered) {
            if (sensors == null) throw new IllegalArgumentException("No sensor types were provided for registration!");
            for (int i = 0; i < sensors.length; i++) {
                if (sensors[i] == null) throw new InvalidSensorTypeException(sensorTypes[i]);
                sensorManager.registerListener(this, sensors[i], pollingRate);
            }
            isRegistered = true;
            onRegister();
            return true;
        }
        return false;
    }

    /**
     * Unregisters listeners for all registered Sensor types.
     * @return true if all Sensors were unregistered successfully, false if not
     */
    public boolean unregister() {
        if (isRegistered) {
            for (Sensor sensor : sensors) {
                sensorManager.unregisterListener(this, sensor);
            }
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
