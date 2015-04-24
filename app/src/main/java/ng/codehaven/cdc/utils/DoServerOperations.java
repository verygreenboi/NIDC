package ng.codehaven.cdc.utils;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

import ng.codehaven.cdc.Constants;
import ng.codehaven.cdc.interfaces.ServerOperations;

public class DoServerOperations implements ServerOperations {

    private String order;
    private int limit, skip;
    private String[] keywords;
    private Context mContext;
    private boolean isBootStrapped;

    public DoServerOperations(String order, int limit, int skip, String[] keywords) {
        this.order = order;
        this.limit = limit;
        this.skip = skip;
        this.keywords = keywords;
    }

    public DoServerOperations(Context c, String order, int limit, int skip) {
        this.order = order;
        this.limit = limit;
        this.skip = skip;
        this.mContext = c;
    }

    @Override
    public List<ParseObject> getItems() throws ParseException {
        SharedPrefUtil sp = new SharedPrefUtil(mContext);
        isBootStrapped = sp.getBool("seen");
        ParseQuery<ParseObject> q = ParseQuery.getQuery(Constants.CET_CLASS);
        q.orderByAscending(order);
        q.setLimit(limit);
        q.setSkip(skip);
        return q.find();
    }

    @Override
    public List<ParseObject> doMultiSearch() throws ParseException {
        ParseQuery<ParseObject> q = ParseQuery.getQuery(Constants.CET_CLASS);
        q.orderByAscending(order);
        q.setLimit(limit);
        q.setSkip(skip);
        q.whereContainedIn("keywords", Arrays.asList(keywords));
        return q.find();
    }

}
