package com.pokerbd.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class PokerHandRank extends AppCompatActivity {

    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_poker_hand_rank);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        close = (Button) findViewById(R.id.closehand);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseHandRules();
            }
        });


    }
    public void CloseHandRules(){
        finish();
    }
}