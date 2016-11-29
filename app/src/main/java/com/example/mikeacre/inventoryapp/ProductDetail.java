package com.example.mikeacre.inventoryapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mikeacre.inventoryapp.Data.InventoryContract.InventoryEntry;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by mikeacre on 11/28/2016.
 */

public class ProductDetail extends AppCompatActivity {

    //static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        requestPermissions(new String[]{"EAD_EXTERNAL_STORAGE"}, 1);

        Intent intent = getIntent();
        Uri currentPetUri = intent.getData();
        String[] projection = {InventoryEntry.COLUMN_PRODUCT_NAME, InventoryEntry.COLUMN_PICTURE};
        Log.e("URI:: ", currentPetUri.toString());
        Cursor cursor = getContentResolver().query(currentPetUri, projection, null, null, null, null);


        TextView prodName = (TextView) findViewById(R.id.prod_detail_name);
        ImageView prodImg = (ImageView) findViewById(R.id.prod_detail_img);

        String count = ""+cursor.getCount();
        cursor.moveToFirst();
        prodName.setText(cursor.getString(0));
        String uriString = cursor.getString(1);
        Uri imageUri = Uri.parse(uriString);

        final InputStream imageStream;
        try {
            imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            prodImg.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        cursor.close();


    }
}
