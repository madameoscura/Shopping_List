package com.example.shoppinglist.logic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final  String TAG = DBHelper.class.getSimpleName();
    private static final String DBName = "ShoppingList_List.db3";
    private static final int DBVersion = 1;

    public static final String SHOPPINGLIST_TABLE = "ShoppingList_List";
    public static final String ITEMS_TABLE = "Items_List";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SHOPPINGLISTCONTENT = "title";
    public static final String COLUMN_ISDONE = "isDone";
    public static final String COLUMN_CREATE_DATE = "createDate";
    public static final String COLUMN_DONE_DATE = "doneDate";
    public static final String COLUMN_NO_OF_ITEMS_INLIST = "noOfItemsInList";
    public static final String COLUMN_SHOPPINGLISTID = "shoppingListID";

    public static final String COLUMN_ITEMSLISTCONTENT = "title";

    public static final String SQL_CREATE_SHOPPINGLIST =
            "CREATE TABLE " + SHOPPINGLIST_TABLE+
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SHOPPINGLISTCONTENT + " TEXT NOT NULL, " +
                    COLUMN_ISDONE + " INT NOT NULL, " +
                    COLUMN_CREATE_DATE + " TEXT NOT NULL, " +
                    COLUMN_DONE_DATE + " TEXT, " +
                    COLUMN_NO_OF_ITEMS_INLIST + " INT );";

    public static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + ITEMS_TABLE+
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ITEMSLISTCONTENT + " TEXT NOT NULL, " +
                    COLUMN_ISDONE + " INT NOT NULL, " +
                    COLUMN_CREATE_DATE + " TEXT NOT NULL, " +
                    COLUMN_DONE_DATE + " TEXT, " +
                    COLUMN_SHOPPINGLISTID + " INT );";

    public DBHelper (Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_SHOPPINGLIST + " angelegt");
            db.execSQL(SQL_CREATE_SHOPPINGLIST);
            db.execSQL(SQL_CREATE_ITEMS);

        } catch (Exception e) {
            Log.e(TAG, "Fehler beim Anlegen der Tabelle: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
