package ng.codehaven.cdc;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ng.codehaven.cdc.adapters.CetListAdapter;
import ng.codehaven.cdc.fragments.DetailFragment;
import ng.codehaven.cdc.interfaces.ServiceCallbacks;
import ng.codehaven.cdc.models.Item;
import ng.codehaven.cdc.services.GetItemsService;
import ng.codehaven.cdc.utils.AdsCreator;
import ng.codehaven.cdc.utils.LoopItems;

public class SearchResultsActivity extends ActionBarActivity implements
        CetListAdapter.ListHandler, ServiceCallbacks, ServiceConnection, SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.cetList)
    protected SuperRecyclerView mRecyclerView;
    @InjectView(R.id.toolbar)
    protected Toolbar mToolbar;
    @InjectView(R.id.adView)
    protected AdView mAdView;
    private String[] i;
    private GetItemsService mService;
    private CetListAdapter mAdapter;
    private LoopItems lp;
    private JSONObject searchObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());
        i = getIntent().getStringExtra(SearchManager.QUERY).split(" ");

        searchObject = getJSONObject(0, false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setRefreshListener(this);
        mRecyclerView.setRefreshingColorResources(R.color.refresh_1, R.color.primary, R.color.primaryDark, R.color.primaryAlt);

        AdsCreator adsCreator = new AdsCreator(this);
        AdRequest adRequest = adsCreator.adRequest();
        mAdView.loadAd(adRequest);

    }

    private JSONObject getJSONObject(int skip, boolean isRefreshing) {
        JSONObject jj = new JSONObject();
        try {
            jj.put("skip", skip);
            jj.put("isRefresh", isRefreshing);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jj;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent bindIntent = new Intent(this, GetItemsService.class);
        bindService(bindIntent, this, BIND_AUTO_CREATE);

        mRecyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                if (mService != null){
                    mService.doSearch(getJSONObject(mAdapter.getItemCount(),false), i);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mService != null) {
            mService.setmCallBack(null);
            unbindService(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOperationProgress(int progress) {

    }

    @Override
    public void onOperationCompleted(List<ParseObject> items) {

    }

    @Override
    public void onMoreOperationComplete(List<ParseObject> items) {
        lp = new LoopItems(this, items);
        mAdapter.add(lp.getItems());
        mRecyclerView.hideMoreProgress();
    }

    @Override
    public void onRefreshList(List<ParseObject> items) {
        if (items != null) {
            mAdapter.clear();
            lp = new LoopItems(this, items);
            mAdapter.add(lp.getItems());
        }
    }

    @Override
    public void onSearchComplete(List<ParseObject> items) {
        lp = new LoopItems(SearchResultsActivity.this, items);

        mAdapter = new CetListAdapter(lp.getItems(), SearchResultsActivity.this);
        mAdapter.SetOnItemClickListener(SearchResultsActivity.this);

        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Called when a connection to the Service has been established, with
     * the {@link IBinder} of the communication channel to the
     * Service.
     *
     * @param name    The concrete component name of the service that has
     *                been connected.
     * @param service The IBinder of the Service's communication channel,
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((GetItemsService.LocalBinder) service).getService();
        mService.setmCallBack(this);
        if (mService != null) {
            mService.doSearch(searchObject, i);
        }
    }

    /**
     * Called when a connection to the Service has been lost.  This typically
     * happens when the process hosting the service has crashed or been killed.
     * This does <em>not</em> remove the ServiceConnection itself -- this
     * binding to the service will remain active, and you will receive a call
     * to {@link #onServiceConnected} when the Service is next running.
     *
     * @param name The concrete component name of the service whose
     *             connection has been lost.
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onTitleClick(View v, Item item) {
        Bundle b = new Bundle();
        b.putString(DetailFragment.ARG_ID, item.getObjectId());
        b.putString(DetailFragment.ARG_CET, item.getCet());
        b.putString(DetailFragment.ARG_DESCRIPTION, item.getDescription());
        b.putInt(DetailFragment.ARG_IMPORT_DUTY, item.getDuty());
        b.putInt(DetailFragment.ARG_VAT, item.getVat());
        b.putInt(DetailFragment.ARG_LEVY, item.getLevy());

        //TODO: Implement tablet UI
        Intent i = new Intent(this, DetailsActivity.class);
        i.putExtras(b);
        startActivity(i);

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onRefresh() {
        if (mService!=null){
            mService.doSearch(getJSONObject(mAdapter.getItemCount(),true), i);
        }
    }
}
