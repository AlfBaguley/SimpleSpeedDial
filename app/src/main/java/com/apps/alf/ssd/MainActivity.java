package com.apps.alf.ssd;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.Manifest.*;

public class MainActivity extends AppCompatActivity implements OnInitListener {

    // The debug instance variables
    public final static String DEBUGTAG = "AWB";
    // The Speech recognition variables
    static final int MY_SPEECH_RECOGNITION_CHECK_CODE = 456;
    // My call variables
    static final int MY_CALL_CHECK_CODE = 789;
    private static final int MY_TTS_CHECK_CODE = 123;
    private static final int REQUEST_PHONE_CALL = 1;

    // TTS instance variables+;
    public static Cursor cursorResultSet;
    public static String contactArray[] = new String[10];
    public static String phoneNumberArray[] = new String[10];
    public static int tapCount = 0;  //counter to monitor number of screen taps ... 3 will call the speech recogniser
    public static boolean tapped3times = false;
    // The SimpleSpeedDialler class variables
    final SimpleSpeedDialAssistant mySSDA = new SimpleSpeedDialAssistant();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    final Intent speechrecognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    ArrayList<String> speechRecognitionReturnStringsArray;
    private int arrayCount;

    /**
     *
     */
    private TextToSpeech myTTS;
    private Intent TTSintent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA); // Text to Speech intent object
    private MyTouchListener screentaps = new MyTouchListener(this, speechrecognitionIntent, MY_SPEECH_RECOGNITION_CHECK_CODE);

    // +++++++++++++++++++++++ ONCREATE

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(DEBUGTAG, "OnCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        AddTouchListener();

        // Database stuff

        SSDDatabase db = new SSDDatabase(getApplicationContext(), null, null, 1);
        cursorResultSet = db.readAllFromDatabase();
        db.LoadContactsArray(cursorResultSet);
    }
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // end of ONCREATE

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

    @SuppressWarnings({"deprecation", "MissingPermission"})
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		/*
         * DETERMINES WHAT TO DO WHEN GET DATA BACK FROM AN INTENT NEED SWITCH
		 * STATEMENTS HERE TO DEAL WITH INTENTS DATA FROM TTS AND SPEECH
		 * RECOGNITION
		 */

        switch (requestCode) {

            case MY_TTS_CHECK_CODE:    //i.e If this is a response to TTS intent

                Toast.makeText(this, "Text to speech task", Toast.LENGTH_SHORT).show();

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    myTTS = new TextToSpeech(this, this);  //context = this and onInit is in "this" class (see auto generated bit near the end of this code//

                } else {

                    // missing data, so install it...

                    Intent installIntent = new Intent();
                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }

                break;

            case MY_SPEECH_RECOGNITION_CHECK_CODE: //i.e If this is a response to Speech Recognition intent

                switch (resultCode) {

                    case RESULT_OK:

                        speechRecognitionReturnStringsArray = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


                        if (mySSDA.IsValidPhoneNumber(speechRecognitionReturnStringsArray)) {

                            myTTS.setSpeechRate((float) 2.0); // speak a bit quickly please
                            myTTS.speak(mySSDA.getTTSString(), TextToSpeech.QUEUE_ADD, null);
                            Log.d(DEBUGTAG, "Valid phone number");

                            do {
                                // don't dial until end of chat

                            } while (myTTS.isSpeaking());



                            if (ActivityCompat.checkSelfPermission(this, permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


                                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},
                                REQUEST_PHONE_CALL);


                            }
else {

                                Log.d(DEBUGTAG, "I have permission to make a call");

                                Intent callIntent = new Intent(Intent.ACTION_CALL);

                                try {


                                    callIntent.setData(Uri.parse(mySSDA.getPhoneNumberString()));
                                    startActivity(callIntent);


                                } catch (Exception e) {   // handle exception such as incompatible phone command
                                    Log.d(DEBUGTAG, "Call intent failed");
                                    Toast.makeText(this, "Oh dear call Intent failed", Toast.LENGTH_SHORT).show();
                                    myTTS.speak("Oh dear,call intent failed", TextToSpeech.QUEUE_ADD, null);
                                    break; //

                                }
                            }


                        } else {
                            Toast.makeText(this, "Oh dear, No such number", Toast.LENGTH_SHORT).show();
                            myTTS.setSpeechRate((float) 2.0); // speak a bit quickly please
                            myTTS.speak(mySSDA.getTTSString(), TextToSpeech.QUEUE_ADD, null);

                        }

                        break;

                    case RecognizerIntent.RESULT_NO_MATCH:
                        Toast.makeText(this, "Oh dear, No match found", Toast.LENGTH_SHORT).show();
                        break;


                    case RecognizerIntent.RESULT_SERVER_ERROR:
                        Toast.makeText(this, "Oh dear, Server Error", Toast.LENGTH_SHORT).show();
                        break;


                    case RecognizerIntent.RESULT_AUDIO_ERROR:
                        Toast.makeText(this, "Oh dear, Audio Error", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(this, "Oh dear, Recogniser result not ok", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

        // /* end of speech recognition switch statement */

                /*
                if not one
                of my
                codes
                then
                return to
                the
                parent
                class
                onactivityresults
                */


    }

		/* end of onactivityresults switch statement */

    // end of OnActivityResult

    // -----------------------------------------------------------------------------------------------------------------------------

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override

    // The result of the request for permissions (in this case I'm interested in the phone) is returned and handled by this over ridden method
  //This should only ever be called when the app runs on the phone for the first time after which the app permissions are updated and made persistent.

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PHONE_CALL : {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);

                    try {

                        callIntent.setData(Uri.parse(mySSDA.getPhoneNumberString()));
                        startActivity(callIntent);

                    } catch (Exception e) {   // handle exception such as incompatible phone command


                        Toast.makeText(this, "Oh dear call Intent failed", Toast.LENGTH_SHORT).show();
                        myTTS.speak("Oh dear,call Intent failed", TextToSpeech.QUEUE_ADD, null);

                        //need to u[date to this for API ?> 21
                        // String utteranceId=this.hashCode() + "";
                        // myTTS.speak("Oh dear,call Intent failed", TextToSpeech.QUEUE_ADD, null, utteranceId);

                        break; //

                    }

                }
            }
        }
    }

    @Override
    public void onInit(int status) {

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
        Log.d(DEBUGTAG, "OnResume called");


    }
    // Need also to re-init TTS and Speech recog perhaps


    // ----------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {

        // Sav instance variables before app closes.

        super.onPause();
        Log.d(DEBUGTAG, "OnPause called");


    }

    @Override
    public void onStart() {

        super.onStart();
        Log.d(DEBUGTAG, "OnStart called");
        speechrecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
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

            @SuppressWarnings({"deprecation", "MissingPermission"})
            @Override
            public void onClick(View v) {

                // Intent to check for Text To Speech engine present

                Toast.makeText(MainActivity.this, "Hey.Speak to me button clicked", Toast.LENGTH_SHORT).show();
                Log.d(DEBUGTAG, "Hey,Speak to me button clicked 111...");
                String text = mySSDA.getTTSgreetingString();
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

    }


    @Override
    public void onStop() {

        super.onStop();
        Log.d(DEBUGTAG, "OnStop called");

    }

    public void onDestroy() {

        super.onDestroy();
        Log.d(DEBUGTAG, "OnDestroy called");

    }

    public void onRestart() {

        super.onRestart();
        Log.d(DEBUGTAG, "OnRestart called");


    }

    // -----------------------------------------------------------------------------------------------------------------------------
    private void AddTouchListener() {

        // Creating an ontouchlistener using the imageview class
        // always start with the findviewbyId (R.id.??) then create the listener
        // with auto-complete to create the basic structure of the return method

        ImageView image = (ImageView) findViewById(R.id.touch_image);  //  Create the image object (ie a reference to a copy of the picture). Is the (ImageView) a cast statement?
        image.setOnTouchListener(screentaps); /* pass the tapcounter  onTouchListener as a
         parameter to the ontouch listener. This class will monitor for 3 taps after which the speech recog will be invoked*/
    }

}