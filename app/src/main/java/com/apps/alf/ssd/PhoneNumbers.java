package com.apps.alf.ssd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static com.apps.alf.ssd.MainActivity.contactArray;
import static com.apps.alf.ssd.MainActivity.phoneNumberArray;

public class PhoneNumbers extends AppCompatActivity {

    static int currentSpeedDialArrayRow;
    static String currentContactName;
    static String currentContactNumber;

    // final SimpleSpeedDialAssistant mySSDA = new SimpleSpeedDialAssistant();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        SSDDatabase db = new SSDDatabase(this, null, null, 1);
        Log.d(MainActivity.DEBUGTAG, "Settings activity oncreate ");
        setContentView(R.layout.phonenumbers);
        Toast.makeText(getApplicationContext(), "Settings loading....ok", Toast.LENGTH_SHORT).show();
        final EditText name = (EditText) findViewById(R.id.ContactName);
        final EditText number = (EditText) findViewById(R.id.ContactNumber);
        final Spinner spnChooseSpeedDialNumber = (Spinner) findViewById(R.id.SpnChooseSpeedDialNumber);
        InitTextBoxes(name, number);
        currentSpeedDialArrayRow = 1;
        spnChooseSpeedDialNumber.setOnItemSelectedListener(new MyClickListeners(this, db));

        //SSDDatabase db = new SSDDatabase(getApplicationContext(),null,null,1);
        //Cursor cursorResultSet = db.readAllFromDatabase();

        // Create a spinner object from the spinner on the activity screen and set its listeners

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
/*create database object and set spinner to first value and text boxes to corresponding record*/

        getMenuInflater().inflate(R.menu.phonenumbers, menu);

        return true;

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:

                finish();
                return true;

            case R.id.action_back:

                finish();
                return true;
        }

        return id == R.id.action_phone_numbers || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*  array start at 0
        database table starts at COL_Speeddial
         */

        /* initialise the array stuff for checking for changes*/

        //currentSpeedDialArrayRow = 1;
        currentContactName = contactArray[0];
        currentContactNumber = phoneNumberArray[0];
    }

    protected void onStop() {
        super.onStop();
     /*   if ((currentContactName.equals(name.getText().toString())) && (phonenumbers.currentContactNumber.equals(number.getText().toString())))

        {
            // Neither contents of name or number have changed !!

            Log.d(MainActivity.DEBUGTAG, name.getText().toString() + " V " + phonenumbers.currentContactName);
            Log.d(MainActivity.DEBUGTAG, number.getText().toString() + "  V " + phonenumbers.currentContactNumber);

        } else {

            Log.d(MainActivity.DEBUGTAG, name.getText().toString() + " Vs " + phonenumbers.currentContactName);
            Log.d(MainActivity.DEBUGTAG, number.getText().toString() + "  Vs " + phonenumbers.currentContactNumber);
            Log.d(MainActivity.DEBUGTAG, number.getText().toString() + " Row " + phonenumbers.currentSpeedDialArrayRow);
            db.Updatedatabase(context, String.valueOf(name.getText()), String.valueOf(number.getText()), phonenumbers.currentSpeedDialArrayRow);
        }
*/
    }
    public void InitTextBoxes(EditText name, EditText number) {

        name.setText(contactArray[0]);
        number.setText(phoneNumberArray[0]);
        currentContactName = contactArray[0];
        currentContactNumber = phoneNumberArray[0];

    }
}