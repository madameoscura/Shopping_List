package com.example.shoppinglist.logic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist.R;
import com.example.shoppinglist.gui.MainActivity;
import com.example.shoppinglist.gui.SecondActivity;
import com.example.shoppinglist.models.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivityListener implements AdapterView.OnItemClickListener {

    MainActivity mainActivity;
    ShoppingList shoppingList;
    ShoppingListAdapter shoppingListAdapter;
    DataSource dataSource;
    List<ShoppingList> shoppingListList;

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    public MainActivityListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.shoppingListList = new ArrayList<>();
        this.dataSource = new DataSource(mainActivity);
        this.shoppingList = new ShoppingList();
        // bindAdapterToListView();
        // showAllListEntries();

    }

/*    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addShoppingList:
                if (mainActivity.inputShoppingList.getText().toString().isEmpty()) {
                    Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.alert_addShoppingListContent), Toast.LENGTH_SHORT).show();
                } else {
                    shoppingList = new ShoppingList();
                    shoppingList.setShoppingListContent(mainActivity.inputShoppingList.getText().toString());
                    shoppingList.setDone(false);
                    shoppingList.setCreateDate(getTimeStamp());

                    shoppingListList.add(shoppingList);

                    shoppingListAdapter.setNotifyOnChange(true);

                    mainActivity.inputShoppingList.setText("");
                    hideSoftKeyboard();
                }
                break;
        }
    } */

    private String getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        String output = simpleDateFormat.format(calendar.getTime());
        return output;
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mainActivity.getCurrentFocus().getWindowToken(), 0);
    }

    private void bindAdapterToListView() {
        shoppingListAdapter = new ShoppingListAdapter(mainActivity, shoppingListList);
        mainActivity.shoppingListView.setAdapter(shoppingListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        shoppingList = (ShoppingList) parent.getItemAtPosition(position);
        Intent intent = new Intent(mainActivity, SecondActivity.class);
        String name = shoppingList.getShoppingListContent();
        intent.putExtra("Name", name);
        intent.putExtra("ShoppingListID", shoppingList.getId());
        //    mainActivity.startActivity(intent);

        mainActivity.startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);


     /*   currentShoppingList.setDone(!currentShoppingList.isDone());
        if (currentShoppingList.isDone()) {
            currentShoppingList.setDoneDate(getTimeStamp());

        } else {
            currentShoppingList.setDoneDate("");
        }

       // JsonHelper.saveShoppingListListInFile(mainActivity, shoppingListList);
        int isDone = currentShoppingList.isDone() ? 1 : 0;
        dataSource.updateShoppingList(currentShoppingList.getId(), currentShoppingList.getShoppingListContent(), isDone, currentShoppingList.getDoneDate());
        shoppingListAdapter.notifyDataSetChanged(); */
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                // Get data from Intent
                //    int noItems = (int) data.getExtras().get("noItems");
                int intNo = data.getIntExtra("noItems", 0);
                int shoppingListID = data.getIntExtra("ShoppingListID", 0);

                shoppingList.setNoOfItemsInList(intNo);
                dataSource.getAllItems(shoppingListID);
                bindAdapterToListView();
                shoppingListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        mainActivity.getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        //get the position where we touched
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        //get item on position
        ShoppingList selectedShoppingList = shoppingListList.get(itemPosition);

        switch (item.getItemId()) {
            //when clicked on "Delete Item"
            case R.id.deleteItem:
                //delete item
                shoppingListList.remove(selectedShoppingList);
                shoppingListAdapter.notifyDataSetChanged();
                //  JsonHelper.saveShoppingListListInFile(mainActivity, shoppingListList);
                //delete item from db
                dataSource.deleteShoppingList(selectedShoppingList);
                break;
            //when clicked on "Edit item"
            case R.id.editItem:
                editDialog(selectedShoppingList);
                break;
        }
        return true;
    }

    private void editDialog(final ShoppingList selectedShoppingList) {
        final EditText editShoppingList = new EditText(mainActivity);
        editShoppingList.setText(selectedShoppingList.getShoppingListContent());

        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setTitle("Edit Shopping List")
                .setMessage("Please update shopping list content")
                .setView(editShoppingList)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedShoppingList.setShoppingListContent(editShoppingList.getText().toString());
                        shoppingListAdapter.notifyDataSetChanged();
                        // JsonHelper.saveShoppingListListInFile(mainActivity, shoppingListList);
                        int isDone = ((selectedShoppingList.isDone()) ? 1 : 0);
                        dataSource.updateShoppingList(selectedShoppingList.getId(), selectedShoppingList.getShoppingListContent(), isDone, selectedShoppingList.getDoneDate());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alert.show();
    }

    private void securityDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setTitle("Attention")
                .setMessage("Do you really want to delete all items?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shoppingListList.clear();
                        shoppingListAdapter.notifyDataSetChanged();
                        // JsonHelper.saveShoppingListListInFile(mainActivity, shoppingListList);
                        dataSource.deleteAllShoppingLists();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mainActivity, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void addItemDialog() {
        final EditText addAShoppingList = new EditText(mainActivity);

        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setTitle("Add Item")
                .setMessage("Please insert new shopping list below")
                .setView(addAShoppingList)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (addAShoppingList.getText().toString().isEmpty()) {
                            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.alert_addShoppingListContent), Toast.LENGTH_SHORT).show();

                        } else {
                            shoppingList = new ShoppingList();
                            shoppingList.setShoppingListContent(addAShoppingList.getText().toString());
                            shoppingList.setDone(false);
                            shoppingList.setCreateDate(getTimeStamp());
                            shoppingList.setDoneDate("");
                            shoppingList.setNoOfItemsInList(0);


                            shoppingList = dataSource.createShoppingList(shoppingList.getShoppingListContent(), 0, shoppingList.getCreateDate(), shoppingList.getDoneDate(), shoppingList.getNoOfItemsInList());
                            shoppingListList.add(shoppingList);
                            shoppingListAdapter.notifyDataSetChanged();
                            //JsonHelper.saveShoppingListListInFile(mainActivity, shoppingListList);


                        }
                        //  hideSoftKeyboard();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mainActivity, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        mainActivity.getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_deleteAllItems:
                if (shoppingListList.isEmpty()) {
                    Toast.makeText(mainActivity, "No items found", Toast.LENGTH_SHORT).show();
                } else {
                    securityDialog();
                }
                break;
            case R.id.option_addShoppingList:
                addItemDialog();
        }
        return true;
    }

    public void onResume() {
        //  shoppingListList = JsonHelper.restoreShoppingListListFromFile(mainActivity);
        //  bindAdapterToListView();
        showAllListEntries();
        shoppingListAdapter.notifyDataSetChanged();
    }

    public void onStop() {
        //   JsonHelper.saveShoppingListListInFile(mainActivity, shoppingListList);
    }

    private void showAllListEntries() {
        // Log.d("MainActivityListener", "Die Datenquelle wird geöffnet.");
        shoppingListList = dataSource.getAllShoppingLists();
        bindAdapterToListView();
    }
}
