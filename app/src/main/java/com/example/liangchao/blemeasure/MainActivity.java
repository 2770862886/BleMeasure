package com.example.liangchao.blemeasure;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
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

        TextView mSpeedView;
        TextView mDistanceView;
        Button mStartBtn;

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
            mSpeedView = (TextView) rootView.findViewById(R.id.speed);
            mDistanceView = (TextView) rootView.findViewById(R.id.distance);

            mStartBtn = (Button) rootView.findViewById(R.id.start);
            mStartBtn.setOnClickListener(this);

            DistanceMeasure.getInstance(getActivity()).registerListener(this);

            return rootView;
        }

        @Override
        public void onMeasureUpdate(float distance, float speed) {
            mSpeedView.setText(speed + "");
        }

        @Override
        public void onClick(View v) {
            DistanceMeasure.getInstance(getActivity()).startMeasure();
        }
    }
}
