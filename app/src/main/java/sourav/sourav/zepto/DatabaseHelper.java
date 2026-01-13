package sourav.sourav.zepto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "zepto.db";
    private static final int DATABASE_VERSION = 2; // Incremented version

    // Table Names
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_ADDRESSES = "addresses";
    public static final String TABLE_PRODUCTS_CACHE = "products_cache";
    public static final String TABLE_CART = "cart";

    // Common column names
    public static final String KEY_ID = "id";

    // Orders Table columns
    public static final String KEY_ORDER_PRODUCT_NAME = "product_name";
    public static final String KEY_ORDER_PRODUCT_PRICE = "product_price";
    public static final String KEY_ORDER_PRODUCT_IMAGE = "product_image";
    public static final String KEY_ORDER_QUANTITY = "quantity";
    public static final String KEY_ORDER_DATE = "order_date";
    public static final String KEY_ORDER_STATUS = "status";

    // Address Table columns
    public static final String KEY_ADDRESS_NAME = "name";
    public static final String KEY_ADDRESS_PHONE = "phone";
    public static final String KEY_ADDRESS_DETAIL = "address_detail";

    // Products Cache & Cart Table columns
    public static final String KEY_PRODUCT_NAME = "name";
    public static final String KEY_PRODUCT_PRICE = "price";
    public static final String KEY_PRODUCT_MRP = "mrp";
    public static final String KEY_PRODUCT_IMAGE = "image";
    public static final String KEY_PRODUCT_QTY_DESC = "qty_desc";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ORDER_PRODUCT_NAME + " TEXT,"
                + KEY_ORDER_PRODUCT_PRICE + " TEXT,"
                + KEY_ORDER_PRODUCT_IMAGE + " INTEGER,"
                + KEY_ORDER_QUANTITY + " TEXT,"
                + KEY_ORDER_DATE + " TEXT,"
                + KEY_ORDER_STATUS + " TEXT" + ")";

        String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_ADDRESSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ADDRESS_NAME + " TEXT,"
                + KEY_ADDRESS_PHONE + " TEXT,"
                + KEY_ADDRESS_DETAIL + " TEXT" + ")";

        String CREATE_PRODUCTS_CACHE_TABLE = "CREATE TABLE " + TABLE_PRODUCTS_CACHE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_PRODUCT_PRICE + " TEXT,"
                + KEY_PRODUCT_MRP + " TEXT,"
                + KEY_PRODUCT_IMAGE + " INTEGER,"
                + KEY_PRODUCT_QTY_DESC + " TEXT" + ")";

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_PRODUCT_PRICE + " TEXT,"
                + KEY_PRODUCT_MRP + " TEXT,"
                + KEY_PRODUCT_IMAGE + " INTEGER,"
                + KEY_PRODUCT_QTY_DESC + " TEXT" + ")";

        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_ADDRESS_TABLE);
        db.execSQL(CREATE_PRODUCTS_CACHE_TABLE);
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS_CACHE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // --- Order Methods ---
    public void addOrder(String name, String price, int image, String qty, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_PRODUCT_NAME, name);
        values.put(KEY_ORDER_PRODUCT_PRICE, price);
        values.put(KEY_ORDER_PRODUCT_IMAGE, image);
        values.put(KEY_ORDER_QUANTITY, qty);
        values.put(KEY_ORDER_DATE, date);
        values.put(KEY_ORDER_STATUS, status);
        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_STATUS, newStatus);
        db.update(TABLE_ORDERS, values, KEY_ID + " = ?", new String[]{String.valueOf(orderId)});
        db.close();
    }

    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ORDERS + " ORDER BY id DESC", null);
    }

    // --- Address Methods ---
    public void addAddress(String name, String phone, String detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS_NAME, name);
        values.put(KEY_ADDRESS_PHONE, phone);
        values.put(KEY_ADDRESS_DETAIL, detail);
        db.insert(TABLE_ADDRESSES, null, values);
        db.close();
    }

    public void updateAddress(int id, String name, String phone, String detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS_NAME, name);
        values.put(KEY_ADDRESS_PHONE, phone);
        values.put(KEY_ADDRESS_DETAIL, detail);
        db.update(TABLE_ADDRESSES, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADDRESSES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getAllAddresses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ADDRESSES, null);
    }

    // --- Cache & Cart Methods ---
    public void cacheProducts(List<Product> products) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS_CACHE);
        for (Product p : products) {
            ContentValues values = new ContentValues();
            values.put(KEY_PRODUCT_NAME, p.getName());
            values.put(KEY_PRODUCT_PRICE, p.getPrice());
            values.put(KEY_PRODUCT_MRP, p.getMrp());
            values.put(KEY_PRODUCT_IMAGE, p.getImageResId());
            values.put(KEY_PRODUCT_QTY_DESC, p.getQuantity());
            db.insert(TABLE_PRODUCTS_CACHE, null, values);
        }
        db.close();
    }

    public List<Product> getCachedProducts() {
        return getProductsFromTable(TABLE_PRODUCTS_CACHE);
    }

    public void saveCart(List<Product> products) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CART);
        for (Product p : products) {
            ContentValues values = new ContentValues();
            values.put(KEY_PRODUCT_NAME, p.getName());
            values.put(KEY_PRODUCT_PRICE, p.getPrice());
            values.put(KEY_PRODUCT_MRP, p.getMrp());
            values.put(KEY_PRODUCT_IMAGE, p.getImageResId());
            values.put(KEY_PRODUCT_QTY_DESC, p.getQuantity());
            db.insert(TABLE_CART, null, values);
        }
        db.close();
    }

    public List<Product> getSavedCart() {
        return getProductsFromTable(TABLE_CART);
    }

    private List<Product> getProductsFromTable(String tableName) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        if (cursor.moveToFirst()) {
            do {
                products.add(new Product(
                        cursor.getString(1),
                        cursor.getInt(4),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }
}
