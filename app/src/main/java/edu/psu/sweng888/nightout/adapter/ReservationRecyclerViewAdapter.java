package edu.psu.sweng888.nightout.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.psu.sweng888.nightout.R;
import edu.psu.sweng888.nightout.ReservationDetailsActivity;
import edu.psu.sweng888.nightout.db.models.Reservation;

public class ReservationRecyclerViewAdapter extends RecyclerView.Adapter<ReservationRecyclerViewAdapter.ViewHolder>{

    private Context context;

    // Data input for the RecyclerView
    // For now, you will not handle the images withing the ImageView.
    private ArrayList<Reservation> reservationDataList;

    private static final String TAG = "ResRecyclerViewAdapter";

    public ReservationRecyclerViewAdapter(Context context, ArrayList<Reservation> reservationDataList) {
        this.context = context;
        this.reservationDataList = reservationDataList;
    }

    // Creating inner class as ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        // Elements defined in the UI
        CardView parentLayout;

        TextView textViewRestaurantName;
        TextView textViewReservationTime;
        TextView textViewReservationDate;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.reservationCardView);
            textViewRestaurantName = itemView.findViewById(R.id.txt_view_restauraunt_name);
            textViewReservationTime = itemView.findViewById(R.id.txt_view_reservation_time);
            textViewReservationDate =  itemView.findViewById(R.id.txt_view_reservation_date);

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),ReservationDetailsActivity.class);
                    intent.putExtra("RES_NAME",textViewRestaurantName.getText());
                    intent.putExtra("RES_TIME",textViewReservationTime.getText());
                    intent.putExtra("RES_DATE",textViewReservationDate.getText());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    // Methods inherited from ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // Implement to logic for inflating the view based on the customized layout.
        // This will be the logic for pretty much all the RecycerView adapters
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_view_items, parent, false);

        // Set the view to be represented into the ViewHolder object.
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // The method implementaiton might change based on the root layout.
    // Everytime a new item is inflated on the view it will be called.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Log.d(TAG, "onBindViewHolder has been called");
        Reservation reservationData = reservationDataList.get(position);

        viewHolder.textViewRestaurantName.setText(reservationData.getLocationName());
        viewHolder.textViewReservationTime.setText(reservationData.getTime());
        viewHolder.textViewReservationDate.setText(reservationData.getDate());

        //Toast.makeText(context, reservationData.getLocation().toString(), Toast.LENGTH_SHORT );
    }

    @Override
    public int getItemCount() {
        return reservationDataList.size();
    }
}
