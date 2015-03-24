package ng.codehaven.cdc.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mrsmith on 11/13/14.
 * Logger utility
 */
public class Logger {
    public static void m(String message) {
        Log.d("EKO-log", message);
    }

    public static void s(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
