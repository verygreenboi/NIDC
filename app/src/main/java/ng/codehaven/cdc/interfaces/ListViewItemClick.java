package ng.codehaven.cdc.interfaces;

import android.view.View;

import org.json.JSONObject;

/**
 * Created by Thompson on 07/03/2015.
 */
public interface ListViewItemClick {
    public void onTitleClick(View v, int position, JSONObject item);

    public void onItemLongClick(int position);
}
