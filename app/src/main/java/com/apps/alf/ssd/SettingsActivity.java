package com.apps.alf.ssd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static int clickcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toast.makeText(getApplicationContext(), "Settings loading....ok", Toast.LENGTH_SHORT).show();
        clickcount = 0;

        final Spinner spnChooseSpeedDialNumber = (Spinner) findViewById(R.id.SpnChooseSpeedDialNumber);

        // Create a spinner object from the spinner on the activity screen and set its listeners
        spnChooseSpeedDialNumber.setOnItemSelectedListener(new MyClickListeners());
        //SSDDatabase db = new SSDDatabase(getApplicationContext(),null,null,1);
        //Cursor cursorResultSet = db.readAllFromDatabase();

        if (MainActivity.cursorResultSet.moveToFirst()) {
            EditText name = (EditText) findViewById(R.id.ContactName);
            name.setText(MainActivity.cursorResultSet.getString(1));
            EditText number = (EditText) findViewById(R.id.ContactNumber);
            number.setText(MainActivity.cursorResultSet.getString(2));
        } else
            Log.d(MainActivity.DEBUGTAG, "Database error");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
/*create database object and set spinner to first value and text boxes to corresponding record*/
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

}