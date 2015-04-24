package ng.codehaven.cdc.utils;

import android.content.Context;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import ng.codehaven.cdc.models.Item;

public class LoopItems {
    private List<ParseObject> mItems;
    private List<Item> i = new ArrayList<>();
    private Context mContext;

    public LoopItems(Context c, List<ParseObject> o) {
        mContext = c;
        mItems = o;
    }

    public List<Item> getItems(){
        if (mItems != null){
            for (ParseObject p : mItems) {
                i.add(new Item(
                        mContext,
                        p.getString("cet"),
                        p.getString("description"),
                        p.getObjectId(),
                        p.getInt("levy"),
                        p.getInt("vat"),
                        p.getInt("duty"),
                        p.getCreatedAt()));
            }
        }
        return i;
    }

}
