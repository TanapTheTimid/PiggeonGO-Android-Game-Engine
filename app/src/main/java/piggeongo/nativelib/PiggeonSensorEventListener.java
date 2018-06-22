package piggeongo.nativelib;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Poom on 5/30/2018.
 */

public class PiggeonSensorEventListener implements SensorEventListener {
    private SensorManager mSensorManager;

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    public PiggeonSensorEventListener(SensorManager mSensorManager){
        this.mSensorManager = mSensorManager;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        mSensorManager.getRotationMatrix(mRotationMatrix, null,
                mAccelerometerReading, mMagnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);

        // "mOrientationAngles" now has up-to-date information.
    }

    public float getPitch(){
        float pitch = (float)Math.atan(mRotationMatrix[7]
                /mRotationMatrix[8]);

        return pitch;
    }

    public float getYaw(){
        float yaw = (float)Math.atan(
                (-mRotationMatrix[6])
                        /
                            Math.sqrt(mRotationMatrix[7] * mRotationMatrix[7] +
                                        mRotationMatrix[8] * mRotationMatrix[8]));

        return yaw;
    }

    public float getRoll(){
        float roll = (float)Math.atan(mRotationMatrix[3]/mRotationMatrix[0]);
        return roll;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
