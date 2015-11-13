package com.example.vex.basicgesture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;


public class BasicActivity extends Activity {

    private GestureDetectorCompat mDetector; //Gesture detector object.
    private ImageView imgView;
    private int status_bar_height;
    private Random rand;
    private int screenWidth;
    private int screenHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener()); //Instance of it.

        imgView =(ImageView)findViewById(R.id.earlImgView);
        status_bar_height = getStatusBarHeight();
        rand = new Random();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){ //Override old touch handler.
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener { //Gesture class.
        private static final String DEBUG_TAG = "Gestures"; //For writing to console.

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) { //Full click up and down.
            Log.d(DEBUG_TAG, "onSingle: " + event.toString());

            CharSequence text = "You need to double tap on Earl!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();

            return true;
        }

        //for each drawable in the on screen check if the projected click will enter any of the drawables bounding boxes

        @Override
        public boolean onDoubleTap(MotionEvent event) { //Full double tap. Two full clicks.
            int x, y;

            Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());

            if(event.getX() > imgView.getX() && event.getX() < imgView.getX() + imgView.getWidth()){
                if (event.getY() > imgView.getY() + status_bar_height && event.getY() < imgView.getY() + status_bar_height + imgView.getHeight()){

                    x = rand.nextInt((screenWidth - imgView.getWidth()) + 1);
                    y = rand.nextInt((screenHeight - imgView.getHeight() - status_bar_height) + 1);

                    imgView.setX(x);
                    imgView.setY(y);
                }

            }

            return true;
        }
    }
}