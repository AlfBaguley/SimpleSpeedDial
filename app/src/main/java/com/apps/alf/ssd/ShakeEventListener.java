package com.apps.alf.ssd;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


public class ShakeEventListener implements SensorEventListener {


    /**
     * Minimum movement force to consider.
     */
    private static final int MIN_FORCE = 10;

    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     */

    //private static final int MIN_DIRECTION_CHANGE = 3;

    private static final int MIN_DIRECTION_CHANGE = 5;

    /**
     * Maximum pause between movements.
     */
    private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;

    /**
     * Maximum allowed time for shake gesture.
     */
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;
    //private static final int MAX_TOTAL_DURATION_OF_SHAKE = 2200;

    /**
     * Time when the gesture started.
     */
    private long mFirstDirectionChangeTime = 0;

    /**
     * Time when the last movement started.
     */
    private long mLastDirectionChangeTime;

    /**
     * How many movements are considered so far.
     */
    private int mDirectionChangeCount = 0;

    /**
     * The last x position.
     */
    private float lastX = 0;

    /**
     * The last y position.
     */
    private float lastY = 0;

    /**
     * The last z position.
     */
    private float lastZ = 0;

    /**
     * OnShakeListener that is called when shake is detected.
     */
    private OnShakeListener mShakeListener;

    @Override
    public void onSensorChanged(SensorEvent se) {
        // get sensor data
        //float x = se.values[SensorManager.DATA_X];
        //float y = se.values[SensorManager.DATA_Y];
        //float z = se.values[SensorManager.DATA_Z];
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];

        // calculate movement
        float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);

        if (totalMovement > MIN_FORCE) {

            // get time
            long now = System.currentTimeMillis();

            // store first movement time
            if (mFirstDirectionChangeTime == 0) {
                mFirstDirectionChangeTime = now;
                mLastDirectionChangeTime = now;
            }

            // check if the last movement was not long ago
            long lastChangeWasAgo = now - mLastDirectionChangeTime;
            if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

                // store movement data
                mLastDirectionChangeTime = now;
                mDirectionChangeCount++;

                // store last sensor data
                lastX = x;
                lastY = y;
                lastZ = z;

                // check how many movements are so far
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

                    // check total duration
                    long totalDuration = now - mFirstDirectionChangeTime;
                    if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
                        mShakeListener.onShake();
                        resetShakeParameters();
                    }
                }

            } else {
                resetShakeParameters();
            }
        }
    }

    private void resetShakeParameters() {
        mFirstDirectionChangeTime = 0;
        mDirectionChangeCount = 0;
        mLastDirectionChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

	/*
     * Resets the shake parameters to their default values.
	 */

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void setOnShakeListener(OnShakeListener listener) {  // a setter
        mShakeListener = listener;
    }

    /**
     * Interface for shake gesture.
     */
    public interface OnShakeListener {

        /**
         * Called when shake gesture is detected.
         */
        void onShake();
    }

}