package com.example.liangchao.blemeasure.dist;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liangchao on 12/22/14.
 */
public class DistanceMeasure {

    private static DistanceMeasure sInstance;
    private XYZAccelerometer mAcceler;
    private SensorManager mSensorMgr;
    private MeasureData mData;
    private Handler mHandler;
    private Handler mClientHandler;

    private MeasureListener mListener;

    private static final int UPDATE_INTERVAL = 500;

    private static final int TIMER_DONE  = 0;
    private static final int MEASURE_START = 1;
    private static final int MEASURE_END = 2;
    private static final int ERROR = 3;

    private Timer mTimer;
    private int mCount;

    private long mTimeStart;
    private long mTimeEnd;

    public interface MeasureListener {
        void onMeasureUpdate(float distance, float speed);
    }

    public static DistanceMeasure getInstance(Context context) {
        synchronized (DistanceMeasure.class) {
            if (sInstance == null) {
                sInstance = new DistanceMeasure(context);
            }
        }
        return sInstance;
    }

    public void registerListener(MeasureListener listener) {
        mListener = listener;
    }

    public void startMeasure() {
        mData = new MeasureData(UPDATE_INTERVAL);
        mHandler.sendEmptyMessage(MEASURE_START);
        mCount = 0;
    }

    public void stopMeasure() {

    }

    private DistanceMeasure(Context context) {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TIMER_DONE:
                        onMeasureDone();

                        if (mListener != null)
                            mListener.onMeasureUpdate(0, mData.getLastSpeed());
                        break;

                    case MEASURE_START:
                        mTimer = new Timer();
                        mTimer.scheduleAtFixedRate(
                            new TimerTask() {
                                public void run() {
                                    mData.addPoint(mAcceler.getPoint());

                                    mClientHandler.post(new Runnable() {
                                        public void run() {
                                            if (mListener != null)
                                                mListener.onMeasureUpdate(mData.getDistance(), mData.getLastSpeed());
                                        }
                                    });
                                }
                            }, 0, UPDATE_INTERVAL);
                        break;

                    case ERROR:
                        break;

                    default:
                        break;
                }
            }
        };

        mSensorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAcceler = new XYZAccelerometer();
        mSensorMgr.registerListener(mAcceler,
                mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        mClientHandler = new Handler(context.getMainLooper());
    }

    private void onMeasureDone() {
        mData.process();
    }
}
