package com.apps.alf.ssd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alf on 30/12/2015.
 */
public class TapCounter extends Activity implements View.OnTouchListener {

    CountDownTimer quickTapTimer = new CountDownTimer(1000, 1) {
        @Override
        public void onTick(long millisUntilFinished) {
// nothing required its just a timer
        }

        @Override
        public void onFinish() {
            MainActivity.tapCount = 0;
            MainActivity.tapped3times = false;
            Log.d(MainActivity.DEBUGTAG, "TIMER TIMED OUT....reset counter to zero and timer reset");
        }
    };
    private int tapsCounted;
    private Intent srIntent;
    private int srIntentCode;
    private Context context;

    public TapCounter(Context context, Intent srIntent, int srIntentCode) {
        this.srIntent = srIntent;
        this.srIntentCode = srIntentCode;
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            MainActivity.tapCount++;
            switch (MainActivity.tapCount) {

                case 1:
                    quickTapTimer.start();
                    Log.d(MainActivity.DEBUGTAG, "Timer started");
                    Log.d(MainActivity.DEBUGTAG, "Tapped once");
                    break;

                // enable a timer here that resets the tapcounter back to 0 after 1 second so that only 4 quick taps is registerd}
                case 2:
                    Log.d(MainActivity.DEBUGTAG, "Tapped twice");
                    break;

                case 3:
                    MainActivity.tapped3times = true;
                    quickTapTimer.cancel();
                    Log.d(MainActivity.DEBUGTAG, "Tapped 3 times !!!!!");
                    MainActivity.tapCount = 0;
                    srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                    try {
                        ((Activity) context).startActivityForResult(srIntent, srIntentCode);
                        // context object is cast to activity object and the result of the intent call is passed to the activity with that context
                    } catch (Exception ex) {

                        Log.d(MainActivity.DEBUGTAG, "Exception thrown starting intent...." + ex);
                    }

                    break;

                default:
                    MainActivity.tapped3times = false;
                    Log.d(MainActivity.DEBUGTAG, "Tapped MORE THAN 4 times");
                    quickTapTimer.cancel();
                    MainActivity.tapCount = 0;

            }
            return true;

        } else return false;// ignore other actions

    }
}