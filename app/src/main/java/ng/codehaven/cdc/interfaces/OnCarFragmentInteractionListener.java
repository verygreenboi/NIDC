package ng.codehaven.cdc.interfaces;

import java.util.List;

import ng.codehaven.cdc.models.Car;

public interface OnCarFragmentInteractionListener {
    void carListInteraction (List<Car> list, boolean used);

    void carInteraction (Car car);
}
