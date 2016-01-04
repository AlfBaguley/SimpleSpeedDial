package com.apps.alf.ssd;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import static com.apps.alf.ssd.MainActivity.DEBUGTAG;
import static com.apps.alf.ssd.SettingsActivity.clickcount;

public class MyClickListeners extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        clickcount = clickcount + 1;
        CharSequence chosenSpeedDialNumber = (CharSequence) parent.getItemAtPosition(position);
        Log.d(DEBUGTAG, "Item selected: " + chosenSpeedDialNumber);
        Toast.makeText(parent.getContext(), "You have chosen speed dial " + chosenSpeedDialNumber, Toast.LENGTH_SHORT).show();

        //SSDDatabase db = new SSDDatabase(this,null,null,1);
        //Cursor cursorResultSet = db.readAllFromDatabase();
        //cursorResultSet.moveToPosition(-1);
        // while (MainActivity.cursorResultSet.moveToNext())

        MainActivity.cursorResultSet.moveToPosition(position);
        Log.d(DEBUGTAG, "Accessing View..." + clickcount);
        View p = view.getRootView();
        EditText name = (EditText) p.findViewById(R.id.ContactName);
        name.setText(MainActivity.cursorResultSet.getString(1));
        EditText number = (EditText) p.findViewById(R.id.ContactNumber);
        number.setText(MainActivity.cursorResultSet.getString(2));

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}
