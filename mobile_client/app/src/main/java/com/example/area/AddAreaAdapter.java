package com.example.area;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The type Add area adapter.
 */
public class AddAreaAdapter extends RecyclerView.Adapter<AddAreaAdapter.AddAreaViewHolder> {
    private ArrayList<AddAreaItem> mAreaList;
    private AddAreaAdapter.OnItemClickListener mListener;

    /**
     * The interface On item click listener.
     */
    public interface OnItemClickListener {
        /**
         * Add click.
         *
         * @param position the position
         */
        void addClick(int position);
    }

    /**
     * Sets on item click listener.
     *
     * @param listener the listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * The type Add area view holder.
     */
    public static class AddAreaViewHolder extends RecyclerView.ViewHolder {
        /**
         * The M text view 1.
         */
        public TextView mTextView1;
        /**
         * The M text view 2.
         */
        public TextView mTextView2;
        /**
         * The Mimage view.
         */
        public ImageView mimageView;

        /**
         * Instantiates a new Add area view holder.
         *
         * @param itemView the item view
         * @param listener the listener
         */
        public AddAreaViewHolder(View itemView, final AddAreaAdapter.OnItemClickListener listener) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mimageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.addClick(position);
                        }
                    }
                }
            });
        }
    }

    /**
     * Instantiates a new Add area adapter.
     *
     * @param AreaList the area list
     */
    public AddAreaAdapter(ArrayList<AddAreaItem> AreaList) {
        mAreaList = AreaList;
    }

    @Override
    public AddAreaAdapter.AddAreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_area_item, parent, false);
        AddAreaAdapter.AddAreaViewHolder evh = new AddAreaAdapter.AddAreaViewHolder(v, mListener);
        return evh;
    }

    /**
     * Update the RecyclerView.ViewHolder contents with the item at the given position and also sets up some private fields to be used by RecyclerView.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(AddAreaAdapter.AddAreaViewHolder holder, int position) {
        AddAreaItem currentItem = mAreaList.get(position);
        holder.mTextView1.setText(currentItem.getName());
        holder.mTextView2.setText(currentItem.getDescription());
        holder.mimageView.setImageResource(currentItem.getImageRessource());
    }

    /**
     * Returns the total number of items in the data set held by the adapter
     * @return
     */
    @Override
    public int getItemCount() {
        return mAreaList.size();
    }
}
