package com.example.mikeacre.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mikeacre.inventoryapp.Data.InventoryContract;

/**
 * Created by mikeacre on 11/30/2016.
 */

public class ProductAdapter extends CursorAdapter {

    private LayoutInflater cursorInflator;
    private Context mContext;

    public TextView sellButton;

    public ProductAdapter(Context context, Cursor c) {
        super(context, c);
        cursorInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflator.inflate(R.layout.inventory_list_item, null, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int thisId;
        TextView textName = (TextView) view.findViewById(R.id.product_name);
        TextView textPirce = (TextView) view.findViewById(R.id.product_price);
        final TextView textQoh = (TextView) view.findViewById(R.id.product_qoh);
        textName.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME)));
        textPirce.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE)));
        textQoh.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QOH)));
        sellButton = (TextView) view.findViewById(R.id.sellbutton);
        thisId = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry._ID));


        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int currQty = Integer.parseInt(textQoh.getText().toString());
                Uri currUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, thisId);
                if (currQty > 0) {
                    currQty = currQty - 1;
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.COLUMN_QOH, currQty);
                    mContext.getContentResolver().update(currUri, values, InventoryContract.InventoryEntry._ID+"=?", new String[]{thisId+""});
                   // textQoh.setText("" + currQty);
                }
            }
        });

    }
}
