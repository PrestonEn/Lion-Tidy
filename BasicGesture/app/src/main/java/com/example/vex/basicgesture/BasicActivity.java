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
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import android.media.SoundPool;
import android.media.AudioManager;

import java.util.Random;


public class BasicActivity extends Activity {

    private GestureDetectorCompat mDetector; // Gesture detector object
    private ImageView imgView;
    private int status_bar_height; // To account for image location shift
    private Random rand;
    private int screenWidth;  // To keep Earl on the screen
    private int screenHeight;
    int soundIDs[];
    SoundPool soundPool;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener()); // Instance of it

        imgView =(ImageView)findViewById(R.id.earlImgView);
        status_bar_height = getStatusBarHeight();
        rand = new Random();

        soundIDs = new int[3];
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundIDs[0] = soundPool.load(this, R.raw.dealie, 1);
        soundIDs[1] = soundPool.load(this, R.raw.face, 1);


        // Get device's screen size
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

            // If the user clicked with Earl's hitbox
            if(event.getRawX() > imgView.getX() && event.getRawX() < imgView.getX() + imgView.getWidth()){
                if (event.getRawY() > imgView.getY() + status_bar_height && event.getRawY() < imgView.getY() + status_bar_height + imgView.getHeight()){

                    x = rand.nextInt((screenWidth - imgView.getWidth()) + 1);
                    y = rand.nextInt((screenHeight - imgView.getHeight() - status_bar_height) + 1);

                    soundPool.play(soundIDs[0], 1, 1, 1, 0, 1);

                    imgView.setX(x); // Move Earl to new random location on the screen
                    imgView.setY(y);
                }

            }

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { // Swipe in a direction
            Animation animate;
            int hitbox_extend = 20; // Because "flinging" is finicky
            float x;

            Log.d(DEBUG_TAG, "onFling: " + e1.toString());
            Log.d(DEBUG_TAG, "onFling: " + e2.toString());

            // If the user clicked with Earl's hitbox
            if(e1.getRawX() > imgView.getX() - hitbox_extend && e1.getRawX() < imgView.getX() + imgView.getWidth() + hitbox_extend){
                if (e1.getRawY() > imgView.getY() - hitbox_extend + status_bar_height && e1.getRawY() < imgView.getY() + status_bar_height + imgView.getHeight() + hitbox_extend){

                    if(e2.getRawX() > e1.getRawX()){ // If the user swiped in the positive x direction
                        x = screenWidth - imgView.getX();
                    } else { // If the user swiped lin the negative x direction
                        x = -1*(imgView.getX() + imgView.getWidth());
                    }

                    soundPool.play(soundIDs[1], 1, 1, 1, 0, 1);


                    animate = new TranslateAnimation(0, x, 0, ((e2.getRawY() - e1.getRawY())/(e2.getRawX() - e1.getRawX()))*x);
                    animate.setDuration(1000);
                    imgView.startAnimation(animate);
                    imgView.setVisibility(View.INVISIBLE); // Hide original imageView
                    imgView.postDelayed(new Runnable() { // After the animation
                        @Override
                        public void run() {
                            int x, y;

                            x = rand.nextInt((screenWidth - imgView.getWidth()) + 1);
                            y = rand.nextInt((screenHeight - imgView.getHeight() - status_bar_height) + 1);

                            imgView.setX(x); // Move Earl to new random location on the screen after the animation is finished
                            imgView.setY(y);
                            imgView.setVisibility(View.VISIBLE); // Make Earl visible in new location
                        }
                    }, 1050); // Start slightly after animation has finished to avoid weird momentary earls appearing
                }
            }
            return true;
        }
    }
}