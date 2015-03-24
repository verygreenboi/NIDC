package ng.codehaven.cdc.interfaces;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Thompson on 22/03/2015.
 */
public interface ServiceCallbacks {
    public void onOperationProgress(int progress);

    public void onOperationCompleted(List<ParseObject> items);

    public void onMoreOperationComplete(List<ParseObject> items);

    public void onRefreshList(List<ParseObject> items);

    public void onSearchComplete(List<ParseObject> items);
}
