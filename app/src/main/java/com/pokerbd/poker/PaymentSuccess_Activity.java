package com.pokerbd.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.pokerbd.poker.R;

import org.json.JSONException;

public class PaymentSuccess_Activity extends AppCompatActivity {
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        tvResult = findViewById(R.id.tvResult);

        if (getIntent().getExtras() == null) {
            tvResult.setText("Failed to get data from bkash");
            return;
        }
        else {
            tvResult.setText(
                    "TransactionID= " + getIntent().getExtras().getString("TRANSACTION_ID") + " \n\n" +
                            "PaidAmount= " + getIntent().getExtras().getString("PAID_AMOUNT") + " \n\n" +
                            "OtherData= " + getIntent().getExtras().getString("PAYMENT_SERIALIZE") + " \n\n"
            );
        }
    }


    private void goPurchase(){
        Intent intent = new Intent(this, CoinPurchase.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goPurchase();
    }
}
