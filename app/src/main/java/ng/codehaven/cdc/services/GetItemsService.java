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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ng.codehaven.cdc.interfaces.ServiceCallbacks;
import ng.codehaven.cdc.utils.DoServerOperations;
import ng.codehaven.cdc.utils.Logger;

public class GetItemsService extends Service {

    private DoServerOperations mServer;

    private String[] keywords;

    private String order;
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

    public void doGetItems(JSONObject j) {
        new doGetItemsAsyncTask().execute(j);
    }

    public void doSearch(JSONObject s, String[] payload) {
        mPayload = payload;
        new doSearchAsyncTask().execute(s);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    public class LocalBinder extends Binder {
        public GetItemsService getService() {
            return GetItemsService.this;
        }
    }

    private final class doSearchAsyncTask extends AsyncTask<JSONObject, Integer, List<ParseObject>> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
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

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param items The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
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

        /**
         * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
         * {@link #doInBackground(Object[])} has finished.</p>
         * <p/>
         * <p>The default implementation simply invokes {@link #onCancelled()} and
         * ignores the result. If you write your own implementation, do not call
         * <code>super.onCancelled(result)</code>.</p>
         *
         * @param parseObjects The result, if any, computed in
         *                     {@link #doInBackground(Object[])}, can be null
         * @see #cancel(boolean)
         * @see #isCancelled()
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(List<ParseObject> parseObjects) {
            super.onCancelled(parseObjects);
        }
    }

    private final class doGetItemsAsyncTask extends AsyncTask<JSONObject, Integer, List<ParseObject>> {

        @Override
        protected List<ParseObject> doInBackground(JSONObject... params) {
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
