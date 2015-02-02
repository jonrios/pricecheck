package com.jrios.pricecheck.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jrios.pricecheck.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rios on 01/02/2015.
 */
public class DatabaseOperations extends SQLiteOpenHelper implements ProductDAO{

    private Context context;

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "pricecheck";

    private SimpleDateFormat dateFormat;

    private DatabaseListenerManager listenerManager;

    private static DatabaseOperations _instance;

    public static DatabaseOperations getInstance(Context context){
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
        String sqlScript = getScript();

        db.execSQL(sqlScript);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    private String getScript() {
        InputStream is = context.getResources().openRawResource(R.raw.db);
        byte buffer[] = null;
        String rv = null;

        try {
            buffer = new byte[is.available()];

            is.read(buffer);

            rv = new String(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rv;
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
        return null;
    }

    @Override
    public List<ProductDTO> getLastCheckedProducts(int amount) {
        return null;
    }

    @Override
    public void addProduct(ProductDTO product) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", product.getProductname());
        values.put("upc", product.getUpc());


        // insert row
        long product_id = db.insert("products", null, values);

        values.clear();

        values.put("product_id", product_id);
        values.put("date_updated", dateFormat.format(Calendar.getInstance().getTimeInMillis()));

        db.insert("last_updated", null, values);

        listenerManager.triggerListeners(DatabaseListenerManager.TABLE_PRODUCTS);
        listenerManager.triggerListeners(DatabaseListenerManager.TABLE_LAST_UPDATED);

    }

    @Override
    public void removeProduct(int id) {

    }

    @Override
    public void removeLastCheckedProduct(int id) {

    }
}
