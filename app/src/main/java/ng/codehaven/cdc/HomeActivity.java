package ng.codehaven.cdc;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import ng.codehaven.cdc.adapters.MenuListAdapter;
import ng.codehaven.cdc.fragments.CalculatorFragment;
import ng.codehaven.cdc.fragments.CetListFragment;
import ng.codehaven.cdc.fragments.DetailFragment;
import ng.codehaven.cdc.fragments.FavoritesFragment;
import ng.codehaven.cdc.fragments.ResultFragment;
import ng.codehaven.cdc.fragments.WebViewFragment;
import ng.codehaven.cdc.interfaces.FragmentIdentity;
import ng.codehaven.cdc.interfaces.OnCarFragmentInteractionListener;
import ng.codehaven.cdc.models.Car;
import ng.codehaven.cdc.models.Item;
import ng.codehaven.cdc.models.ListMenuItem;
import ng.codehaven.cdc.utils.AdsCreator;
import ng.codehaven.cdc.utils.Calculator;
import ng.codehaven.cdc.utils.Logger;
import ng.codehaven.cdc.utils.SharedPrefUtil;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener,
        CetListFragment.bubbleItemUp, DetailFragment.doCalculate,
        MenuListAdapter.MenuSelected, FragmentIdentity,
        CalculatorFragment.doCalculate, OnCarFragmentInteractionListener {

    @InjectView(R.id.container)
    protected FrameLayout mContainer;
    @InjectView(R.id.adView)
    protected AdView mAdView;

    @InjectView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Optional
    @InjectView(R.id.drawerLayout)
    protected DrawerLayout mDrawerLayout;
    @Optional
    @InjectView(R.id.navdrawer_items_list)
    protected RecyclerView mMenuListLayout;
    @Optional
    @InjectView(R.id.left_layout)
    protected FrameLayout mLeftLayout;

    ArrayList<ListMenuItem> mMenuList;
    InputMethodManager manager;
    private FrameLayout mDetailsLayout;
    private MenuListAdapter mMenuAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mDrawerOpened;
    private boolean mDrawerSeen;
    private SharedPrefUtil mSharedPrefUtil;
    private int oldPosition = -1;
    private FragmentManager fm;
    private boolean isLarge;
    private Bundle mCalcResult;
    private int mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isLarge = getResources().getBoolean(R.bool.isLarge);

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mSharedPrefUtil = new SharedPrefUtil(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = mSharedPrefUtil.getSharedPref(Constants.DRAWER_PREF, Context.MODE_PRIVATE);
        mDrawerOpened = sp.getBoolean(Constants.DRAWER_OPEN_PREF, false);
        mDrawerSeen = sp.getBoolean(Constants.DRAWER_SEEN_PREF, false);


        //Get a Tracker (should auto-report)
        ((App) getApplication()).getTracker(App.TrackerName.APP_TRACKER);

        // Setup menu
        String[] mMenuItems = getResources().getStringArray(R.array.menu_list);

        mMenuList = new ArrayList<>();

        mMenuList.add(new ListMenuItem(mMenuItems[0], Constants.MENU_IMGS[0]));
        mMenuList.add(new ListMenuItem(mMenuItems[1], Constants.MENU_IMGS[1]));
        mMenuList.add(new ListMenuItem(mMenuItems[2], Constants.MENU_IMGS[2]));
        mMenuList.add(new ListMenuItem(mMenuItems[3], Constants.MENU_IMGS[3]));
        mMenuList.add(new ListMenuItem(mMenuItems[4], Constants.MENU_IMGS[4]));

        mMenuAdapter = new MenuListAdapter(this, mMenuList);
        mMenuAdapter.SetOnMenuSelected(this);

        setupNavDrawer();

        loadFragment(mContainer.getId(), new CalculatorFragment());

        AdsCreator adsCreator = new AdsCreator(this);
        AdRequest adRequest = adsCreator.adRequest();
        mAdView.loadAd(adRequest);

    }

    private void setupNavDrawer() {
//        mDetailsLayout = (FrameLayout) findViewById(R.id.details_container);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                manager.hideSoftInputFromWindow(mContainer.getWindowToken(), 0);
                mDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                mDrawerOpened = false;
                mDrawerSeen = true;
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mMenuListLayout.setLayoutManager(new LinearLayoutManager(this));
        mMenuListLayout.setAdapter(mMenuAdapter);
    }

    private void loadFragment (int containerId, Fragment fragment) {
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(containerId, fragment).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onStop () {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSharedPrefUtil.getSharedPref(Constants.DRAWER_PREF, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.DRAWER_OPEN_PREF, mDrawerOpened).apply();
        editor.putBoolean(Constants.DRAWER_SEEN_PREF, mDrawerSeen).apply();
    }

    @Override
    protected void onResume () {
        super.onResume();
        if (!mDrawerOpened && !mDrawerSeen) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onStart () {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_cet_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2) {
                    Intent i = new Intent(HomeActivity.this, SearchResultsActivity.class);
                    i.setAction(Intent.ACTION_SEARCH);
                    i.putExtra(SearchManager.QUERY, s);
                    startActivity(i);
                    return false;
                } else {
                    Logger.s(HomeActivity.this, "Search keyword is too short.");
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
    public boolean onSearchRequested () {
        return super.onSearchRequested();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    @Override
    public void sendItemUp(Item item) {
        Bundle b = new Bundle();
        b.putString(DetailFragment.ARG_ID, item.getObjectId());
        b.putString(DetailFragment.ARG_CET, item.getCet());
        b.putString(DetailFragment.ARG_DESCRIPTION, item.getDescription());
        b.putInt(DetailFragment.ARG_IMPORT_DUTY, item.getDuty());
        b.putInt(DetailFragment.ARG_VAT, item.getVat());
        b.putInt(DetailFragment.ARG_LEVY, item.getLevy());

        if (mDetailsLayout != null && mDetailsLayout.getVisibility() == View.VISIBLE) {
            loadFragment(mDetailsLayout.getId(), DetailFragment.newInstance(b));
        } else {
            Intent i = new Intent(this, DetailsActivity.class);
            i.putExtras(b);
            startActivity(i);
        }
    }

    @Override
    public void handleCalculateClick(Bundle item) {

    }

    @Override
    public void itemSelected(int position) {
        String item = mMenuList.get(position).getTitle();
        mDrawerLayout.closeDrawer(mLeftLayout);
        Bundle b = new Bundle();

        fm = getSupportFragmentManager();
        switch (position) {
            case 0:
                fm.beginTransaction().replace(mContainer.getId(), new CalculatorFragment()).addToBackStack(null).commit();
                break;
            case 1:
                fm.beginTransaction().replace(mContainer.getId(), new CetListFragment()).addToBackStack(null).commit();
                break;
            case 2:
                fm.beginTransaction().replace(mContainer.getId(), new FavoritesFragment()).addToBackStack(null).commit();
                break;
            case 3:
                b.putString("url", Constants.PROHIBITION_URL);
                b.putInt("step", 3);
                fm.beginTransaction().replace(mContainer.getId(), WebViewFragment.newInstance(b)).addToBackStack(null).commit();
                break;
            case 4:
                b.putString("url", Constants.CEMA_URL);
                b.putInt("step", 4);
                fm.beginTransaction().replace(mContainer.getId(), WebViewFragment.newInstance(b)).addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void getId(int i) {
        if (oldPosition != -1) {
            mMenuList.get(oldPosition).setIsSelected(false);
        }
        mMenuList.get(i).setIsSelected(true);
        mMenuAdapter.setSelectedItem(i, mMenuList);
        oldPosition = i;

        if (isLarge) {
            switch (i) {
                case 0:
                    // TODO: hide details
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void getTitle(String s) {
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(s);
    }

    @Override
    public void doCalc(Bundle b) {
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(mContainer.getId(), ResultFragment.newInstance(Calculator.calDuties(b))).addToBackStack(null).commit();
    }

    @Override
    public void carListInteraction (List<Car> list, boolean used) {

    }

    @Override
    public void carInteraction (Car car) {

    }
}
