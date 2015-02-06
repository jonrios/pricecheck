package com.jrios.pricecheck.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rios on 01/02/2015.
 */
public class DatabaseOperations extends SQLiteOpenHelper implements ProductDAO{

    private static final String TAG = "DatabaseOperations";
    private Context context;

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "pricecheck";

    private SimpleDateFormat dateFormat;

    private DatabaseListenerManager listenerManager;

    private static DatabaseOperations _instance;

    private String createTableProducts = "CREATE TABLE products (\n" +
            "    id INTEGER PRIMARY KEY,\n" +
            "    name TEXT,\n" +
            "    upc TEXT,\n" +
            "    size INTEGER,\n" +
            "    unit INTEGER\n)";

    private String createTableShops = "CREATE TABLE shops (\n" +
            "    id INTEGER PRIMARY KEY,\n" +
            "    name TEXT,\n" +
            "    location TEXT,\n" +
            "    coordinates TEXT)";

    private String createTablePrices = "CREATE TABLE prices (" +
                    "    product_id INTEGER,\n" +
                    "    shop_id INTEGER,\n" +
                    "    price_date DATETIME,\n" +
                    "    price REAL,\n" +
                    "    is_offer BOOLEAN,\n" +
                    "    PRIMARY KEY(product_id, shop_id)\n" +
                    "    FOREIGN KEY(product_id) REFERENCES products(id),\n" +
                    "    FOREIGN KEY(shop_id) REFERENCES shops(id))";

    private String createTableLastUpdated = "CREATE TABLE last_updated (\n" +
            "    product_id INTEGER PRIMARY KEY,\n" +
            "    date DATETIME,\n" +
            "    FOREIGN KEY(product_id) REFERENCES products(id))";

    public synchronized static DatabaseOperations getInstance(Context context){
        if(_instance == null)
            _instance = new DatabaseOperations(context);
        return _instance;
    }

    public DatabaseOperations(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        listenerManager = new DatabaseListenerManager();


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableProducts);
        db.execSQL(createTableShops);
        db.execSQL(createTablePrices);
        db.execSQL(createTableLastUpdated);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS last_updated");
        db.execSQL("DROP TABLE IF EXISTS prices");
        db.execSQL("DROP TABLE IF EXISTS shops");
        db.execSQL("DROP TABLE IF EXISTS products");

        onCreate(db);
    }

    public void registerListener(int table, DatabaseListener listener){
        listenerManager.registerListener(table, listener);
    }

    public void unregisterListener(int table, DatabaseListener listener){
        listenerManager.unregisterListener(table, listener);
    }

    /*
    PRODUCT OPERATIONS
     */

    @Override
    public List<ProductDTO> getProducts() {
        return null;
    }

    @Override
    public ProductDTO getProduct(int id) {
        return null;
    }

    @Override
    public ProductDTO getProduct(String upc) {
        return null;
    }

    @Override
    public List<ProductDTO> getLastCheckedProducts() {
        SQLiteDatabase db = getReadableDatabase();
        List<ProductDTO> rv = new ArrayList<>();
        String query =
                "SELECT p.id, p.name, p.upc, p.size, p.unit " +
                        "FROM products AS p JOIN last_updated AS lu " +
                        "ON p.id = lu.product_id " +
                        "ORDER BY lu.date DESC";

        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                ProductDTO p = new ProductDTO();
                p.setId(c.getInt(0));
                p.setProductName(c.getString(1));
                p.setUpc(c.getString(2));
                p.setProductSize(c.getInt(3));
                p.setProductSizeUnit(c.getInt(4));

                rv.add(p);

                c.moveToNext();
            }
        }

        c.close();

        return rv;
    }

    @Override
    public List<ProductDTO> getLastCheckedProducts(int amount) {
        return null;
    }

    @Override
    public void addProduct(ProductDTO product) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", product.getProductName());
        values.put("upc", product.getUpc());
        values.put("size", product.getProductSize());
        values.put("unit", product.getProductSizeUnit());

        // insert row
        long product_id = db.insert("products", null, values);

        values.clear();

        values.put("product_id", product_id);
        values.put("date", dateFormat.format(Calendar.getInstance().getTimeInMillis()));

        db.insert("last_updated", null, values);

        listenerManager.triggerListeners(DatabaseListenerManager.TABLE_PRODUCTS);
        listenerManager.triggerListeners(DatabaseListenerManager.TABLE_LAST_UPDATED);

    }

    @Override
    public void removeProduct(int id) {

    }

    @Override
    public void removeLastCheckedProduct(int id) {
        Log.d(TAG, "removeLastCheckedProduct(id = "+id+")");
        SQLiteDatabase db = getWritableDatabase();

        db.delete("last_updated", "product_id = ?", new String[]{String.valueOf(id)});
    }
}
