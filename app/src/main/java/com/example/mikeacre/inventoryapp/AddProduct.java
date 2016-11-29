package com.example.mikeacre.inventoryapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mikeacre.inventoryapp.Data.InventoryContract;
import com.example.mikeacre.inventoryapp.Data.InventoryContract.InventoryEntry;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by mikeacre on 11/28/2016.
 */

public class AddProduct extends AppCompatActivity {

    private static final int SELECT_PHOTO = 1;
    public ImageView newProductImage;
    Uri prodImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Button pickImage = (Button) findViewById(R.id.change_image_btn);
        newProductImage = (ImageView) findViewById(R.id.new_product_img);

        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        Button submitButton = (Button) findViewById(R.id.add_new_product);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addProduct();
            }
        });
    }

    private void addProduct(){
        EditText prodName = (EditText) findViewById(R.id.new_product_name);
        EditText prodQoh = (EditText) findViewById(R.id.new_product_qty);
        EditText prodPrice = (EditText) findViewById(R.id.new_product_price);

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, prodName.getText().toString());
        values.put(InventoryEntry.COLUMN_PRICE, prodPrice.getText().toString());
        values.put(InventoryEntry.COLUMN_QOH, prodQoh.getText().toString());
        values.put(InventoryEntry.COLUMN_PICTURE, prodImgUri.toString());
        getContentResolver().insert(InventoryEntry.CONTENT_URI, values);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        prodImgUri = imageUri;
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        newProductImage.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }


    }
}