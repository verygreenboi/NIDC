package ng.codehaven.cdc.fragments;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import ng.codehaven.cdc.Constants;
import ng.codehaven.cdc.DividerItemDecoration;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.adapters.CetListAdapter;
import ng.codehaven.cdc.interfaces.FragmentIdentity;
import ng.codehaven.cdc.interfaces.ServiceCallbacks;
import ng.codehaven.cdc.models.Item;
import ng.codehaven.cdc.services.GetItemsService;
import ng.codehaven.cdc.utils.LoopItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class CetListFragment extends Fragment implements CetListAdapter.ListHandler,
        SwipeRefreshLayout.OnRefreshListener, ServiceCallbacks, ServiceConnection {


    public static final int ID = 1;
    public static final String TITLE = "TARIFF LIST";
    private SuperRecyclerView mRecycler;
    private CetListAdapter mAdapter;
    private GetItemsService mService;
    private bubbleItemUp handler;
    private LoopItems lp;
    private FragmentIdentity identity;

    public CetListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_cet_list, container, false);

        mRecycler = (SuperRecyclerView) v.findViewById(R.id.cetList);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);

        mRecycler.addItemDecoration(itemDecoration);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent bindIntent = new Intent(getActivity(), GetItemsService.class);
        getActivity().bindService(bindIntent, this, Context.BIND_AUTO_CREATE);

        mRecycler.setRefreshListener(CetListFragment.this);
        mRecycler.setRefreshingColorResources(R.color.refresh_1, R.color.primary, R.color.primaryDark, R.color.primaryAlt);

        mRecycler.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                if (mService != null) {
                    mService.doGetItems(getJsonObject("cet", 20, mAdapter.getItemCount(), false));
                }
            }
        }, 10);

        identity.getId(ID);
        identity.getTitle(TITLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mService != null) {
            mService.setmCallBack(null);
            getActivity().unbindService(this);
        }
    }

    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(android.os.Bundle)} will be called after this.
     *
     * @param activity Attached activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        handler = (bubbleItemUp) getActivity();
        identity = (FragmentIdentity) getActivity();
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

    @Override
    public void onTitleClick(View v, Item item) {

        handler.sendItemUp(item);

    }

    @Override
    public void onItemLongClick(int position) {

    }


    @Override
    public void onRefresh() {
        if (mService != null) {
            mService.doGetItems(getJsonObject("cet", Constants.INIT_LIMIT, Constants.INIT_SKIP, true));
        }
    }

    @Override
    public void onOperationProgress(int progress) {

    }

    @Override
    public void onOperationCompleted(List<ParseObject> items) {

        lp = new LoopItems(getActivity(), items);

        mAdapter = new CetListAdapter(lp.getItems(), getActivity());

        mAdapter.SetOnItemClickListener(CetListFragment.this);

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onMoreOperationComplete(List<ParseObject> items) {

        lp = new LoopItems(getActivity(), items);

        mAdapter.add(lp.getItems());
        mRecycler.hideMoreProgress();
    }

    @Override
    public void onRefreshList(List<ParseObject> items) {
        if (items != null) {
            mAdapter.clear();
            lp = new LoopItems(getActivity(), items);
            mAdapter.add(lp.getItems());
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

    public interface bubbleItemUp {
        void sendItemUp(Item item);
    }
}
