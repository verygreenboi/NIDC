package ng.codehaven.cdc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ng.codehaven.cdc.R;
import ng.codehaven.cdc.models.Item;

public class CetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> items;
    private Context mContext;
    private ListHandler lh;

    private Item mItem;

    public CetListAdapter(List<Item> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cet_item, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindItem((ItemHolder) holder, position);
    }

    private void bindItem(ItemHolder holder, int position) {
        mItem = items.get(position);
        ItemHolder vh = (ItemHolder) holder;
        vh.bindItem(mItem);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (items != null) {
            size = items.size();
        }
        return size;
    }

    public void add(List<Item> mItems) {
        int intDataSize = items.size();
        items.addAll(intDataSize, mItems);
        notifyItemRangeInserted(intDataSize, mItems.size());

    }

    public void SetOnItemClickListener(ListHandler handler) {
        this.lh = handler;
    }

    public void clear() {
        int intDataSize = items.size();
        items.clear();
        notifyItemRangeRemoved(0, intDataSize);
    }

    public void insert(Item i, int position) {
        items.add(position, i);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public interface ListHandler {
        void onTitleClick(View v, Item item);

        void onItemLongClick(int position);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        private TextView mDescription, mCetCode;
        private RelativeLayout mContainer;
        private CheckBox mFav;

        public ItemHolder(View itemView) {
            super(itemView);
            mContainer = (RelativeLayout) itemView.findViewById(R.id.list_container);
            mDescription = (TextView) itemView.findViewById(R.id.desc);
            mCetCode = (TextView) itemView.findViewById(R.id.cetCode);
            mFav = (CheckBox) itemView.findViewById(R.id.fav);

            mContainer.setOnLongClickListener(this);
            mDescription.setOnLongClickListener(this);
            mCetCode.setOnLongClickListener(this);

            mContainer.setOnClickListener(this);
            mCetCode.setOnClickListener(this);
            mDescription.setOnClickListener(this);
        }

        public RelativeLayout getmContainer() {
            return mContainer;
        }

        public TextView getmDescription() {
            return mDescription;
        }

        public TextView getmCetCode() {
            return mCetCode;
        }

        public void bindItem(Item item) {
            mDescription.setText(item.getDescription());
            mCetCode.setText(item.getCet());
            mFav.setChecked(item.isFavorite());
        }


        /**
         * Called when a view has been clicked and held.
         *
         * @param v The view that was clicked and held.
         * @return true if the callback consumed the long click, false otherwise.
         */
        @Override
        public boolean onLongClick(View v) {
            boolean checked = mFav.isChecked();
            int pos = getAdapterPosition();

            Item i = items.get(pos);

            mFav.setChecked(!checked);
            i.setFavorite(!checked);

            lh.onItemLongClick(pos);

            return true;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (mItem != null) {
                int pos = getAdapterPosition();
                lh.onTitleClick(v, items.get(pos));
            }
        }
    }

}
