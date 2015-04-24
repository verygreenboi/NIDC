package ng.codehaven.cdc.models.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import ng.codehaven.cdc.models.databases.Items;
import ng.codehaven.cdc.utils.Logger;

public class FavoriteItemsProvider extends ContentProvider {

    private static final String AUTH = "ng.codehaven.idc.models.providers.FavoriteItemsProvider";
    public static final Uri ITEM_URI = Uri.parse("content://" + AUTH + "/" + Items.TABLE_NAME);

    final static int ITEM = 1;

    SQLiteDatabase db;

    Items dbHelper;

    private final static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTH, Items.TABLE_NAME, ITEM);
    }

    public FavoriteItemsProvider() {
    }

    @Override
    public boolean onCreate() {
        dbHelper = new Items(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dbHelper.getWritableDatabase();

        if (uriMatcher.match(uri) == ITEM) {
            db.insert(Items.TABLE_NAME, null, values);
            Logger.m("DONE!");
        }

        db.close();

        getContext().getContentResolver().notifyChange(uri, null);

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor c;
        db = dbHelper.getReadableDatabase();
        c = db.query(Items.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
