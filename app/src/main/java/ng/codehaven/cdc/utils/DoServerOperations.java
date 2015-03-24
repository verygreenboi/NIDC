package ng.codehaven.cdc.utils;

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

    public DoServerOperations(String order, int limit, int skip, String[] keywords) {
        this.order = order;
        this.limit = limit;
        this.skip = skip;
        this.keywords = keywords;
    }

    public DoServerOperations(String order, int limit, int skip) {
        this.order = order;
        this.limit = limit;
        this.skip = skip;
    }

    @Override
    public List<ParseObject> getItems() throws ParseException {
        ParseQuery<ParseObject> q = ParseQuery.getQuery(Constants.CET_CLASS);
        q.orderByAscending(order);
        q.setLimit(limit);
        q.setSkip(skip);
        return q.find();
    }

    @Override
    public List<ParseObject> doSearch() throws ParseException {
        ParseQuery<ParseObject> q = ParseQuery.getQuery(Constants.CET_CLASS);
        q.orderByAscending(order);
        q.setLimit(limit);
        q.setSkip(skip);
        q.whereContainedIn("keywords", Arrays.asList(keywords));
        return q.find();
    }

}
