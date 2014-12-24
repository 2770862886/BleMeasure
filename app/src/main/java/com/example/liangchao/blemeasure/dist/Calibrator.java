package com.example.liangchao.blemeasure.dist;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;


/**
 * Created by liangchao on 12/24/14.
 */
public class Calibrator {
    final static int UPDATE_INTERVAL = 400;
    final static int ITERATIONS = 5;
    Handler hRefresh;
    XYZAccelerometer acc;
    int eventNumber;
    private LinkedList calData;

    public Calibrator(Handler hRefresh, XYZAccelerometer acc, int eventNumber) {
        this.hRefresh = hRefresh;
        this.acc = acc;
        this.eventNumber = eventNumber;
    }

    public void calibrate() {
        final Timer calTimer = new Timer();
        calData = new LinkedList();
        acc.setdX(0);
        acc.setdY(0);
        acc.setdZ(0);

        calTimer.scheduleAtFixedRate(
                new TimerTask() {

                    public void run() {
                        addCalData(calData);
                        if (calData.size() > ITERATIONS) {
                            calTimer.cancel();
                            try {
                                calSensor(calData);
                            } catch (Exception ex) {
                                try {
                                    throw ex;
                                } catch (Exception ex1) {
                                    hRefresh.sendEmptyMessage(5);
                                }
                            }
                            hRefresh.sendEmptyMessage(eventNumber);
                        }
                    }
                },
                0,
                UPDATE_INTERVAL);
    }

    private void addCalData(LinkedList cD) {
        Point p = acc.getPoint();
        cD.add(p);
        acc.reset();
    }

    private void calSensor(LinkedList<Point> cD) throws Exception {
        if (cD.size() < ITERATIONS-1) {
            throw new Exception("not enough data to calibrate");
        }
        float x = 0;
        float y = 0;
        float z = 0;
        // Don't use first measure
        for (int i = 1; i < cD.size(); ++i) {
            x += cD.get(i).getX();
            y += cD.get(i).getY();
            z += cD.get(i).getZ();
        }

        x = x / (cD.size() - 1);
        y = y / (cD.size() - 1);
        z = z / (cD.size() - 1);

        acc.setdX(-x);
        acc.setdY(-y);
        acc.setdZ(-z);
    }
}
