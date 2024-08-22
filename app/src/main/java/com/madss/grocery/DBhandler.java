package com.madss.grocery;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBhandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Grocery.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_COUNT = "count";

    private static final String COLUMN_OFFPRICE = "off";

    public DBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " TEXT,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_COUNT + " INTEGER,"
                + COLUMN_OFFPRICE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean isProductExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + "=?", new String[]{id}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void addProduct(String id, String name, String price, String image, int count, String Offprice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_COUNT, count);
        values.put(COLUMN_OFFPRICE,Offprice);
        db.insert(TABLE_NAME, null, values);
    }

    public void updateProductCount(String id, int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COUNT, count);
        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{id});
    }

    public int getProductCount(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_COUNT}, COLUMN_ID + "=?", new String[]{id}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT));
            cursor.close();
            return count;
        } else {
            return 0;
        }
    }

    public void removeProductIfCountZero(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = getProductCount(id);
        if (count == 0) {
            db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        }
    }


    public List<PojoAddToCart> getAllProducts() {
        List<PojoAddToCart> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
                int imageIndex = cursor.getColumnIndex(COLUMN_IMAGE);
                int countIndex = cursor.getColumnIndex(COLUMN_COUNT);
                int offpriceIndex = cursor.getColumnIndex(COLUMN_OFFPRICE);

                if (idIndex == -1 || nameIndex == -1 || priceIndex == -1 || imageIndex == -1 || countIndex == -1 || offpriceIndex==-1) {
                    // Log an error or handle the missing column scenario
                    // For example: Log.e("DBhandler", "Column not found in cursor");
                    continue;
                }

                String productid = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String price = cursor.getString(priceIndex);
                String image = cursor.getString(imageIndex);
                int count = cursor.getInt(countIndex);
                String offprice = cursor.getString(offpriceIndex);

                productList.add(new PojoAddToCart(productid, name, price, image, String.valueOf(count),offprice));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
