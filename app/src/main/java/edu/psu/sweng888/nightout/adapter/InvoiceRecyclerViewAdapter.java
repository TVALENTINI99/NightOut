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
import android.widget.TextView;

import java.util.ArrayList;

import edu.psu.sweng888.nightout.InvoiceDetailsActivity;
import edu.psu.sweng888.nightout.R;
import edu.psu.sweng888.nightout.db.models.Invoice;


public class InvoiceRecyclerViewAdapter extends RecyclerView.Adapter<InvoiceRecyclerViewAdapter.ViewHolder>{

    private Context context;

    // Data input for the RecyclerView
    // For now, you will not handle the images withing the ImageView.
    private ArrayList<Invoice> invoiceDataList;

    private static final String TAG = "ResRecyclerViewAdapter";

    public InvoiceRecyclerViewAdapter(Context context, ArrayList<Invoice> invoiceDataList) {
        this.context = context;
        this.invoiceDataList = invoiceDataList;
    }

    // Creating inner class as ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        // Elements defined in the UI
        CardView parentLayout;

        TextView textViewInvoiceName;
        TextView textViewInvoiceTotal;
        TextView textViewInvoiceDate;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.invoiceCardView);
            textViewInvoiceName = itemView.findViewById(R.id.txt_view_invoice_name);
            textViewInvoiceTotal = itemView.findViewById(R.id.txt_view_invoice_total);
            textViewInvoiceDate =  itemView.findViewById(R.id.txt_view_invoice_date);

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),InvoiceDetailsActivity.class);
                    intent.putExtra("INVOICE_NAME", textViewInvoiceName.getText());
                    intent.putExtra("INVOICE_TOTAL", textViewInvoiceTotal.getText());
                    intent.putExtra("INVOICE_DATE", textViewInvoiceDate.getText());
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
                .inflate(R.layout.invoice_view_items, parent, false);

        // Set the view to be represented into the ViewHolder object.
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // The method implementaiton might change based on the root layout.
    // Everytime a new item is inflated on the view it will be called.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Log.d(TAG, "onBindViewHolder has been called");
        Invoice invoiceData = invoiceDataList.get(position);


        viewHolder.textViewInvoiceName.setText(invoiceData.getLocationName());
        viewHolder.textViewInvoiceTotal.setText(invoiceData.getTotal());
        viewHolder.textViewInvoiceDate.setText(invoiceData.getDate());
    }

    @Override
    public int getItemCount() {
        return invoiceDataList.size();
    }
}
