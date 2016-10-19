package ng.codehaven.cdc.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ng.codehaven.cdc.interfaces.ServiceCallbacks;
import ng.codehaven.cdc.utils.DoServerOperations;

public class GetItemsService extends Service {

    private DoServerOperations mServer;

    private String[] keywords;

    private int limit, skip;
    private boolean refresh;
    private String[] mPayload;

    private ServiceCallbacks mCallBack;

    private LocalBinder mLocalBinder = new LocalBinder();

    public void setmCallBack(ServiceCallbacks mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind (Intent intent) {
        return mLocalBinder;
    }

    public void doGetItems(JSONObject j) {
        new doGetItemsAsyncTask().execute(j);
    }

    public void doSearch(JSONObject s, String[] payload) {
        mPayload = payload;
        new doSearchAsyncTask().execute(s);
    }

    public class LocalBinder extends Binder {
        public GetItemsService getService() {
            return GetItemsService.this;
        }
    }

    private final class doSearchAsyncTask extends AsyncTask<JSONObject, Integer, List<ParseObject>> {

        @Override
        protected List<ParseObject> doInBackground(JSONObject... params) {
            limit = 0;
            try {
                skip = params[0].getInt("page");
                refresh = params[0].getBoolean("isRefresh");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            mServer = new DoServerOperations("cet", 20, skip, mPayload);

            List<ParseObject> l = null;

            try {
                l = mServer.doMultiSearch();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return l;
        }

        @Override
        protected void onPostExecute(List<ParseObject> items) {
            if (mCallBack != null) {
                if (!refresh) {
                    if (skip > 0) {
                        mCallBack.onMoreOperationComplete(items);
                    } else {
                        mCallBack.onSearchComplete(items);
                    }
                } else {
                    mCallBack.onRefreshList(items);
                }
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(List<ParseObject> parseObjects) {
            super.onCancelled(parseObjects);
        }
    }

    private final class doGetItemsAsyncTask extends AsyncTask<JSONObject, Integer, List<ParseObject>> {

        @Override
        protected List<ParseObject> doInBackground(JSONObject... params) {
            String order;
            try {
                order = params[0].getString("order");
                limit = params[0].getInt("limit");
                skip = params[0].getInt("skip");
                refresh = params[0].getBoolean("refresh");

            } catch (JSONException e) {
                e.printStackTrace();
                order = null;
                limit = 20;
                skip = 0;
                refresh = false;
            }

            mServer = new DoServerOperations(getApplicationContext(), order, limit, skip);

            List<ParseObject> items = null;

            try {
                items = mServer.getItems();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return items;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<ParseObject> items) {
            if (mCallBack != null) {
                List<ParseObject> l = null;
                if (items != null) {
                    l = items;
                }

                if (!refresh) {
                    if (skip > 0) {
                        mCallBack.onMoreOperationComplete(l);
                    } else {
                        mCallBack.onOperationCompleted(l);
                    }
                } else {
                    mCallBack.onRefreshList(l);
                }
            }
            stopForeground(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (mCallBack != null && values.length > 0) {
                for (Integer value : values) {
                    mCallBack.onOperationProgress(value);
                }
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(List<ParseObject> items) {
            super.onCancelled(items);
        }
    }
}
