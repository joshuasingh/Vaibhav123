package com.example.joshuamsingh.producto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joshua M Singh on 19-04-2018.
 */

public class DBHandler  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="uid.db";
    private static final String TABLE_PRODUCTS="login";
    public static final String COLUMN_ID="uid";
    public static final String COLUMN_STATUS="status";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       String query ="CREATE TABLE"+TABLE_PRODUCTS+"{"+COLUMN_ID
               +" INTEGER PRIMARY KEY "+COLUMN_STATUS+"TEXT"+"};";

     sqLiteDatabase.execSQL( query );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
  sqLiteDatabase.execSQL("DROP TABLE IF EXIST"+TABLE_PRODUCTS);
          onCreate(sqLiteDatabase);
    }


    public  void addstatus(uid Uid)
    {
        ContentValues values=new ContentValues();
        values.put(COLUMN_ID,Uid.getUid1());
        values.put(COLUMN_ID,Uid.getStatus());
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_PRODUCTS,null,values);
        db.close();
    }

    public  String getstatus(String uid){
        SQLiteDatabase db=getWritableDatabase();
       String query="Select"+COLUMN_STATUS+"FROM"+TABLE_PRODUCTS+"WHERE"
                +COLUMN_ID+"=\""+uid+" \";";
        Cursor c=db.rawQuery(query,null);
        String dbstring=c.getString(c.getColumnIndex("status"));

        db.close();
        return dbstring;

    }








}
