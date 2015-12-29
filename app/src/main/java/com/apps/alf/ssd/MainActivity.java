package com.apps.alf.ssd;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements OnInitListener {

    // The debug instance variables
    public final static String DEBUGTAG = "AWB";
    // The Speech recognition variables
    static final int MY_SPEECH_RECOGNITION_CHECK_CODE = 456;
    // My call variables
    static final int MY_CALL_CHECK_CODE = 789;
    private static final int MY_TTS_CHECK_CODE = 123;

    // TTS instance variables+;
    public static Cursor cursorResultSet;                                        // created
    // The SimpleSpeedDialler class variables
    final SimpleSpeedDialAssistant mySSDA = new SimpleSpeedDialAssistant();
    Boolean alreadyDoingOnShakeEvent = false;
    ArrayList<String> speechRecognitionReturnStringsArray;
    // The Shake instance variables
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private TextToSpeech myTTS;
    private Intent TTSintent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA); // Text to Speech intent object
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // +++++++++++++++++++++++ ONCREATE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

		/*--------------------------------------------------------------------	

			Speech recognition intents set up */

        final Intent speechrecognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        SSDDatabase db = new SSDDatabase(getApplicationContext(), null, null, 1);
        cursorResultSet = db.readAllFromDatabase();

        while (cursorResultSet.moveToNext()) {
            Log.d(DEBUGTAG, cursorResultSet.getString(0));
            Log.d(DEBUGTAG, cursorResultSet.getString(1));
            Log.d(DEBUGTAG, cursorResultSet.getString(2));

        }

		/*
         * IT MAY BE BETTER TO USE THE SpeechRecognizer CLASS INSTEAD TO GET
		 * ROUND THE "didnt recognise that, try again" message!before a timeout?
		 */

		/*
		 * Starts an activity that will prompt the user for speech and send it
		 * through a speech recognizer.
		 */

        speechrecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		/*
		 * Required constants that tell the SR service what languages to use
		 * 
		 * 
		 * 
		 * ----------------------------------------------------------------------
		 * ....next the listeners
		 * 
		 * First the shake listener (which uses on board sensors)
		 */

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		/*
		 * System level services in this case sensor services
		 */

        mSensorListener = new ShakeEventListener();
		/*
		 * shake event listener object instance created
		 */

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

			/*
			 * Create the beeper i.e. Create and instance of ToneGenerator. Send
			 * the tone to the "alarm" stream (classic beeps go there) with 50%
			 * volume
			 */

                                               ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);

                                               // Finally Create the actual on-shake service routine for shake
                                               // events

                                               public void onShake() {

                                                   // can do this if not already servicing a shake event listener

                                                   if (!alreadyDoingOnShakeEvent) {
                                                       toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                                                       Log.d(DEBUGTAG, "YOU SHOOK ME");

                                                       if (isConnected()) { // i.e. Is connected to the Internet

                                                           startActivityForResult(speechrecognitionIntent, MY_SPEECH_RECOGNITION_CHECK_CODE);
                                                           alreadyDoingOnShakeEvent = true; // prevent response to
                                                           // further shake
                                                           // events
						/*
						 * Gets the SR activity going and gives the SR a code to
						 * return with the data which will identify it from any
						 * other data returned from other intents also lets the
						 * "intentee" know that you want a result (data) back.
						 */

                                                           // Toast.makeText(getApplicationContext(),
                                                           // "Intent for Speech recognition sent",
                                                           // Toast.LENGTH_LONG).show();

                                                       } else {
                                                           Toast.makeText(getApplicationContext(), "Activity not possible because i am not connected to Internet", Toast.LENGTH_LONG).show();

                                                       }
                                                   } else {

                                                   }

                                               }
                                           }

        );

        // ----------------------------------------------------------------------------------------------------------------------------

		/*
		 * TTS intent action configured
		 */

        //TTSintent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);//

        try {
            startActivityForResult(TTSintent, MainActivity.MY_TTS_CHECK_CODE);

        } catch (Exception ex) {

            Log.d(MainActivity.DEBUGTAG, "Exception thrown...." + ex);
        }


        Button speakButton = (Button) findViewById(R.id.btnSpeak);

		/*
		 * The speak button
		 */
        speakButton.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                // Intent to check for Text To Speech engine present

                Toast.makeText(MainActivity.this, "Hey.Speak to me button clicked", Toast.LENGTH_SHORT).show();
                Log.d(DEBUGTAG, "Hey,Speak to me button clicked");

                String text = mySSDA.getTTSstring();
				/*
				 * and use the myTTSstring / method which returns
				 * 
				 * the string to speak first initialise the intent
				 */

                if (text != null && text.length() > 0) {

                    Toast.makeText(MainActivity.this, "Saying: " + text, Toast.LENGTH_LONG).show();

                    myTTS.speak(text, TextToSpeech.QUEUE_ADD, null);

                }
            }
        });

        // ---------------------------------------------------------------------------------------------------------------------------

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // end of ONCREATE

    // -----------------------------------------------------------------------------------------------------------------

    public boolean isConnected()
	/*
	 * Make sure we have an internet connection because Speech recog needs
	 * internet access
	 */

    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.isAvailable() && net.isConnected();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("deprecation")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		/*
		 * DETERMINES WHAT TO DO WHEN GET DATA BACK FROM AN INTENT NEED SWITCH
		 * STATEMENTS HERE TO DEAL WITH INTENTS DATA FROM TTS AND SPEECH
		 * RECOGNITION
		 */

        switch (requestCode) {

            case MY_TTS_CHECK_CODE:

                Toast.makeText(this, "Text to speech task", Toast.LENGTH_SHORT).show();

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                    // success, create the TTS instance

                    myTTS = new TextToSpeech(this, this);  //context = this and onInit is in "this" class (see auto generated bit near the end of this code//

                } else {

                    // missing data, install it

                    Intent installIntent = new Intent();

                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);

                    startActivity(installIntent);
                }

                break;

            case MY_SPEECH_RECOGNITION_CHECK_CODE:
                // Toast.makeText(this, "Speech recognition task",
                // Toast.LENGTH_SHORT).show();

                switch (resultCode) {

                    case RESULT_OK:

                    {

                        // RESULT_OK is a standard (any) activity result code
                        speechRecognitionReturnStringsArray = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        myTTS.setSpeechRate((float) 2.0); // speak a bit quickly please
                        myTTS.speak(mySSDA.GetPhoneNumberString(speechRecognitionReturnStringsArray), TextToSpeech.QUEUE_ADD, null);

                        do {
                            // don't dial until end of chat
                        } while (myTTS.isSpeaking());


                        Log.d(DEBUGTAG, speechRecognitionReturnStringsArray.toString());
                        alreadyDoingOnShakeEvent = false; // allow shake events again

                        if (mySSDA.IsValidPhoneNumber(speechRecognitionReturnStringsArray)) {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            // Create Intent instance for making phone call

                            try {

						/*
						 * Give the intent object a acceptable phone command
						 * such as "tel:01889503300"
						 */

                                callIntent.setData(Uri.parse(mySSDA.ProcessReturnStringsandReturnNumberToDial(speechRecognitionReturnStringsArray)));
                            } catch (Exception e) {

						/*
						 * handle exception such as incompatible phone command
						 * such as Null....
						 */

                                Toast.makeText(this, "Oh dear call Intent failed", Toast.LENGTH_SHORT).show();
                                myTTS.speak("Oh dear, call intent failed", TextToSpeech.QUEUE_ADD, null);
                                break; // i.e. get out of this routine this if statement
                                // without making a phone call

                            }

                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(callIntent);

                        } else {
                            Toast.makeText(this, "Oh dear, No such number", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        break;
                    }

                    case RecognizerIntent.RESULT_NO_MATCH: // this is a result code sent
                        // specifically
                        // by speech recogniser //
                    {
                        Toast.makeText(this, "Oh dear, No match found", Toast.LENGTH_SHORT).show();
                        alreadyDoingOnShakeEvent = false;
                        break;
                    }

                    case RecognizerIntent.RESULT_SERVER_ERROR: {
                        Toast.makeText(this, "Oh dear, Server Error", Toast.LENGTH_SHORT).show();
                        alreadyDoingOnShakeEvent = false;
                        break;
                    }

                    case RecognizerIntent.RESULT_AUDIO_ERROR: {
                        Toast.makeText(this, "Oh dear, Audio Error", Toast.LENGTH_SHORT).show();
                        alreadyDoingOnShakeEvent = false;
                        break;
                    }

                    default: {
                        Toast.makeText(this, "Oh dear, Recogniser result not ok", Toast.LENGTH_SHORT).show();
                        alreadyDoingOnShakeEvent = false;
                        break;
                    }

                } // /* end of speech recognition switch statement */

            default:
                super.onActivityResult(requestCode, resultCode, data); // if not one
                // of my
                // codes
                // then
                // return to
                // the
                // parent
                // class
                // onactivityresults
                break;                                                        // //
        }

		/* end of onactivityresults switch statement */

    }// end of OnActivityResult

    // -----------------------------------------------------------------------------------------------------------------------------

	/*
	 * public class ServiceReceiver extends BroadcastReceiver {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) {
	 * MyPhoneStateListener phoneListener=new MyPhoneStateListener();
	 * TelephonyManager telephony = (TelephonyManager)
	 * context.getSystemService(Context.TELEPHONY_SERVICE);
	 * telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE); } }
	 * 
	 * 
	 * For the full class including the imports, please download the files
	 * below. In here we have another class called MyPhoneStateListener, which
	 * would be shown at the bottom. What this class would do is execute the
	 * phoneListener when the telephony.listen has received a LISTEN_CALL_STATE.
	 * 
	 * 
	 * public class MyPhoneStateListener extends PhoneStateListener { public
	 * void onCallStateChanged(int state,String incomingNumber){ switch(state){
	 * case TelephonyManager.CALL_STATE_IDLE: Log.d("DEBUG", "IDLE"); break;
	 * case TelephonyManager.CALL_STATE_OFFHOOK: Log.d("DEBUG", "OFFHOOK");
	 * break; case TelephonyManager.CALL_STATE_RINGING: Log.d("DEBUG",
	 * "RINGING"); break; } } }
	 */

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub for text to speech stuff. Called to
        // flag completion of TTS engine initialisation
        // ( method to implement from TTS listener interface
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(MainActivity.this,

                    "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
        } else if (status == TextToSpeech.ERROR) {

            Toast.makeText(MainActivity.this,

                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();

        }

    }

    // ----------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    // ----------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            // open new activity
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;

        }
        return super.onOptionsItemSelected(item);

    }


    // -----------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        // Register the listener to activate the sensor for this activity

        // Need also to re-init TTS and Speech recog perhaps

    }

    // ----------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener); // if you're not
		/*

		going to use the
		sensors then
		"switch them off"
		(sort of)
		need to also unregister TTS and Speech recog perhaps

		*/

        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
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
                "Main Page", // TODO: Define a title for the content shown.
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

    // -----------------------------------------------------------------------------------------------------------------------------
}