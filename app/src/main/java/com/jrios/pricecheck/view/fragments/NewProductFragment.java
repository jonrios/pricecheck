package com.jrios.pricecheck.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jrios.pricecheck.R;
import com.jrios.pricecheck.view.MainActivity;


public class NewProductFragment extends Fragment {

    private static NewProductFragment _instance;

    public static NewProductFragment getInstance() {
        if(_instance == null)
            _instance = new NewProductFragment();
        return _instance;
    }

    public NewProductFragment(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(getString(R.string.newProduct));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_product, container, false);


        return rootView;
    }


}
