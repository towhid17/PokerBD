package com.pokerbd.poker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

public class CoinWithdraw extends AppCompatActivity {

    Button btnSellClose, btnTaka;
    TextView withdrawcoins, BarStart, BarEnd, chips, taka;
    Slider TakaSlider;
    private float seekbarValue = 0;
    private long amtchips = 0;
    private double amttk = 0;

    Thread cwt;
    public static boolean iscwt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_coin_withdraw);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        withdrawcoins = (TextView) findViewById(R.id.withdrawcoins);
        withdrawcoins.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getCurrentCoin()));
        btnSellClose = (Button) findViewById(R.id.btnCloseSell);
        BarStart = (TextView) findViewById(R.id.BarStart);
        BarEnd = (TextView) findViewById(R.id.BarEnd);
        taka = (TextView) findViewById(R.id.taka);
        chips = (TextView) findViewById(R.id.chips);
        TakaSlider = (Slider) findViewById(R.id.TakaSlider);
        btnTaka = (Button) findViewById(R.id.btnTaka);

        long startvalue = ClientToServer.minCoinWithdraw*10000000;
        long step = 10000000;
        chips.setText("Buy in: " + BoardChoosing.modifyChipsString(startvalue));
        double v11 = Math.round(startvalue/10000000);
        v11 = v11*ClientToServer.coinPricePerCrore;
        taka.setText("Amount: "+v11+" TK");

        amttk = v11;
        amtchips = startvalue;

        TakaSlider.setValueFrom(startvalue);
        long v = ClientToServer.user.getCurrentCoin();
        if(v<(ClientToServer.minCoinWithdraw*10000000+10000000)) v=ClientToServer.minCoinWithdraw*10000000+10000000;
        else {
            v = v / (step);
            v = Math.round(v);
            v = v * (step);
        }
        TakaSlider.setValueTo(v);
        TakaSlider.setValue(startvalue);
        TakaSlider.setStepSize(step);
        BarStart.setText(BoardChoosing.modifyChipsString(startvalue));
        BarEnd.setText(BoardChoosing.modifyChipsString(v));
        seekbarValue = startvalue;

        iscwt = true;

        cwt = new Thread(){
            @Override
            public void run() {
                while (iscwt) {
                    // System.out.println("in HostThread");
                    if (ClientToServer.isForceLogOut) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goLogin();
                            }
                        });
                        ClientToServer.isForceLogOut = false;
                    }
                }

            }
        };
        cwt.start();

        TakaSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = TakaSlider.getValue();
                chips.setText("Buy in: "+BoardChoosing.modifyChipsString(seekbarValue));
                double v = Math.round(seekbarValue/10000000);
                v = v*ClientToServer.coinPricePerCrore;
                amttk = v;
                amtchips = Math.round(seekbarValue);
                taka.setText("Amount: "+v+" TK");
            }
        });


        btnTaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seekbarValue>ClientToServer.user.getCurrentCoin()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Not enough CHIPS",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    goCashIn();
                }
            }
        });

        btnSellClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
    }

    private void goHome(){
        HomeScreen.isHomeThread = true;
        iscwt = false;
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void goCashIn(){
        CashIn.amountChips = amtchips;
        CashIn.amountTK = amttk;
        iscwt = false;
        Intent intent = new Intent(this, CashIn.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        Login.isLoginThread = true;
        iscwt = false;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goHome();
    }
}