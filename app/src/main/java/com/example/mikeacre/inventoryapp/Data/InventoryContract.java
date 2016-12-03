package com.example.mikeacre.inventoryapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mikeacre on 11/28/2016.
 */

public class InventoryContract {

    //Empty constructor so class is not initialized
    private InventoryContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.mikeacre.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";

    public static final class InventoryEntry implements BaseColumns{

        public static final String TABLE_NAME = "inventory";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "productname";
        public static final String COLUMN_QOH ="qoh";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_PICTURE = "picture";
        public static final String COLUMN_VENDOR_EMAIL = "vendoremail";
        public static final String COLUMN_REORDER_QTY = "reorderqty";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

    }

}
