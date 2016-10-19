package ng.codehaven.cdc.interfaces;

import com.parse.ParseObject;

import java.util.List;

public interface ServiceCallbacks {
    void onOperationProgress (int progress);

    void onOperationCompleted (List<ParseObject> items);

    void onMoreOperationComplete (List<ParseObject> items);

    void onRefreshList (List<ParseObject> items);

    void onSearchComplete (List<ParseObject> items);
}
