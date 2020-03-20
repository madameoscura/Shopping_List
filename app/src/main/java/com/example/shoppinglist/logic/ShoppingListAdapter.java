package com.example.shoppinglist.logic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.shoppinglist.R;
import com.example.shoppinglist.models.ShoppingList;

import java.util.List;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {

    private Context context;
    private List<ShoppingList> shoppingListList;
    private LayoutInflater layoutInflater;
    private Resources resources;

    public ShoppingListAdapter(Context context, List<ShoppingList> shoppingListList) {
        super(context, R.layout.shoppinglist_row, shoppingListList);

        this.context = context;
        this.shoppingListList = shoppingListList;
        this.layoutInflater = LayoutInflater.from(context);
        this.resources = context.getResources();
    }

    //vergleichbar mit getCell Methode
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Erzeugen der View-Hierarchie auf Grundlage des Layouts
        View rowView;
        if (convertView == null) {
            rowView = layoutInflater.inflate(R.layout.shoppinglist_row, parent, false);
        } else {
            rowView = convertView;
        }

        //Anfordern des zur Listenposition gehörenden Datenobjektes
        ShoppingList currentShoppingList = shoppingListList.get(position);

        //Finden der einzelnen View-Objekte
        LinearLayout cellLayout = (LinearLayout) rowView.findViewById(R.id.cellLayout);
        TextView shoppingListContent = (TextView) rowView.findViewById(R.id.shoppingListContent);
        TextView createDate = (TextView) rowView.findViewById(R.id.createDate);
        TextView doneDate = (TextView) rowView.findViewById(R.id.doneDate);
        TextView noOfItemsInList = (TextView) rowView.findViewById(R.id.noOfItemsInList);
        ImageView isCheckedImage = (ImageView) rowView.findViewById(R.id.imageView);

        shoppingListContent.setText(currentShoppingList.getShoppingListContent());
        createDate.setText(currentShoppingList.getCreateDate());
        doneDate.setText(currentShoppingList.getDoneDate());
        noOfItemsInList.setText(String.valueOf(currentShoppingList.getNoOfItemsInList()));

        if (currentShoppingList.isDone()) {
            isCheckedImage.setImageBitmap(getBitmap(android.R.drawable.checkbox_on_background));
            cellLayout.setBackgroundColor(Color.rgb(0,255,0));
        } else {
            isCheckedImage.setImageBitmap(getBitmap(android.R.drawable.checkbox_off_background));
            cellLayout.setBackgroundColor(Color.rgb(255,255,255));
        }

        //Rückgabe der befüllten View-Hierarchie an dir aufrufende AdapterView
        return rowView;
    }

    private Bitmap getBitmap(int resID) {
        return BitmapFactory.decodeResource(resources, resID);
    }
}
