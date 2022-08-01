package com.example.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1; // Database version
    private static final String DATABASE_NAME = "MyDB"; // Database Name
    private static final String TABLE_NAME = "ITEMS"; // Table name
    private static final String ITEM_ID = "ITEM_ID"; // item id column name
    private static final String ITEM_NAME = "ITEM_NAME"; // item name column name
    private static final String ITEM_DESC = "DETAIL"; // detail column name
    private static final String ITEM_SIZE = "SIZE"; // size column name
    private static final String ITEM_QTY = "QUANTITY"; // quantity column name
    private static final String ITEM_URGENT = "URGENT"; // urgent column name
    private static final String ITEM_BOUGHT = "BOUGHT"; // bought column name
    private static final String ITEM_DATE = "DATE"; // date column name

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME + "("
                + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEM_NAME + " TEXT NOT NULL, "
                + ITEM_DESC + " TEXT, "
                + ITEM_SIZE + " TEXT NOT NULL, "
                + ITEM_QTY + " INTEGER DEFAULT 0, "
                + ITEM_URGENT + " INTEGER DEFAULT 0, "
                + ITEM_BOUGHT + " INTEGER DEFAULT 0, "
                + ITEM_DATE + " TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Drop older table if existed
        onCreate(db); // Create table again
    }

    // Add item
    public long AddItem(Item item) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_NAME, item.getName());
        contentValues.put(ITEM_DESC, item.getDetails());
        contentValues.put(ITEM_SIZE, item.getSize());
        contentValues.put(ITEM_QTY, item.getQty());
        contentValues.put(ITEM_URGENT, item.getUrgent());
        contentValues.put(ITEM_BOUGHT, item.getBought());
        contentValues.put(ITEM_DATE, item.getDate());
        return db.insert(TABLE_NAME,null, contentValues);
    }

    // Count database rows
    public long CountRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    // Update item
    public long Update(Item item) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_NAME, item.getName());
        contentValues.put(ITEM_DESC, item.getDetails());
        contentValues.put(ITEM_SIZE, item.getSize());
        contentValues.put(ITEM_QTY, item.getQty());
        contentValues.put(ITEM_URGENT, item.getUrgent());
        contentValues.put(ITEM_BOUGHT, item.getBought());
        contentValues.put(ITEM_DATE, item.getDate());
        return db.update(TABLE_NAME, contentValues,ITEM_ID + "=" + item.getId(),null);
    }

    // Delete item
    public long DeleteItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_NAME,ITEM_ID + "=" + id,null);
    }

    // Get item list
    public ArrayList<Item> getAllItem() {
        ArrayList<Item> itemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ITEM_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Item item = new Item(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                cursor.getInt(5)==1
            );
            item.setId(cursor.getInt(0));
            item.setBought(cursor.getInt(6)==1);
            item.setDate(cursor.getString(7));
            itemList.add(item);
        }

        return itemList;
    }
}
