package com.pokerbd.poker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

public class JoinFriends extends AppCompatActivity {

    int[] images1 = {R.drawable.emptyplayer, R.drawable.emptyplayer, R.drawable.emptyplayer, R.drawable.emptyplayer, R.drawable.emptyplayer};
    String[] imagesString = new String[5];

    String[] names1 = {"Empty", "Empty", "Empty", "Empty", "Empty"};
    String[] ranks1 = {"None", "None", "None", "None", "None"};

    TextInputLayout joinCode;
    Button btnJoinfrn;

    public static boolean inJoinFriends = false;

    //buy in pop up
    RelativeLayout BuyInPopUp;
    TextView tH;
    TextView TBlind;
    TextView TBuyin;
    TextView HBarStart;
    TextView HBarEnd;
    Slider TSlider;
    Button btnT;
    private double seekbarValue = 0;
    public static int btnMode = 0;

    private boolean shouldOpneBuyIn = false;

    //mode
    public static boolean joinGameThread = false;
    public static boolean joinWaitingRoom = false;
    public static boolean isRemoved = false;
    public static String removeMessage = "";

    ListView joinList;
    Button hostExit2;

    CustomAdapter1 customAdapter1;

    Thread joinThread;
    public static long popUpbuyin = 100000;
    public static long popUpblind = 10000;

