package com.pokerbd.poker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import org.json.JSONException;


public class playwithfriend extends AppCompatActivity {

    ImageView btnBack;
    ImageView btnHost;
    ImageView btnJoin;

    public static boolean inPlayWithFriends = false;

    private boolean shouldOpneBuyIn = false;

    //buy in pop up
    RelativeLayout BuyInPopUp;
    TextView tH;
    TextView TBlind;
    TextView TBuyin;
    TextView HBarStart;
    TextView HBarEnd;
    Slider TSlider;
    Button btnT;
    private double seekbarValue = 100000;
    Thread playFriendThread;
    public static long popUpbuyin = 100000;
    public static long popUpblind = 10000;

    //hostThread boolean
    public static boolean setBoardCoin = false;
    public static boolean initializeRoom = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_playwithfriend);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnHost = (ImageView) findViewById(R.id.btnhost);
        btnJoin = (ImageView) findViewById(R.id.btnJoin);

        //pop up Buy in
        BuyInPopUp = (RelativeLayout) findViewById(R.id.BuyInPopUp);
        tH = (TextView) findViewById(R.id.tH);
        TBlind = (TextView) findViewById(R.id.TBlind);
        TBuyin = (TextView) findViewById(R.id.TBuyin);
        HBarStart = (TextView) findViewById(R.id.HBarStart);
        HBarEnd = (TextView) findViewById(R.id.HBarEnd);
        TSlider = (Slider) findViewById(R.id.TSlider);
        btnT = (Button) findViewById(R.id.btnT);

        inPlayWithFriends = true;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClientToServer.createWaitingRoom(ClientToServer.boardType[0], ClientToServer.minEntryValue[0], ClientToServer.minCallValue[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

       btnT.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   ClientToServer.sendJoinAmount((long) Math.ceil(seekbarValue));
                   showPopUpBuyin(false);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });

        TSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = TSlider.getValue();
                TBuyin.setText("Buy in: "+BoardChoosing.modifyChipsString(seekbarValue));
            }
        });

        iniTializePopUpBuyIn();
        showPopUpBuyin(false);



        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goJoinFriends();
            }
        });


        //................................................................
        // .........................hostThread............................
        // ...............................................................

        playFriendThread = new Thread(){
            @Override
            public void run() {
                while (inPlayWithFriends){
                    if(ClientToServer.DISCONNECTED){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goConnectivity();
                            }
                        });
                        ClientToServer.DISCONNECTED = false;
                    }
                    if(setBoardCoin){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(ClientToServer.user.getCurrentCoin()<ClientToServer.minEntryValue[0]){
                                    Toast toast = Toast.makeText(getApplicationContext(),"Your Current CHIPS is not enough. Buy Coin or watch ad to get CHIPS",Toast.LENGTH_SHORT);
                                }
                                else {
                                    iniTializePopUpBuyIn();
                                    if(shouldOpneBuyIn) {
                                        showPopUpBuyin(true);
                                    }
                                }
                            }
                        });
                        setBoardCoin = false;
                        JoinFriends.askBoardCoin = false;
                    }
                    if(initializeRoom){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HostFriends.initializeData = true;
                                goWaitingRoom();
                                System.out.println("Go waiting Room.........................");
                            }
                        });
                        initializeRoom = false;
                        JoinFriends.initializeData = false;
                    }

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
        playFriendThread.start();

        //..........................................................................................
        //....................................hostThreadEnd.........................................
        //..........................................................................................


    }

    private void goWaitingRoom(){
        inPlayWithFriends = false;
        HostFriends.inHostFriends = true;
        Intent intent = new Intent(this, HostFriends.class);
        startActivity(intent);
        finish();
    }

    private void showPopUpBuyin(boolean isActive){
        if(isActive){
            BuyInPopUp.setVisibility(View.VISIBLE);
        }
        else{
            BuyInPopUp.setVisibility(View.GONE);
        }
    }

    private void iniTializePopUpBuyIn(){
        long startvalue = ClientToServer.minEntryValue[0];
        long diff = ClientToServer.user.getCurrentCoin()-startvalue;
        if(diff>=ClientToServer.minCallValue[0]) {
            TBuyin.setText("Buy in: " + BoardChoosing.modifyChipsString(startvalue));
            TBlind.setText(BoardChoosing.modifyChipsString(ClientToServer.minCallValue[0]));
            TSlider.setValueFrom(startvalue);
            long v = ClientToServer.user.getCurrentCoin();
            v = v/(ClientToServer.minCallValue[0]);
            v = Math.round(v);
            v = v*(ClientToServer.minCallValue[0]);
            TSlider.setValueTo(v);
            TSlider.setStepSize(ClientToServer.minCallValue[0]);
            TSlider.setValue(startvalue);
            HBarStart.setText(BoardChoosing.modifyChipsString(startvalue));
            HBarEnd.setText(BoardChoosing.modifyChipsString(v));
            seekbarValue = startvalue;
            shouldOpneBuyIn = true;
        }
        else{
            shouldOpneBuyIn = false;
            Toast.makeText(playwithfriend.this, "CHIPS amount is LOW, BUY CHIPS", Toast.LENGTH_SHORT).show();
        }
    }

    public void goHome(){
        inPlayWithFriends = false;
        HomeScreen.isHomeThread = true;
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void goJoinFriends(){
        inPlayWithFriends = false;
        JoinFriends.inJoinFriends = true;
        Intent intent = new Intent(this, JoinFriends.class);
        startActivity(intent);
        finish();
    }

    private void goConnectivity(){
        inPlayWithFriends = false;
        Connectivity.isConnectivityThread = true;
        Intent intent = new Intent(this, Connectivity.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        inPlayWithFriends = false;
        Login.isLoginThread = true;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goHome();
    }
}