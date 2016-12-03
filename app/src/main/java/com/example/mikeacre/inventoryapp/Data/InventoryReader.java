package com.example.mikeacre.inventoryapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mikeacre on 11/28/2016.
 */

public class InventoryReader extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "InventoryApp.db";

    public InventoryReader(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME + " ("
                + InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_PICTURE + " TEXT, "
                + InventoryContract.InventoryEntry.COLUMN_PRICE + " REAL NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_QOH + " INTEGER NOT NULL DEFAULT 0,"
                + InventoryContract.InventoryEntry.COLUMN_VENDOR_EMAIL + " TEXT,"
                + InventoryContract.InventoryEntry.COLUMN_REORDER_QTY + " INTEGER);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

