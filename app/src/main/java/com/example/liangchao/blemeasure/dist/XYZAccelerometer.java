package com.example.liangchao.blemeasure.dist;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

/**
 * Created by liangchao on 12/24/14.
 */
class XYZAccelerometer extends Accelerometer {

    private static final int BUFFER_SIZE = 500;
    // calibration
    private  float dX = 0;
    private  float dY = 0;
    private  float dZ = 0;
    // buffer variables
    private float X;
    private float Y;
    private float Z;
    private int cnt = 0;

    // returns last SenorEvent parameters
    public Point getLastPoint(){
        return new Point(lastX, lastY, lastZ, 1);
    }

    // returrns parameters, using buffer: average acceleration
    // since last call of getPoint().
    public Point getPoint() {
        if (cnt == 0) {
            return new Point(lastX, lastY, lastZ, 1);
        }

        Point p =  new Point(X, Y, Z, cnt);

        reset();
        return p;
    }

    // resets buffer
    public void reset() {
        cnt = 0;
        X = 0;
        Y = 0;
        Z = 0;
    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0] + dX;
        float y = event.values[1] + dY;
        float z = event.values[2] + dZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        X += x;
        Y += y;
        Z += z;

        if (cnt < BUFFER_SIZE-1) {
            cnt ++;
        } else {
            reset();
        }
    }

    public int getCnt(){
        return cnt;
    }

    public  void setdX(float dX) {
        this.dX = dX;
    }

    public  void setdY(float dY) {
        this.dY = dY;
    }

    public  void setdZ(float dZ) {
        this.dZ = dZ;
    }
}
