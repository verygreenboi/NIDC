package ng.codehaven.cdc.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import ng.codehaven.cdc.fragments.DetailFragment;
import ng.codehaven.cdc.models.databases.Items;
import ng.codehaven.cdc.models.providers.FavoriteItemsProvider;
import ng.codehaven.cdc.utils.Logger;


public class Item {
    private String cet, description, objectId;
    private int levy, vat, duty;
    private Date createdAt;
    private Context mContext;
    public static final String FAV_KEY = "ng.codehaven.idc.FAVORITE_PREFERENCE";

    private Realm realm;

    Items dbHelper;
    SQLiteDatabase db;

    String[] from = {
            DetailFragment.ARG_ID,
            DetailFragment.ARG_CET,
            DetailFragment.ARG_DESCRIPTION,
            DetailFragment.ARG_IMPORT_DUTY,
            DetailFragment.ARG_LEVY,
            DetailFragment.ARG_VAT
    };

    // Defines a string to contain the selection clause
    String mSelectionClause = null;

    public Item(Context context, String cet, String description, String objectId, int levy, int vat, int duty, Date createdAt) {
        this.mContext = context;
        this.cet = cet;
        this.description = description;
        this.objectId = objectId;
        this.levy = levy;
        this.vat = vat;
        this.duty = duty;
        this.createdAt = createdAt;
        this.dbHelper = new Items(mContext);
        this.db = dbHelper.getWritableDatabase();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFavorite() {
        return sp(mContext, FAV_KEY).getBoolean(getObjectId(), false);
    }

    public void setFavorite(boolean isFavorite) {
        realm = Realm.getInstance(mContext);

        if (isFavorite) {
            createRealm(realm);
        } else {
            RealmResults<Favorite> r = realm.where(Favorite.class).equalTo("objectId", getObjectId()).findAll();
            deleteRealm(r);
        }

        SharedPreferences.Editor editor = sp(mContext, FAV_KEY).edit();
        editor.putBoolean(getObjectId(), isFavorite).apply();
    }

    private void deleteRealm(RealmResults<Favorite> r) {
        realm.beginTransaction();
            r.remove(0);
        realm.commitTransaction();
    }

    private void createRealm(Realm realm) {
        realm.beginTransaction();

        Favorite favorite = realm.createObject(Favorite.class);

        favorite.setCet(getCet());
        favorite.setDescription(getDescription());
        favorite.setDuty(getDuty());
        favorite.setLevy(getLevy());
        favorite.setObjectId(getObjectId());

        realm.commitTransaction();
    }

    public String getCet() {
        return cet;
    }

    public void setCet(String cet) {
        this.cet = cet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getLevy() {
        return levy;
    }

    public void setLevy(int levy) {
        this.levy = levy;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public int getDuty() {
        return duty;
    }

    public void setDuty(int duty) {
        this.duty = duty;
    }

    /**
     * Compares this instance with the specified object and indicates if they
     * are equal. In order to be equal, {@code o} must represent the same object
     * as this instance using a class-specific comparison. The general contract
     * is that this comparison should be reflexive, symmetric, and transitive.
     * Also, no object reference other than null is equal to null.
     * <p/>
     * <p>The default implementation returns {@code true} only if {@code this ==
     * o}. See <a href="{@docRoot}reference/java/lang/Object.html#writing_equals">Writing a correct
     * {@code equals} method</a>
     * if you intend implementing your own {@code equals} method.
     * <p/>
     * <p>The general contract for the {@code equals} and {@link
     * #hashCode()} methods is that if {@code equals} returns {@code true} for
     * any two objects, then {@code hashCode()} must return the same value for
     * these objects. This means that subclasses of {@code Object} usually
     * override either both methods or neither of them.
     *
     * @param o the object to compare this instance with.
     * @return {@code true} if the specified object is equal to this {@code
     * Object}; {@code false} otherwise.
     * @see #hashCode
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;
        return objectId.equals(item.objectId) && description.equals(item.description);
    }

    /**
     * Returns an integer hash code for this object. By contract, any two
     * objects for which {@link #equals} returns {@code true} must return
     * the same hash code value. This means that subclasses of {@code Object}
     * usually override both methods or neither method.
     * <p/>
     * <p>Note that hash values must not change over time unless information used in equals
     * comparisons also changes.
     * <p/>
     * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_hashCode">Writing a correct
     * {@code hashCode} method</a>
     * if you intend implementing your own {@code hashCode} method.
     *
     * @return this object's hash code.
     * @see #equals
     */
    @Override
    public int hashCode() {
        int result = objectId.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }

    /**
     * Returns a string containing a concise, human-readable description of this
     * object. Subclasses are encouraged to override this method and provide an
     * implementation that takes into account the object's type and data. The
     * default implementation is equivalent to the following expression:
     * <pre>
     *   getClass().getName() + '@' + Integer.toHexString(hashCode())</pre>
     * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_toString">Writing a useful
     * {@code toString} method</a>
     * if you intend implementing your own {@code toString} method.
     *
     * @return a printable representation of this object.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    private SharedPreferences sp(Context c, String key) {
        return c.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    private Cursor getCusor(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private void insertToDb(Uri uri, ContentValues cv) {
        mContext.getContentResolver().insert(uri, cv);
    }

}
