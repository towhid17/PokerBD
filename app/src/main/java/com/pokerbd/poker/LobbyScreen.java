package com.pokerbd.poker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.slider.Slider;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.jaygoo.widget.VerticalRangeSeekBar;


import org.json.JSONException;

import java.util.ArrayList;

public class LobbyScreen extends AppCompatActivity {

    public static long raiseCost = 0;
    public static long callCost = 0;
    public static long deductRoundCoin = 0;
    public static long smallBlindDeduct = 0;
    public static long bigBlindDeduct = 0;
    public static int smallBlindSeat = 0;
    public static int bigBlindSeat = 0;
    public static long raiseMaxCost = 0;
    public static long raiseStepSize = 0;

    private static long allinanim_coin = 0;

    public static int Timerlevel = 0;

    public static ArrayList<WinnerData>[] winLevel = new ArrayList[5];


    int[] timerCounter = new int[20];
    int timerCountIdx = 0;
    int timerCounter2 = 0;

    public static int boardType = 0;

    public static float sliderValue = 0;

//    ======================== ALL player details objects ====================================

    public static PlayerDetails[] playerDetails = new PlayerDetails[5];

    public static int MYID;

    public static boolean inLobby = false;
    public static String Messeage;

    public static int PlayerWithTurnID = 0;
    public static int prevPlayerWithTurnID = 0;
    public static String prevPlayerCall = "check";
    public static long prevPlayerCallValue = 0;

    public static boolean isMyTurn = false;

    //for button activity
    public static boolean isFoldAvailable = false;
    public static boolean isCallAvailable = false;
    public static boolean isAllinAvailable = false;
    public static boolean isCheckAvailable = false;
    public static boolean isRaiseAvailable = false;

    //Gamethread command
    public static boolean LoadPlayerCards = false;
    public static boolean LoadBoardCards = false;
    public static boolean NoWinner = false;
    public static boolean Winner = false;
    public static boolean loadRoomData = false;
    public static boolean showRoundStartMessage = false;
    public static boolean showTurnInfo = false;
    public static boolean showEndCycle = false;
    public static boolean showAllCards = false;
    public static boolean PlayerWithTurn = false;
    public static boolean waitForPlayersToBuy = false;
    public static boolean proceesResult = false;
    public static boolean enableButton = false;
    public static boolean disableButton = false;
    public static boolean addBoardCoinsuccess = false;
    public static boolean abortResponse = false;
    public static boolean kickMeResponse = false;
    public static boolean deductBlind = false;

//    //Winners seatPosition and winAmount and suit
//    public static boolean[] WinnerSeats = new boolean[5];         //accordign to playerDetails id;
//    public static long[] winAmmount = new long[5];
//    public static String[] winnerSuit = new String[5];

    public static String Command = "no";
    public static Thread lobbyThread;

    ImageView btnback;
    ImageView btnRules2;

    //player money and name
    TextView playerOne;
    TextView playerTwo;
    TextView playerThree;
    TextView playerFour;
    TextView playerFive;
    TextView playerOneCoin;
    TextView playerOneCoin2;
    TextView playerTwoCoin;
    TextView playerThreeCoin;
    TextView playerFourCoin;
    TextView playerFiveCoin;

    //Player Image
    CircleImageView imgPlayerOne;
    CircleImageView imgPlayerTwo;
    CircleImageView imgPlayerThree;
    CircleImageView imgPlayerFour;
    CircleImageView imgPlayerFive;

    //hostess messages
    TextView cmd;
    TextView roundMoney;

    //player pop up pane
    RelativeLayout playerTwoPopup;
    RelativeLayout playerThreePopup;
    RelativeLayout playerFourPopup;
    RelativeLayout playerFivePopup;
    ImageView playerTwoCard1;
    ImageView playerTwoCard2;
    ImageView playerThreeCard1;
    ImageView playerThreeCard2;
    ImageView playerFourCard1;
    ImageView playerFourCard2;
    ImageView playerFiveCard1;
    ImageView playerFiveCard2;
    TextView playerOneStatus;
    TextView playerTwoStatus;
    TextView playerFourStatus;
    TextView playerThreeStatus;
    TextView playerFiveStatus;
    ProgressBar playerFivePbar;
    ProgressBar playerFourPbar;
    ProgressBar playerTwoPbar;
    ProgressBar playerThreePbar;

    ProgressBar playerOnePbar;

    //player one cards
    ImageView playerOneCard1;
    ImageView playerOneCard2;
    ImageView mycard1;
    ImageView mycard2;

    //player bid status
    ImageView isPlayerThreeHand;
    TextView playerThreeBid;
    ImageView isPlayerTwoHand;
    TextView playerTwoBid;
    ImageView isPlayerFourHand;
    TextView playerFourBid;
    ImageView isPlayerFiveHand;
    TextView playerFiveBid;
    //animation
    TextView playerBid2;
    TextView playerBid3;
    TextView playerBid4;
    TextView playerBid5;


    //player action button
    ImageView btnFold;
    ImageView btnRaise;
    ImageView btnAllin;
    ImageView btnCall;
    TextView callbtnvalue;

    //raise option
    ImageView btnConfirm;
    Button btnAllin2;
    RelativeLayout raiseback;
    //Slider slider;
    Button btnraiseclose;

    VerticalRangeSeekBar seekBar;


    //Board card
    ImageView card1;
    ImageView card2;
    ImageView card3;
    ImageView card4;
    ImageView card5;

    //player Pop Up Window
    ConstraintLayout playerPopUpWindow;
    Button playerPopUpClose;
    TextView HandsPlayed2;
    TextView BestWinningHand2;
    TextView WinPercentage2;
    TextView biggestPotWin2;
    TextView usernamePM2;
    TextView rankinhome2;
    TextView coinWon;
    TextView winStreak;
    CircleImageView propicPM2;

    //add coin pop up
    RelativeLayout addCoinPopUp;
    TextView LBuyin;
    TextView Lbarstart;
    TextView Lbarend;
    Slider LSlider;
    Button btnL;
    ImageView btncoinadd;
    Button buychipClose;
    double addcoinAmount = 0;


    TextView myselfWinner;

