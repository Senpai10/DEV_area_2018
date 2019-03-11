package com.example.area;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * The type Manage area adapter.
 */
public class ManageAreaAdapter extends RecyclerView.Adapter<ManageAreaAdapter.ManageAreaViewHolder> {
    private ArrayList<ManageAreaItem> mAreaList;
    private OnItemClickListener mListener;

    /**
     * The interface On item click listener.
     */
    public interface OnItemClickListener {
        /**
         * On delete click.
         *
         * @param position the position
         */
        void onDeleteClick(int position);
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
     * The type Manage area view holder.
     */
    public static class ManageAreaViewHolder extends RecyclerView.ViewHolder {
        /**
         * The M image view.
         */
        public ImageView mImageView;
        /**
         * The M text view 1.
         */
        public TextView mTextView1;
        /**
         * The M text view 2.
         */
        public TextView mTextView2;
        /**
         * The M delete image.
         */
        public ImageView mDeleteImage;

        /**
         * Instantiates a new Manage area view holder.
         *
         * @param itemView the item view
         * @param listener the listener
         */
        public ManageAreaViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mDeleteImage = itemView.findViewById(R.id.image_delete);

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    /**
     * Instantiates a new Manage area adapter.
     *
     * @param AreaList the area list
     */
    public ManageAreaAdapter(ArrayList<ManageAreaItem> AreaList) {
        mAreaList = AreaList;
    }

    @Override
    public ManageAreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_area_item, parent, false);
        ManageAreaViewHolder evh = new ManageAreaViewHolder(v, mListener);
        return evh;
    }

    /**
     * Update the RecyclerView.ViewHolder contents with the item at the given position and also sets up some private fields to be used by RecyclerView.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ManageAreaViewHolder holder, int position) {
        ManageAreaItem currentItem = mAreaList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    /**
     * Returns the total number of items in the data set held by the adapter
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mAreaList.size();
    }
}