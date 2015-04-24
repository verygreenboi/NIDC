package ng.codehaven.cdc.models.databases;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ng.codehaven.cdc.fragments.DetailFragment;

public class Items extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "idc";
    public static final String TABLE_NAME = "ITEMS";
    public static final int VERSION = 1;
    public static final String I_ID = "_id";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    public Items(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDb = "create table if not exists " + TABLE_NAME + "" +
                "("
                + I_ID + " text,"
                + DetailFragment.ARG_ID + " text,"
                + DetailFragment.ARG_CET + " text,"
                + DetailFragment.ARG_DESCRIPTION + " text,"
                + DetailFragment.ARG_IMPORT_DUTY + " int(2),"
                + DetailFragment.ARG_LEVY + " int(2),"
                + DetailFragment.ARG_VAT + " int(2),"
                + DetailFragment.ARG_FAV + " int(1))";
        db.execSQL(createDb);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table "+TABLE_NAME);
    }
}
