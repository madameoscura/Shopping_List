package com.example.shoppinglist.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shoppinglist.models.Items;
import com.example.shoppinglist.models.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private static final String TAG = DataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    //Items items;

    private String[] shoppingListColumns = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_SHOPPINGLISTCONTENT,
            DBHelper.COLUMN_ISDONE,
            DBHelper.COLUMN_CREATE_DATE,
            DBHelper.COLUMN_DONE_DATE,
            DBHelper.COLUMN_NO_OF_ITEMS_INLIST,
    };

    private String[] itemListColumns = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_ITEMSLISTCONTENT,
            DBHelper.COLUMN_ISDONE,
            DBHelper.COLUMN_CREATE_DATE,
            DBHelper.COLUMN_DONE_DATE,
            DBHelper.COLUMN_SHOPPINGLISTID,
    };

    public DataSource(Context context) {
        Log.d(TAG, "Unsere DataSource erzeugt jetzt den DBHelper");
        dbHelper = new DBHelper(context);
    }

    public void openWritable() {
        Log.d(TAG, "Eine Referenz (schreibend) auf die Datenbank wird jetzt abgefragt");
        database = dbHelper.getWritableDatabase();
        Log.d(TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void openReadable() {
        Log.d(TAG, "Eine Referenz (lesend) auf die Datenbank wird jetzt abgefragt");
        database = dbHelper.getReadableDatabase();
        Log.d(TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(TAG, "Datenbank-Referenz mit Hilfe des DBHelpers geschlossen");
    }

    public ShoppingList createShoppingList(String shoppingListContent, int isDone, String createDate, String doneDate, int noOfItemsInList) {
        //open db
        openWritable();
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_SHOPPINGLISTCONTENT, shoppingListContent);
        values.put(DBHelper.COLUMN_ISDONE, isDone);
        values.put(DBHelper.COLUMN_CREATE_DATE, createDate);
        values.put(DBHelper.COLUMN_DONE_DATE, doneDate);
        values.put(DBHelper.COLUMN_NO_OF_ITEMS_INLIST, noOfItemsInList);

        long insertId = database.insert(DBHelper.SHOPPINGLIST_TABLE, null, values);

        Cursor cursor = database.query(DBHelper.SHOPPINGLIST_TABLE, shoppingListColumns,
                DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        ShoppingList shoppingList = cursorToShoppingList(cursor);
        cursor.close();
        //close db
        close();
        return shoppingList;
    }

    public Items createItemsList(String itemsListContent, int isDone, String createDate, String doneDate, int shoppingListID) {
        //open db
        openWritable();
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_ITEMSLISTCONTENT, itemsListContent);
        values.put(DBHelper.COLUMN_ISDONE, isDone);
        values.put(DBHelper.COLUMN_CREATE_DATE, createDate);
        values.put(DBHelper.COLUMN_DONE_DATE, doneDate);
        values.put(DBHelper.COLUMN_SHOPPINGLISTID, shoppingListID);

        long insertId = database.insert(DBHelper.ITEMS_TABLE, null, values);

        Cursor cursor = database.query(DBHelper.ITEMS_TABLE, itemListColumns,
                DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Items items = cursorToItem(cursor);
        cursor.close();
        //close db
        close();
        return items;
    }

    //get a specific Task in List
    private ShoppingList cursorToShoppingList(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
        int idShoppingListContent = cursor.getColumnIndex(DBHelper.COLUMN_SHOPPINGLISTCONTENT);
        int idIsDone = cursor.getColumnIndex(DBHelper.COLUMN_ISDONE);
        int idCreateDate = cursor.getColumnIndex(DBHelper.COLUMN_CREATE_DATE);
        int idDoneDate = cursor.getColumnIndex(DBHelper.COLUMN_DONE_DATE);
        int idNoOfItemsInList = cursor.getColumnIndex(DBHelper.COLUMN_NO_OF_ITEMS_INLIST);

        int id = cursor.getInt(idIndex);
        String shoppingListContent = cursor.getString(idShoppingListContent);
        boolean isDone = (cursor.getInt(idIsDone) == 1);
        String createDate = cursor.getString(idCreateDate);
        String doneDate = cursor.getString(idDoneDate);
        int noOfItemsInList = cursor.getInt(idNoOfItemsInList);

        ShoppingList shoppingList = new ShoppingList(id, shoppingListContent, isDone, createDate, doneDate, noOfItemsInList);
        return shoppingList;
    }

    private Items cursorToItem(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
        int idItemsListContent = cursor.getColumnIndex(DBHelper.COLUMN_ITEMSLISTCONTENT);
        int idIsDone = cursor.getColumnIndex(DBHelper.COLUMN_ISDONE);
        int idCreateDate = cursor.getColumnIndex(DBHelper.COLUMN_CREATE_DATE);
        int idDoneDate = cursor.getColumnIndex(DBHelper.COLUMN_DONE_DATE);
        int idShoppingListID = cursor.getColumnIndex(DBHelper.COLUMN_SHOPPINGLISTID);

        int id = cursor.getInt(idIndex);
        String itemsListContent = cursor.getString(idItemsListContent);
        boolean isDone = (cursor.getInt(idIsDone) == 1);
        String createDate = cursor.getString(idCreateDate);
        String doneDate = cursor.getString(idDoneDate);
        int shoppingListID = cursor.getInt(idShoppingListID);

        Items items = new Items(id, itemsListContent, isDone, createDate, doneDate, shoppingListID);
        return items;
    }

    public List<ShoppingList> getAllShoppingLists() {
        List<ShoppingList> shoppingListList = new ArrayList<>();
        //open db
        openReadable();
        Cursor cursor = database.query(DBHelper.SHOPPINGLIST_TABLE, shoppingListColumns,
                null, null, null, null, null);
        cursor.moveToFirst();

        ShoppingList shoppingList;

        while (!cursor.isAfterLast()) {
            shoppingList = cursorToShoppingList(cursor);
            shoppingListList.add(shoppingList);
            Log.d(TAG, "ID; " + shoppingList.getId() + ", Inhalt: " + shoppingList.toString());
            cursor.moveToNext();
        }
        cursor.close();
        //close db
        close();
        return shoppingListList;
    }

    public List<Items> getAllItems(int listID) {
        List<Items> itemsList = new ArrayList<>();
        //open db
        openReadable();

        Cursor cursor = database.query(DBHelper.ITEMS_TABLE, itemListColumns,
                DBHelper.COLUMN_SHOPPINGLISTID + " = " + listID, null, null, null, null);
        cursor.moveToFirst();

        Items items;

        while (!cursor.isAfterLast()) {

            items = cursorToItem(cursor);
            itemsList.add(items);
            Log.d(TAG, "ID; " + items.getId() + ", Inhalt: " + items.toString());

            cursor.moveToNext();
        }
        cursor.close();
        //close db
        close();
        return itemsList;
    }

    public void deleteShoppingList(ShoppingList shoppingList) {
        long id = shoppingList.getId();
        openWritable();
        database.delete(DBHelper.SHOPPINGLIST_TABLE, DBHelper.COLUMN_ID + " =? " + id, null);
        close();
        Log.d(TAG, "Eintrag gelöscht! ID: " + id + "Inhalt: " + shoppingList.toString());
    }

    public void deleteItem(Items items) {
        long id = items.getId();
        openWritable();
        database.delete(DBHelper.ITEMS_TABLE, DBHelper.COLUMN_ID + " = " + id, null);
        close();
        Log.d(TAG, "Eintrag gelöscht! ID: " + id + "Inhalt: " + items.toString());
    }

    public ShoppingList updateShoppingList(long id, String newShoppingListContent, int newIsDone, String newDoneDate) {
        openWritable();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SHOPPINGLISTCONTENT, newShoppingListContent);
        values.put(DBHelper.COLUMN_ISDONE, newIsDone);
        values.put(DBHelper.COLUMN_DONE_DATE, newDoneDate);

        database.update(DBHelper.SHOPPINGLIST_TABLE, values, DBHelper.COLUMN_ID + " = " + id, null);

        Cursor cursor = database.query(DBHelper.SHOPPINGLIST_TABLE, shoppingListColumns,
                DBHelper.COLUMN_ID + "=" + id, null,
                null, null, null);
        cursor.moveToFirst();
        ShoppingList shoppingList = cursorToShoppingList(cursor);
        cursor.close();
        close();
        return shoppingList;
    }


    public Items updateItem(long id, String newItemsListContent, int newIsDone, String newDoneDate) {
        openWritable();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ITEMSLISTCONTENT, newItemsListContent);
        values.put(DBHelper.COLUMN_ISDONE, newIsDone);
        values.put(DBHelper.COLUMN_DONE_DATE, newDoneDate);

        database.update(DBHelper.ITEMS_TABLE, values, DBHelper.COLUMN_ID + " = " + id, null);

        Cursor cursor = database.query(DBHelper.ITEMS_TABLE, itemListColumns,
                DBHelper.COLUMN_ID + "=" + id, null,
                null, null, null);
        cursor.moveToFirst();
        Items items = cursorToItem(cursor);
        cursor.close();
        close();
        return items;
    }

    public void deleteAllShoppingLists() {
        openWritable();
        database.delete(DBHelper.SHOPPINGLIST_TABLE, null, null);
        close();
    }

    public void deleteAllItems() {
        openWritable();
        database.delete(DBHelper.ITEMS_TABLE, null, null);
        close();
    }
}
