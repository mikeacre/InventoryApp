package com.example.mikeacre.inventoryapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikeacre.inventoryapp.Data.InventoryContract;
import com.example.mikeacre.inventoryapp.Data.InventoryContract.InventoryEntry;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by mikeacre on 11/28/2016.
 */

public class AddProduct extends AppCompatActivity {

    private static final int SELECT_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    public ImageView newProductImage;
    Uri prodImgUri = Uri.parse("");
    int thisId = -1;

    EditText prodName;
    EditText prodQoh;
    EditText prodPrice;
    EditText vendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        prodName = (EditText) findViewById(R.id.new_product_name);
        prodQoh = (EditText) findViewById(R.id.new_product_qty);
        prodPrice = (EditText) findViewById(R.id.new_product_price);
        vendEmail = (EditText) findViewById(R.id.new_product_vendor);
        newProductImage = (ImageView) findViewById(R.id.new_product_img);

        Intent intent = getIntent();
        if (intent != null) {
            Uri sentUri = intent.getData();
            if (sentUri != null)
                fillTheViews(sentUri);
        }


        Button pickImage = (Button) findViewById(R.id.change_image_btn);
        Button takeImage = (Button) findViewById(R.id.take_image_btn);

        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        takeImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        Button submitButton = (Button) findViewById(R.id.add_new_product);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void fillTheViews(Uri thisUri) {


        Cursor fillCursor = getContentResolver().query(thisUri, null, null, null, null);
        fillCursor.moveToFirst();
        thisId = fillCursor.getInt(0);
        prodName.setText(fillCursor.getString(1));
        prodPrice.setText(fillCursor.getString(3));
        prodQoh.setText(fillCursor.getString(4));
        vendEmail.setText(fillCursor.getString(5));
        prodImgUri = Uri.parse(fillCursor.getString(2));
        TextView invText = (TextView) findViewById(R.id.detail_product_inventory_text);
        Button editBtn = (Button) findViewById(R.id.add_new_product);
        editBtn.setText(R.string.edit_button_text);
        invText.setText(R.string.inventory_text_edit_screen);
        Uri editImageUri = Uri.parse(fillCursor.getString(2));
        displayImage(editImageUri);

    }

    private boolean checkValues() {
        if (prodName.getText().toString().equals("")
                || prodPrice.getText().toString().equals("")
                || prodQoh.getText().toString().equals("") || vendEmail.getText().toString().equals("") || prodImgUri.toString().equals(""))
            return false;
        else
            return true;
    }


    private void addProduct() {

        if (checkValues()) {

            ContentValues values = new ContentValues();
            values.put(InventoryEntry.COLUMN_PRODUCT_NAME, prodName.getText().toString());
            values.put(InventoryEntry.COLUMN_PRICE, prodPrice.getText().toString());
            values.put(InventoryEntry.COLUMN_QOH, prodQoh.getText().toString());
            values.put(InventoryEntry.COLUMN_PICTURE, prodImgUri.toString());
            values.put(InventoryEntry.COLUMN_VENDOR_EMAIL, vendEmail.getText().toString());

            if (thisId >= 0)
                getContentResolver().update(InventoryEntry.CONTENT_URI, values, InventoryEntry._ID + "=?", new String[]{thisId + ""});
            else
                getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            NavUtils.navigateUpFromSameTask(this);

        } else {
            Toast.makeText(getBaseContext(), R.string.null_item_toast_msg, Toast.LENGTH_SHORT).show();
        }


    }

    public void displayImage(Uri thisUri) {

        try {
            if (!thisUri.toString().equals("")) {
                prodImgUri = thisUri;
                final InputStream imageStream = getContentResolver().openInputStream(thisUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                newProductImage.setImageBitmap(selectedImage);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK)
                    displayImage(imageReturnedIntent.getData());
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK)
                    displayImage(imageReturnedIntent.getData());
                break;
        }


    }
}