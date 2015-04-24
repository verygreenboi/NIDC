package ng.codehaven.cdc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmResults;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.models.Favorite;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {


    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Realm realm = Realm.getInstance(getActivity());
        RealmResults<Favorite> r = realm.where(Favorite.class).findAll();



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }


}
