package ng.codehaven.cdc.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;
import ng.codehaven.cdc.DividerItemDecoration;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.adapters.CetListAdapter;
import ng.codehaven.cdc.interfaces.FragmentIdentity;
import ng.codehaven.cdc.models.Favorite;
import ng.codehaven.cdc.models.Item;
import ng.codehaven.cdc.services.GetItemsService;
import ng.codehaven.cdc.utils.LoopItems;

/**
 * A simple {@link Fragment} subclass.
 */

public class FavoritesFragment extends Fragment implements CetListAdapter.ListHandler {

    public static final int ID = 2;
    public static final String TITLE = "Favorites";
    @InjectView(R.id.cetList)
    protected SuperRecyclerView mRecycler;
    List<Item> i;
    LoopItems lp;
    private FragmentIdentity identity;
    private GetItemsService mService;
    private CetListFragment.bubbleItemUp handler;
    private CetListAdapter mAdapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("List");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Realm realm = Realm.getInstance(getActivity());
        RealmResults<Favorite> r = realm.where(Favorite.class).findAll();

        lp = new LoopItems(getActivity(), r);

        i = lp.getItemsFromFavorite();

        mAdapter = new CetListAdapter(i, getActivity());
        mAdapter.SetOnItemClickListener(FavoritesFragment.this);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.inject(this, v);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);

        mRecycler.addItemDecoration(itemDecoration);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecycler.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        identity.getId(ID);
        identity.getTitle(TITLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        identity = (FragmentIdentity) getActivity();
        handler = (CetListFragment.bubbleItemUp) getActivity();
    }

    @Override
    public void onTitleClick(View v, Item item) {
        handler.sendItemUp(item);
    }

    @Override
    public void onItemLongClick(int position) {
        mAdapter.remove(position);
    }
}
