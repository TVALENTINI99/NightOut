package edu.psu.sweng888.nightout;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class InvoiceDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String mName;
    private String mDate;
    private String mTotal;

    private ActionBar mActionBar;

    private TextView mTextViewName;
    private TextView mTextViewDate;
    private TextView mTextViewTotal;

    private Button mButtonPayInvoice;
    private static PayPalConfiguration configuration=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId("AVGkXe-VuJu0XoJ6ggoImFVva9AuXlMPBDPoQxiM_EbpdlSZkL_i6_B_pi85gP43vel1QxUZc938jugz");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        mActionBar=getSupportActionBar();
        mActionBar.setTitle(R.string.invoice_details);

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

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        startService(intent);

        mButtonPayInvoice.setOnClickListener(this);

    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(mTextViewTotal.getText().toString()),"USD",mTextViewName.getText().toString(),PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent,0);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    finish();

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}
