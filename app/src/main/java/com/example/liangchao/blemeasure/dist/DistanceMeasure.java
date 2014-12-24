package com.example.liangchao.blemeasure.dist;

import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

/**
 * Created by liangchao on 12/22/14.
 */
public class DistanceMeasure {

    private XYZAccelerometer mAcceler;
    private SensorManager mSensorMgr;

    private Handler mHandler;

    private static final int TIMER_DONE  = 0;
    private static final int MEASURE_START = 1;
    private static final int MEASURE_END = 2;
    private static final int ERROR = 3;

    private long mTimeStart;
    private long mTimeEnd;




    public DistanceMeasure getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        static final DistanceMeasure INSTANCE = new DistanceMeasure();
    }

    private DistanceMeasure() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case
                }
            }
        }
    }

}
