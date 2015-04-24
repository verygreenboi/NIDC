package ng.codehaven.cdc.utils;

import android.content.Context;

import ng.codehaven.cdc.R;

/**
 * Created by Thompson on 26/03/2015.
 */
public class debug {
    public static boolean isDebug(Context c){
        return c.getString(R.string.isDebug).equals("true");
    }
}
