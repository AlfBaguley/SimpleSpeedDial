package com.apps.alf.ssd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Alf on 13/12/2015.
 */
class SSDDatabase extends SQLiteOpenHelper

{
    // Instance variables .....//

    private static final String Speeddial_table = "Speeddials";
    // private static final String COL_ID = "ID";
    private static final String COL_Speeddial = "Speeddial";
    private static final String COL_Name = "Name";
    private static final String COL_Number = "Number";
    private static final String sql = "select * from speeddials";
    private static final String MYTAG = "AWB";
// these are all string constants, the table column names//

    public SSDDatabase(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "MySSD.db", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creates a table with some field names  speeddial is the primary key

        //CharSequence text = "Databased created";
        // Toast.makeText(context,text, Toast.LENGTH_SHORT).show;

        String sql = String
                .format("Create table %s (%s INTEGER PRIMARY KEY, %s VARCHAR(50) NOT NULL, %s VARCHAR(20) NOT NULL)",
                        Speeddial_table, COL_Speeddial, COL_Name, COL_Number);
        db.execSQL(sql);
        Log.d(MYTAG, "DB table created");
        // db = getWritableDatabase();  // create a new SQLiteDatabase object, call it db
        ContentValues values = new ContentValues(); // create an object full of values

        values.put(COL_Speeddial, 1);
        values.put(COL_Name, "Home");  // x and y are instance variables of the object "point"
        values.put(COL_Number, "01952840465");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 2);
        values.put(COL_Name, "Karen");  // x and y are instance variables of the object "point"
        values.put(COL_Number, "07850769076");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 3);
        values.put(COL_Name, "George");  // x and y are instance variables of the object "point"
        values.put(COL_Number, "07534344366");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 4);
        values.put(COL_Name, "Emma");  // x and y are instance variables of the object "point"
        values.put(COL_Number, "07498286323");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 5);
        values.put(COL_Name, "Mum and Dad");  // x and y are instance variables of the object "point"
        values.put(COL_Number, "01606853203");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 6);
        values.put(COL_Name, "Available");  // x and y are instance variables of the object "point"
        values.put(COL_Number, " ");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 7);
        values.put(COL_Name, "Available");  // x and y are instance variables of the object "point"
        values.put(COL_Number, " ");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 8);
        values.put(COL_Name, "Available");  // x and y are instance variables of the object "point"
        values.put(COL_Number, " ");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 9);
        values.put(COL_Name, "Available");  // x and y are instance variables of the object "point"
        values.put(COL_Number, "");
        db.insert(Speeddial_table, null, values);  // pass the object full of values to the database points table

        values.put(COL_Speeddial, 10);
        values.put(COL_Name, "Available");  // x and y are instance variables of the object "point"
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

    public void writeTableRowToDatabase(ArrayList<String> rowData, int rowPosition) {

        /*

        Using Contentvalue class object
        and db.insert
        Requires SQL string

      code here that writes data to row at "rowPosition" (name and phone number) at the position speeddial number.
     */

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


    }

}

