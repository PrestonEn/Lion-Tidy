package com.example.tyler.scalegesture;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private ScaleGestureDetector mySGD;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imgView =(ImageView)findViewById(R.id.earlImgView);
        mySGD = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        mySGD.onTouchEvent(ev); //Make sure the detector can actually examine things.
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        public boolean onScale(ScaleGestureDetector detector){
            double factor = detector.getScaleFactor();
            Log.e("TylersScale", "Info: " + factor);
            double w = imgView.getLayoutParams().width * factor;
            double h = imgView.getLayoutParams().height * factor;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)w,(int)h);
            imgView.setLayoutParams(lp);
            return true;
        }
    }
}
