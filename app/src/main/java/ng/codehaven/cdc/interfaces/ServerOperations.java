package ng.codehaven.cdc.interfaces;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Thompson on 22/03/2015.
 */
public interface ServerOperations {

    public List<ParseObject> getItems() throws Exception;

    public List<ParseObject> doSearch() throws Exception;

}
