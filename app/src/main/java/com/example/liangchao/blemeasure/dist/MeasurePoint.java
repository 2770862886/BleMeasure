package com.example.liangchao.blemeasure.dist;

/**
 * Created by liangchao on 12/24/14.
 */
public class MeasurePoint {
    private float x;
    private float y;
    private float z;
    private float speedBefore;
    private float speedAfter;
    private float distance;
    private float acceleration;
    private long interval;
    private Point averagePoint;

    public MeasurePoint(float x, float y, float z, float speedBefore, long interval, Point averagePoint) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.speedBefore = speedBefore;
        this.interval = interval;
        this.averagePoint = averagePoint;
        speedAfter = 0;
        calc();
    }

    private void calc() {
        //Acceleration as projection of current vector on average
        acceleration = this.x*averagePoint.getX() +
                this.y*averagePoint.getY() +
                this.z*averagePoint.getZ();
        acceleration = acceleration / ((float)Math.sqrt(averagePoint.getForce()));
        float t = ((float)interval / 1000f);
        speedAfter = speedBefore + acceleration * t;
        distance = speedBefore*t + acceleration*t*t/2;
    }

    public String getStoreString() {
        String s = "write here whatever you want";
        return s;
    }

    public float getSpeedAfter() {
        return speedAfter;
    }
}
