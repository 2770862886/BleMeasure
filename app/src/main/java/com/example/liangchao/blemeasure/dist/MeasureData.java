package com.example.liangchao.blemeasure.dist;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

/**
 * Created by liangchao on 12/24/14.
 */
public class MeasureData {
    // points from accelerometr
    private LinkedList<Point> mAccData;
    private LinkedList<MeasurePoint> mData;

    // timer interval of generating points
    private long interval;

    public MeasureData(long interval) {
        this.interval = interval;
        mAccData = new LinkedList<Point>();
        mData = new LinkedList<MeasurePoint>();
    }

    public void addPoint(Point p){
        mAccData.add(p);
    }

    public void process() {
        for (int i = 0; i < mAccData.size(); ++i) {
            Point p = mAccData.get(i);
            float speed = 0;

            if (i > 0) {
                speed = mData.get(i-1).getSpeedAfter();
            }
            mData.add(new MeasurePoint(p.getX(), p.getY(), p.getZ(), speed, interval, getAveragePoint()));
        }
    }

    public boolean saveExt(Context con, String fname) throws Throwable {
        try {
            File file = new File(con.getExternalFilesDir(null), fname);
            FileOutputStream os = new FileOutputStream(file);
            OutputStreamWriter out = new OutputStreamWriter(os);

            for (int i = 0; i < mData.size(); ++i) {
                MeasurePoint m = mData.get(i);
                out.write(m.getStoreString());
            }

            out.close();
        } catch (Throwable t) {
            throw (t);
        }
        return true;
    }

    private Point getAveragePoint() {
        float x = 0;
        float y = 0;
        float z = 0;

        for(int i = 0; i < mAccData.size(); ++i){
            Point p = mAccData.get(i);
            x += p.getX();
            y += p.getY();
            z += p.getZ();
        }

        return new Point(x, y, z, 1);
    }

    public float getLastSpeed(){
        return mData.getLast().getSpeedAfter();
    }

    public float getLastSpeedKm(){
        float ms = getLastSpeed();
        return ms*3.6f;
    }
}