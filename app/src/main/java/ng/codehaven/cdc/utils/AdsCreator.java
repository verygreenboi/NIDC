package ng.codehaven.cdc.utils;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;

import ng.codehaven.cdc.BuildConfig;
import ng.codehaven.cdc.R;

public class AdsCreator {
    Context mContext;
    public AdsCreator(Context context) {
        mContext = context;
    }


    public AdRequest adRequest(){

        if (BuildConfig.DEBUG) {
            return new AdRequest.Builder().addTestDevice("A618CA31BE191BACDFD025765E3B6CC5").addTestDevice("8BDE7AAE4423990D6E5BA2A194991C09").build();
        } else {
            return new AdRequest.Builder().build();
        }
    }
}
