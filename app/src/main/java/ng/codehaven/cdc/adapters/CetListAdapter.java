package ng.codehaven.cdc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ng.codehaven.cdc.R;
import ng.codehaven.cdc.models.Item;

/**
 * Created by Thompson on 05/03/2015.
 */
public class CetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public static final int TYPE_ITEM = 1;
    private List<Item> items;
    private Context mContext;
    private String mDesc, mCet;
    private int MIN_ITEMS = 1;
    private ListHandler lh;

    public CetListAdapter(List<Item> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cet_item, parent, false);
        ItemHolder ih = new ItemHolder(v);
        ih.getmDescription().setOnClickListener(this);
        ih.getmContainer().setOnClickListener(this);
        ih.getmCetCode().setOnClickListener(this);

        return ih;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindItem((ItemHolder) holder, position);
    }

    private void bindItem(ItemHolder holder, int position) {
        mDesc = items.get(position).getDescription();
        mCet = items.get(position).getCet();

        ItemHolder vh = (ItemHolder) holder;

        vh.mDescription.setText(mDesc);
        vh.mCetCode.setText(mCet);

        vh.getmDescription().setTag(position);
        vh.getmCetCode().setTag(position);
        vh.getmContainer().setTag(position);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (items != null) {
            size = items.size();
        }
        return size;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Item i = items.get(position);
        lh.onTitleClick(v, position, i);
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

    public interface ListHandler {
        public void onTitleClick(View v, int position, Item item);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private TextView mDescription, mCetCode;
        private RelativeLayout mContainer;

        public ItemHolder(View itemView) {
            super(itemView);

            mContainer = (RelativeLayout) itemView.findViewById(R.id.list_container);
            mDescription = (TextView) itemView.findViewById(R.id.desc);
            mCetCode = (TextView) itemView.findViewById(R.id.cetCode);
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
    }

}
