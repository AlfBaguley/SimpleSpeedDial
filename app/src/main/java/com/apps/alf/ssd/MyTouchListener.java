package com.apps.alf.ssd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Alf on 30/12/2015.
 */
public class MyTouchListener extends AppCompatActivity implements View.OnTouchListener {

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public MyTouchListener(Context context, Intent srIntent, int srIntentCode) {
        this.srIntent = srIntent;
        this.srIntentCode = srIntentCode;
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:

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
                        Log.d(MainActivity.DEBUGTAG, "Tapped MORE THAN 3 times");
                        quickTapTimer.cancel();
                        MainActivity.tapCount = 0;

                }

            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_HOVER_ENTER:
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                break;
            case MotionEvent.ACTION_OUTSIDE:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }


        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TapCounter Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.apps.alf.ssd/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TapCounter Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.apps.alf.ssd/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}