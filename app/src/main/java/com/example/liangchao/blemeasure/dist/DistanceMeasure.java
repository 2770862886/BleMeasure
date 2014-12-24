package com.example.liangchao.blemeasure.dist;

/**
 * Created by liangchao on 12/22/14.
 */
public class DistanceMeasure {

    private long mTimeStart;
    private long mTimeEnd;

    public DistanceMeasure getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        static final DistanceMeasure INSTANCE = new DistanceMeasure();
    }


}
