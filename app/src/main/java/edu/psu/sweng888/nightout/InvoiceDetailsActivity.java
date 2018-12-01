package edu.psu.sweng888.nightout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InvoiceDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String mName;
    private String mDate;
    private String mTotal;

    private TextView mTextViewName;
    private TextView mTextViewDate;
    private TextView mTextViewTotal;

    private Button mButtonPayInvoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        Intent detailIntent=getIntent();
        mName =detailIntent.getStringExtra("INVOICE_NAME");
        mTotal =detailIntent.getStringExtra("INVOICE_TOTAL");
        mDate =detailIntent.getStringExtra("INVOICE_DATE");

        mButtonPayInvoice=findViewById(R.id.btn_pay_invoice);

        mTextViewName =findViewById(R.id.text_view_invoice_det_name);
        mTextViewDate =findViewById(R.id.textView_invoice_det_date_val);
        mTextViewTotal =findViewById(R.id.textView_invoice_det_total_val);

        mTextViewName.setText(mName);
        mTextViewDate.setText(mDate);
        mTextViewTotal.setText(mTotal);

        mButtonPayInvoice.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
