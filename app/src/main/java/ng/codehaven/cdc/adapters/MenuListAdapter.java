package ng.codehaven.cdc.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ng.codehaven.cdc.Constants;
import ng.codehaven.cdc.R;
import ng.codehaven.cdc.models.ListMenuItem;
import ng.codehaven.cdc.utils.Logger;

public class MenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context c;
    private ArrayList<ListMenuItem> mMenu;
    private int selectedItem = 0;

    private MenuSelected handler;

    public MenuListAdapter(Context c, ArrayList<ListMenuItem> menu) {
        this.c = c;
        this.mMenu = menu;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.nav_item_layout, parent, false);
        return new MenuItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MenuItemHolder mih = (MenuItemHolder) holder;
        mih.bindItem(mMenu.get(position));
    }

    @Override
    public int getItemCount() {
        return mMenu.size();
    }

    public void setSelectedItem(int selectedItem, ArrayList<ListMenuItem> data) {
        this.selectedItem = selectedItem;
        Logger.m(String.valueOf(selectedItem));
        mMenu = data;
        notifyDataSetChanged();
    }

    public void SetOnMenuSelected(MenuSelected handler) {
        this.handler = handler;
    }

    public interface MenuSelected {
        void itemSelected(int position);
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.icon)
        protected ImageView mIcon;

        @InjectView(R.id.title)
        protected TextView mTitle;

        public MenuItemHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            handler.itemSelected(getAdapterPosition());
        }

        public void bindItem(ListMenuItem mItem) {

            Typeface tf1 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Bold.ttf");
            Typeface tf2 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Light.ttf");

            mTitle.setText(mItem.getTitle());
            if (mItem.isSelected()) {
                mTitle.setTypeface(tf1);
                mTitle.setTextColor(c.getResources().getColor(R.color.primary));
            } else {
                mTitle.setTypeface(tf2);
                mTitle.setTextColor(c.getResources().getColor(R.color.abc_primary_text_material_dark));
            }


            mIcon.setImageResource(mItem.getIcon());
        }

    }
}
