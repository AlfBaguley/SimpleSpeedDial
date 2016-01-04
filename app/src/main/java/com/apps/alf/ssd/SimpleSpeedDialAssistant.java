package com.apps.alf.ssd;

import android.app.Activity;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.ArrayList;

public class SimpleSpeedDialAssistant extends Activity implements OnInitListener {
    // the instance variables
    // TTS string constants


    private static final String SSDIloveAlf = "Hi Alf, you sexy bastard";
    private String phoneNumberString;
    private String ttsString;

    // ArrayList<String> speechRecognitionReturnStringsArray;cursorResultSet.getString(2));

    public String getTTSgreetingString() {
        // returns the string to be spoken by the assistant

        return SSDIloveAlf;
    }

    public String getPhoneNumberString() {
        return phoneNumberString;
    }

    public String getTTSString() {
        return ttsString;
    }

    public boolean IsValidPhoneNumber(ArrayList<String> myVoiceInput) {

        boolean isValid;
        Integer count;

        phoneNumberString = null;
        ttsString = "";
        isValid = false;
        count = 1;

        do {

            if (myVoiceInput.contains("simple speed dial " + count.toString())) {

                isValid = true;
                Log.d(MainActivity.DEBUGTAG, "Got one!!!!!");

                // List speech recog output to log cat
                for (int i = 1; i < myVoiceInput.size(); i++) {
                    Log.d(MainActivity.DEBUGTAG, "You said " + myVoiceInput.get(i - 1));
                }

                Log.d(MainActivity.DEBUGTAG, "Ready to dial " + MainActivity.contactArray[count - 1] + " on " + MainActivity.phoneNumberArray[count - 1]);
                phoneNumberString = "tel:" + MainActivity.phoneNumberArray[count - 1];
                ttsString = "Speed dial " + count.toString() + " " + MainActivity.contactArray[count - 1];
            } else
                count++;

        } while (!isValid && count <= MainActivity.contactArray.length);


        if (!isValid) {

            if (myVoiceInput.contains("simple speed dial list")) {
                             /* if you said "simple speed dial list" then concatenate the numbers and return it as TTS*/

                for (Integer j = 1; j <= MainActivity.contactArray.length; j++) {

                    ttsString = ttsString + "speed dial " + j.toString() + " " + MainActivity.contactArray[j - 1] + ", ";

                }

                Log.d(MainActivity.DEBUGTAG, ttsString);
            } else {
                Log.d(MainActivity.DEBUGTAG, "No such number");
                ttsString = "No such number";
            }
        }

        return isValid;

    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub

    }
}
