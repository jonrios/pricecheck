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

    private String upc;

    public ProductInfoTask(String upc){
        this.upc = upc;
    }

    @Override
    protected ProductDTO doInBackground(String... params) {
        HttpGet h = new HttpGet("http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=668BD942-8CEF-42C7-BDC6-2C80B227B19B&upc="+upc);
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
                rv = new ProductDTO(jsonProduct.getString("productname"));
            }

            Log.d("ProductInfoTask","Product: "+jsonProduct.getString("productname"));

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rv;
    }
}
