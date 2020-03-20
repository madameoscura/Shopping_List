package com.example.shoppinglist.logic;

import android.content.Context;
import android.util.Log;

import com.example.shoppinglist.models.ShoppingList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    private static final String TAG = JsonHelper.class.getSimpleName();
    private static final String FILENAME_SHOPPINGLISTLIST = "ShoppingListlList.json";

    private static String createJsonStringFromShoppingListList(List<ShoppingList> shoppingListList) {
        JSONObject shoppingListData = new JSONObject();
        JSONArray shoppingListArray = new JSONArray();
        try {
            for (ShoppingList shoppingListObject : shoppingListList) {
                JSONObject shoppingList = new JSONObject();

                shoppingList.put("id", shoppingListObject.getId());
                shoppingList.put("shoppingListContent", shoppingListObject.getShoppingListContent());
                shoppingList.put("isDone", shoppingListObject.isDone());
                shoppingList.put("createDate", shoppingListObject.getCreateDate());
                shoppingList.put("doneDate", shoppingListObject.getDoneDate());
                shoppingList.put("noOfItemsInList", shoppingListObject.getNoOfItemsInList());


                shoppingListArray.put(shoppingList);
            }
            shoppingListData.put("shoppingLists", shoppingListArray);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException" + e.getMessage());
        }

        Log.i(TAG, "Aus ShoppingListList generierter JSON-String" + shoppingListData.toString());
        return shoppingListData.toString();
    }

    static void saveShoppingListListInFile(Context context, List<ShoppingList> shoppingListList) {
        String jsonString = createJsonStringFromShoppingListList(shoppingListList);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(FILENAME_SHOPPINGLISTLIST, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException; " + e.getMessage());
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.e(TAG,"IOException: " + e.getMessage());
                }
            }
        }
    }

    private static List<ShoppingList> createShoppingListListFromJSONString (String jsonString) {
        List<ShoppingList> receiveShoppingListList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            //Anfordern des JSON-Array-Knotens mit den ShoppingList-Objekten
            JSONArray shoppingLists = jsonObject.getJSONArray("shoppingLists");

            //Durchlaufen des Arrays und Auslesen der Daten jedes Objektes
            for (int i = 0; i < shoppingLists.length(); i++) {
                JSONObject shoppingList = shoppingLists.getJSONObject(i);

                int id = shoppingList.getInt("id");
                String shoppingListContent = shoppingList.getString("shoppingListContent");
                boolean isDone = shoppingList.getBoolean("isDone");
                String createDate = shoppingList.getString("createDate");
                String doneDate = shoppingList.getString("doneDate");
                int noOfItemsInList = shoppingList.getInt("noOfItemsInList");

                receiveShoppingListList.add(new ShoppingList(id, shoppingListContent, isDone, createDate, doneDate, noOfItemsInList));
            }
        } catch (JSONException e) {
            Log.e (TAG, "JSONException: " + e.getMessage());
        }
        return receiveShoppingListList;
    }

    static List<ShoppingList> restoreShoppingListListFromFile(Context context) {
        String jsonString = "";
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = context.openFileInput(FILENAME_SHOPPINGLISTLIST);
            InputStream stream = new BufferedInputStream(fileInputStream);
            jsonString = convertStreamToString(stream);
            Log.i(TAG, "JSON-String aus Datei gelesen" + jsonString);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException " + e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException: " + e.getMessage());
                }
            }
        }
        return createShoppingListListFromJSONString(jsonString);
    }

    private static String convertStreamToString(InputStream stream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;

        try {
            //solang wir etwas einlesen, mache nach jeder Zeile Zeilenumbruch
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }
        }
        return stringBuilder.toString();
    }
}
