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

import androidx.annotation.NonNull;

import com.example.shoppinglist.R;
import com.example.shoppinglist.models.Items;
import com.example.shoppinglist.models.ShoppingList;

import java.util.List;

public class ItemsListAdapter extends ArrayAdapter<Items> {

    private Context context;
    private List<Items> itemsList;
    private LayoutInflater layoutInflater;
    private Resources resources;

    public ItemsListAdapter(Context context, List<Items> itemsList) {
        super(context, R.layout.itemslist_row, itemsList);

        this.context = context;
        this.itemsList = itemsList;
        this.layoutInflater = LayoutInflater.from(context);
        this.resources = context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Erzeugen der View-Hierarchie auf Grundlage des Layouts
        View rowView;
        if (convertView == null) {
            rowView = layoutInflater.inflate(R.layout.itemslist_row, parent, false);
        } else {
            rowView = convertView;
        }

        //Anfordern des zur Listenposition gehörenden Datenobjektes
        Items currentItem = itemsList.get(position);

        //Finden der einzelnen View-Objekte
        LinearLayout cellLayout = (LinearLayout) rowView.findViewById(R.id.itemscellLayout);
        TextView itemsListContent = (TextView) rowView.findViewById(R.id.itemsListContent);
        TextView createDate = (TextView) rowView.findViewById(R.id.createDate);
        TextView doneDate = (TextView) rowView.findViewById(R.id.doneDate);
        ImageView isCheckedImage = (ImageView) rowView.findViewById(R.id.itemsImageView);

        itemsListContent.setText(currentItem.getItemsListContent());
        createDate.setText(currentItem.getCreateDate());
        doneDate.setText(currentItem.getDoneDate());

        if (currentItem.isDone()) {
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
