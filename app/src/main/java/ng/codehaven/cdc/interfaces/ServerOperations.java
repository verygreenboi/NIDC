package ng.codehaven.cdc.interfaces;

import com.parse.ParseObject;

import java.util.List;

public interface ServerOperations {

    List<ParseObject> getItems () throws Exception;

    List<ParseObject> doMultiSearch () throws Exception;

}
