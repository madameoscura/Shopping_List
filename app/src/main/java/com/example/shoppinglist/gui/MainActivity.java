package com.example.shoppinglist.gui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.logic.MainActivityListener;

public class MainActivity extends AppCompatActivity {

    public EditText inputShoppingList;
    public ImageButton addShoppingList;
    public ListView shoppingListView;

    MainActivityListener mainActivityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.Layout verbinden
        setContentView(R.layout.activity_main);

        //2. GUI Elemente verbinden
      //  inputShoppingList = findViewById(R.id.inputShoppingList);
      //  addShoppingList = findViewById(R.id.addShoppingList);
        shoppingListView = findViewById(R.id.shoppingListView);

        //3. Logic verbinden
        mainActivityListener = new MainActivityListener(this);

        //4. Listener zuweisen

       // addShoppingList.setOnClickListener(mainActivityListener);

        shoppingListView.setOnItemClickListener(mainActivityListener);

        registerForContextMenu(shoppingListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        mainActivityListener.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return mainActivityListener.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mainActivityListener.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mainActivityListener.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        mainActivityListener.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainActivityListener.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainActivityListener.onStop();
    }

}
