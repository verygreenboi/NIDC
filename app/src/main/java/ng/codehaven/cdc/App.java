package ng.codehaven.cdc;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.parse.ParseInstallation;

import java.util.HashMap;


public class App extends Application {

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-61338222-2";
    private static final String[] BRANDS = {"toyota", "honda"};
    private static final String[] TOYOTA_MODELS = {};
    private static final String[] HONDA_MODELS = {};
    private static final String TAG = "Volley Request";
    public static int GENERAL_TRACKER = 0;
    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static App sInstance;
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    public static String[] getModelArray (CharSequence text) {
        int index = -1;
        String[] model;
        for (int i = 0; i < BRANDS.length; i++) {
            if (TextUtils.equals(text, BRANDS[i])) {
                index = i;
            }
        }
        switch (index) {
            case 0:
                model = TOYOTA_MODELS;
                break;
            case 1:
                model = HONDA_MODELS;
                break;
            default:
                model = new String[0];
                break;
        }
        return model;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, Constants.APPLICATION_ID, Constants.APPLICATION_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // initialize the singleton
        sInstance = this;
    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.app_tracker)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue () {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }



}
