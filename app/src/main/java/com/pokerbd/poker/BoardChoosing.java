package com.pokerbd.poker;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BoardChoosing extends AppCompatActivity {

    private float seekbarValue = 0;

    float[] BuyInArray = {100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000};

    RelativeLayout board1;
    RelativeLayout board2;
    RelativeLayout board3;
    RelativeLayout board4;
    RelativeLayout board5;
    RelativeLayout board6;
    RelativeLayout board7;
    RelativeLayout board8;
    RelativeLayout board9;
    RelativeLayout board10;


    //silver
    TextView tS;
    TextView Sblind;
    TextView SBuyin;
    TextView Sbarstart;
    TextView Sbarend;
    Slider SSlider;
    Button btnS;
    //silver2
    TextView tS2;
    TextView Sblind2;
    TextView SBuyin2;
    TextView Sbarstart2;
    TextView Sbarend2;
    Slider SSlider2;
    Button btnS2;
    //Golden
    TextView tG;
    TextView Gblind;
    TextView GBuyin;
    TextView Gbarstart;
    TextView Gbarend;
    Slider GSlider;
    Button btnG;
    //Golden2
    TextView tG2;
    TextView Gblind2;
    TextView GBuyin2;
    TextView Gbarstart2;
    TextView Gbarend2;
    Slider GSlider2;
    Button btnG2;
    //Diamond
    TextView tD;
    TextView Dblind;
    TextView DBuyin;
    TextView Dbarstart;
    TextView Dbarend;
    Slider DSlider;
    Button btnD;
    //Diamond2
    TextView tD2;
    TextView Dblind2;
    TextView DBuyin2;
    TextView Dbarstart2;
    TextView Dbarend2;
    Slider DSlider2;
    Button btnD2;
    //Champions
    TextView tC;
    TextView Cblind;
    TextView CBuyin;
    TextView Cbarstart;
    TextView Cbarend;
    Slider CSlider;
    Button btnC;
    //Champions2
    TextView tC2;
    TextView Cblind2;
    TextView CBuyin2;
    TextView Cbarstart2;
    TextView Cbarend2;
    Slider CSlider2;
    Button btnC2;
    //Knight's
    TextView tK;
    TextView Kblind;
    TextView KBuyin;
    TextView Kbarstart;
    TextView Kbarend;
    Slider KSlider;
    Button btnK;
    //Knight's2
    TextView tK2;
    TextView Kblind2;
    TextView KBuyin2;
    TextView Kbarstart2;
    TextView Kbarend2;
    Slider KSlider2;
    Button btnK2;

    int boardType = 0;
    private ImageView backbtn2;


    Thread BCThread;
    boolean isBCThread = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_board_choosing);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        board1 = (RelativeLayout) findViewById(R.id.board1);
        board2 = (RelativeLayout) findViewById(R.id.board2);
        board3 = (RelativeLayout) findViewById(R.id.board3);
        board4 = (RelativeLayout) findViewById(R.id.board4);
        board5 = (RelativeLayout) findViewById(R.id.board5);
        board6 = (RelativeLayout) findViewById(R.id.board6);
        board7 = (RelativeLayout) findViewById(R.id.board7);
        board8 = (RelativeLayout) findViewById(R.id.board8);
        board9 = (RelativeLayout) findViewById(R.id.board9);
        board10 = (RelativeLayout) findViewById(R.id.board10);

        tS = (TextView) findViewById(R.id.tH);
        Sblind = (TextView) findViewById(R.id.TBlind);
        SBuyin = (TextView) findViewById(R.id.TBuyin);
        Sbarstart = (TextView) findViewById(R.id.HBarStart);
        Sbarend = (TextView) findViewById(R.id.HBarEnd);
        SSlider = (Slider) findViewById(R.id.TSlider);
        btnS = (Button) findViewById(R.id.btnT);

        tS2 = (TextView) findViewById(R.id.tH2);
        Sblind2 = (TextView) findViewById(R.id.TBlind2);
        SBuyin2 = (TextView) findViewById(R.id.TBuyin2);
        Sbarstart2 = (TextView) findViewById(R.id.HBarStart2);
        Sbarend2 = (TextView) findViewById(R.id.HBarEnd2);
        SSlider2 = (Slider) findViewById(R.id.TSlider2);
        btnS2 = (Button) findViewById(R.id.btnT2);

        tG = (TextView) findViewById(R.id.tG);
        Gblind = (TextView) findViewById(R.id.GBlind);
        GBuyin = (TextView) findViewById(R.id.GBuyin);
        Gbarstart = (TextView) findViewById(R.id.GBarStart);
        Gbarend = (TextView) findViewById(R.id.GBarEnd);
        GSlider = (Slider) findViewById(R.id.GSlider);
        btnG = (Button) findViewById(R.id.btnG);

        tG2 = (TextView) findViewById(R.id.tG2);
        Gblind2 = (TextView) findViewById(R.id.GBlind2);
        GBuyin2 = (TextView) findViewById(R.id.GBuyin2);
        Gbarstart2 = (TextView) findViewById(R.id.GBarStart2);
        Gbarend2 = (TextView) findViewById(R.id.GBarEnd2);
        GSlider2 = (Slider) findViewById(R.id.GSlider2);
        btnG2 = (Button) findViewById(R.id.btnG2);

        tD = (TextView) findViewById(R.id.tD);
        Dblind = (TextView) findViewById(R.id.DBlind);
        DBuyin = (TextView) findViewById(R.id.DBuyin);
        Dbarstart = (TextView) findViewById(R.id.DBarStart);
        Dbarend = (TextView) findViewById(R.id.DBarEnd);
        DSlider = (Slider) findViewById(R.id.DSlider);
        btnD = (Button) findViewById(R.id.btnD);

        tD2 = (TextView) findViewById(R.id.tD2);
        Dblind2 = (TextView) findViewById(R.id.DBlind2);
        DBuyin2 = (TextView) findViewById(R.id.DBuyin2);
        Dbarstart2 = (TextView) findViewById(R.id.DBarStart2);
        Dbarend2 = (TextView) findViewById(R.id.DBarEnd2);
        DSlider2 = (Slider) findViewById(R.id.DSlider2);
        btnD2 = (Button) findViewById(R.id.btnD2);

        tC = (TextView) findViewById(R.id.tC);
        Cblind = (TextView) findViewById(R.id.CBlind);
        CBuyin = (TextView) findViewById(R.id.CBuyin);
        Cbarstart = (TextView) findViewById(R.id.CBarStart);
        Cbarend = (TextView) findViewById(R.id.CBarEnd);
        CSlider = (Slider) findViewById(R.id.CSlider);
        btnC = (Button) findViewById(R.id.btnC);

        tC2 = (TextView) findViewById(R.id.tC2);
        Cblind2 = (TextView) findViewById(R.id.CBlind2);
        CBuyin2 = (TextView) findViewById(R.id.CBuyin2);
        Cbarstart2 = (TextView) findViewById(R.id.CBarStart2);
        Cbarend2 = (TextView) findViewById(R.id.CBarEnd2);
        CSlider2 = (Slider) findViewById(R.id.CSlider2);
        btnC2 = (Button) findViewById(R.id.btnC2);

        tK = (TextView) findViewById(R.id.tK);
        Kblind = (TextView) findViewById(R.id.KBlind);
        KBuyin = (TextView) findViewById(R.id.KBuyin);
        Kbarstart = (TextView) findViewById(R.id.KBarStart);
        Kbarend = (TextView) findViewById(R.id.KBarEnd);
        KSlider = (Slider) findViewById(R.id.KSlider);
        btnK = (Button) findViewById(R.id.btnK);

        tK2 = (TextView) findViewById(R.id.tK2);
        Kblind2 = (TextView) findViewById(R.id.KBlind2);
        KBuyin2 = (TextView) findViewById(R.id.KBuyin2);
        Kbarstart2 = (TextView) findViewById(R.id.KBarStart2);
        Kbarend2 = (TextView) findViewById(R.id.KBarEnd2);
        KSlider2 = (Slider) findViewById(R.id.KSlider2);
        btnK2 = (Button) findViewById(R.id.btnK2);

        isBCThread = true;

        BCThread = new Thread(){
            @Override
            public void run() {
                while (isBCThread) {
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
        BCThread.start();

        ResetTabInfo();

        SSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = SSlider.getValue();
                BuyInArray[0] = seekbarValue;
                SBuyin.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });
        SSlider2.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = SSlider2.getValue();
                BuyInArray[1] = seekbarValue;
                SBuyin2.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });
        GSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = GSlider.getValue();
                BuyInArray[2] = seekbarValue;
                GBuyin.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });
        GSlider2.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = GSlider2.getValue();
                BuyInArray[3] = seekbarValue;
                GBuyin2.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });
        DSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = DSlider.getValue();
                BuyInArray[4] = seekbarValue;
                DBuyin.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });
        DSlider2.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = DSlider2.getValue();
                BuyInArray[5] = seekbarValue;
                DBuyin2.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });
        CSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = CSlider.getValue();
                BuyInArray[6] = seekbarValue;
                CBuyin.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });
        CSlider2.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = CSlider2.getValue();
                BuyInArray[7] = seekbarValue;
                CBuyin2.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });
        KSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = KSlider.getValue();
                BuyInArray[8] = seekbarValue;
                KBuyin.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });
        KSlider2.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekbarValue = KSlider2.getValue();
                BuyInArray[9] = seekbarValue;
                KBuyin2.setText("Buy in: "+modifyChipsString(seekbarValue));
            }
        });

        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(0);
            }
        });
        btnS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(1);
            }
        });
        btnG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(2);
            }
        });
        btnG2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(3);
            }
        });
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(4);
            }
        });
        btnD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(5);
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(6);
            }
        });
        btnC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(7);
            }
        });
        btnK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(8);
            }
        });
        btnK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJoinTable(9);
            }
        });



        backbtn2 = (ImageView) findViewById(R.id.backbtn2);

        backbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

    }

    private void ResetTabInfo(){
        for(int i=0; i<10; i++){
            BuyInArray[i] = ClientToServer.minEntryValue[i];
        }

        SSlider.setValueFrom(BuyInArray[0]);
        SSlider2.setValueFrom(BuyInArray[1]);
        GSlider.setValueFrom(BuyInArray[2]);
        GSlider2.setValueFrom(BuyInArray[3]);
        DSlider.setValueFrom(BuyInArray[4]);
        DSlider2.setValueFrom(BuyInArray[5]);
        CSlider.setValueFrom(BuyInArray[6]);
        CSlider2.setValueFrom(BuyInArray[7]);
        KSlider.setValueFrom(BuyInArray[8]);
        KSlider2.setValueFrom(BuyInArray[9]);

        SSlider.setValue(BuyInArray[0]);
        SSlider2.setValue(BuyInArray[1]);
        GSlider.setValue(BuyInArray[2]);
        GSlider2.setValue(BuyInArray[3]);
        DSlider.setValue(BuyInArray[4]);
        DSlider2.setValue(BuyInArray[5]);
        CSlider.setValue(BuyInArray[6]);
        CSlider2.setValue(BuyInArray[7]);
        KSlider.setValue(BuyInArray[8]);
        KSlider2.setValue(BuyInArray[9]);

        long vt = ClientToServer.user.getCurrentCoin();

        SSlider.setValueTo(ClientToServer.maxEntryValue[0]);
        SSlider2.setValueTo(ClientToServer.maxEntryValue[1]);
        GSlider.setValueTo(ClientToServer.maxEntryValue[2]);
        GSlider2.setValueTo(ClientToServer.maxEntryValue[3]);
        DSlider.setValueTo(ClientToServer.maxEntryValue[4]);
        DSlider2.setValueTo(ClientToServer.maxEntryValue[5]);
        CSlider.setValueTo(ClientToServer.maxEntryValue[6]);
        CSlider2.setValueTo(ClientToServer.maxEntryValue[7]);
        KSlider.setValueTo(ClientToServer.maxEntryValue[8]);
        KSlider2.setValueTo(ClientToServer.maxEntryValue[9]);

        SSlider.setStepSize(ClientToServer.minCallValue[0]);
        SSlider2.setStepSize(ClientToServer.minCallValue[1]);
        GSlider.setStepSize(ClientToServer.minCallValue[2]);
        GSlider2.setStepSize(ClientToServer.minCallValue[3]);
        DSlider.setStepSize(ClientToServer.minCallValue[4]);
        DSlider2.setStepSize(ClientToServer.minCallValue[5]);
        CSlider.setStepSize(ClientToServer.minCallValue[6]);
        CSlider2.setStepSize(ClientToServer.minCallValue[7]);
        KSlider.setStepSize(ClientToServer.minCallValue[8]);
        KSlider2.setStepSize(ClientToServer.minCallValue[9]);

        seekbarValue = ClientToServer.minEntryValue[0];

        SBuyin.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[0]));
        SBuyin2.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[1]));
        GBuyin.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[2]));
        GBuyin2.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[3]));
        DBuyin.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[4]));
        DBuyin2.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[5]));
        CBuyin.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[6]));
        CBuyin2.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[7]));
        KBuyin.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[8]));
        KBuyin2.setText("Buy in: "+modifyChipsString(ClientToServer.minEntryValue[9]));

        Sbarstart.setText(modifyChipsString(ClientToServer.minEntryValue[0]));
        Sbarstart2.setText(modifyChipsString(ClientToServer.minEntryValue[1]));
        Gbarstart.setText(modifyChipsString(ClientToServer.minEntryValue[2]));
        Gbarstart2.setText(modifyChipsString(ClientToServer.minEntryValue[3]));
        Dbarstart.setText(modifyChipsString(ClientToServer.minEntryValue[4]));
        Dbarstart2.setText(modifyChipsString(ClientToServer.minEntryValue[5]));
        Cbarstart.setText(modifyChipsString(ClientToServer.minEntryValue[6]));
        Cbarstart2.setText(modifyChipsString(ClientToServer.minEntryValue[7]));
        Kbarstart.setText(modifyChipsString(ClientToServer.minEntryValue[8]));
        Kbarstart2.setText(modifyChipsString(ClientToServer.minEntryValue[9]));

        Sbarend.setText(modifyChipsString(ClientToServer.maxEntryValue[0]));
        Sbarend2.setText(modifyChipsString(ClientToServer.maxEntryValue[1]));
        Gbarend.setText(modifyChipsString(ClientToServer.maxEntryValue[2]));
        Gbarend2.setText(modifyChipsString(ClientToServer.maxEntryValue[3]));
        Dbarend.setText(modifyChipsString(ClientToServer.maxEntryValue[4]));
        Dbarend2.setText(modifyChipsString(ClientToServer.maxEntryValue[5]));
        Cbarend.setText(modifyChipsString(ClientToServer.maxEntryValue[6]));
        Cbarend2.setText(modifyChipsString(ClientToServer.maxEntryValue[7]));
        Kbarend.setText(modifyChipsString(ClientToServer.maxEntryValue[8]));
        Kbarend2.setText(modifyChipsString(ClientToServer.maxEntryValue[9]));


        Sblind.setText(modifyChipsString(ClientToServer.minCallValue[0]));
        Sblind2.setText(modifyChipsString(ClientToServer.minCallValue[1]));
        Gblind.setText(modifyChipsString(ClientToServer.minCallValue[2]));
        Gblind2.setText(modifyChipsString(ClientToServer.minCallValue[3]));
        Dblind.setText(modifyChipsString(ClientToServer.minCallValue[4]));
        Dblind2.setText(modifyChipsString(ClientToServer.minCallValue[5]));
        Cblind.setText(modifyChipsString(ClientToServer.minCallValue[6]));
        Cblind2.setText(modifyChipsString(ClientToServer.minCallValue[7]));
        Kblind.setText(modifyChipsString(ClientToServer.minCallValue[8]));
        Kblind2.setText(modifyChipsString(ClientToServer.minCallValue[9]));

        tS.setText("Bronze Table \n ");
        tS2.setText("Silver Table \n ");
        tG.setText("Gold Table \nMCR: "+modifyChipsString(ClientToServer.mcr[2]));
        tG2.setText("Gold Table II\nMCR: "+modifyChipsString(ClientToServer.mcr[3]));
        tD.setText("Diamond Table \nMCR: "+modifyChipsString(ClientToServer.mcr[4]));
        tD2.setText("Diamond Table II\nMCR: "+modifyChipsString(ClientToServer.mcr[5]));
        tC.setText("Champion's Table \nMCR: "+modifyChipsString(ClientToServer.mcr[6]));
        tC2.setText("Champion's Table II\nMCR: "+modifyChipsString(ClientToServer.mcr[7]));
        tK.setText("Knight's Table \nMCR: "+modifyChipsString(ClientToServer.mcr[8]));
        tK2.setText("Knight's Table II\nMCR: "+modifyChipsString(ClientToServer.mcr[9]));

        if(ClientToServer.hiddenStatus[0]==false){
            board1.setVisibility(View.GONE);
        }
        else board1.setVisibility(View.VISIBLE);
        if(ClientToServer.hiddenStatus[1]==false){
            board2.setVisibility(View.GONE);
        }
        else board2.setVisibility(View.VISIBLE);
        if(ClientToServer.hiddenStatus[2]==false){
            board3.setVisibility(View.GONE);
        }
        else board3.setVisibility(View.VISIBLE);
        if(ClientToServer.hiddenStatus[3]==false){
            board4.setVisibility(View.GONE);
        }
        else board4.setVisibility(View.VISIBLE);
        if(ClientToServer.hiddenStatus[4]==false){
            board5.setVisibility(View.GONE);
        }
        else board5.setVisibility(View.VISIBLE);
        if(ClientToServer.hiddenStatus[5]==false){
            board6.setVisibility(View.GONE);
        }
        else board6.setVisibility(View.VISIBLE);
        if(ClientToServer.hiddenStatus[6]==false){
            board7.setVisibility(View.GONE);
        }
        else board7.setVisibility(View.VISIBLE);
        if(ClientToServer.hiddenStatus[7]==false){
            board8.setVisibility(View.GONE);
        }
        else board8.setVisibility(View.VISIBLE);
        if(ClientToServer.hiddenStatus[8]==false){
            board9.setVisibility(View.GONE);
        }
        else board9.setVisibility(View.VISIBLE);
        if(ClientToServer.hiddenStatus[9]==false){
            board10.setVisibility(View.GONE);
        }
        else board10.setVisibility(View.VISIBLE);


    }

    private void checkJoinTable(int id){
        if(ClientToServer.user.getCurrentCoin()>=ClientToServer.mcr[id]) {
            if (ClientToServer.user.getCurrentCoin() >= BuyInArray[id]) {
                boardType = id;
                goLobby();
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BoardChoosing.this, "You don't have enough chips, Buy chips or watch video to earn chips", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BoardChoosing.this, "Minimum Required Chips: "+modifyChipsString(ClientToServer.mcr[id]), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void goLobby(){
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("........................SeekBarValue....................");
                    System.out.println(BuyInArray[boardType]);
                    System.out.println(seekbarValue);
                    System.out.println("........................................................");
                    MainActivity.clientToServer.requestJoin(-1, -1, ClientToServer.boardType[boardType], ClientToServer.minEntryValue[boardType], ClientToServer.minCallValue[boardType], -1, -1, (long) BuyInArray[boardType]);;
                    LobbyScreen.inLobby = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("join done");
            }
        }.start();

        LobbyScreen.inLobby = true;
        isBCThread = false;
        LobbyScreen.boardType = this.boardType;
        Intent intent = new Intent(this, LobbyScreen.class);
        startActivity(intent);
        finish();
    }

    private void goHome(){
        HomeScreen.isHomeThread = true;
        isBCThread = false;
        ResetTabInfo();
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        Login.isLoginThread = true;
        isBCThread = false;
        ResetTabInfo();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public static String modifyChipsString(double chips){
        //System.out.println("Chips: "+ chips);
        String str = "";
        long c = Math.round(chips);
        long cc = c;
       // System.out.println("CC: "+ cc);
        int count = 0;
        while (c>0){
            count++;
            c = c/10;
        }
        if(count<6){
            return String.valueOf(cc);
        }
        else if(count<8){
            double t = cc/100000.0;
            //System.out.println("double value: "+ t);
            str = String.valueOf(t);
            //System.out.println(str);
            if(str.length()>=4) {
                return str.substring(0, 4)+" L";
            }
            else return str+" L";
        }
        else{
            double t = cc/10000000.0;
            //System.out.println("double value: "+ t);
            str = String.valueOf(t);
           // System.out.println(str);
            if(str.length()>=4) {
                return str.substring(0, 4)+" Cr";
            }
            else return str+" Cr";
        }
    }

    @Override
    public void onBackPressed() {
        goHome();
    }

}