package com.example.mikeacre.inventoryapp;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.support.design.widget.FloatingActionButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.mikeacre.inventoryapp.Data.InventoryContract.InventoryEntry;
import com.example.mikeacre.inventoryapp.Data.InventoryReader;


public class InventoryHome extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    ProductAdapter mAdapter;
    public static final int INV_LOADER = 0;
    static final String[] PROJECTION = new String[]{
            InventoryEntry._ID,
            InventoryEntry.COLUMN_PRODUCT_NAME,
            InventoryEntry.COLUMN_QOH,
            InventoryEntry.COLUMN_PRICE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_home);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryHome.this, AddProduct.class);
                startActivity(intent);
            }
        });

        ListView inventoryList = (ListView) findViewById(R.id.inventory_list_view);
        inventoryList.setEmptyView(findViewById(R.id.empty_list_view));

        Cursor cursor = null;
        mAdapter = new ProductAdapter(getBaseContext(), cursor);
        inventoryList.setAdapter(mAdapter);

        inventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(InventoryHome.this, ProductDetail.class);
                Uri currentPetUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(INV_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, InventoryEntry.CONTENT_URI, PROJECTION, null, null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }

    public void sellOne(int id){
        //this
    }
}
