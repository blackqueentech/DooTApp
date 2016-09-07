package com.example.dellaanjeh.dootapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dellaanjeh on 9/1/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DooT";
    private static final String DATABASE_TABLE_NAME = "Dooties";
    public static final String COLUMN1 = "DooT_ID";
    public static final String COLUMN2 = "DooT_Name";
    public static final String COLUMN3 = "Doo_Date";
    public static final String COLUMN4 = "Notes";
    public static final String COLUMN5 = "Status";
    public static final String COLUMN6 = "Priority";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This runs once after the installation and creates a database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATABASE_TABLE_NAME + " (" + COLUMN1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN2 + " TEXT, " + COLUMN3 + " TEXT, " + COLUMN4 + " TEXT, " + COLUMN5 + " TEXT, " + COLUMN6 + " TEXT)");

    }

    /**
     * This would run after the user updates the app. This is in case you want
     * to modify the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    /**
     * This method adds a record to the database. All we pass in is the todo
     * text
     */
    public long addDoot(String name, String dooDate, String notes, String status, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN2, name);
        cv.put(COLUMN3, dooDate);
        cv.put(COLUMN4, notes);
        cv.put(COLUMN5, status);
        cv.put(COLUMN6, priority);
        return db.insert(DATABASE_TABLE_NAME, null, cv);
    }

    public long editDoot(Integer id, String name, String dooDate, String notes, String status, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN2, name);
        cv.put(COLUMN3, dooDate);
        cv.put(COLUMN4, notes);
        cv.put(COLUMN5, status);
        cv.put(COLUMN6, priority);
        return db.update(DATABASE_TABLE_NAME, cv, COLUMN1 + "=?", new String[]{id.toString()});
    }

    /**
     * //This method returns all notes from the database
     */
    public ArrayList<Doot> getAllDoots() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Doot> dootList = new ArrayList<Doot>();

        Cursor cursor = db.rawQuery("SELECT * from " + DATABASE_TABLE_NAME,
                new String[] {});

        if (cursor.moveToFirst()) {
            do {
                Doot doot = new Doot();

                doot.id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN1));
                doot.name = cursor.getString(cursor.getColumnIndex(COLUMN2));
                doot.dooDate = cursor.getString(cursor.getColumnIndex(COLUMN3));
                doot.notes = cursor.getString(cursor.getColumnIndex(COLUMN4));
                doot.status = cursor.getString(cursor.getColumnIndex(COLUMN5));
                doot.priority = cursor.getString(cursor.getColumnIndex(COLUMN6));

                dootList.add(doot);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return dootList;
    }

    public Doot getDoot(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE_NAME, new String[]{COLUMN1, COLUMN2, COLUMN3, COLUMN4, COLUMN5, COLUMN6}, COLUMN1 + "=?", new String[]{"" + id},null,null,null,null);
        if(cursor.moveToFirst()){
            Doot doot = new Doot();
            doot.id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN1));
            doot.name = cursor.getString(cursor.getColumnIndex(COLUMN2));
            doot.dooDate = cursor.getString(cursor.getColumnIndex(COLUMN3));
            doot.notes = cursor.getString(cursor.getColumnIndex(COLUMN4));
            doot.status = cursor.getString(cursor.getColumnIndex(COLUMN5));
            doot.priority = cursor.getString(cursor.getColumnIndex(COLUMN6));
            return doot;
        }else{
            return null;
        }
    }
    /*
     * //This method deletes a record from the database.
     */
    public void deleteDoot(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String string = String.valueOf(id);
        int delete = db.delete(DATABASE_TABLE_NAME, COLUMN1 + "=?", new String[]{string});
        Log.i("Q","Deleted " + delete);
    }
}