    //joinFriend boolean
    public static boolean joinResponse = false;
    public static boolean askBoardCoin = false;
    public static boolean initializeData = false;
    public static boolean loadData = false;
    public static boolean gameStart = false;
    public static boolean responseCode = false;
    public static boolean invalidCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_friends);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        for(int i=0; i<5; i++){
            imagesString[i] = MainActivity.GuestImageLink;
        }


        joinCode = (TextInputLayout) findViewById(R.id.joinCode);
        btnJoinfrn = (Button) findViewById(R.id.btnJoinfrn);
        joinList = (ListView) findViewById(R.id.joinList);

        //pop up Buy in
        BuyInPopUp = (RelativeLayout) findViewById(R.id.BuyInPopUp);
        tH = (TextView) findViewById(R.id.tH);
        TBlind = (TextView) findViewById(R.id.TBlind);
        TBuyin = (TextView) findViewById(R.id.TBuyin);
        HBarStart = (TextView) findViewById(R.id.HBarStart);
        HBarEnd = (TextView) findViewById(R.id.HBarEnd);
        TSlider = (Slider) findViewById(R.id.TSlider);
        btnT = (Button) findViewById(R.id.btnT);

        customAdapter1 = new CustomAdapter1();
        joinList.setAdapter(customAdapter1);

        hostExit2 = (Button) findViewById(R.id.hostExit2);

        inJoinFriends = true;

        hostExit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(ClientToServer.user.getSeatPosition()>-1) {
                        ClientToServer.removeMeFromWaitingRoom();
                    }
                    gotPlayFriend();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnJoinfrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = joinCode.getEditText().getText().toString().trim();
                if(code.isEmpty()){
                    Toast.makeText(JoinFriends.this, "Enter Valid Code", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (btnMode == 0) {
                        try {
                            ClientToServer.joinWaitingRoomByCode(Integer.parseInt(code));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(btnMode==1){
                        btnMode = 0;
                        btnJoinfrn.setText("Join With Friends");
                        try {
                            ClientToServer.removeMeFromWaitingRoom();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        gotPlayFriend();

                    }
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

        //................................................................
        // .........................joinThread............................
        // ...............................................................

        joinThread = new Thread(){
            @Override
            public void run() {
                while (inJoinFriends) {
                    //System.out.println("In JoinThread");
                    if(ClientToServer.DISCONNECTED){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goConnectivity();
                            }
                        });
                        ClientToServer.DISCONNECTED = false;
                    }
                    if (!ClientToServer.hasConnected) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(JoinFriends.this, "Disconnected, Check your internet connection", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (loadData) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("//////////////////////////Join Friends/////////////////////////////////////////////////////////\n" +
                                        "////////////////////////////////////////load DATA////////////////////////////////////\n" +
                                        "/////////////////////////////////////////////////////////////////////////////////////////");
                                loadList();
                            }
                        });
                        loadData = false;
                        HostFriends.loadData = false;
                    }
                    if (gameStart) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goLobby();
                            }
                        });
                        gameStart = false;
                        HostFriends.gameStart = false;
                    }
                    if (askBoardCoin) {
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
                        askBoardCoin = false;
                        playwithfriend.setBoardCoin = false;
                    }
                    if (responseCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(invalidCode){
                                    Toast.makeText(JoinFriends.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                                   // invalidCode = true;
                                    btnMode = 0;
                                    ClientToServer.user.setSeatPosition(-1);
                                }
                                else{
                                    btnMode = 1;
                                    btnJoinfrn.setText("Leave Room");
                                    showAferJoinMode(true);
                                }
                            }
                        });
                        responseCode = false;
                    }
                    if (isRemoved) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAferJoinMode(false);
                                btnMode=0;
                                Toast.makeText(JoinFriends.this, removeMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                        isRemoved = false;
                        HostFriends.isRemoved = false;
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
        joinThread.start();

        //..........................................................................................
        //....................................hostThreadEnd.........................................
        //..........................................................................................

    }

    private void showAferJoinMode(boolean isActive){
        if(isActive){
            joinList.setVisibility(View.VISIBLE);
            btnJoinfrn.setText("Leave Room");
            joinCode.setVisibility(View.GONE);
        }
        else{
            joinList.setVisibility(View.GONE);
            btnJoinfrn.setText("Join with Friends");
            ClientToServer.user.setSeatPosition(-1);
            joinCode.setVisibility(View.VISIBLE);
        }
    }

    private void loadList() {
        User[] users = ClientToServer.user.getInWaitingRoomPlayers();
        if(users!=null){
            for(int i=0; i<users.length; i++){
                if(users[i]!=null) {
                    names1[i] = users[i].getUsername();
                    ranks1[i] = users[i].getRank();
                    imagesString[i] = users[i].getImageLink();
                }
                else{
                    names1[i] = "Empty";
                    ranks1[i] = "None";
                    imagesString[i] = MainActivity.GuestImageLink;
                }

            }
        }
        if(customAdapter1!=null) {
            customAdapter1.notifyDataSetChanged();
        }

    }

    class CustomAdapter1 extends BaseAdapter {

        @Override
        public int getCount() {
            return images1.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.row, null);

            CircleImageView imageView = (CircleImageView) view1.findViewById(R.id.propic);
            TextView textView = (TextView) view1.findViewById(R.id.proname);
            TextView textView1 = (TextView) view1.findViewById(R.id.poprank);

            Glide.with(view1).load(String.valueOf(imagesString[i])).into(imageView);
            //imageView.setImageResource(images1[i]);
            textView.setText(names1[i]);
            textView1.setText(ranks1[i]);

            return view1;
        }
    }

    private void gotPlayFriend() {
        playwithfriend.inPlayWithFriends = true;
        inJoinFriends = false;
        ClientToServer.user.setSeatPosition(-1);
        ClientToServer.user.deInitializeInvitationData();
        playwithfriend.initializeRoom = false;
        Intent intent = new Intent(this, playwithfriend.class);
        startActivity(intent);
        finish();
    }

    private void showPopUpBuyin(boolean isActive){
        if(isActive){
            BuyInPopUp.setVisibility(View.VISIBLE);
            btnJoinfrn.setVisibility(View.GONE);
        }
        else{
            BuyInPopUp.setVisibility(View.GONE);
            btnJoinfrn.setVisibility(View.VISIBLE);
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
            TSlider.setValue(startvalue);
            TSlider.setStepSize(ClientToServer.minCallValue[0]);
            HBarStart.setText(BoardChoosing.modifyChipsString(startvalue));
            HBarEnd.setText(BoardChoosing.modifyChipsString(v));
            seekbarValue = startvalue;
            shouldOpneBuyIn = true;
        }
        else{
            shouldOpneBuyIn = false;
            Toast.makeText(JoinFriends.this, "CHIPS amount is LOW, BUY CHIPS", Toast.LENGTH_SHORT).show();
        }
        btnJoinfrn.setVisibility(View.GONE);
    }

    private void goLobby(){
        inJoinFriends = false;
        Intent intent = new Intent(this, LobbyScreen.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        inJoinFriends = false;
        Login.isLoginThread = true;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void goConnectivity(){
        inJoinFriends = false;
        ClientToServer.user.setSeatPosition(-1);
        ClientToServer.user.deInitializeInvitationData();
        Connectivity.isConnectivityThread = true;
        Intent intent = new Intent(this, Connectivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        try {
            if(ClientToServer.user.getSeatPosition()>-1) {
                ClientToServer.removeMeFromWaitingRoom();
            }
            gotPlayFriend();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}