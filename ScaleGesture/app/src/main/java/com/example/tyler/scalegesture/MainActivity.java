package com.example.tyler.scalegesture;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private ScaleGestureDetector mySGD;
    private ImageView imgView;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get device's screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;

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
            double w = (imgView.getLayoutParams().width * factor < 3 ? 3 : imgView.getLayoutParams().width * factor);
            double h = (imgView.getLayoutParams().height * factor < 6 ? 6 : (imgView.getLayoutParams().height * factor > screenHeight ?  screenHeight : imgView.getLayoutParams().height * factor));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)w,(int)h);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            imgView.setLayoutParams(lp);
            return true;
        }
    }
}
