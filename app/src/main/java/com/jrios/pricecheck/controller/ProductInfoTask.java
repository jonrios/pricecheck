package com.jrios.pricecheck.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.jrios.pricecheck.model.ProductDTO;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by rios on 30/01/2015.
 */
public class ProductInfoTask extends AsyncTask<String, Void, ProductDTO>{

    private ResultCallback callback;

    public static final int RESULT_OK = 0;
    public static final int RESULT_NOT_FOUND = 1;
    public static final int RESULT_ERROR = -1;

    public ProductInfoTask(ResultCallback callback){
        super();

        this.callback = callback;
    }

    @Override
    protected ProductDTO doInBackground(String... params) {
        HttpGet h = new HttpGet("http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=668BD942-8CEF-42C7-BDC6-2C80B227B19B&upc="+params[0]);
        HttpClient hc = new DefaultHttpClient();
        ProductDTO rv = null;

        try {
            HttpResponse rs = hc.execute(h);
            BufferedReader reader = new BufferedReader(new InputStreamReader(rs.getEntity().getContent()));
            String json = reader.readLine();

            JSONObject jsonProduct = null, tmp = null;
            tmp = new JSONObject(json);

            if(tmp.length() != 0){
                jsonProduct = new JSONObject(tmp.getString("0"));
                String productName = jsonProduct.getString("productname");
                if(productName != null) {
                    if (productName.equals("") || productName.equals(" ")) {
                        callback.notifyResult(RESULT_NOT_FOUND, null);
                        return null;
                    }
                    rv = new ProductDTO();
                    rv.setProductName(productName);
                    rv.setUpc(params[0]);
                    Log.d("ProductInfoTask","Product: "+productName);

                    callback.notifyResult(RESULT_OK, rv);
                }
            }else{
                Log.d("ProductInfoTask","No product found");
                callback.notifyResult(RESULT_NOT_FOUND, null);
            }


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            callback.notifyResult(RESULT_ERROR, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            callback.notifyResult(RESULT_ERROR, null);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.notifyResult(RESULT_ERROR, null);
        }

        return rv;
    }

    public interface ResultCallback{
        public void notifyResult(int resultCode, Object result);
    }
}
