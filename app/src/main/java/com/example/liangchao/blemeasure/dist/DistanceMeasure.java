package com.example.liangchao.blemeasure.dist;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
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

    private Calibrator mCalibrator;

    private MeasureListener mListener;

    private static final int UPDATE_INTERVAL = 500;

    private static final int TIMER_DONE  = 0;
    private static final int MEASURE_START = 1;
    private static final int MEASURE_STOP = 2;
    private static final int ERROR = 3;

    private static final int CLIENT_UPDATE = 0;

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
        if (mData == null)
            return;

        mHandler.sendEmptyMessage(MEASURE_START);
        mCount = 0;
    }

    public void stopMeasure() {
        mHandler.sendEmptyMessage(MEASURE_STOP);
    }

    public void calibrate() {
        mData = new MeasureData(UPDATE_INTERVAL);
        mCalibrator.calibrate();
    }

    public void reset() {
        mData.reset();
    }

    private DistanceMeasure(Context context) {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TIMER_DONE:
                        onMeasureDone();

                        if (mListener != null) {
                            mListener.onMeasureUpdate(mData.getDistance(), mData.getLastSpeed());
                        }
                        break;

                    case MEASURE_START:
                        mTimer = new Timer();
                        mTimer.scheduleAtFixedRate(
                            new TimerTask() {
                                public void run() {
                                    mCount ++;
                                    mData.addPoint(mAcceler.getPoint());
                                    onMeasureDone();

                                    Message msg = mClientHandler.obtainMessage(CLIENT_UPDATE);
                                    msg.getData().putFloat("s", mData.getLastSpeed());
                                    msg.getData().putFloat("d", mData.getDistance());
                                    mClientHandler.sendMessage(msg);

                                    /*
                                    if (mCount > 20) {
                                        mTimer.cancel();
                                        mHandler.sendEmptyMessage(TIMER_DONE);
                                    }*/
                                }
                            }, 0, UPDATE_INTERVAL);
                        break;

                    case MEASURE_STOP:
                        mTimer.cancel();
                        mHandler.sendEmptyMessage(TIMER_DONE);
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
                mSensorMgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_GAME);

        mClientHandler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CLIENT_UPDATE:
                        Bundle data = msg.getData();
                        if (data == null || mListener == null)
                            break;

                        float speed = data.getFloat("s");
                        float distance = data.getFloat("d");

                        mListener.onMeasureUpdate(distance, speed);
                        break;

                    default:
                        break;
                }
            }
        };
        mCalibrator = new Calibrator(mHandler, mAcceler, MEASURE_START);
    }

    private void onMeasureDone() {
        mData.process();
    }
}
