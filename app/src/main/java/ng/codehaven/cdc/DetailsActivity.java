package ng.codehaven.cdc;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ng.codehaven.cdc.fragments.AddCalculateVariablesDialog;
import ng.codehaven.cdc.fragments.DetailFragment;
import ng.codehaven.cdc.fragments.ResultFragment;
import ng.codehaven.cdc.utils.AdsCreator;
import ng.codehaven.cdc.utils.Calculator;


public class DetailsActivity extends AppCompatActivity implements DetailFragment.doCalculate, AddCalculateVariablesDialog.DialogActions {

    @InjectView(R.id.adView) protected AdView mAdView;
    @InjectView(R.id.cont) protected FrameLayout mContainer;
    @InjectView(R.id.toolbar) protected Toolbar mToolbar;
    @InjectView(R.id.desc) protected TextView mDescView;
    @InjectView(R.id.cetCode) protected TextView mCetView;

    Bundle b, mItem;
    AddCalculateVariablesDialog frag;
    FragmentManager fm = getSupportFragmentManager();


    private String mCet;
    private String mDesc;

    public static ActivityOptions getTransition(Activity activity, View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setTransitionName("item");
            return ActivityOptions.makeSceneTransitionAnimation(activity,
                    v, "item");
        } else {
            return null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Get a Tracker (should auto-report)
        ((App) getApplication()).getTracker(App.TrackerName.APP_TRACKER);

        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        init(b);
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

    private void init(Bundle b) {
        AdsCreator adsCreator = new AdsCreator(this);
        AdRequest adRequest = adsCreator.adRequest();
        mAdView.loadAd(adRequest);

        if (b != null){
            mCet = b.getString(DetailFragment.ARG_CET);
            mDesc = b.getString(DetailFragment.ARG_DESCRIPTION);

            mCetView.setText(mCet);
            mDescView.setText(mDesc);

            mDescView.setTextSize(TypedValue.COMPLEX_UNIT_SP, lengthOfString(mDesc));

        }

        loadFragment(mContainer.getId(), DetailFragment.newInstance(b));
    }

    private float lengthOfString(String mDesc) {
        float defaultSize = 24;
        if (mDesc.length() >= 30 && mDesc.length() < 40){
            defaultSize = 18;
        } else if (mDesc.length() > 40){
            defaultSize = 16;
        }
        return defaultSize;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(int containerId, Fragment fragment) {
        fm.beginTransaction().replace(containerId, fragment).commit();
    }

    @Override
    public void handleCalculateClick(Bundle item) {
        if (item != null){
            doCalculation(item);
        }
    }

    private void doCalculation(Bundle item) {
        mItem = item;

        frag = AddCalculateVariablesDialog.newInstance(getString(R.string.app_name));
        frag.show(fm, "calculate");

    }

    @Override
    public void onCancel() {
        if (frag != null){
            frag.dismiss();
        }
    }

    @Override
    public void onCalculate(double cif, double fob, double rate, boolean isDollars) {
        if (mItem != null){
            mItem.putString("cif", String.valueOf(cif));
            mItem.putString("fob", String.valueOf(fob));
            mItem.putString("xRate", String.valueOf(rate));
            mItem.putBoolean("inDollars", isDollars);
            b = Calculator.getDuties(mItem);

            frag.dismiss();
            FragmentTransaction f = fm.beginTransaction();
            f.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            f.replace(mContainer.getId(), ResultFragment.newInstance(b)).addToBackStack(null).commit();


        }
    }
}
