package com.example.shoppinglist.gui;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppinglist.R;
import com.example.shoppinglist.logic.MainActivityListener;
import com.example.shoppinglist.logic.SecondActivityListener;

public class SecondActivity extends AppCompatActivity {

    public ListView itemsListView;
    SecondActivityListener secondActivityListener;
   public TextView shoppingListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.Layout verbinden
        setContentView(R.layout.activity_second);

        //2. GUI Elemente verbinden
         itemsListView = findViewById(R.id.itemsListView);
         shoppingListName = findViewById(R.id.shoppingListName);

        //3. Logic verbinden
        secondActivityListener = new SecondActivityListener(this);

        //4. Listener zuweisen

        itemsListView.setOnItemClickListener(secondActivityListener);

        registerForContextMenu(itemsListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        secondActivityListener.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return secondActivityListener.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return secondActivityListener.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return secondActivityListener.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        secondActivityListener.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        secondActivityListener.onStop();
    }
}
