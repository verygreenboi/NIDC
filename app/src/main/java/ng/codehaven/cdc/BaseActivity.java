package ng.codehaven.cdc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import ng.codehaven.cdc.adapters.MenuListAdapter;
import ng.codehaven.cdc.models.ListMenuItem;
import ng.codehaven.cdc.utils.SharedPrefUtil;

public abstract class BaseActivity extends AppCompatActivity implements MenuListAdapter.MenuSelected {
    protected Toolbar mToolbar;
    protected DrawerLayout mDrawerLayout;
    protected RecyclerView mMenuListLayout;
    protected ArrayList<ListMenuItem> mMenuList;
    protected MenuListAdapter mMenuAdapter;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected boolean mDrawerOpened;
    protected boolean mDrawerSeen;

    protected SharedPrefUtil mSharedPrefUtil;

    protected int oldPosition = -1;

    protected abstract boolean isProtected ();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        if (hasMenu() && listMenu() != null) {
            setupMenu(mMenuList);
        }
    }

    protected abstract int getLayoutResource ();

    protected abstract boolean hasMenu ();

    protected abstract ArrayList<ListMenuItem> listMenu ();

    private void setupMenu (ArrayList<ListMenuItem> mMenuList) {
        mMenuAdapter = new MenuListAdapter(mContext(), mMenuList);
//        mMenuAdapter.SetOnMenuSelected(mContext());
    }

    protected abstract Context mContext ();


}
