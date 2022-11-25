package com.pokerbd.poker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;

public class HostFriends extends AppCompatActivity {

    TextView roomCode;

    Button btnshareCode;
    Button btnCopy;
    Button hostExit;
    Button btnStart;
    Button btnKick;

    private static int timerCounter2 = 0;


    ListView alreadyJoinedList;

    public static boolean inHostFriends = false;
    public static boolean isRemoved = false;

    int[] images = {R.drawable.emptyplayer, R.drawable.emptyplayer, R.drawable.emptyplayer, R.drawable.emptyplayer};
    String[] imagesString = new String[4];
    String[] names = {"Empty", "Empty", "Empty", "Empty"};
    String[] ranks = {"None", "None", "None", "None"};


    Thread HostThread;

    //thread boolean
    public static boolean loadData = false;
    public static boolean initializeData = false;
    public static boolean gameStart = false;

    CustomAdapter customAdapter;

    //pop up profile
    private int currentSelectedID = -1;
    ConstraintLayout playerPopUpWindow;
    Button playerPopUpClose;
    TextView HandsPlayed2;
    TextView BestWinningHand2;
    TextView WinPercentage2;
    TextView biggestPotWin2;
    TextView usernamePM2;
    TextView rankinhome2;
    ImageView propicPM2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_host_friends);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        for(int i=0; i<4; i++){
            imagesString[i] = MainActivity.GuestImageLink;
        }

        roomCode = (TextView) findViewById(R.id.roomCode);
        btnshareCode = (Button) findViewById(R.id.btnshareCode);
        btnCopy = (Button) findViewById(R.id.btnCopy);
        hostExit = (Button) findViewById(R.id.hostExit);

        //pop up window
        playerPopUpWindow = (ConstraintLayout) findViewById(R.id.playerPopUpWindow);
        playerPopUpClose = (Button) findViewById(R.id.playerPopUpClose);
        HandsPlayed2 = (TextView) findViewById(R.id.winStreak);
        BestWinningHand2 = (TextView) findViewById(R.id.BestWinningHand2);
        WinPercentage2 = (TextView) findViewById(R.id.WinPercentage2);
        biggestPotWin2 = (TextView) findViewById(R.id.biggestPotWin2);
        usernamePM2 = (TextView) findViewById(R.id.usernamePM2);
        rankinhome2 = (TextView) findViewById(R.id.rankinhome2);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnKick = (Button) findViewById(R.id.btnKick);
        propicPM2 = (ImageView) findViewById(R.id.propicPM2);

        showPlayerWindowPopUp(false);

        inHostFriends = true;

        alreadyJoinedList = (ListView) findViewById(R.id.alreadyJoinedList);

        loadList();

        btnshareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, roomCode.getText());
                intent.setType("text/plain");
                intent.createChooser(intent, "Share by");
                startActivity(intent);
            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("roomCode", roomCode.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(HostFriends.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        hostExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClientToServer.removeMeFromWaitingRoom();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CountDownTimer mCountDownTimer;
                timerCounter2 = 0;
                mCountDownTimer=new CountDownTimer(1000,100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timerCounter2++;
                    }

                    @Override
                    public void onFinish() {
                        //Do what you want
                        try {
                            gotPlayFriend();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mCountDownTimer.start();
            }
        });

        playerPopUpClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlayerWindowPopUp(false);
            }
        });

        btnKick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSelectedID != 0) {
                    int[] arr = new int[1];
                    arr[0] = currentSelectedID;
                    try {
                        ClientToServer.removeFromWaitingRoom(arr);
                        showPlayerWindowPopUp(false);
                        names[currentSelectedID - 1] = "Empty";
                        ranks[currentSelectedID - 1] = "None";
                        if(customAdapter!=null) {
                            customAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = 0;
                for(int i=0; i<4; i++){
                    if(!names[i].equalsIgnoreCase("Empty"))
                        c++;
                }
                if(c>0) {
                    try {
                        ClientToServer.sendStartGameRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(HostFriends.this, "Nobody is there to play with", Toast.LENGTH_SHORT).show();
                }
            }
        });

        customAdapter = new CustomAdapter();
        alreadyJoinedList.setAdapter(customAdapter);

        alreadyJoinedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    setPopUpWindows(0);
                }
                else if(i==1){
                    setPopUpWindows(1);
                }
                else if(i==2){
                    setPopUpWindows(2);
                }
                else if(i==3){
                    setPopUpWindows(3);
                }
            }
        });

        //................................................................
        // .........................hostThread............................
        // ...............................................................

        HostThread = new Thread(){
            @Override
            public void run() {
                while (inHostFriends) {
                   // System.out.println("in HostThread");
                    if(ClientToServer.DISCONNECTED){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goConnectivity();
                            }
                        });
                        ClientToServer.DISCONNECTED = false;
                    }
                    if (initializeData) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                InitializeData();
                            }
                        });
                        initializeData = false;
                    }
                    if (loadData) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadList();
                            }
                        });
                        loadData = false;
                        JoinFriends.loadData = false;
                    }
                    if (gameStart) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goLobby();
                            }
                        });
                        gameStart = false;
                        JoinFriends.gameStart = false;
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
        HostThread.start();

        //..........................................................................................
        //....................................hostThreadEnd.........................................
        //..........................................................................................

    }

    private void InitializeData(){
        roomCode.setText(String.valueOf(ClientToServer.user.getGameCode()));
    }

    private void loadList() {
        User[] users = ClientToServer.user.getInWaitingRoomPlayers();
        if(users!=null){
            for(int i=0; i<users.length; i++){
                if(users[i]!=null) {
                    if(i==0) continue;
                    names[i-1] = users[i].getUsername();
                    ranks[i-1] = users[i].getRank();
                    imagesString[i-1] = users[i].getImageLink();
                }
                else{
                    if(i==0) continue;
                    names[i-1] = "Empty";
                    ranks[i-1] = "None";
                    imagesString[i-1] = MainActivity.GuestImageLink;
                }
            }
        }
        if(customAdapter!=null) {
            customAdapter.notifyDataSetChanged();
        }

    }

    private void showPlayerWindowPopUp(boolean isActive){
        if(isActive){
            playerPopUpWindow.setVisibility(View.VISIBLE);
            btnCopy.setVisibility(View.GONE);
            btnshareCode.setVisibility(View.GONE);
            btnStart.setVisibility(View.GONE);
        }
        else{
            playerPopUpWindow.setVisibility(View.GONE);
            btnCopy.setVisibility(View.VISIBLE);
            btnshareCode.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
        }

    }

    private void setPopUpWindows(int id){
        if(!names[id].equalsIgnoreCase("Empty")){
            showPlayerWindowPopUp(true);
            User[] players = ClientToServer.user.getInWaitingRoomPlayers();
            for(int i=0; i<players.length; i++){
                if(players[i]!=null){
                    if(players[i].getUsername().equalsIgnoreCase(names[id]) && id==players[i].getSeatPosition()-1){
                        BestWinningHand2.setText(String.valueOf(players[i].getBestHand()));
                        biggestPotWin2.setText(BoardChoosing.modifyChipsString(players[i].getBiggestWin()));
                        String wp = String.valueOf(players[i].getWinPercentage());
                        String str = "";
                        if(wp.length()>=5){
                            str = wp.substring(0, 5);
                        }
                        WinPercentage2.setText(str+"%");
                        HandsPlayed2.setText(String.valueOf(players[i].getRoundsPlayed()));
                        usernamePM2.setText(String.valueOf(players[i].getUsername()));
                        rankinhome2.setText(String.valueOf(players[i].getRank()));
                        currentSelectedID = players[i].getSeatPosition();
                        Glide.with(this).load(players[i].getImageLink()).into(propicPM2);
                    }
                }
            }
        }
        else showPlayerWindowPopUp(false);
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return images.length;
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
            //  imageView.setImageResource(images[i]);
            textView.setText(names[i]);
            textView1.setText(ranks[i]);

            return view1;
        }
    }

    private void gotPlayFriend() throws JSONException {
        inHostFriends = false;
        ClientToServer.user.deInitializeInvitationData();
        Intent intent = new Intent(this, playwithfriend.class);
        startActivity(intent);
        finish();
    }

    private void goLobby(){
        inHostFriends = false;
        Intent intent = new Intent(this, LobbyScreen.class);
        startActivity(intent);
        finish();
    }

    private void goConnectivity(){
        inHostFriends = false;
        ClientToServer.user.deInitializeInvitationData();
        Connectivity.isConnectivityThread = true;
        Intent intent = new Intent(this, Connectivity.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        inHostFriends = false;
        Login.isLoginThread = true;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        try {
            ClientToServer.removeMeFromWaitingRoom();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CountDownTimer mCountDownTimer;
        timerCounter2 = 0;
        mCountDownTimer=new CountDownTimer(1000,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerCounter2++;
            }

            @Override
            public void onFinish() {
                //Do what you want
                try {
                    gotPlayFriend();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        mCountDownTimer.start();
    }

}

