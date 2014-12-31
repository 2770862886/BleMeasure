package com.example.liangchao.blemeasure;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.liangchao.blemeasure.dist.DistanceMeasure;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
            implements
            DistanceMeasure.MeasureListener,
            View.OnClickListener {

        TextView mViewSpeed;
        TextView mViewDistance;
        Button mBtnStart;
        Button mBtnCalibrate;
        Button mBtnStop;
        Button mBtnClear;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstance) {
            super.onCreate(savedInstance);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mViewSpeed = (TextView) rootView.findViewById(R.id.speed);
            mViewDistance = (TextView) rootView.findViewById(R.id.distance);

            mBtnStart = (Button) rootView.findViewById(R.id.start);
            mBtnStart.setOnClickListener(this);

            mBtnCalibrate = (Button) rootView.findViewById(R.id.calibrate);
            mBtnCalibrate.setOnClickListener(this);

            mBtnStop = (Button) rootView.findViewById(R.id.stop);
            mBtnStop.setOnClickListener(this);

            mBtnClear = (Button) rootView.findViewById(R.id.clear);
            mBtnClear.setOnClickListener(this);

            DistanceMeasure.getInstance(getActivity()).registerListener(this);

            return rootView;
        }

        @Override
        public void onMeasureUpdate(float distance, float speed) {
            mViewSpeed.setText(speed + "");
            mViewDistance.setText(distance + "");
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.start) {
                DistanceMeasure.getInstance(getActivity()).startMeasure();
            } else if (v.getId() == R.id.calibrate) {
                DistanceMeasure.getInstance(getActivity()).calibrate();
            } else if (v.getId() == R.id.stop) {
                DistanceMeasure.getInstance(getActivity()).stopMeasure();
            } else if (v.getId() == R.id.clear) {
                DistanceMeasure.getInstance(getActivity()).reset();
                mViewDistance.setText("0.0");
                mViewSpeed.setText("0.0");
            }
        }
    }
}
