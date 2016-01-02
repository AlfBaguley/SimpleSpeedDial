package com.apps.alf.ssd;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import static com.apps.alf.ssd.MainActivity.DEBUGTAG;
import static com.apps.alf.ssd.MainActivity.cursorResultSet;
import static com.apps.alf.ssd.SettingsActivity.clickcount;


public class MyClickListeners extends ActionBarActivity implements AdapterView.OnItemSelectedListener {


    public void SpeakToMeClickListener(View view) {
        //Toast.makeText(MainActivity.this, "Speak to me button clicked",
        //Toast.LENGTH_SHORT).show();
        //Log.d(MainActivity.DEBUGTAG, "Speak to me button clicked");
    }

    public void ListenToMeClickListener(View view) {
        // Toast.makeText(MainActivity.this, "Listen to me button clicked", Toast.LENGTH_SHORT).show();
        //Log.d(MainActivity.DEBUGTAG, "Listen to me button clicked");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        clickcount = clickcount + 1;
        CharSequence chosenSpeedDialNumber = (CharSequence) parent.getItemAtPosition(position);
        Toast.makeText(parent.getContext(), "You have chosen speed dial " + chosenSpeedDialNumber, Toast.LENGTH_SHORT).show();

        //SSDDatabase db = new SSDDatabase(this,null,null,1);
        //Cursor cursorResultSet = db.readAllFromDatabase();
        cursorResultSet.moveToPosition(-1);
        while (MainActivity.cursorResultSet.moveToNext()) {
            Log.d(DEBUGTAG, "Accessing View..." + clickcount);
            View p = view.getRootView();
            EditText name = (EditText) p.findViewById(R.id.ContactName);
            name.setText(MainActivity.cursorResultSet.getString(1));
            EditText number = (EditText) p.findViewById(R.id.ContactNumber);
            number.setText(MainActivity.cursorResultSet.getString(2));
        }


    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}

