package com.apps.alf.ssd;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Printer;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import static java.lang.System.arraycopy;


/**
 * Created by Alf on 13/12/2015.
 */
class SSDDatabase extends SQLiteOpenHelper

{
    // Instance variables .....//

    public String speeddialArray[][] = new String[10][3];
    public String speedtextArray[][] = new String[10][3];
    private static final String COL_Number = "Number";
    private static final String sql = "select * from speeddials";
    private static final String Speeddial_table = "Speeddials";
    // private static final String COL_ID = "ID";
    private static final String COL_Speeddial = "Speeddial";
    private static final String COL_Name = "Name";
    private static final String MYTAG = "AWB";
    // these are all string constants, the table column names//


    public String[] a;
    public String[] b;
    public String[] c;

    public SSDDatabase(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "MySSD.db", factory, 1);

        //The constructor for the SSDDatabase Object
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creates a table with some field names  speeddial is the primary key

        //CharSequence text = "Databased created";
        // Toast.makeText(context,text, Toast.LENGTH_SHORT).show;

        int c;

        String sql = String
                .format("Create table %s (%s INTEGER PRIMARY KEY, %s VARCHAR(50) NOT NULL, %s VARCHAR(20) NOT NULL)",
                        Speeddial_table, COL_Speeddial, COL_Name, COL_Number);
        db.execSQL(sql);
        Log.d(MYTAG, "DB table created");
        // db = getWritableDatabase();  // create a new SQLiteDatabase object, call it db
        ContentValues values = new ContentValues(); // create an object full of values



        values.put(COL_Speeddial, 1);
        values.put(COL_Name, "Home");
        values.put(COL_Number, "01952840465");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 2);
        values.put(COL_Name, "Karen");
        values.put(COL_Number, "07850769076");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 3);
        values.put(COL_Name, "George");
        values.put(COL_Number, "07534344366");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 4);
        values.put(COL_Name, "Emma");
        values.put(COL_Number, "07498286323");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 5);
        values.put(COL_Name, "Mum and Dad");
        values.put(COL_Number, "01606853203");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 6);
        values.put(COL_Name, "Available");
        values.put(COL_Number, " ");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 7);
        values.put(COL_Name, "Available");
        values.put(COL_Number, " ");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 8);
        values.put(COL_Name, "Available");
        values.put(COL_Number, " ");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 9);
        values.put(COL_Name, "Available");
        values.put(COL_Number, "");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 10);
        values.put(COL_Name, "Available");
        values.put(COL_Number, " ");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Cursor readAllFromDatabase()

    {

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cs = db.rawQuery(sql, null);
            Log.d(MYTAG, "I opened a database ");
            return cs;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(MYTAG, "Can't open database ");
            return null;
        }



/*
      code here that reads data at row "rowPostion" and returns  speeddial number, name and phone number.
     The retuned should a 2 string arraylist (with speeddial      being the position indicator)

      Using Cursor class object
      Requires SQL query (select) string
      */


    }

    public void writeTableRowToDatabase(String table, Spinner speedDial, EditText contactName, EditText contactNumber) {

        /*

        Using Contentvalue class object
        and db.insert
        Requires SQL string(?)

      code here that writes data to row at "rowPosition" (name and phone number) at position speed dial number.
     */

        ContentValues cv = new ContentValues();

        SQLiteDatabase db = this.getWritableDatabase();

        //cv.put(COL_Speeddial, String.valueOf(speedDial.));
        cv.put(COL_Name, String.valueOf(contactName.getText()));
        cv.put(COL_Number, String.valueOf(contactNumber.getText()));
        db.insert(Speeddial_table, null, cv);

    }

    public void LoadContactsArray(Cursor databaseCursor) {

        Log.d(MYTAG, "LoadContactsArray: ");
        int arrayCount;
        arrayCount = 0;
        while (databaseCursor.moveToNext()) {

            Log.d(MYTAG, databaseCursor.getString(0));
            Log.d(MYTAG, databaseCursor.getString(1));
            Log.d(MYTAG, databaseCursor.getString(2));
            MainActivity.contactArray[arrayCount] = databaseCursor.getString(1);
            MainActivity.phoneNumberArray[arrayCount] = databaseCursor.getString(2);

            arrayCount++;

        }
    }

    public void initialiseDatabaseArrays(Resources Res) {

        int i, j;

        for (i = 0; i < 10; i++)

        {
            // Log.d(MYTAG, " Speed Text > " + Res.getStringArray(R.array.speed_texts)[i]);
            // Log.d(MYTAG, " Contact Name> " + Res.getStringArray(R.array.speed_text_contact_name)[i]);
            //  Log.d(MYTAG, " Phone Number > " + Res.getStringArray(R.array.speed_text_phone_number)[i]);


            speedtextArray[i][0] = Res.getStringArray(R.array.speed_texts)[i];
            speedtextArray[i][1] = Res.getStringArray(R.array.speed_text_contact_name)[i];
            speedtextArray[i][2] = Res.getStringArray(R.array.speed_text_phone_number)[i];
            speeddialArray[i][0] = Res.getStringArray(R.array.speed_dials)[i];
            //   speeddialArray[i][1] = Res.getStringArray(R.array.speed_dial_contact_name)[i];
            //   speeddialArray[i][2] = Res.getStringArray(R.array.speed_dial_phone_number)[i];

            //speeddialArray[i][0] = a[i];
            // Log.d(MYTAG, "Speed dial > " + myarray[i]);
            //Log.d(MYTAG, " Contact > " + b[i]);
            // Log.d(MYTAG, "Number > " + c[i]);
            //speeddialArray[i][1] = b[i];
            //speeddialArray[i][2] = c[i];

        }


        for (i = 0; i < 10; i++) ;
        {

            Log.d(MYTAG, speeddialArray[i][0]);
            Log.d(MYTAG, speeddialArray[i][1]);
            Log.d(MYTAG, speeddialArray[i][2]);

        }

        for (i = 0; i < 10; i++) ;
        {

            Log.d(MYTAG, speedtextArray[i][0]);
            Log.d(MYTAG, speedtextArray[i][1]);
            Log.d(MYTAG, speedtextArray[i][2]);

        }
    }

    public void Updatedatabase(Context context, final String contactName, final String contactNumber, final int rowPosition) {

        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues cv = new ContentValues();

        /*  array start at 0
        database table starts at COL_Speeddial
         */

        final AlertDialog.Builder builder = new AlertDialog.Builder((context));
        builder.setMessage(R.string.Dialog_message)
                .setTitle(R.string.Dialog_title)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //arrays and update database row

                        MainActivity.contactArray[rowPosition] = contactName;
                        MainActivity.phoneNumberArray[rowPosition] = contactNumber;
                        cv.put(COL_Name, contactName);
                        cv.put(COL_Number, contactNumber);


                        // my database row starts aty 1 not 0 !!!
                        // convert db row number to a string

                        Integer Row = rowPosition + 1;
                        String rowStr = Row.toString();

                        db.update(Speeddial_table, cv, COL_Speeddial + "=" + rowStr, null);
                             /*change the contents of the names and numbers arrays
                            for that row and also update row in database
                             */

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // do nowt
                        Log.d(MainActivity.DEBUGTAG, "Changes Discarded");

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}




