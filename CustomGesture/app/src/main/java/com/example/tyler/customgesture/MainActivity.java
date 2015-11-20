package com.example.tyler.customgesture;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGesturePerformedListener {
    private GestureLibrary gestureLib;
    private ImageView imgView;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
        View inflate = getLayoutInflater().inflate(R.layout.activity_main, null);
        gestureOverlayView.addView(inflate);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLib.load()) {
            finish();
        }
        setContentView(gestureOverlayView);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        imgView =(ImageView)findViewById(R.id.imageViewEarl);

        ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
        for (Prediction prediction : predictions) {

            if (prediction.score > 5.0) {
                imgView.setImageResource(R.drawable.happy_earl);
            }
            else{
                imgView.setImageResource(R.drawable.sad_earl);
            }

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    imgView.setImageResource(0);
                }
            }, 2000);
        }
    }
}
