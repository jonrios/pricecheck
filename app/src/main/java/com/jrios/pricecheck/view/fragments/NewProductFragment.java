package com.jrios.pricecheck.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.jrios.pricecheck.R;
import com.jrios.pricecheck.model.DatabaseOperations;
import com.jrios.pricecheck.model.ProductDAO;
import com.jrios.pricecheck.model.ProductDTO;
import com.jrios.pricecheck.view.MainActivity;


public class NewProductFragment extends Fragment {

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

        etProductName = (EditText) rootView.findViewById(R.id.etProductName);
        etProductSize = (EditText) rootView.findViewById(R.id.etProductSize);
        spProductSizeUnit = (Spinner) rootView.findViewById(R.id.spProductSizeUnit);
        etUPC = (EditText) rootView.findViewById(R.id.etUPC);



        return rootView;
    }


}
