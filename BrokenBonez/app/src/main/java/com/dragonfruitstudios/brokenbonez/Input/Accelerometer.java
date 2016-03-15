package com.dragonfruitstudios.brokenbonez.Input;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.util.Log;

public class Accelerometer {
    /**
     * Returns a value between -1 and 1 determining the strength of the bike tilting.
     */
    public static float calculateTiltStrength(SensorEvent event) {
        // TODO: The emulator gives incorrect accelerometer values for some reason.
        if (isEmulator()) {
            return 0f;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float accelY = event.values[1];
            return accelY / 9.81f;
        }
        return 0f;
    }

    private static boolean isEmulator() {
        // Taken from: http://stackoverflow.com/a/21505193/492186
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}