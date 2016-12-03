package com.example.mikeacre.inventoryapp;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mikeacre.inventoryapp.Data.InventoryContract.InventoryEntry;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by mikeacre on 11/28/2016.
 */

public class ProductDetail extends AppCompatActivity {

    private static String thisId;
    private static long thisIdLong;
    private int currentQty;
    private int changeqty = 1;
    private TextView prodQty;
    private boolean permission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission = false;
            TextView permisionText = (TextView) findViewById(R.id.no_permission);
            permisionText.setVisibility(View.VISIBLE);
        }

                Intent intent = getIntent();
        Uri currentInvUri = intent.getData();
        String[] projection = {InventoryEntry._ID,InventoryEntry.COLUMN_PRODUCT_NAME, InventoryEntry.COLUMN_PICTURE,InventoryEntry.COLUMN_QOH,InventoryEntry.COLUMN_PRICE,InventoryEntry.COLUMN_VENDOR_EMAIL,InventoryEntry.COLUMN_REORDER_QTY};
        Log.e("URI:: ", currentInvUri.toString());
        Cursor cursor = getContentResolver().query(currentInvUri, projection, null, null, null, null);

        TextView prodName = (TextView) findViewById(R.id.prod_detail_name);
        ImageView prodImg = (ImageView) findViewById(R.id.prod_detail_img);
        prodQty = (TextView) findViewById(R.id.detail_item_qty);
        TextView prodPrice = (TextView) findViewById(R.id.detail_item_price);

        cursor.moveToFirst();
        thisId = cursor.getString(0);
        thisIdLong = cursor.getLong(0);
        prodName.setText(cursor.getString(1));
        String uriString = cursor.getString(2);
        prodQty.setText(getString(R.string.detail_qty_text)+cursor.getString(3));
        currentQty = Integer.parseInt(cursor.getString(3));
        prodPrice.setText(getString(R.string.detail_price_text)+cursor.getString(4));
        Uri imageUri = Uri.parse(uriString);

        //
        //Set up Sell/Receive button

        Button addBtn = (Button) findViewById(R.id.add_qty_btn);
        Button subBtn = (Button) findViewById(R.id.subtract_qty_btn);
        Button recBtn = (Button) findViewById(R.id.recieve_qty_btn);
        Button selBtn = (Button) findViewById(R.id.sell_qty_btn);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeValue('a');
            }
        });
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeValue('d');
            }

        });
        recBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adjustQty("up");
            }
        });
        selBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adjustQty("down");
            }
        });


        //
        //Set up order more button and intent
        //
        Button orderBtn = (Button) findViewById(R.id.order_more_btn);
        String subject = getString(R.string.email_subject);
        String text = getString(R.string.email_subject_start) + cursor.getString(6) + getString(R.string.email_subject_middle) + cursor.getString(1);
        final Intent orderIntent = new Intent(Intent.ACTION_SEND);
        orderIntent.setType("text/html");
        orderIntent.putExtra(Intent.EXTRA_EMAIL, "mikeacre@gmail.com");
        orderIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        orderIntent.putExtra(Intent.EXTRA_TEXT, text);
        orderIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        orderBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(orderIntent);
            }
        });
        if(permission) {
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                prodImg.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        cursor.close();


    }

    private void adjustQty(String dir) {
        int newQoh;
        if(dir == "up")
            newQoh = currentQty + changeqty;
        else if((currentQty - changeqty)< 0)
            newQoh = 0;
        else
            newQoh = currentQty - changeqty;


        ContentValues adjustValue = new ContentValues();
        adjustValue.put(InventoryEntry.COLUMN_QOH, newQoh);

        String selection = InventoryEntry._ID+"=?";
        String[] selArgs = new String[] {thisId};
        getContentResolver().update(InventoryEntry.CONTENT_URI, adjustValue, selection, selArgs);
        currentQty = newQoh;
        prodQty.setText(getString(R.string.detail_qty_text)+newQoh);
    }

    public void changeValue(char dir){
        EditText qtyTxt = (EditText) findViewById(R.id.prod_detail_adjust_qty_txt);
        Editable editQtyTxt = qtyTxt.getText();

        Integer integer = Integer.parseInt(editQtyTxt.toString());
        int currtext = integer.intValue();
        Log.e(" ", "   "+qtyTxt.toString());// Integer.parseInt(qtyTxt.toString());
        if(dir == 'a') {
            qtyTxt.setText(currtext + 1 + "");
            changeqty++;
        }
        else if (currtext > 1) {
            qtyTxt.setText(currtext - 1 + "");
            changeqty--;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit_item:
                Intent intent = new Intent(ProductDetail.this, AddProduct.class);
                Uri currentInvUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, thisIdLong);
                intent.setData(currentInvUri);
                startActivity(intent);
                 return true;
            case R.id.action_delete_item:
                    getContentResolver().delete(InventoryEntry.CONTENT_URI, InventoryEntry._ID, new String[]{thisId});
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
