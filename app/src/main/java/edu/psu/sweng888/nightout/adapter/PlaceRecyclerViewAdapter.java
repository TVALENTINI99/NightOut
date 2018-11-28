package edu.psu.sweng888.nightout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import edu.psu.sweng888.nightout.R;

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Bitmap> bitmapList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView placeImageView;

        public MyViewHolder(View view) {
            super(view);

            placeImageView = view.findViewById(R.id.horizontal_item_view_image);

        }
    }

    public PlaceRecyclerViewAdapter(Context context, ArrayList<Bitmap> bitmapList) {
        this.context = context;
        this.bitmapList = bitmapList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_images, parent, false);

        if (itemView.getLayoutParams ().width == RecyclerView.LayoutParams.MATCH_PARENT)
            itemView.getLayoutParams ().width = RecyclerView.LayoutParams.WRAP_CONTENT;

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.placeImageView.setImageBitmap(bitmapList.get(position));

    }


    @Override
    public int getItemCount() {
        return bitmapList.size();
    }
}
