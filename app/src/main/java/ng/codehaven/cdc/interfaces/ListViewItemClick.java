package ng.codehaven.cdc.interfaces;

import android.view.View;

import org.json.JSONObject;

/**
 * Created by Thompson on 07/03/2015.
 */
public interface ListViewItemClick {
    void onTitleClick (View v, int position, JSONObject item);

    void onItemLongClick (int position);
}
