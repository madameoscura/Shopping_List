package com.example.shoppinglist.logic;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppinglist.R;
import com.example.shoppinglist.gui.SecondActivity;
import com.example.shoppinglist.models.Items;
import com.example.shoppinglist.models.ShoppingList;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SecondActivityListener implements AdapterView.OnItemClickListener {

    SecondActivity secondActivity;
    Items items;
    ItemsListAdapter itemsListAdapter;
    DataSource dataSource;
    List<Items> itemsList = new ArrayList<>();
    int listID;
    Bundle bundle;

    public SecondActivityListener(SecondActivity secondActivity) {
        this.secondActivity = secondActivity;
        this.dataSource = new DataSource(secondActivity);
        bindAdapterToListView();
        bundle = secondActivity.getIntent().getExtras();
        if (bundle.getString("Name") != null) {
            secondActivity.shoppingListName.setText(bundle.getString("Name"));
        }
            listID = (bundle.getInt("ShoppingListID"));
        }

    private String getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        String output = simpleDateFormat.format(calendar.getTime());
        return output;
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) secondActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(secondActivity.getCurrentFocus().getWindowToken(), 0);
    }

    private void bindAdapterToListView() {
        itemsListAdapter = new ItemsListAdapter(secondActivity, itemsList);
        secondActivity.itemsListView.setAdapter(itemsListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Items currentItem = (Items) parent.getItemAtPosition(position);


        currentItem.setDone(!currentItem.isDone());
        if (currentItem.isDone()) {
            currentItem.setDoneDate(getTimeStamp());

        } else {
            currentItem.setDoneDate("");
        }

        int isDone = currentItem.isDone() ? 1 : 0;
        dataSource.updateItem(currentItem.getId(), currentItem.getItemsListContent(), isDone, currentItem.getDoneDate());
        itemsListAdapter.notifyDataSetChanged();
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        secondActivity.getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        //get the position where we touched
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        //get item on position
        Items selectedItem = itemsList.get(itemPosition);

        switch (item.getItemId()) {
            //when clicked on "Delete Item"
            case R.id.deleteItem:
                //delete item
                itemsList.remove(selectedItem);
                itemsListAdapter.notifyDataSetChanged();
                //  JsonHelper.saveShoppingListListInFile(mainActivity, shoppingListList);
                //delete item from db
                dataSource.deleteItem(selectedItem);
                break;
            //when clicked on "Edit item"
            case R.id.editItem:
                editDialog(selectedItem);
                break;
        }
        return true;
    }

    private void editDialog(final Items selectedItem) {
        final EditText editItem = new EditText(secondActivity);
        editItem.setText(selectedItem.getItemsListContent());

        AlertDialog.Builder alert = new AlertDialog.Builder(secondActivity);
        alert.setTitle("Edit Item")
                .setMessage("Please update item content")
                .setView(editItem)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItem.setItemsListContent(editItem.getText().toString());
                        itemsListAdapter.notifyDataSetChanged();

                        int isDone = ((selectedItem.isDone()) ? 1 : 0);
                        dataSource.updateItem(selectedItem.getId(), selectedItem.getItemsListContent(), isDone, selectedItem.getDoneDate());
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
        AlertDialog.Builder alert = new AlertDialog.Builder(secondActivity);
        alert.setTitle("Attention")
                .setMessage("Do you really want to delete all items?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemsList.clear();
                        itemsListAdapter.notifyDataSetChanged();

                        dataSource.deleteAllItems();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(secondActivity, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void addItemDialog() {
        final EditText addAnItem = new EditText(secondActivity);

        AlertDialog.Builder alert = new AlertDialog.Builder(secondActivity);
        alert.setTitle("Add Item")
                .setMessage("Please insert new item below")
                .setView(addAnItem)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (addAnItem.getText().toString().isEmpty()) {
                            Toast.makeText(secondActivity, secondActivity.getResources().getString(R.string.alert_addShoppingListContent), Toast.LENGTH_SHORT).show();

                        } else {
                            items = new Items();
                            items.setItemsListContent(addAnItem.getText().toString());
                            items.setDone(false);
                            items.setCreateDate(getTimeStamp());
                            items.setDoneDate("");
                            items.setShoppingListID(bundle.getInt("ShoppingListID"));


                            items = dataSource.createItemsList(items.getItemsListContent(), 0, items.getCreateDate(), items.getDoneDate(), items.getShoppingListID());
                            itemsList.add(items);
                            itemsListAdapter.notifyDataSetChanged();
                            //JsonHelper.saveShoppingListListInFile(mainActivity, shoppingListList);


                        }
                        //  hideSoftKeyboard();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(secondActivity, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        secondActivity.getMenuInflater().inflate(R.menu.option_itemsmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_deleteAllItems:
                if (itemsList.isEmpty()) {
                    Toast.makeText(secondActivity, "No items found", Toast.LENGTH_SHORT).show();
                } else {
                    securityDialog();
                }
                break;
            case R.id.option_addAnItem:
                addItemDialog();
                break;
            case R.id.option_backToShoppingLists:
                // Get the text from the EditText

                int number = itemsList.size();

                // Put the String to pass back into an Intent and close this activity
                Intent intent = new Intent();
                intent.putExtra("noItems", number);
                intent.putExtra("ShoppingListID", bundle.getInt("ShoppingListID"));
                secondActivity.setResult(Activity.RESULT_OK, intent);
                secondActivity.finish();
                break;
        }
        return true;
    }

    public void onResume() {
        //  shoppingListList = JsonHelper.restoreShoppingListListFromFile(mainActivity);
        //  bindAdapterToListView();
        showAllListEntries();
        itemsListAdapter.notifyDataSetChanged();
    }

    public void onStop() {
        //   JsonHelper.saveShoppingListListInFile(mainActivity, shoppingListList);
    }

    private void showAllListEntries() {
        // Log.d("MainActivityListener", "Die Datenquelle wird geöffnet.");

        itemsList = dataSource.getAllItems(listID);
        bindAdapterToListView();
    }
}
