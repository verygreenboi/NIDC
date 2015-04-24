package ng.codehaven.cdc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ng.codehaven.cdc.Constants;

public class SharedPrefUtil {

    private Context mContext;
    /**
    ** Default constructor
    ** @param mContext {@link #mContext Context}
    **
    **/

    public SharedPrefUtil(Context mContext) {
        this.mContext = mContext;
    }

    public SharedPreferences getSharedPref(String key, int mode){
        return mContext.getSharedPreferences(key, mode);
    }

    public boolean getBool(String key){
        SharedPreferences isDBLoaded = mContext.getSharedPreferences(Constants.BOOTSTRAP, 0);
        return isDBLoaded.getBoolean("seen", false);
    }

}
