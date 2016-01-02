package com.apps.alf.ssd;

import android.app.Activity;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.ArrayList;

public class SimpleSpeedDialAssistant extends Activity implements OnInitListener {
    // the instance variables
    // TTS string constants

    private static final String SSDIloveAlf = "Hi Alf, you sexy bastard";
    private static final String[] contactArray = {"List", "Home", "Karen mobile", "George mobile", "Emma mobile", "Mum and Dad", "Alf mobile", "Alf mobile", "Alf mobile", "Alf mobile", "Alf mobile"};
    private static final String[] phoneNumberArray = {" ", "01952840465", "07850769076", "07534344366", "07498286323", "01606853203", "07798734186", "07798734186", "07798734186", "07798734186", "07798734186"};

    // ArrayList<String> speechRecognitionReturnStringsArray;

    public String getTTSstring() {

        // returns the string to be spoken by the assistant

        return SSDIloveAlf;

    }


    public String ProcessReturnStringsandReturnNumberToDial(ArrayList<String> speechRecognitionReturnStringsArray) {

		/* This checks the text you said during speech recognition and returns the phone number from the phone number array if its there.
         *
		 */
        String numberToDial = null;

        Log.d(MainActivity.DEBUGTAG, "There are " + speechRecognitionReturnStringsArray.size() + " suggestions");

        for (int i = 1; i < speechRecognitionReturnStringsArray.size(); i++) {
            Log.d(MainActivity.DEBUGTAG, "You said " + speechRecognitionReturnStringsArray.get(i));
        }

        for (Integer i = 1; i < contactArray.length; i++) {

            if (speechRecognitionReturnStringsArray.contains("simple speed dial " + i.toString())) {
                numberToDial = "tel:" + phoneNumberArray[i];
                Log.d(MainActivity.DEBUGTAG, "Dialing " + contactArray[i]);
                return numberToDial;
            } else {
                numberToDial = null;
                Log.d(MainActivity.DEBUGTAG, "No such number");
            }
        }

        return numberToDial;
    }


    public boolean IsValidPhoneNumber(ArrayList<String> myVoiceInput) {


        boolean IsValid = false;
        Integer count = 1;

        do {

            if (myVoiceInput.contains("simple speed dial " + count.toString()))
            // choose the number from the array

            {
                IsValid = true;
                Log.d(MainActivity.DEBUGTAG, "Got one");
            } else {
                IsValid = false;
            }
            count++;

        } while (!IsValid && count < contactArray.length);

        return IsValid;
    }

    public String GetPhoneNumberString(ArrayList<String> myVoiceInput) {
	
	/* This returns the phone number as a text string to be spoke back by the SSD assistant using TTS*/

        boolean isValid = false;
        String myTTSstring = " ";
        Integer count = 1;

        do {

            if (myVoiceInput.contains("simple speed dial " + count.toString()))
							/*check to see if the simple speed dial command was said and 
								if so use the counter to find the array location for the phone number to dial and then return it as the TTS*/

            {
                myTTSstring = "speed dial" + count.toString() + " " + contactArray[count];
                isValid = true;
            } else if (myVoiceInput.contains("simple speed dial list")) {
		/* if you said "simple speed dial list" then concatenate the numbers and return it as TTS*/

                for (Integer i = 1; i < contactArray.length; i++) {

                    myTTSstring = myTTSstring + "speed dial " + i.toString() + " " + contactArray[i] + ",";

                }
                Log.d(MainActivity.DEBUGTAG, myTTSstring);
                isValid = true;
            } else


            {
                myTTSstring = "No such number choose again";
            }

            count++;

        }

        while (!isValid && count < contactArray.length);

        Log.d(MainActivity.DEBUGTAG, "You said " + myVoiceInput.get(0));

        return myTTSstring;

    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub

    }
}
