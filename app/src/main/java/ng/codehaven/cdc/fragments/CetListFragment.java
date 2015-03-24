package ng.codehaven.cdc.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ng.codehaven.cdc.R;
import ng.codehaven.cdc.adapters.CetListAdapter;
import ng.codehaven.cdc.interfaces.ServiceCallbacks;
import ng.codehaven.cdc.models.Item;
import ng.codehaven.cdc.services.GetItemsService;
import ng.codehaven.cdc.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class CetListFragment extends Fragment implements CetListAdapter.ListHandler,
        SwipeRefreshLayout.OnRefreshListener, ServiceCallbacks, ServiceConnection {


    private SuperRecyclerView mRecycler;
    private CetListAdapter mAdapter;
    private List<ParseObject> mItems;

    private GetItemsService mService;

    private List<Item> i;

    public CetListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            i = savedInstanceState.getParcelableArrayList("items");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_cet_list, container, false);

        mRecycler = (SuperRecyclerView) v.findViewById(R.id.cetList);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent bindIntent = new Intent(getActivity(), GetItemsService.class);
        getActivity().bindService(bindIntent, this, Context.BIND_AUTO_CREATE);

        mRecycler.setRefreshListener(CetListFragment.this);
        mRecycler.setRefreshingColorResources(R.color.refresh_1, R.color.refresh_2, R.color.refresh_3, R.color.refresh_4);

        mRecycler.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                if (mService != null) {
                    mService.doGetItems(getJsonObject("cet", 20, mAdapter.getItemCount(), false));
                }
            }
        }, 10);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mService != null) {
            mService.setmCallBack(null);
            getActivity().unbindService(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_cet_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(android.os.Bundle)},
     * {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}, and
     * {@link #onActivityCreated(android.os.Bundle)}.
     * <p/>
     * <p>This corresponds to {@link android.app.Activity#onSaveInstanceState(android.os.Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (i != null)
            outState.putParcelableArrayList("items", (ArrayList<? extends android.os.Parcelable>) i);
    }

    @Override
    public void onTitleClick(View v, int position, Item item) {
        int id = v.getId();
        if (id == R.id.desc || id == R.id.cetCode) {
//            Bundle b = new Bundle();
//            b.putString("id", item.getObjectId());
//            b.putString("cet", item.getString("cet"));
//            b.putString("cet", item.getString("description"));
//            b.putInt("import_duty", item.getInt("import_duty"));
//            b.putInt("vat", item.getInt("vat"));
//            b.putInt("levy", item.getInt("levy"));
//
//            Logger.m(b.toString());
        } else {
            Logger.s(getActivity(), "Container");
        }
    }


    @Override
    public void onRefresh() {
        if (mService != null) {
            mService.doGetItems(getJsonObject("cet", 20, 0, true));
        }
    }

    @Override
    public void onOperationProgress(int progress) {

    }

    @Override
    public void onOperationCompleted(List<ParseObject> items) {

        mItems = items;
        mAdapter = new CetListAdapter(loopItems(items), getActivity());

        mAdapter.SetOnItemClickListener(CetListFragment.this);

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onMoreOperationComplete(List<ParseObject> items) {

        mAdapter.add(loopItems(items));
    }

    @Override
    public void onRefreshList(List<ParseObject> items) {
        if (items != null) {
            mAdapter.clear();
            mAdapter.add(loopItems(items));
        }
    }

    @Override
    public void onSearchComplete(List<ParseObject> items) {

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((GetItemsService.LocalBinder) service).getService();
        mService.setmCallBack(this);
        if (mService != null) {
            mService.doGetItems(getJsonObject("cet", 20, 0, false));
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private JSONObject getJsonObject(String order, int limit, int skip, boolean refresh) {
        JSONObject j = new JSONObject();
        try {
            j.put("order", order);
            j.put("limit", limit);
            j.put("skip", skip);
            j.put("refresh", refresh);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

    private List<Item> loopItems(List<ParseObject> items) {
        i = new ArrayList<>();
        if (items != null) {
            for (ParseObject p : items) {
                i.add(new Item(
                        p.getString("cet"),
                        p.getString("description"),
                        p.getObjectId(),
                        p.getInt("levy"),
                        p.getInt("vat"),
                        p.getInt("duty")));
            }
        }
        return i;
    }
}
