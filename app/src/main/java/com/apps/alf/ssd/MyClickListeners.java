package com.apps.alf.ssd;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import static com.apps.alf.ssd.MainActivity.DEBUGTAG;


public class MyClickListeners extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Context context;
    private SSDDatabase db;
    private EditText name;
    private EditText number;
    // private String currentContactName;
    //private String currentContactNumber;
    private int rowPosition;

    public MyClickListeners(Context context, SSDDatabase db)

    {
        this.context = context;
        this.db = db;
        // constructor to pass the context over to the listener
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        CharSequence chosenSpeedDialNumber = (CharSequence) parent.getItemAtPosition(position);
        Log.d(DEBUGTAG, "Item selected: " + chosenSpeedDialNumber);
        Toast.makeText(parent.getContext(), "You have chosen speed dial " + chosenSpeedDialNumber, Toast.LENGTH_SHORT).show();

        //  MainActivity.cursorResultSet.moveToPosition(position);
        Log.d(DEBUGTAG, "Accessing View...");
        View p = view.getRootView();
        EditText name = (EditText) p.findViewById(R.id.ContactName);
        EditText number = (EditText) p.findViewById(R.id.ContactNumber);

        if ((PhoneNumbers.currentContactName.equals(name.getText().toString())) && (PhoneNumbers.currentContactNumber.equals(number.getText().toString())))

        {
            // Neither contents of name or number have changed !!

            Log.d(MainActivity.DEBUGTAG, name.getText().toString() + " V " + PhoneNumbers.currentContactName);
            Log.d(MainActivity.DEBUGTAG, number.getText().toString() + "  V " + PhoneNumbers.currentContactNumber);

        } else {

            Log.d(MainActivity.DEBUGTAG, name.getText().toString() + " Vs " + PhoneNumbers.currentContactName);
            Log.d(MainActivity.DEBUGTAG, number.getText().toString() + "  Vs " + PhoneNumbers.currentContactNumber);
            Log.d(MainActivity.DEBUGTAG, number.getText().toString() + " Row " + PhoneNumbers.currentSpeedDialArrayRow);
            db.Updatedatabase(context, String.valueOf(name.getText()), String.valueOf(number.getText()), PhoneNumbers.currentSpeedDialArrayRow);
        }


// Contents of one of the name or number text boxes has changed so need to give the option to save the new values to the database

// Alert box generated ....with save and cancel buttons




       /*  do you want to save the changed value of contact and number

            if yes

                write to contacts etc array and write to database...


            else
            ..... don't change owt */

        name.setText(MainActivity.contactArray[position]);
        number.setText(MainActivity.phoneNumberArray[position]);
        PhoneNumbers.currentContactName = name.getText().toString();
        PhoneNumbers.currentContactNumber = number.getText().toString();
        PhoneNumbers.currentSpeedDialArrayRow = position;

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

}

