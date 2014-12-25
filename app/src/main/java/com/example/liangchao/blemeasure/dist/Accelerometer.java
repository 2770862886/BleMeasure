package com.example.liangchao.blemeasure.dist;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by liangchao on 12/23/14.
 */
abstract class Accelerometer implements SensorEventListener {

    public abstract Point getPoint();
    protected float lastX, lastY, lastZ;
    public void onSensorChanged(SensorEvent event) {
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