    CountDownTimer mCountDownTimer;
    CountDownTimer mCountDownTimer11;
    CountDownTimer mCountDownTimer111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lobby_screen);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        inLobby = true;

        btnback = (ImageView) findViewById(R.id.btnExit);
        btnRules2 = (ImageView) findViewById(R.id.btnRules2);

        //player array
        for(int i=0; i<=4; i++){
            playerDetails[i] = new PlayerDetails();
            playerDetails[i].setEmpty();
        }

        //winnerReesult array initialize
        for(int i=0; i<5; i++){
            winLevel[i] = new ArrayList<>();
        }

        //Player name and Money
        playerOne = (TextView) findViewById(R.id.playerOne);
        playerTwo = (TextView) findViewById(R.id.playerTwo);
        playerThree = (TextView) findViewById(R.id.playerThree);
        playerFour = (TextView) findViewById(R.id.playerFour);
        playerFive = (TextView) findViewById(R.id.playerFive);
        playerOneCoin = (TextView) findViewById(R.id.playerOneCoin);
        playerOneCoin2 = (TextView) findViewById(R.id.playerOneCoin2);
        playerTwoCoin = (TextView) findViewById(R.id.playerTwoCoin);
        playerThreeCoin = (TextView) findViewById(R.id.playerThreeCoin);
        playerFourCoin = (TextView) findViewById(R.id.playerFourCoin);
        playerFiveCoin = (TextView) findViewById(R.id.playerFiveCoin);

        //Player Image
        imgPlayerOne = (CircleImageView) findViewById(R.id.imgPlayerOne);
        imgPlayerTwo = (CircleImageView) findViewById(R.id.imgPlayerTwo);
        imgPlayerThree = (CircleImageView) findViewById(R.id.imgPlayerThree);
        imgPlayerFour = (CircleImageView) findViewById(R.id.imgPlayerFour);
        imgPlayerFive = (CircleImageView) findViewById(R.id.imgPlayerFive);

        // Hostess message
        cmd = (TextView) findViewById(R.id.cmd);
        roundMoney = (TextView) findViewById(R.id.roundMoney);

        //player pop up pane
        playerTwoPopup = (RelativeLayout) findViewById(R.id.playerTwoPopup);
        playerThreePopup = (RelativeLayout) findViewById(R.id.playerThreePopup);
        playerFourPopup = (RelativeLayout) findViewById(R.id.playerFourPopup);
        playerFivePopup = (RelativeLayout) findViewById(R.id.playerFivePopup);
        playerTwoCard1 = (ImageView) findViewById(R.id.playerTwoCard1);
        playerTwoCard2 = (ImageView) findViewById(R.id.playerTwoCard2);
        playerThreeCard1 = (ImageView) findViewById(R.id.playerThreeCard1);
        playerThreeCard2 = (ImageView) findViewById(R.id.playerThreeCard2);
        playerFourCard1 = (ImageView) findViewById(R.id.playerFourCard1);
        playerFourCard2 = (ImageView) findViewById(R.id.playerFourCard2);
        playerFiveCard1 = (ImageView) findViewById(R.id.playerFiveCard1);
        playerFiveCard2 = (ImageView) findViewById(R.id.playerFiveCard2);
        playerOneStatus = (TextView) findViewById(R.id.playerOneStatus);
        playerTwoStatus = (TextView) findViewById(R.id.playerTwoStatus);
        playerFourStatus = (TextView) findViewById(R.id.playerFourStatus);
        playerThreeStatus = (TextView) findViewById(R.id.playerThreeStatus);
        playerFiveStatus = (TextView) findViewById(R.id.playerFiveStatus);
        playerFivePbar = (ProgressBar) findViewById(R.id.playerFivePbar);
        playerFourPbar = (ProgressBar) findViewById(R.id.playerFourPbar);
        playerTwoPbar = (ProgressBar) findViewById(R.id.playerTwoPbar);
        playerThreePbar = (ProgressBar) findViewById(R.id.playerThreePbar);

        playerOnePbar = (ProgressBar) findViewById(R.id.playerOnePbar);

        //player one cards
        playerOneCard1 = (ImageView) findViewById(R.id.playerOneCard1);
        playerOneCard2 = (ImageView) findViewById(R.id.playerOneCard2);
        mycard1 = (ImageView) findViewById(R.id.mycard1);
        mycard2 = (ImageView) findViewById(R.id.mycard2);
        mycard1.setVisibility(View.GONE);
        mycard2.setVisibility(View.GONE);

        //player bid status
        isPlayerThreeHand = (ImageView) findViewById(R.id.isPlayerThreeHand);
        playerThreeBid = (TextView) findViewById(R.id.playerThreeBid);
        isPlayerTwoHand = (ImageView) findViewById(R.id.isPlayerTwoHand);
        playerTwoBid = (TextView) findViewById(R.id.playerTwoBid);
        isPlayerFourHand = (ImageView) findViewById(R.id.isPlayerFourHand);
        playerFourBid = (TextView) findViewById(R.id.playerFourBid);
        isPlayerFiveHand = (ImageView) findViewById(R.id.isPlayerFiveHand);
        playerFiveBid = (TextView) findViewById(R.id.playerFiveBid);
        //animation
        playerBid2 = (TextView) findViewById(R.id.playerTwoBid2);
        playerBid3 = (TextView) findViewById(R.id.playerThreeBid2);
        playerBid4 = (TextView) findViewById(R.id.playerFourBid2);
        playerBid5 = (TextView) findViewById(R.id.playerFiveBid2);

        playerBid2.setVisibility(View.GONE);
        playerBid3.setVisibility(View.GONE);
        playerBid4.setVisibility(View.GONE);
        playerBid5.setVisibility(View.GONE);


        //raise back
        //slider = (Slider) findViewById(R.id.silverSlider);
        raiseback = (RelativeLayout) findViewById(R.id.raiseback);
        btnConfirm = (ImageView) findViewById(R.id.btnConfirm);
        btnAllin2 = (Button) findViewById(R.id.btnAllin2);
        callbtnvalue = (TextView) findViewById(R.id.callbtnvalue);
        btnraiseclose = (Button) findViewById(R.id.btnraiseclose);

        seekBar = (VerticalRangeSeekBar) findViewById(R.id.seekbar);

        //Board Card
        card1 = (ImageView) findViewById(R.id.card1);
        card2 = (ImageView) findViewById(R.id.card2);
        card3 = (ImageView) findViewById(R.id.card3);
        card4 = (ImageView) findViewById(R.id.card4);
        card5 = (ImageView) findViewById(R.id.card5);

        // Player Action Button
        btnRaise = (ImageView) findViewById(R.id.btnRaise);
        btnFold = (ImageView) findViewById(R.id.btnFold);
        btnAllin = (ImageView) findViewById(R.id.btnAllin);
        btnCall = (ImageView) findViewById(R.id.btnCall);

        // Player Pop Up Window
        playerPopUpWindow = (ConstraintLayout) findViewById(R.id.playerPopUpWindow);
        playerPopUpClose = (Button) findViewById(R.id.playerPopUpClose);
        HandsPlayed2 = (TextView) findViewById(R.id.handsPlayed2);
        BestWinningHand2 = (TextView) findViewById(R.id.BestWinningHand2);
        WinPercentage2 = (TextView) findViewById(R.id.WinPercentage2);
        biggestPotWin2 = (TextView) findViewById(R.id.biggestPotWin2);
        usernamePM2 = (TextView) findViewById(R.id.usernamePM2);
        rankinhome2 = (TextView) findViewById(R.id.rankinhome2);
        coinWon = (TextView) findViewById(R.id.coinWon);
        winStreak = (TextView) findViewById(R.id.winStreak);
        propicPM2 = (CircleImageView) findViewById(R.id.propicPM2);

        //add coin pop up
        addCoinPopUp = (RelativeLayout) findViewById(R.id.addCoinPopUp);
        LBuyin = (TextView) findViewById(R.id.LBuyin);
        Lbarstart = (TextView) findViewById(R.id.LBarStart);
        Lbarend = (TextView) findViewById(R.id.LBarEnd);
        LSlider = (Slider) findViewById(R.id.LSlider);
        btnL = (Button) findViewById(R.id.btnLB);
        btncoinadd = (ImageView) findViewById(R.id.btncoinadd);
        buychipClose = (Button) findViewById(R.id.buychipClose);

        btncoinadd.setVisibility(View.GONE);

        myselfWinner = (TextView) findViewById(R.id.myselfWinner);

        showAddCoinPopUp(false);
        showPlayerWindowPopUp(false);
        showProgressBar(-1);
        showEveryPlayerCard(false);
        showPlayerAction(false);
        showRaisewindow(false);
        showStatus(false);
        showMyselfWinner(false);

        abortResponse = false;
        kickMeResponse = false;
        inLobby = true;

        cmd.setVisibility(View.GONE);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isMyTurn) {
                        if(isCallAvailable) {
                            MainActivity.clientToServer.clickedCallButton();
                            showActionAnimation("call");
                        }
                        else if(isCheckAvailable){
                            MainActivity.clientToServer.clickedCheckButton();
                        }
                        showPlayerAction(false);
                        showRaisewindow(false);
                        showProgressBar(-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnAllin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isMyTurn) {
                        allinanim_coin = ClientToServer.user.getBoardCoin();
                        MainActivity.clientToServer.clickedAllInButton();
                        showActionAnimation("allin");
                        showPlayerAction(false);
                        showRaisewindow(false);
                        showProgressBar(-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnAllin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isMyTurn) {
                        allinanim_coin = ClientToServer.user.getBoardCoin();
                        MainActivity.clientToServer.clickedAllInButton();
                        showActionAnimation("allin");
                        showPlayerAction(false);
                        showRaisewindow(false);
                        showProgressBar(-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isMyTurn) {
                        MainActivity.clientToServer.clickedFoldButton();
                        showPlayerAction(false);
                        showRaisewindow(false);
                        showProgressBar(-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isMyTurn) {
                        MainActivity.clientToServer.clickedRaiseButton((int) Math.round(sliderValue));
                        showActionAnimation("raise");
                        showRaisewindow(false);
                        showPlayerAction(false);
                        showProgressBar(-1);
                        if(mCountDownTimer!=null){
                            mCountDownTimer.cancel();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnraiseclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRaisewindow(false);
            }
        });

        btnRaise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMyTurn) {
                    long startvalue = Math.round(raiseCost);
                    long diff = ClientToServer.user.getBoardCoin()-startvalue;

                    long v = Math.round(raiseMaxCost);
                    if(raiseMaxCost>ClientToServer.user.getBoardCoin()){
                        v = Math.round(ClientToServer.user.getBoardCoin());
                    }

                    int ticks = Math.round((v-startvalue)/raiseStepSize);

                    if(raiseMaxCost==raiseCost){
                        showRaisewindow(true);
                        seekBar.setVisibility(View.GONE);
                        sliderValue = Math.round(raiseCost);
                    }
                    else if(diff==0){
                        showRaisewindow(true);
                        seekBar.setVisibility(View.GONE);
                        sliderValue = Math.round(raiseCost);
                    }
                    else if(diff>0) {

                        if(ticks<=0){
                            showRaisewindow(true);
                            seekBar.setVisibility(View.GONE);
                            sliderValue = Math.round(raiseCost);
                        }
                        else {
                            seekBar.setVisibility(View.VISIBLE);
                            showRaisewindow(true);

                            v = Math.round(raiseStepSize) * ticks + startvalue;
                            v = Math.round(v);
                            seekBar.setRange(startvalue, v);
                            seekBar.setSteps(ticks);
                            seekBar.setProgress(startvalue);
                            sliderValue = startvalue;
                        }
                    }
                    else {
                        Toast.makeText(LobbyScreen.this, "Board CHIPS amount is LOW, ADD CHIPS", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buychipClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCoinPopUp(false);
            }
        });

        //................................................................
        // .........................GameThread............................
        // ...............................................................

        lobbyThread = new Thread(){
            @Override
            public void run() {
                while (inLobby){
                   // System.out.println("In Lobby Thread");
                    if(ClientToServer.DISCONNECTED){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goConnectivity();
                            }
                        });
                        ClientToServer.DISCONNECTED = false;
                    }
                    if(proceesResult){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgressBar(-1);
                                if(mCountDownTimer!=null){
                                    mCountDownTimer.cancel();
                                }
                                showPlayerAction(false);
                                System.out.println("LobbyScreen ProcessResult");
                                //cmdMessage();
                                showMyselfWinner(true);
                                if(!ClientToServer.user.getCall().equalsIgnoreCase("low")){
                                    ClientToServer.user.setCall("new");
                                }

                            }
                        });
                        Command = "no";
                        proceesResult = false;
                    }
                    if(loadRoomData){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btncoinadd.setVisibility(View.VISIBLE);
                                loadPlayerData();
                                setPlayerInSeat();
                                updatePlayerCoin();
                                disableInactivePlayer();
                                System.out.println("LobbyScreen: LoadPlayerData");
                            }
                        });
                        Command = "no";
                        loadRoomData = false;
                    }
                    if(showRoundStartMessage){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initializeRound();
                                //cmdMessage();
                                if(!ClientToServer.user.getCall().equalsIgnoreCase("low")){
                                    addCoinPopUp.setVisibility(View.GONE);
                                    ClientToServer.user.setCall("");
                                }
                            }
                        });
                        Command = "no";
                        showRoundStartMessage = false;
                    }
                    if(LoadPlayerCards){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setMyCards();
                            }
                        });
                        Command = "no";
                        LoadPlayerCards = false;
                    }
                    if(LoadBoardCards){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startSufleSound();
                                setBoardCard();
                            }
                        });
                        Command = "no";
                        LoadBoardCards = false;
                    }
                    if(PlayerWithTurn){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setActivePlayer();
                                showTurnInfo();
                                prevPlayerCallAnim();
                            }
                        });
                        Command = "no";
                        PlayerWithTurn = false;
                    }
                    if(showTurnInfo) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadPlayerData();
                                setPlayerInSeat();
                                updatePlayerCoin();
                                showTurnInfo();
                            }
                        });
                        Command = "no";
                        showTurnInfo = false;
                    }
                    if(showEndCycle){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                showProgressBar(-1);
                                if(mCountDownTimer!=null){
                                    mCountDownTimer.cancel();
                                }
                                //cmdMessage();
                            }
                        });
                        Command = "no";
                        showEndCycle = false;
                    }
                    if(NoWinner){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cmdMessage();
                            }
                        });
                        Command = "no";
                        NoWinner = false;
                    }
                    if(Winner) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //cmdMessage();
