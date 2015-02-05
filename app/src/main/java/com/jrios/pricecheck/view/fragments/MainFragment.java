package com.jrios.pricecheck.view.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jrios.pricecheck.R;
import com.jrios.pricecheck.model.DatabaseListener;
import com.jrios.pricecheck.model.DatabaseListenerManager;
import com.jrios.pricecheck.model.DatabaseOperations;
import com.jrios.pricecheck.model.ProductDTO;
import com.jrios.pricecheck.view.MainActivity;
import com.jrios.pricecheck.view.utils.SwipeableRecyclerViewTouchListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static MainFragment _instance;

    private List<ProductDTO> lProducts;
    private LastProductsAdapter adapter;

    private FloatingActionButton fab;

    private DatabaseOperations db;

    public static MainFragment getInstance(){
        if(_instance == null)
            _instance = new MainFragment();
        return _instance;
    }

    public MainFragment() {
        db = DatabaseOperations.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        db.registerListener(DatabaseListenerManager.TABLE_LAST_UPDATED, new DatabaseListener() {
            @Override
            public void trigger() {

            }
        });

        fab = (FloatingActionButton) rootView.findViewById(R.id.last_updated_fab);

        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.lastProductsList);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        lProducts = new ArrayList<>();
        populateList(lProducts);

        adapter = new LastProductsAdapter(lProducts);
        recList.setAdapter(adapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recList,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    lProducts.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    lProducts.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });


        recList.addOnItemTouchListener(swipeTouchListener);

        fab.attachToRecyclerView(recList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                Fragment fragment = NewProductFragment.getInstance();

                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        return rootView;
    }

    private void populateList(List<ProductDTO> lProducts) {
        lProducts.add(new ProductDTO("Test1"));
        lProducts.add(new ProductDTO("Test2"));
        lProducts.add(new ProductDTO("Test3"));
        lProducts.add(new ProductDTO("Test4"));
        lProducts.add(new ProductDTO("Test5"));
        lProducts.add(new ProductDTO("Test6"));
        lProducts.add(new ProductDTO("Test7"));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getString(R.string.app_name));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _instance = null;
    }

    private class LastProductsAdapter extends RecyclerView.Adapter<ProductViewHolder>{

        private List<ProductDTO> lProducts;

        private LastProductsAdapter(List<ProductDTO> lProducts) {
            this.lProducts = lProducts;
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.last_products_row, parent, false);

            return new ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            ProductDTO product = lProducts.get(position);
            holder.tvProductName.setText(product.getProductname());

        }

        @Override
        public int getItemCount() {
            return lProducts.size();
        }
    }



    private static class ProductViewHolder extends RecyclerView.ViewHolder{

        protected TextView tvProductName;

        public ProductViewHolder(View itemView) {
            super(itemView);

            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
        }
    }

}
