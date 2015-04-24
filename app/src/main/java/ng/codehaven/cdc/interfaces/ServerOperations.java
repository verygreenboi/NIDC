package ng.codehaven.cdc.interfaces;

import com.parse.ParseObject;

import java.util.List;

public interface ServerOperations {

    public List<ParseObject> getItems() throws Exception;

    public List<ParseObject> doMultiSearch() throws Exception;

}
