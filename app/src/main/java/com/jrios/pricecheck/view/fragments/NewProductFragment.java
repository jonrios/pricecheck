package com.jrios.pricecheck.view.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jrios.pricecheck.R;
import com.jrios.pricecheck.controller.ProductInfoTask;
import com.jrios.pricecheck.model.DatabaseOperations;
import com.jrios.pricecheck.model.ProductDAO;
import com.jrios.pricecheck.model.ProductDTO;
import com.jrios.pricecheck.view.MainActivity;


public class NewProductFragment extends Fragment {
    private static String TAG = "NewProductFragment";

    private static NewProductFragment _instance;

    private EditText etProductName;
    private EditText etProductSize;
    private Spinner spProductSizeUnit;
    private EditText etUPC;

    ProductDAO db;


    public static NewProductFragment getInstance() {
        if(_instance == null)
            _instance = new NewProductFragment();
        return _instance;
    }



    public NewProductFragment(){
        db = DatabaseOperations.getInstance(getActivity());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_product, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            ProductDTO p = new ProductDTO();
            p.setProductName(etProductName.getText().toString());
            p.setProductSize(Integer.parseInt(etProductSize.getText().toString()));
            p.setProductSizeUnit(spProductSizeUnit.getSelectedItemPosition());
            p.setUpc(etUPC.getText().toString());

            // TODO Parse fields

            db.addProduct(p);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            Fragment fragment = new MainFragment();

            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        } else if(item.getItemId() == R.id.action_upc){
            Log.d(TAG, "Starting Barcode Scanner");
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            try {
                startActivityForResult(intent, 0);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), R.string.barcodeActivityNotFound, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setHasOptionsMenu(true);

        ((MainActivity)activity).onSectionAttached(getString(R.string.newProduct));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_product, container, false);

        Bundle b;
        String upc;

        etProductName = (EditText) rootView.findViewById(R.id.etProductName);
        etProductSize = (EditText) rootView.findViewById(R.id.etProductSize);
        spProductSizeUnit = (Spinner) rootView.findViewById(R.id.spProductSizeUnit);
        etUPC = (EditText) rootView.findViewById(R.id.etUPC);

        if((b = getArguments()) != null){
            if((upc = b.getString("upc")) != null){
                etUPC.setText(upc);
            }
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){ // Result OK
            String result = data.getStringExtra("SCAN_RESULT");

            Log.d(TAG, "Starting ProductInfoTask(upc = "+result+")");
            ProductInfoTask productInfoTask = new ProductInfoTask(new ProductInfoTask.ResultCallback() {
                @Override
                public void notifyResult(int resultCode, Object result) {
                    if(resultCode == ProductInfoTask.RESULT_OK){
                        Log.d(TAG, "ProductInfoTask RESULT_OK");
                        Log.d(TAG, "ProductName: "+((ProductDTO)result).getProductName());

                        // TODO show name suggestion

                    } else if(resultCode == ProductInfoTask.RESULT_NOT_FOUND){
                        Toast.makeText(getActivity(), R.string.productInfoNotFound, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            productInfoTask.execute(result);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
