package com.pokerbd.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PaymentOption extends AppCompatActivity {

    ImageView bkashbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);

        bkashbtn = (ImageView) findViewById(R.id.bkashbtn);

        bkashbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBkashActivity();
            }
        });
    }

    private void goBkashActivity() {
        Intent intent = new Intent(this, BkashActivity.class);
        startActivity(intent);
        finish();
    }
}