//                            }
//                        });
                        Command = "no";
                        Winner = false;
                    }
                    if(showAllCards) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAllPlayerCards();
                            }
                        });
                        Command = "no";
                        showAllCards = false;
                    }
                    if(waitForPlayersToBuy) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                User[] users = ClientToServer.user.getInGamePlayers();
                                int count = 0;
                                if(users!=null){
                                    for (int i=0; i<users.length; i++){
                                        if(users[i]!=null){
                                            count++;
                                        }
                                    }
                                }
                                if(count<=1){
                                    Messeage = "Other Players Left ";
                                    playerOneCard1.setImageResource(R.drawable.cardcover);
                                    playerOneCard2.setImageResource(R.drawable.cardcover);
                                }
                                if(count>1){
                                    Messeage = "Waiting for other players to buy CHIPS";
                                }
                                cmdMessage();
                            }
                        });
                        Command = "no";
                        waitForPlayersToBuy = false;
                    }
                    if(enableButton){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showPlayerAction(true);
                            }
                        });
                        Command = "no";
                        enableButton = false;
                    }
                    if(disableButton){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showPlayerAction(false);
                            }
                        });
                        Command = "no";
                        disableButton = false;
                    }
                    if(addBoardCoinsuccess){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                playerDetails[0].setTotalCoin(ClientToServer.user.getBoardCoin());
                                playerOneCoin.setText(String.valueOf(ClientToServer.user.getBoardCoin()));
                                Toast.makeText(LobbyScreen.this, "Coin added Succesfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Command = "no";
                        addBoardCoinsuccess = false;
                    }
                    if(abortResponse){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goHome2();
                            }
                        });
                        Command = "no";
                        abortResponse = false;
                    }
                    if(kickMeResponse){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goHome2();
                            }
                        });
                        Command = "no";
                        kickMeResponse = false;
                    }
                    if(deductBlind){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showBlindStatus();
                                showBidAnimation(smallBlindSeat);
                                showBidAnimation(bigBlindSeat);
                            }
                        });
                        Command = "no";
                        deductBlind = false;
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
        lobbyThread.start();

        //..........................................................................................
        //....................................GamethreadEND.........................................
        //..........................................................................................


//        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
//            @Override
//            public void onStartTrackingTouch(@NonNull Slider slider) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(@NonNull Slider slider) {
//                sliderValue = slider.getValue();
//                raiseText.setText(BoardChoosing.modifyChipsString(sliderValue));
//            }
//        });


        seekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                //leftValue is left seekbar value, rightValue is right seekbar value
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view,  boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view,  boolean isLeft) {
                //stop tracking touch
                sliderValue = view.getLeftSeekBar().getProgress();
            }
        });


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    goHome();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnRules2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opneRules();
            }
        });

        btncoinadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ClientToServer.user.getCurrentCoin()<ClientToServer.user.getMinCallValue()){
                    Toast.makeText(LobbyScreen.this, "Your current chips isn't enough, Buy chips!", Toast.LENGTH_LONG).show();
                }
                else if(ClientToServer.user.getBoardCoin() >= ClientToServer.maxEntryValue[boardType]){
                    Toast.makeText(LobbyScreen.this, "Your board chips amount is high", Toast.LENGTH_LONG).show();
                }
                else if(!ClientToServer.user.getCall().equalsIgnoreCase("new") && !ClientToServer.user.getCall().equalsIgnoreCase("low") && !ClientToServer.user.getCall().equalsIgnoreCase("fold"))
                {
                    Toast.makeText(LobbyScreen.this, "Not available right now", Toast.LENGTH_LONG).show();
                }
                else{
                    showAddCoinPopUp(true);
                }
            }
        });

        LSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                addcoinAmount = LSlider.getValue();
                LBuyin.setText("Chips: "+BoardChoosing.modifyChipsString(addcoinAmount));
            }
        });

        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClientToServer.sendAddBoardCoinInGameRequest((long) addcoinAmount);
                    showAddCoinPopUp(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ////Player Profile Pop Up when Image of player pressed

        imgPlayerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPopUpWindows(1);
            }
        });
        imgPlayerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPopUpWindows(2);
            }
        });
        imgPlayerFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPopUpWindows(3);
            }
        });
        imgPlayerFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPopUpWindows(4);
            }
        });
        imgPlayerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPopUpWindows(0);
            }
        });

        playerPopUpClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlayerWindowPopUp(false);
            }
        });


        Glide.with(this).load(ClientToServer.user.getImageLink()).into(imgPlayerOne);
        playerOne.setText(ClientToServer.user.getUsername());
        playerOneCoin.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getBoardCoin()));
        playerOneCoin2.setVisibility(View.GONE);

        initializeForBidAnimation();

    }

    public void goHome() throws JSONException {
        //inLobby = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        if(mCountDownTimer111!=null){
            mCountDownTimer111.cancel();
        }
        if(mCountDownTimer!=null) {
            mCountDownTimer.cancel();
        }
        //HomeScreen.isHomeThread = true;
        if(ClientToServer.isExit) {
            ClientToServer.requestExit();
            ClientToServer.isExit = false;
            ClientToServer.isAbort = false;
        }
        else {
            ClientToServer.requestAbort();
            ClientToServer.isAbort = false;
            ClientToServer.isExit = false;
        }

