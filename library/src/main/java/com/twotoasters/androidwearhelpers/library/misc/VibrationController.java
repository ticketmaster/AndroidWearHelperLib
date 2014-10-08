package com.twotoasters.androidwearhelpers.library.misc;

import android.content.Context;
import android.os.Vibrator;

/**
 * A simple class that exposes a device's vibration functionality. Note that use of this class
 * requires the "android.permission.VIBRATE" permission.
 */
public class VibrationController {

    public static final long INTENSITY_LOW = 100;
    public static final long INTENSITY_MEDIUM = 200;
    public static final long INTENSITY_HIGH = 500;

    private Vibrator vibrationService;

    public VibrationController(Context context) {
        vibrationService = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * Indicates whether the device is capable of vibrating.
     * @return true if the Vibration service exists and the device has a vibrator
     */
    public boolean canVibrate() {
        return vibrationService != null && vibrationService.hasVibrator();
    }

    /**
     * Vibrates the device according to the specified intensity. Intensity is determined by the duration
     * of the vibration (in ms), and can be one of INTENSITY_LOW, INTENSITY_MEDIUM, INTENSITY_HIGH, or
     * a custom duration.
     *
     * @param intensity The vibration intensity (duration in ms)
     */
    public void vibrate(long intensity) {
        if (canVibrate()) {
            try {
                vibrationService.vibrate(intensity);
            } catch (SecurityException e) {
                // Vibration permission is likely missing
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the device from vibrating if it is in the middle of a vibration.
     */
    public void stopVibrating() {
        if (canVibrate()) vibrationService.cancel();
    }

}
