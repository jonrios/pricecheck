package com.jrios.pricecheck.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rios on 02/02/2015.
 */
public class DatabaseListenerManager {

    public final static int TABLE_PRODUCTS = 0;
    public final static int TABLE_LAST_UPDATED = 1;
    public final static int TABLE_SHOPS = 2;
    public final static int TABLE_PRICE = 3;

    private Map<Integer, List<DatabaseListener>> mListeners;

    public DatabaseListenerManager(){
        mListeners = new HashMap<>();
    }

    public void registerListener(int table, DatabaseListener listener){
        List<DatabaseListener> lListener = mListeners.get(table);

        if(lListener == null){
            lListener = new ArrayList<>();
            lListener.add(listener);
            mListeners.put(table,lListener);
        } else {
            lListener.add(listener);
        }
    }

    public void unregisterListener(int table, DatabaseListener listener){
        List<DatabaseListener> lListener = mListeners.get(table);

        if(lListener != null){
            lListener.remove(listener);
        }
    }

    public void triggerListeners(int table){
        List<DatabaseListener> lListener = mListeners.get(table);

        if(lListener != null) {
            for (DatabaseListener dl : lListener) {
                if (dl != null) {
                    dl.trigger();
                }
            }
        }
    }
}