//        ClientToServer.user.deInitializeGameData();
//        finish();
//        Intent intent = new Intent(this, HomeScreen.class);
//        startActivity(intent);
    }

    private void goHome2(){
        inLobby = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        if(mCountDownTimer111!=null){
            mCountDownTimer111.cancel();
        }
        if(mCountDownTimer!=null) {
            mCountDownTimer.cancel();
        }
        HomeScreen.isHomeThread = true;
        ClientToServer.isAbort = false;
        ClientToServer.isExit = false;

        ClientToServer.user.deInitializeGameData();
        finish();
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    private void goLogin(){
        inLobby = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        if(mCountDownTimer111!=null) {
            mCountDownTimer111.cancel();
        }
        if(mCountDownTimer!=null) {
            mCountDownTimer.cancel();
        }
        Login.isLoginThread = true;
        ClientToServer.isAbort = false;
        ClientToServer.isExit = false;

        ClientToServer.user.deInitializeGameData();
        finish();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void resetBoardMyselfCard(){
       // playerOneCard1.setImageResource(android.R.color.transparent);
      //  playerOneCard2.setImageResource(android.R.color.transparent);
        card1.setImageResource(android.R.color.transparent);
        card2.setImageResource(android.R.color.transparent);
        card3.setImageResource(android.R.color.transparent);
        card4.setImageResource(android.R.color.transparent);
        card5.setImageResource(android.R.color.transparent);
    }

    private void cmdMessage(){
        cmd.setText(Messeage);
        cmd.setVisibility(View.VISIBLE);

        CountDownTimer mCountDownTimer;
        timerCounter2 = 0;
        mCountDownTimer=new CountDownTimer(5000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerCounter2++;
            }

            @Override
            public void onFinish() {
                //Do what you want
                timerCounter2++;
                cmd.setVisibility(View.GONE);
            }
        };
        mCountDownTimer.start();
    }

    private void progressAnimation(ProgressBar mProgressBar){
        if(timerCountIdx>=19) timerCountIdx=0;
        else timerCountIdx++;
        timerCounter[timerCountIdx] = 0;
        mProgressBar.setProgress(0);

        mCountDownTimer=new CountDownTimer(20000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerCounter[timerCountIdx]++;
                mProgressBar.setProgress((int) timerCounter[timerCountIdx] *100/(20000/1000));

            }

            @Override
            public void onFinish() {
                //Do what you want
                timerCounter[timerCountIdx]++;
                mProgressBar.setProgress(100);
                if(isMyTurn){
                    new Thread() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showRaisewindow(false);
                                }
                            });
                            try {
                                MainActivity.clientToServer.clickedFoldButton();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        };
        mCountDownTimer.start();
    }

    private void showAllPlayerCards(){

        showProgressBar(-1);
        showEveryPlayerCard(true);

        CountDownTimer mCountDownTimer;
        timerCounter2 = 0;
        mCountDownTimer=new CountDownTimer(5000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerCounter2++;
            }

            @Override
            public void onFinish() {
                //Do what you want
                timerCounter2++;
                showEveryPlayerCard(false);
            }
        };
        mCountDownTimer.start();
    }

    private void showPlayerAction(boolean isActive){
        if(isActive){
            if(isCallAvailable) {
                btnCall.setImageResource(R.drawable.call);
                btnCall.setVisibility(View.VISIBLE);
                callbtnvalue.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getRoundCall()));
                callbtnvalue.setVisibility(View.VISIBLE);
            }
            else if(isCheckAvailable){
                btnCall.setImageResource(R.drawable.check);
                btnCall.setVisibility(View.VISIBLE);
                callbtnvalue.setVisibility(View.GONE);
            }
            if(isFoldAvailable){
                btnFold.setVisibility(View.VISIBLE);
            }
            if(isRaiseAvailable && raiseCost>0) {
                btnRaise.setVisibility(View.VISIBLE);
            }
            if(isAllinAvailable) btnAllin.setVisibility(View.VISIBLE);
        }
        else{
            btnFold.setVisibility(View.GONE);
            btnRaise.setVisibility(View.GONE);
            btnAllin.setVisibility(View.GONE);
            btnCall.setVisibility(View.GONE);
            callbtnvalue.setVisibility(View.GONE);
        }
    }

    private void showEveryPlayerCard(boolean isRoundEnd){
        loadPlayerData();
        if(isRoundEnd){
            String c1 = "1.2";
            String[] x;
            if(!playerDetails[1].getUsername().equalsIgnoreCase("Empty")) {
                c1 = playerDetails[1].getCard1();
                if(c1!=null) {
                    x = c1.split("\\.");
                    setCard(Integer.parseInt(x[0]), Integer.parseInt(x[1]), playerTwoCard1);
                }
            }
            if(!playerDetails[1].getUsername().equalsIgnoreCase("Empty")) {
                c1 = playerDetails[1].getCard2();
                if(c1!=null) {
                    x = c1.split("\\.");
                    setCard(Integer.parseInt(x[0]), Integer.parseInt(x[1]), playerTwoCard2);
                }
            }
            if(!playerDetails[2].getUsername().equalsIgnoreCase("Empty")) {
                c1 = playerDetails[2].getCard1();
                if(c1!=null) {
                    x = c1.split("\\.");
                    setCard(Integer.parseInt(x[0]), Integer.parseInt(x[1]), playerThreeCard1);
                }
            }
            if(!playerDetails[2].getUsername().equalsIgnoreCase("Empty")) {
                c1 = playerDetails[2].getCard2();
                if(c1!=null) {
                    x = c1.split("\\.");
                    setCard(Integer.parseInt(x[0]), Integer.parseInt(x[1]), playerThreeCard2);
                }
            }
            if(!playerDetails[3].getUsername().equalsIgnoreCase("Empty")) {
                c1 = playerDetails[3].getCard1();
                if(c1!=null) {
                    x = c1.split("\\.");
                    setCard(Integer.parseInt(x[0]), Integer.parseInt(x[1]), playerFourCard1);
                }
            }
            if(!playerDetails[3].getUsername().equalsIgnoreCase("Empty")) {
                c1 = playerDetails[3].getCard2();
                if(c1!=null) {
                    x = c1.split("\\.");
                    setCard(Integer.parseInt(x[0]), Integer.parseInt(x[1]), playerFourCard2);
                }
            }
            if(!playerDetails[4].getUsername().equalsIgnoreCase("Empty")) {
                c1 = playerDetails[4].getCard1();
                if(c1!=null) {
                    x = c1.split("\\.");
                    setCard(Integer.parseInt(x[0]), Integer.parseInt(x[1]), playerFiveCard1);
                }
            }
            if(!playerDetails[4].getUsername().equalsIgnoreCase("Empty")) {
                c1 = playerDetails[4].getCard2();
                if(c1!=null) {
                    x = c1.split("\\.");
                    setCard(Integer.parseInt(x[0]), Integer.parseInt(x[1]), playerFiveCard2);
                }
            }
        }
        else{
            playerTwoCard1.setImageResource(android.R.color.transparent);
            playerTwoCard2.setImageResource(android.R.color.transparent);
            playerThreeCard1.setImageResource(android.R.color.transparent);
            playerThreeCard2.setImageResource(android.R.color.transparent);
            playerFourCard1.setImageResource(android.R.color.transparent);
            playerFourCard2.setImageResource(android.R.color.transparent);
            playerFiveCard1.setImageResource(android.R.color.transparent);
            playerFiveCard2.setImageResource(android.R.color.transparent);
        }
    }

    private void showProgressBar(int id){
        if(id==1){
            playerOnePbar.setAlpha((float) 0.7);
            playerTwoPbar.setAlpha(0);
            playerThreePbar.setAlpha(0);
            playerFourPbar.setAlpha(0);
            playerFivePbar.setAlpha(0);
            progressAnimation(playerOnePbar);
        }
        else if(id==2){
            playerOnePbar.setAlpha(0);
            playerTwoPbar.setAlpha((float)0.7);
            playerThreePbar.setAlpha(0);
            playerFourPbar.setAlpha(0);
            playerFivePbar.setAlpha(0);
            progressAnimation(playerTwoPbar);
        }
        else if(id==3){
            playerOnePbar.setAlpha(0);
            playerTwoPbar.setAlpha(0);
            playerThreePbar.setAlpha((float)0.7);
            playerFourPbar.setAlpha(0);
            playerFivePbar.setAlpha(0);
            progressAnimation(playerThreePbar);
        }
        else if(id==4){
            playerOnePbar.setAlpha(0);
            playerTwoPbar.setAlpha(0);
            playerThreePbar.setAlpha(0);
            playerFourPbar.setAlpha((float)0.7);
            playerFivePbar.setAlpha(0);
            progressAnimation(playerFourPbar);
        }
        else if(id==5){
            playerOnePbar.setAlpha(0);
            playerTwoPbar.setAlpha(0);
            playerThreePbar.setAlpha(0);
            playerFourPbar.setAlpha(0);
            playerFivePbar.setAlpha((float)0.7);
            progressAnimation(playerFivePbar);
        }
        else if(id==-1){
            playerOnePbar.setAlpha(0);
            playerTwoPbar.setAlpha(0);
            playerThreePbar.setAlpha(0);
            playerFourPbar.setAlpha(0);
            playerFivePbar.setAlpha(0);
        }
    }

    private void showRaisewindow(boolean act){
        if(act){
            raiseback.setVisibility(View.VISIBLE);
            //slider.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            btnAllin2.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(View.VISIBLE);
        }
        else{
            raiseback.setVisibility(View.GONE);
            //slider.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
            btnAllin2.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);

        }
    }


    private void setPlayerInSeat(){
        playerOne.setText(playerDetails[0].getUsername());
        playerTwo.setText(playerDetails[1].getUsername());
        playerThree.setText(playerDetails[2].getUsername());
        playerFour.setText(playerDetails[3].getUsername());
        playerFive.setText(playerDetails[4].getUsername());

        //images
        if(!playerDetails[0].getUsername().equalsIgnoreCase("Empty")){
            Glide.with(this).load(String.valueOf(playerDetails[0].getImgLink())).into(imgPlayerOne);
        }
        else{
            imgPlayerOne.setImageResource(R.drawable.emptyplayer);
        }
        if(!playerDetails[1].getUsername().equalsIgnoreCase("Empty")){
            Glide.with(this).load(String.valueOf(playerDetails[1].getImgLink())).into(imgPlayerTwo);
        }
        else{
            imgPlayerTwo.setImageResource(R.drawable.emptyplayer);
        }
        if(!playerDetails[2].getUsername().equalsIgnoreCase("Empty")){
            Glide.with(this).load(String.valueOf(playerDetails[2].getImgLink())).into(imgPlayerThree);
        }
        else{
            imgPlayerThree.setImageResource(R.drawable.emptyplayer);
        }
        if(!playerDetails[3].getUsername().equalsIgnoreCase("Empty")){
            Glide.with(this).load(String.valueOf(playerDetails[3].getImgLink())).into(imgPlayerFour);
        }
        else{
            imgPlayerFour.setImageResource(R.drawable.emptyplayer);
        }
        if(!playerDetails[4].getUsername().equalsIgnoreCase("Empty")){
            Glide.with(this).load(String.valueOf(playerDetails[4].getImgLink())).into(imgPlayerFive);
        }
        else{
            imgPlayerFive.setImageResource(R.drawable.emptyplayer);
        }

    }

    private void updatePlayerCoin(){
        playerOneCoin.setText(BoardChoosing.modifyChipsString(playerDetails[0].getTotalCoin()));
        playerTwoCoin.setText(BoardChoosing.modifyChipsString(playerDetails[1].getTotalCoin()));
        playerThreeCoin.setText(BoardChoosing.modifyChipsString(playerDetails[2].getTotalCoin()));
        playerFourCoin.setText(BoardChoosing.modifyChipsString(playerDetails[3].getTotalCoin()));
        playerFiveCoin.setText(BoardChoosing.modifyChipsString(playerDetails[4].getTotalCoin()));
    }

    private void setMyCards(){
        playerOneCard1.setVisibility(View.GONE);
        playerOneCard2.setVisibility(View.GONE);

        ArrayList<Card> cards = ClientToServer.user.getPlayerCards();
        int s1 = cards.get(0).getSuit();
        int v1 = cards.get(0).getValue();
        System.out.println("MyCard1: "+s1+"."+v1);
        setCard(s1, v1, playerOneCard1);

        int s2 = cards.get(1).getSuit();
        int v2 = cards.get(1).getValue();
        System.out.println("MyCard2: "+s2+"."+v2);
        setCard(s2, v2, playerOneCard2);

        animateCard(mycard1, playerOneCard1);
        animateCard(mycard2, playerOneCard2);

//        playerOneCard1.setVisibility(View.VISIBLE);
//        playerOneCard2.setVisibility(View.VISIBLE);
//        mycard1.setVisibility(View.GONE);
//        mycard2.setVisibility(View.GONE);
    }

    private void animateCard(ImageView card, ImageView cardHolder){
        CountDownTimer mCountDownTimer;
        timerCounter2 = 0;
        mCountDownTimer=new CountDownTimer(400,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerCounter2++;
            }

            @Override
            public void onFinish() {
                //Do what you want
                card.setVisibility(View.VISIBLE);
                timerCounter2++;
                card.animate()
                        .x(cardHolder.getX())
                        .y(cardHolder.getY())
                        .setDuration(1000)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                card.setX(cardHolder.getX());
                                card.setY(cardHolder.getY());
                                card.setVisibility(View.GONE);
                                cardHolder.setVisibility(View.VISIBLE);
                            }
                        }).start();
            }
        };

        card.setVisibility(View.GONE);
        card.animate()
                .x(roundMoney.getX())
                .y(roundMoney.getY())
                .setDuration(100)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        card.setX(roundMoney.getX());
                        card.setY(roundMoney.getY());
                        mCountDownTimer.start();
                    }
                }).start();
    }

    private void setBoardCard(){
        CountDownTimer mCountDownTimer;
        timerCounter2 = 0;
        mCountDownTimer=new CountDownTimer(400,100) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerCounter2++;
            }

            @Override
            public void onFinish() {
                //Do what you want
                timerCounter2++;
            }
        };
        mCountDownTimer.start();


        ArrayList<Card> cards = MainActivity.clientToServer.user.getBoardCards();

        for(int i=0; i<cards.size(); i++){
            if(i==0){
                int s1 = cards.get(0).getSuit();
                int v1 = cards.get(0).getValue();
                setCard(s1, v1, card1);
            }
            else if(i==1){
                int s1 = cards.get(1).getSuit();
                int v1 = cards.get(1).getValue();
                setCard(s1, v1, card2);
            }
            else if(i==2){
                int s1 = cards.get(2).getSuit();
                int v1 = cards.get(2).getValue();
                setCard(s1, v1, card3);
            }
            else if(i==3){
                int s1 = cards.get(3).getSuit();
                int v1 = cards.get(3).getValue();
                setCard(s1, v1, card4);
            }
            else if(i==4){
                int s1 = cards.get(4).getSuit();
                int v1 = cards.get(4).getValue();
                setCard(s1, v1, card5);
            }
        }
    }


    private void setCard(int s1, int v1, ImageView playerOneCard1){

        if(s1==1){
            if(v1==2) playerOneCard1.setImageResource(R.drawable.h2);
            else if(v1==3) playerOneCard1.setImageResource(R.drawable.h3);
            else if(v1==4) playerOneCard1.setImageResource(R.drawable.h4);
            else if(v1==5) playerOneCard1.setImageResource(R.drawable.h5);
            else if(v1==6) playerOneCard1.setImageResource(R.drawable.h6);
            else if(v1==7) playerOneCard1.setImageResource(R.drawable.h7);
            else if(v1==8) playerOneCard1.setImageResource(R.drawable.h8);
            else if(v1==9) playerOneCard1.setImageResource(R.drawable.h9);
            else if(v1==10) playerOneCard1.setImageResource(R.drawable.h10);
            else if(v1==11) playerOneCard1.setImageResource(R.drawable.hjoker);
            else if(v1==12) playerOneCard1.setImageResource(R.drawable.hqueen);
            else if(v1==13) playerOneCard1.setImageResource(R.drawable.hking);
            else if(v1==14) playerOneCard1.setImageResource(R.drawable.hace);
        }
        else if(s1==2){
            if(v1==2) playerOneCard1.setImageResource(R.drawable.d2);
            else if(v1==3) playerOneCard1.setImageResource(R.drawable.d3);
            else if(v1==4) playerOneCard1.setImageResource(R.drawable.d4);
            else if(v1==5) playerOneCard1.setImageResource(R.drawable.d5);
            else if(v1==6) playerOneCard1.setImageResource(R.drawable.d6);
            else if(v1==7) playerOneCard1.setImageResource(R.drawable.d7);
            else if(v1==8) playerOneCard1.setImageResource(R.drawable.d8);
            else if(v1==9) playerOneCard1.setImageResource(R.drawable.d9);
            else if(v1==10) playerOneCard1.setImageResource(R.drawable.d10);
            else if(v1==11) playerOneCard1.setImageResource(R.drawable.djoker);
            else if(v1==12) playerOneCard1.setImageResource(R.drawable.dqueen);
            else if(v1==13) playerOneCard1.setImageResource(R.drawable.dking);
            else if(v1==14) playerOneCard1.setImageResource(R.drawable.dace);
        }
        else if(s1==3){
            if(v1==2) playerOneCard1.setImageResource(R.drawable.c2);
            else if(v1==3) playerOneCard1.setImageResource(R.drawable.c3);
            else if(v1==4) playerOneCard1.setImageResource(R.drawable.c4);
            else if(v1==5) playerOneCard1.setImageResource(R.drawable.c5);
            else if(v1==6) playerOneCard1.setImageResource(R.drawable.c6);
            else if(v1==7) playerOneCard1.setImageResource(R.drawable.c7);
            else if(v1==8) playerOneCard1.setImageResource(R.drawable.c8);
            else if(v1==9) playerOneCard1.setImageResource(R.drawable.c9);
            else if(v1==10) playerOneCard1.setImageResource(R.drawable.c10);
            else if(v1==11) playerOneCard1.setImageResource(R.drawable.cjoker);
            else if(v1==12) playerOneCard1.setImageResource(R.drawable.cqueen);
            else if(v1==13) playerOneCard1.setImageResource(R.drawable.cking);
            else if(v1==14) playerOneCard1.setImageResource(R.drawable.cace);
        }
        else if(s1==4){
            if(v1==2) playerOneCard1.setImageResource(R.drawable.s2);
            else if(v1==3) playerOneCard1.setImageResource(R.drawable.s3);
            else if(v1==4) playerOneCard1.setImageResource(R.drawable.s4);
            else if(v1==5) playerOneCard1.setImageResource(R.drawable.s5);
            else if(v1==6) playerOneCard1.setImageResource(R.drawable.s6);
            else if(v1==7) playerOneCard1.setImageResource(R.drawable.s7);
            else if(v1==8) playerOneCard1.setImageResource(R.drawable.s8);
            else if(v1==9) playerOneCard1.setImageResource(R.drawable.s9);
            else if(v1==10) playerOneCard1.setImageResource(R.drawable.s10);
            else if(v1==11) playerOneCard1.setImageResource(R.drawable.sjoker);
            else if(v1==12) playerOneCard1.setImageResource(R.drawable.squeen);
            else if(v1==13) playerOneCard1.setImageResource(R.drawable.sking);
            else if(v1==14) playerOneCard1.setImageResource(R.drawable.sace);
        }
    }

