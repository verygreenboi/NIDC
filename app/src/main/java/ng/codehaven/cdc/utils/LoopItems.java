package ng.codehaven.cdc.utils;

import android.content.Context;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import ng.codehaven.cdc.models.Favorite;
import ng.codehaven.cdc.models.Item;

public class LoopItems {
    private List<ParseObject> mItems;
    private List<Item> i = new ArrayList<>();
    private Context mContext;
    private RealmResults<Favorite> favorites;

    public LoopItems(Context c, List<ParseObject> o) {
        mContext = c;
        mItems = o;
    }

    public LoopItems(Context c, RealmResults<Favorite> f) {
        this.mContext = c;
        this.favorites = f;
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
                        p.getInt("import_duty"),
                        p.getCreatedAt()));
            }
        }
        return i;
    }

    public List<Item> getItemsFromFavorite() {
        if (favorites != null) {
            for (Favorite f : favorites) {
                i.add(new Item(
                        mContext,
                        f.getCet(),
                        f.getDescription(),
                        f.getObjectId(),
                        f.getLevy(),
                        f.getVat(),
                        f.getDuty(),
                        f.getCreatedAt()
                ));
            }
        }
        return i;
    }

}