//
    private void showTurnInfo(){
        disableInactivePlayer();

        if(playerDetails[1].getPlayerCallValue()>0){
            playerTwoBid.setText(BoardChoosing.modifyChipsString(playerDetails[1].getPlayerCallValue()));
            playerBid2.setText(BoardChoosing.modifyChipsString(playerDetails[1].getPresentCallValue()));
            playerTwoBid.setAlpha(1);
        }
        else playerTwoBid.setAlpha(0);

        if(playerDetails[2].getPlayerCallValue()>0){
            playerThreeBid.setText(BoardChoosing.modifyChipsString(playerDetails[2].getPlayerCallValue()));
            playerBid3.setText(BoardChoosing.modifyChipsString(playerDetails[2].getPresentCallValue()));
            playerThreeBid.setAlpha(1);
        }
        else playerThreeBid.setAlpha(0);

        if(playerDetails[3].getPlayerCallValue()>0){
            playerFourBid.setText(BoardChoosing.modifyChipsString(playerDetails[3].getPlayerCallValue()));
            playerBid4.setText(BoardChoosing.modifyChipsString(playerDetails[3].getPresentCallValue()));
            playerFourBid.setAlpha(1);
        }
        else playerFourBid.setAlpha(0);

        if(playerDetails[4].getPlayerCallValue()>0){
            playerFiveBid.setText(BoardChoosing.modifyChipsString(playerDetails[4].getPlayerCallValue()));
            playerBid5.setText(BoardChoosing.modifyChipsString(playerDetails[4].getPresentCallValue()));
            playerFiveBid.setAlpha(1);
        }
        else playerFiveBid.setAlpha(0);

        roundMoney.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getRoundCoins()));

    }

    private void showStatus(boolean is){
        if(is){
            playerOneStatus.setAlpha(1);
            playerTwoStatus.setAlpha(1);
            playerThreeStatus.setAlpha(1);
            playerFourStatus.setAlpha(1);
            playerFiveStatus.setAlpha(1);
        }
        else{
            playerOneStatus.setAlpha(0);
            playerTwoStatus.setAlpha(0);
            playerThreeStatus.setAlpha(0);
            playerFourStatus.setAlpha(0);
            playerFiveStatus.setAlpha(0);
        }
    }

    private void showBlindStatus(){
        if(smallBlindSeat==0){
            playerOneStatus.setText("Small Blind");
            playerOneStatus.setAlpha(1);
            playerOneCoin2.setText(BoardChoosing.modifyChipsString(smallBlindDeduct));
        }
        else if(smallBlindSeat==1){
            playerTwoStatus.setText("Small Blind");
            playerTwoStatus.setAlpha(1);
            playerBid2.setText(BoardChoosing.modifyChipsString(smallBlindDeduct));
            playerTwoBid.setText(BoardChoosing.modifyChipsString(smallBlindDeduct));
        }
        else if(smallBlindSeat==2){
            playerThreeStatus.setText("Small Blind");
            playerThreeStatus.setAlpha(1);
            playerBid3.setText(BoardChoosing.modifyChipsString(smallBlindDeduct));
            playerThreeBid.setText(BoardChoosing.modifyChipsString(smallBlindDeduct));

        }
        else if(smallBlindSeat==3){
            playerFourStatus.setText("Small Blind");
            playerFourStatus.setAlpha(1);
            playerBid4.setText(BoardChoosing.modifyChipsString(smallBlindDeduct));
            playerFourBid.setText(BoardChoosing.modifyChipsString(smallBlindDeduct));

        }
        else if(smallBlindSeat==4){
            playerFiveStatus.setText("Small Blind");
            playerFiveStatus.setAlpha(1);
            playerBid5.setText(BoardChoosing.modifyChipsString(smallBlindDeduct));
            playerFiveBid.setText(BoardChoosing.modifyChipsString(smallBlindDeduct));

        }

        if(bigBlindSeat==0){
            playerOneStatus.setText("Big Blind");
            playerOneStatus.setAlpha(1);
            playerOneCoin2.setText(BoardChoosing.modifyChipsString(bigBlindDeduct));
        }
        else if(bigBlindSeat==1){
            playerTwoStatus.setText("Big Blind");
            playerTwoStatus.setAlpha(1);
            playerBid2.setText(BoardChoosing.modifyChipsString(bigBlindDeduct));
            playerTwoBid.setText(BoardChoosing.modifyChipsString(bigBlindDeduct));
        }
        else if(bigBlindSeat==2){
            playerThreeStatus.setText("Big Blind");
            playerThreeStatus.setAlpha(1);
            playerBid3.setText(BoardChoosing.modifyChipsString(bigBlindDeduct));
            playerThreeBid.setText(BoardChoosing.modifyChipsString(bigBlindDeduct));
        }
        else if(bigBlindSeat==3){
            playerFourStatus.setText("Big Blind");
            playerFourStatus.setAlpha(1);
            playerBid4.setText(BoardChoosing.modifyChipsString(bigBlindDeduct));
            playerFourBid.setText(BoardChoosing.modifyChipsString(bigBlindDeduct));
        }
        else if(bigBlindSeat==4){
            playerFiveStatus.setText("Big Blind");
            playerFiveStatus.setAlpha(1);
            playerBid5.setText(BoardChoosing.modifyChipsString(bigBlindDeduct));
            playerFiveBid.setText(BoardChoosing.modifyChipsString(bigBlindDeduct));
        }
    }

    private void disableInactivePlayer(){
        playerOneStatus.setAlpha(0);
        if(playerDetails[1].getUsername().equalsIgnoreCase("Empty")){
            isPlayerTwoHand.setImageResource(android.R.color.transparent);
            playerTwoBid.setAlpha(0);
            playerTwoCoin.setAlpha(0);
            playerTwo.setAlpha(0);
            playerTwoStatus.setAlpha(0);
        }
        else{
            if(playerDetails[1].getPlayerCall().equalsIgnoreCase("fold")){
                isPlayerTwoHand.setImageResource(android.R.color.transparent);
                playerTwoStatus.setAlpha(1);
                playerTwoStatus.setText("Fold");
            }
            else{
                isPlayerTwoHand.setImageResource(R.drawable.ishand);
                playerTwoStatus.setAlpha(1);
                playerTwoStatus.setText(playerDetails[1].getPlayerCall());
            }
            playerTwoBid.setAlpha(1);
            playerTwoCoin.setAlpha(1);
            playerTwo.setAlpha(1);
        }

        if(playerDetails[2].getUsername().equalsIgnoreCase("Empty")){
            isPlayerThreeHand.setImageResource(android.R.color.transparent);
            playerThreeBid.setAlpha(0);
            playerThreeCoin.setAlpha(0);
            playerThree.setAlpha(0);
            playerThreeStatus.setAlpha(0);
        }
        else{
            if(playerDetails[2].getPlayerCall().equalsIgnoreCase("fold")){
                isPlayerThreeHand.setImageResource(android.R.color.transparent);
                playerThreeStatus.setAlpha(1);
                playerThreeStatus.setText("Fold");
            }
            else {
                isPlayerThreeHand.setImageResource(R.drawable.ishand);
                playerThreeStatus.setAlpha(1);
                playerThreeStatus.setText(playerDetails[2].getPlayerCall());
            }
            playerThreeBid.setAlpha(1);
            playerThreeCoin.setAlpha(1);
            playerThree.setAlpha(1);
        }

        if(playerDetails[3].getUsername().equalsIgnoreCase("Empty")){
            isPlayerFourHand.setImageResource(android.R.color.transparent);
            playerFourBid.setAlpha(0);
            playerFourCoin.setAlpha(0);
            playerFour.setAlpha(0);
            playerFourStatus.setAlpha(0);
        }
        else{
            if(playerDetails[3].getPlayerCall().equalsIgnoreCase("fold")){
                isPlayerFourHand.setImageResource(android.R.color.transparent);
                playerFourStatus.setAlpha(1);
                playerFourStatus.setText("Fold");
            }
            else{
                isPlayerFourHand.setImageResource(R.drawable.ishand);
                playerFourStatus.setAlpha(1);
                playerFourStatus.setText(playerDetails[3].getPlayerCall());
            }
            playerFourBid.setAlpha(1);
            playerFourCoin.setAlpha(1);
            playerFour.setAlpha(1);
        }

        if(playerDetails[4].getUsername().equalsIgnoreCase("Empty")){
            isPlayerFiveHand.setImageResource(android.R.color.transparent);
            playerFiveBid.setAlpha(0);
            playerFiveCoin.setAlpha(0);
            playerFive.setAlpha(0);
            playerFiveStatus.setAlpha(0);
        }
        else{
            if(playerDetails[4].getPlayerCall().equalsIgnoreCase("fold")){
                isPlayerFiveHand.setImageResource(android.R.color.transparent);
                playerFiveStatus.setAlpha(1);
                playerFiveStatus.setText("Fold");
            }
            else {
                isPlayerFiveHand.setImageResource(R.drawable.ishand);
                playerFiveStatus.setAlpha(1);
                playerFiveStatus.setText(playerDetails[4].getPlayerCall());
            }
            playerFiveBid.setAlpha(1);
            playerFiveCoin.setAlpha(1);
            playerFive.setAlpha(1);
        }
    }

    private void setActivePlayer(){
        if(mCountDownTimer!=null) mCountDownTimer.cancel();
        if(PlayerWithTurnID==1){
            //showPlayerAction(true);
            showProgressBar(1);
            isMyTurn = true;
        }
        else{
            showProgressBar(PlayerWithTurnID);
            showPlayerAction(false);
            isMyTurn = false;
        }
    }

    private void prevPlayerCallAnim(){
        if(prevPlayerWithTurnID>0){
            if(prevPlayerCall.equalsIgnoreCase("call") || prevPlayerCall.equalsIgnoreCase("raise")) {

                showBidAnimation(prevPlayerWithTurnID);
            }
        }
    }


    private void loadPlayerData(){
        int myid = ClientToServer.user.getSeatPosition();

        for(int i=1; i<5; i++) playerDetails[i].setEmpty();

        playerDetails[0].setUsername(ClientToServer.user.getUsername());
        playerDetails[0].setPlayerCallValue(ClientToServer.user.getTotalCallValue());
        playerDetails[0].setPlayerCall(ClientToServer.user.getCall());
        playerDetails[0].setTotalCoin(ClientToServer.user.getBoardCoin());
        playerDetails[0].setImgLink(ClientToServer.user.getImageLink());
        playerDetails[0].setLoginMethod(ClientToServer.user.getLoginMethod());
        playerDetails[0].setId(ClientToServer.user.getId());
        playerDetails[0].setPresentCallValue(ClientToServer.user.getCallValue());

        ArrayList<Card> cardsusr = ClientToServer.user.getPlayerCards();
        if (cardsusr.size() == 2) {
            String card1 = cardsusr.get(0).toString2();
            String card2 = cardsusr.get(1).toString2();
            playerDetails[0].setCard1(card1);
            playerDetails[0].setCard2(card2);
        }

        User[] players = ClientToServer.user.getInGamePlayers();

        if(players!=null) {
            for (int i = 0; i < players.length; i++) {
                if (players[i] != null) {
                    if(players[i].getLoginMethod().equalsIgnoreCase("guest") && players[i].getUsername().equalsIgnoreCase(ClientToServer.user.getUsername())) continue;
                    else if(!players[i].getLoginMethod().equalsIgnoreCase("guest") && players[i].getId()==ClientToServer.user.getId()) continue;
                    int id = getId(players[i].getSeatPosition(), myid) - 1;
                    playerDetails[id].setUsername(players[i].getUsername());
                    playerDetails[id].setPlayerCallValue(players[i].getTotalCallValue());
                    playerDetails[id].setPlayerCall(players[i].getCall());
                    playerDetails[id].setTotalCoin(players[i].getBoardCoin());
                    playerDetails[id].setImgLink(players[i].getImageLink());
                    playerDetails[id].setLoginMethod(players[i].getLoginMethod());
                    playerDetails[id].setId(players[i].getId());
                    playerDetails[id].setPresentCallValue(players[i].getCallValue());

                    ArrayList<Card> cards = players[i].getPlayerCards();
                    if (cards.size() == 2) {
                        String card1 = cards.get(0).toString2();
                        String card2 = cards.get(1).toString2();
                        playerDetails[id].setCard1(card1);
                        playerDetails[id].setCard2(card2);
                    }

                }
            }
        }
    }


    private static int getId(int id, int myId){
        if(id==myId) return 1;
        else if(id<myId){
            return (6-(myId-id));
        }
        else{
            return (1+(id-myId));
        }
    }

    private void initializeRound(){
        playerOneCard1.setImageResource(android.R.color.transparent);
        playerOneCard2.setImageResource(android.R.color.transparent);
        playerTwoCard1.setImageResource(android.R.color.transparent);
        playerTwoCard2.setImageResource(android.R.color.transparent);
        playerThreeCard1.setImageResource(android.R.color.transparent);
        playerThreeCard2.setImageResource(android.R.color.transparent);
        playerFourCard1.setImageResource(android.R.color.transparent);
        playerFourCard2.setImageResource(android.R.color.transparent);
        playerFiveCard1.setImageResource(android.R.color.transparent);
        playerFiveCard2.setImageResource(android.R.color.transparent);

        card1.setImageResource(android.R.color.transparent);
        card2.setImageResource(android.R.color.transparent);
        card3.setImageResource(android.R.color.transparent);
        card4.setImageResource(android.R.color.transparent);
        card5.setImageResource(android.R.color.transparent);
    }

    private void startSufleSound(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sufle);
        mediaPlayer.start();
    }

    private void showPlayerWindowPopUp(boolean isActive){
        if(isActive){
            playerPopUpWindow.setVisibility(View.VISIBLE);

        }
        else{
            playerPopUpWindow.setVisibility(View.GONE);
        }

    }

    private void setPopUpWindows(int id){
        if(id==0){
            showPlayerWindowPopUp(true);
            BestWinningHand2.setText(String.valueOf(ClientToServer.user.getBestHand()));
            biggestPotWin2.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getBiggestWin()));
            String wp = String.valueOf(ClientToServer.user.getWinPercentage());
            String str = "";
            if(wp.length()>=5){
                str = wp.substring(0, 5);
            }
            else{
                str = wp;
            }
            WinPercentage2.setText(str+"%");
            HandsPlayed2.setText(String.valueOf(ClientToServer.user.getRoundsPlayed()));
            usernamePM2.setText(String.valueOf(ClientToServer.user.getUsername()));
            rankinhome2.setText("LV " + String.valueOf(ClientToServer.user.getLevel()) + " #" + String.valueOf(ClientToServer.user.getRank()));
            coinWon.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getCoinWon()) + "/" + BoardChoosing.modifyChipsString(ClientToServer.user.getCoinLost()));
            winStreak.setText(String.valueOf(ClientToServer.user.getWinStreak()));
            Glide.with(this).load(String.valueOf(playerDetails[id].getImgLink())).into(propicPM2);
        }
        else if(!playerDetails[id].getUsername().equalsIgnoreCase("Empty")){
            showPlayerWindowPopUp(true);
            User[] players = ClientToServer.user.getInGamePlayers();
            if(players!=null) {
                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null) {

                        if (players[i].getLoginMethod().equalsIgnoreCase("guest") &&  players[i].getUsername().equalsIgnoreCase(playerDetails[id].getUsername())) {
                            BestWinningHand2.setText(String.valueOf(players[i].getBestHand()));
                            biggestPotWin2.setText(BoardChoosing.modifyChipsString(players[i].getBiggestWin()));
                            String wp = String.valueOf(players[i].getWinPercentage());
                            String str = "";
                            if(wp.length()>=5){
                                str = wp.substring(0, 5);
                            }
                            else{
                                str = wp;
                            }
                            WinPercentage2.setText(str+"%");
                            HandsPlayed2.setText(String.valueOf(players[i].getRoundsPlayed()));
                            usernamePM2.setText(String.valueOf(players[i].getUsername()));
                            rankinhome2.setText("LV " + String.valueOf(players[i].getLevel()) + " #" + String.valueOf(players[i].getRank()));
                            coinWon.setText(BoardChoosing.modifyChipsString(players[i].getCoinWon()) + "/" + BoardChoosing.modifyChipsString(players[i].getCoinLost()));
                            winStreak.setText(String.valueOf(players[i].getWinStreak()));
                            Glide.with(this).load(String.valueOf(playerDetails[id].getImgLink())).into(propicPM2);
                        }
                        else if(!players[i].getLoginMethod().equalsIgnoreCase("guest") && players[i].getId()==playerDetails[id].getId()){
                            BestWinningHand2.setText(String.valueOf(players[i].getBestHand()));
                            biggestPotWin2.setText(BoardChoosing.modifyChipsString(players[i].getBiggestWin()));
                            String wp = String.valueOf(players[i].getWinPercentage());
                            String str = "";
                            if(wp.length()>=5){
                                str = wp.substring(0, 5);
                            }
                            else{
                                str = wp;
                            }
                            WinPercentage2.setText(str+"%");
                            HandsPlayed2.setText(String.valueOf(players[i].getRoundsPlayed()));
                            usernamePM2.setText(String.valueOf(players[i].getUsername()));
                            rankinhome2.setText("LV " + String.valueOf(players[i].getLevel()) + " #" + String.valueOf(players[i].getRank()));
                            coinWon.setText(BoardChoosing.modifyChipsString(players[i].getCoinWon()) + "/" + BoardChoosing.modifyChipsString(players[i].getCoinLost()));
                            winStreak.setText(String.valueOf(players[i].getWinStreak()));
                            Glide.with(this).load(String.valueOf(playerDetails[id].getImgLink())).into(propicPM2);
                        }
                    }
                }
            }

        }
    }

    private void showLevelWinner(int i){
        Timerlevel = i;

        if(i<5) {
            if (winLevel[i].size() > 0) {
                String result = "";
                for (int j = 0; j < winLevel[i].size(); j++) {
                    int id = winLevel[i].get(j).getSeat();
                    if (id == 0) {
                        result += "YOU WON : " + BoardChoosing.modifyChipsString(winLevel[i].get(j).getWinamount()) + " (" + winLevel[i].get(j).getResultString() + ")\n";
                        playerOneStatus.setText(winLevel[i].get(j).getResultString());
                    } else {
                        result += playerDetails[id].getUsername() + " WON : " + BoardChoosing.modifyChipsString(winLevel[i].get(j).getWinamount()) + " " + winLevel[i].get(j).getResultString() + "\n";
                        if(id==1) playerTwoStatus.setText( winLevel[i].get(j).getResultString());
                        else if(id==2) playerThreeStatus.setText( winLevel[i].get(j).getResultString());
                        else if(id==3) playerFourStatus.setText( winLevel[i].get(j).getResultString());
                        else if(id==4) playerFiveStatus.setText( winLevel[i].get(j).getResultString());
                    }
                }

                boolean isAllfold = true;

                for(int k=0; k<ClientToServer.user.getInGamePlayers().length; k++){
                    if(ClientToServer.user.getInGamePlayers()[k]==null){
                        continue;
                    }
                    int sp = getId(ClientToServer.user.getInGamePlayers()[k].getSeatPosition(), ClientToServer.user.getSeatPosition())-1;
                    if(sp!=0 && !ClientToServer.user.getInGamePlayers()[k].getCall().equalsIgnoreCase("fold")){
                        isAllfold = false;
                    }
                }

                if(isAllfold){
                    result+="Others Folded";
                }

                myselfWinner.setText(result);
                myselfWinner.setVisibility(View.VISIBLE);
                showStatus(true);

                timerCounter2 = 0;
                mCountDownTimer11 = new CountDownTimer(3200, 200) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        timerCounter2++;
                    }

                    @Override
                    public void onFinish() {
                        //Do what you want
                        timerCounter2++;
                        myselfWinner.setVisibility(View.GONE);
                        showStatus(false);
                        loadPlayerData();
                        setPlayerInSeat();
                        updatePlayerCoin();
                        disableInactivePlayer();
                        resetBoardMyselfCard();

                        timerCounter2 = 0;
                        mCountDownTimer111 = new CountDownTimer(300, 100) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                timerCounter2++;
                            }

                            @Override
                            public void onFinish() {
                                //Do what you want
                                timerCounter2++;
                                showLevelWinner(Timerlevel+1);
                            }
                        };
                        mCountDownTimer111.start();
                    }
                };
                mCountDownTimer11.start();


            }
        }

    }

    private void showMyselfWinner(boolean is){
        if(is){
            showLevelWinner(0);
        }
        else{
            myselfWinner.setVisibility(View.GONE);
        }
    }

    private void opneRules(){
        Intent intent = new Intent(this, PokerHandRank.class);
        startActivity(intent);
    }

    private void showAddCoinPopUp(boolean is){
        if(is){
            addcoinAmount = 0;
            long startvalue = Math.max(ClientToServer.minEntryValue[boardType]-ClientToServer.user.getBoardCoin(), 0);
            long diff = ClientToServer.user.getCurrentCoin()-startvalue;
            if(diff>=ClientToServer.minCallValue[boardType]){
                LSlider.setValueFrom(startvalue);
                long v = ClientToServer.maxEntryValue[boardType] - ClientToServer.user.getBoardCoin();
                v = v/(ClientToServer.minCallValue[boardType]);
                v = Math.round(v);
                v = v*(ClientToServer.minCallValue[boardType]);
                LSlider.setValueTo(v);
                LSlider.setStepSize(ClientToServer.minCallValue[boardType]);
                LSlider.setValue(startvalue);
                Lbarstart.setText(BoardChoosing.modifyChipsString(startvalue));
                Lbarend.setText(BoardChoosing.modifyChipsString(v));
                LBuyin.setText("Amount: "+BoardChoosing.modifyChipsString(startvalue));
                addcoinAmount = startvalue;

                addCoinPopUp.setVisibility(View.VISIBLE);
            }
            else{
                Toast.makeText(LobbyScreen.this, "CHIPS amount is LOW, BUY CHIPS", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            addCoinPopUp.setVisibility(View.GONE);
        }
    }

    private void goConnectivity(){
        inLobby = false;
        Connectivity.isConnectivityThread = true;
        Intent intent = new Intent(this, Connectivity.class);
        startActivity(intent);
        finish();
    }

    private void initializeForBidAnimation(){
        CountDownTimer mCountDownTimer;
        timerCounter2 = 0;
        mCountDownTimer=new CountDownTimer(400,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerCounter2++;
            }

            @Override
            public void onFinish() {
                //Do what you want
                playerOneCoin2.setVisibility(View.GONE);
                timerCounter2++;
                playerOneCoin2.animate()
                        .x(playerOneCoin.getX())
                        .y(playerOneCoin.getY())
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                playerOneCoin2.setX(playerOneCoin.getX());
                                playerOneCoin2.setY(playerOneCoin.getY());
                            }
                        }).start();

                playerBid2.setVisibility(View.GONE);
                timerCounter2++;
                playerBid2.animate()
                        .x(playerTwoBid.getX())
                        .y(playerTwoBid.getY())
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                playerBid2.setX(playerTwoBid.getX());
                                playerBid2.setY(playerTwoBid.getY());
                            }
                        }).start();

                playerBid3.setVisibility(View.GONE);
                timerCounter2++;
                playerBid3.animate()
                        .x(playerThreeBid.getX())
                        .y(playerThreeBid.getY())
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                playerBid3.setX(playerThreeBid.getX());
                                playerBid3.setY(playerThreeBid.getY());
                                playerBid3.setVisibility(View.GONE);
                            }
                        }).start();

                playerBid4.setVisibility(View.GONE);
                timerCounter2++;
                playerBid4.animate()
                        .x(playerFourBid.getX())
                        .y(playerFourBid.getY())
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                playerBid4.setX(playerFourBid.getX());
                                playerBid4.setY(playerFourBid.getY());
                                playerBid4.setVisibility(View.GONE);
                            }
                        }).start();

                playerBid5.setVisibility(View.GONE);
                timerCounter2++;
                playerBid5.animate()
                        .x(playerFiveBid.getX())
                        .y(playerFiveBid.getY())
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                playerBid5.setX(playerFiveBid.getX());
                                playerBid5.setY(playerFiveBid.getY());
                                playerBid5.setVisibility(View.GONE);
                            }
                        }).start();
            }
        };
        mCountDownTimer.start();
    }

    private void showBidAnimation(int id){
        if(id==0){
            playerOneCoin2.setVisibility(View.VISIBLE);
            playerOneCoin2.animate()
                    .x(roundMoney.getX())
                    .y(roundMoney.getY())
                    .setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerOneCoin2.setX(roundMoney.getX());
                            playerOneCoin2.setY(roundMoney.getY());
                            playerOneCoin2.setVisibility(View.GONE);
                            roundMoney.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getRoundCoins()));

                        }
                    }).start();

            CountDownTimer mCountDownTimer;
            timerCounter2 = 0;
            mCountDownTimer=new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerCounter2++;
                }

                @Override
                public void onFinish() {
                    //Do what you want
                    playerOneCoin2.setVisibility(View.GONE);
                    timerCounter2++;
                    playerOneCoin2.animate()
                            .x(playerOneCoin.getX())
                            .y(playerOneCoin.getY())
                            .setDuration(1000)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    playerOneCoin2.setX(playerOneCoin.getX());
                                    playerOneCoin2.setY(playerOneCoin.getY());
                                }
                            }).start();
                }
            };
            mCountDownTimer.start();


        }
        else if(id==1){
            playerBid2.setVisibility(View.VISIBLE);
            playerBid2.setText(BoardChoosing.modifyChipsString(prevPlayerCallValue));
            playerBid2.animate()
                    .x(roundMoney.getX())
                    .y(roundMoney.getY())
                    .setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerBid2.setX(roundMoney.getX());
                            playerBid2.setY(roundMoney.getY());
                            playerBid2.setVisibility(View.GONE);
                            roundMoney.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getRoundCoins()));

                        }
                    }).start();

            CountDownTimer mCountDownTimer;
            timerCounter2 = 0;
            mCountDownTimer=new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerCounter2++;
                }

                @Override
                public void onFinish() {
                    //Do what you want
                    playerBid2.setVisibility(View.GONE);
                    timerCounter2++;
                    playerBid2.animate()
                            .x(playerTwoBid.getX())
                            .y(playerTwoBid.getY())
                            .setDuration(1000)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    playerBid2.setX(playerTwoBid.getX());
                                    playerBid2.setY(playerTwoBid.getY());
                                }
                            }).start();
                }
            };
            mCountDownTimer.start();


        }
        else if(id==2){
            playerBid3.setVisibility(View.VISIBLE);
            playerBid3.setText(BoardChoosing.modifyChipsString(prevPlayerCallValue));
            playerBid3.animate()
                    .x(roundMoney.getX())
                    .y(roundMoney.getY())
                    .setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerBid3.setX(roundMoney.getX());
                            playerBid3.setY(roundMoney.getY());
                            playerBid3.setVisibility(View.GONE);
                            roundMoney.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getRoundCoins()));

                        }
                    }).start();

            CountDownTimer mCountDownTimer;
            timerCounter2 = 0;
            mCountDownTimer=new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerCounter2++;
                }

                @Override
                public void onFinish() {
                    //Do what you want
                    playerBid3.setVisibility(View.GONE);
                    timerCounter2++;
                    playerBid3.animate()
                            .x(playerThreeBid.getX())
                            .y(playerThreeBid.getY())
                            .setDuration(1000)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    playerBid3.setX(playerThreeBid.getX());
                                    playerBid3.setY(playerThreeBid.getY());
                                    playerBid3.setVisibility(View.GONE);
                                }
                            }).start();
                }
            };
            mCountDownTimer.start();
        }
        else if(id==3){
            playerBid4.setVisibility(View.VISIBLE);
            playerBid4.setText(BoardChoosing.modifyChipsString(prevPlayerCallValue));
            playerBid4.animate()
                    .x(roundMoney.getX())
                    .y(roundMoney.getY())
                    .setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerBid4.setX(roundMoney.getX());
                            playerBid4.setY(roundMoney.getY());
                            playerBid4.setVisibility(View.GONE);
                            roundMoney.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getRoundCoins()));

                        }
                    }).start();

            CountDownTimer mCountDownTimer;
            timerCounter2 = 0;
            mCountDownTimer=new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerCounter2++;
                }

                @Override
                public void onFinish() {
                    //Do what you want
                    playerBid4.setVisibility(View.GONE);
                    timerCounter2++;
                    playerBid4.animate()
                            .x(playerFourBid.getX())
                            .y(playerFourBid.getY())
                            .setDuration(1000)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    playerBid4.setX(playerFourBid.getX());
                                    playerBid4.setY(playerFourBid.getY());
                                    playerBid4.setVisibility(View.GONE);
                                }
                            }).start();
                }
            };
            mCountDownTimer.start();


        }
        else if(id==4){
            playerBid5.setVisibility(View.VISIBLE);
            playerBid5.setText(BoardChoosing.modifyChipsString(prevPlayerCallValue));
            playerBid5.animate()
                    .x(roundMoney.getX())
                    .y(roundMoney.getY())
                    .setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerBid5.setX(roundMoney.getX());
                            playerBid5.setY(roundMoney.getY());
                            playerBid5.setVisibility(View.GONE);
                            roundMoney.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getRoundCoins()));

                        }
                    }).start();

            CountDownTimer mCountDownTimer;
            timerCounter2 = 0;
            mCountDownTimer=new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerCounter2++;
                }

                @Override
                public void onFinish() {
                    //Do what you want
                    playerBid5.setVisibility(View.GONE);
                    timerCounter2++;
                    playerBid5.animate()
                            .x(playerFiveBid.getX())
                            .y(playerFiveBid.getY())
                            .setDuration(1000)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    playerBid5.setX(playerFiveBid.getX());
                                    playerBid5.setY(playerFiveBid.getY());
                                    playerBid5.setVisibility(View.GONE);
                                }
                            }).start();
                }
            };
            mCountDownTimer.start();
        }

    }

    private void showActionAnimation(String action){
        if(action.equalsIgnoreCase("call")){
            long v = 0;
            if(ClientToServer.user.getCycleCount()==1 && smallBlindSeat==0){
                v = ClientToServer.user.getRoundCall()-(ClientToServer.minCallValue[boardType]/2);
            }
            else if(ClientToServer.user.getCycleCount()==1 && bigBlindSeat==0){
                v = ClientToServer.user.getRoundCall()-ClientToServer.minCallValue[boardType];
            }
            else {
                v = ClientToServer.user.getRoundCall();
            }

            playerOneCoin2.setText(BoardChoosing.modifyChipsString(v));
            //ClientToServer.user.setRoundCoins(ClientToServer.user.getRoundCoins()+v);
            playerOneCoin.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getBoardCoin()-v));
            showBidAnimation(0);
        }
        else if(action.equalsIgnoreCase("raise")){
            long v = 0;
            if(ClientToServer.user.getCycleCount()==1 && smallBlindSeat==0){
                v = Math.round(sliderValue)-(ClientToServer.minCallValue[boardType]/2);
            }
            else if(ClientToServer.user.getCycleCount()==1 && bigBlindSeat==0){
                v = Math.round(sliderValue)-ClientToServer.minCallValue[boardType];
            }
            else {
                v = Math.round(sliderValue);
            }

            playerOneCoin2.setText(BoardChoosing.modifyChipsString(v));
            //ClientToServer.user.setRoundCoins(ClientToServer.user.getRoundCoins()+v);
            playerOneCoin.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getBoardCoin()-v));
            showBidAnimation(0);
        }
        else if(action.equalsIgnoreCase("allin")){
            long v = ClientToServer.user.getBoardCoin();

            playerOneCoin2.setText(BoardChoosing.modifyChipsString(allinanim_coin));
           // ClientToServer.user.setRoundCoins(ClientToServer.user.getRoundCoins()+v);
            playerOneCoin.setText("0");
            showBidAnimation(0);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            goHome();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

