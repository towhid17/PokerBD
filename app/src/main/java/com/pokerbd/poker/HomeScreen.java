package com.pokerbd.poker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;

//import okhttp3.internal.Util;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeScreen extends AppCompatActivity {

    boolean isLoggedIn;
    public AccessToken accessToken;
    public static String FACEBOOK_FIELD_PROFILE_IMAGE = "picture.type(large)";
    public static String FACEBOOK_FIELDS = "fields";

    int logos[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};

    public static boolean isHomeThread = false;
    public static boolean isBoardDataLoaded = false;
    public static int loadBoardDataCount = 0;
    public static boolean isbtnSellActive = false;
    public static boolean isbtnPurchaseActive = false;
    public static boolean isShopDataLoaded = false;
    public static boolean isBuyButtonSuccess = false;

    public static JSONArray notifications;

    public static int timerCounter2 = 0;


    // ------ to store the Advideo ------
    private InterstitialAd mInterstitialAd;
    //---------------------------------

    //profile pic chooser
    RelativeLayout profileoption;
    Button btncloseprofileopt;
    Button btnprofileopt;
    Button btnpropicchange;

    GridView gridView;
    TextView propicchoosetxt;
    Button btnpicopt;


    CountDownTimer mCountDownTimer11;

    ImageView playfrienbtn;
    ImageView handbtn, playnowbtn;
    CircleImageView profilepic;

    ImageView adView;
    private RewardedAd mRewardedAd;

    TextView username, notificationText;
    TextView rankinhome;
    TextView rankname;
    TextView homecoins;
    Button btnSell;
    ImageView btnCoinPurchaseHome, btnnotif;

    Thread homeThread;

    //freecoin
    LottieAnimationView btnfreeCoin;
    RelativeLayout FreeCoinWindow;
    TextView freeCoinText;
    public static long freeCoinAdded = 0;

    public static boolean isDone = false;

    public static int isFreeCoinAdded = 0;
    public static boolean freecoinResponse = false;
    public static boolean isNotificationResponse = false;
    public static boolean currentImageResponse = false;

    ProgressBar mf_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_screen);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        isHomeThread = true;
        isFreeCoinAdded = 0;


        //------------- for mobile ads initialization(both rewarded ad and interstitial ad-----
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //--------------------ends----------------------

        //--------- load the interstitial ad for transition between homescreen to withdraw page----
        loadVideo();
        //--------------- ends--------


        //freecoin
        freeCoinText = (TextView) findViewById(R.id.freeCoinText);
        FreeCoinWindow = (RelativeLayout) findViewById(R.id.FreeCoinWindow);
        btnfreeCoin = (LottieAnimationView) findViewById(R.id.btnfreeCoin);
        btnnotif = (ImageView) findViewById(R.id.btnnotif);

        btnSell = (Button) findViewById(R.id.btnWithDraw);


        //profile pic chooser
        profileoption = (RelativeLayout) findViewById(R.id.profileoption);
        btnprofileopt = (Button) findViewById(R.id.btnprofileopt);
        btnpropicchange = (Button) findViewById(R.id.btnpropicchange);
        btncloseprofileopt = (Button) findViewById(R.id.btncloseproopt);

        btnpicopt = (Button) findViewById(R.id.btnpicopt);
        propicchoosetxt = (TextView) findViewById(R.id.propicchoosetxt);
        gridView = (GridView) findViewById(R.id.gridview);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos);
        gridView.setAdapter(customAdapter);

        // implement setOnItemClickListener event on GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
//                    if(position==0){
//                        ClientToServer.requestImageChange(Login.defaultimageLink);
//                    }
//                    else {
//                        ClientToServer.requestImageChange(MainActivity.ServerImageLink + logos[position]);
//                    }
//                    System.out.println("Request Image Change");
                    ClientToServer.requestImageChange(MainActivity.ServerImageLink + logos[position]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    ClientToServer.getCurrentImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                profileoption.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                btnpicopt.setVisibility(View.GONE);
                propicchoosetxt.setVisibility(View.GONE);
            }
        });

        profileoption.setVisibility(View.GONE);
        gridView.setVisibility(View.GONE);
        btnpicopt.setVisibility(View.GONE);
        propicchoosetxt.setVisibility(View.GONE);

        btnpicopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileoption.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                btnpicopt.setVisibility(View.GONE);
                propicchoosetxt.setVisibility(View.GONE);
            }
        });

        btncloseprofileopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileoption.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                btnpicopt.setVisibility(View.GONE);
                propicchoosetxt.setVisibility(View.GONE);
            }
        });

        btnpropicchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileoption.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                btnpicopt.setVisibility(View.VISIBLE);
                propicchoosetxt.setVisibility(View.VISIBLE);
            }
        });

        btnprofileopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileoption.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                btnpicopt.setVisibility(View.GONE);
                propicchoosetxt.setVisibility(View.GONE);
                openProfile();
            }
        });

        username = (TextView) findViewById(R.id.username);
        rankinhome = (TextView) findViewById(R.id.rankinhome);
        rankname = (TextView) findViewById(R.id.rankname);
        homecoins = (TextView) findViewById(R.id.homecoins);
        notificationText = (TextView) findViewById(R.id.notificationtext);

        profilepic = (CircleImageView) findViewById(R.id.profilepic);

        if(loadBoardDataCount==0) {
            try {
                ClientToServer.sendBoardDataRequest();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adView = (ImageView) findViewById(R.id.adView);
        adView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openAdVideo();
            }
        });



//        try {
//            ClientToServer.requestToken();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        btnCoinPurchaseHome = (ImageView) findViewById(R.id.btnCoinPurchaseHome);
        //btnFriendList = (CardView) findViewById(R.id.btnFriendList);

        mf_progress_bar = (ProgressBar) findViewById(R.id.mf_progress_bar);

        showFreeConSuccesful(false);
        setHomeScreenData();

        playfrienbtn = (ImageView) findViewById(R.id.playfriendbtn);

        btnnotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNotification();
            }
        });

        playfrienbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!Login.isGuestLoggin){
                    OpenActivityplaywithfriend();
                }
                else{
                    Toast.makeText(HomeScreen.this, "Sign in with your Facebook or Google account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        handbtn = (ImageView) findViewById(R.id.handbtn);

        handbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenHandRules();
            }
        });

        playnowbtn = (ImageView) findViewById(R.id.playnowbtn);

        playnowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClientToServer.sendBoardDataRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        profilepic = (CircleImageView) findViewById(R.id.profilepic);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileoption.setVisibility(View.VISIBLE);
            }
        });

        btnfreeCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClientToServer.addFreeCoinRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isbtnSellActive = true;
                try {
                    ClientToServer.shopDataRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnCoinPurchaseHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isbtnPurchaseActive = true;
                try {
                    ClientToServer.shopDataRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            ClientToServer.getNotifications();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(isBuyButtonSuccess){
            btnCoinPurchaseHome.setVisibility(View.VISIBLE);
            btnSell.setVisibility(View.VISIBLE);
        }
        else {
            btnCoinPurchaseHome.setVisibility(View.GONE);
            btnSell.setVisibility(View.GONE);
        }

        //................................................................
        // .........................homeThread............................
        // ...............................................................

        notificationText.setVisibility(View.GONE);
        

        homeThread = new Thread(){
            @Override
            public void run() {
                while (isHomeThread){
                    if(ClientToServer.DISCONNECTED){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goConnectivity();
                            }
                        });
                        ClientToServer.DISCONNECTED = false;
                    }
                    if(freecoinResponse){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(isFreeCoinAdded==1){

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showFreeConSuccesful(true);
                                            homecoins.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getCurrentCoin()));
                                            System.out.println("FreeCoinAdded");
                                        }
                                    });


                                    CountDownTimer mCountDownTimer=new CountDownTimer(4000,1000) {

                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            isFreeCoinAdded = 0;
                                            showFreeConSuccesful(false);
                                            isDone = true;
                                        }
                                    };
                                    mCountDownTimer.start();
                                }
                                else if(isFreeCoinAdded==2){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(HomeScreen.this, "You already spent your DAILY WISH", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    isDone = true;
                                }
                            }
                        });
                        freecoinResponse = false;
                    }
                    if(isBoardDataLoaded && loadBoardDataCount>1) {
                        isBoardDataLoaded = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                OpenBoardChooseWindow();
                            }
                        });

                    }
//                    if(isBuyButtonSuccess){
//                        isBuyButtonSuccess = false;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                btnCoinPurchaseHome.setVisibility(View.VISIBLE);
//                                btnSell.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }

                    if(isShopDataLoaded && isbtnPurchaseActive) {
                        isShopDataLoaded = false;
                        isbtnPurchaseActive = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goShop();
                            }
                        });

                    }
                    if(isShopDataLoaded && isbtnSellActive) {
                        isShopDataLoaded = false;
                        isbtnSellActive = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(ClientToServer.user.getCurrentCoin()>=(10000000*ClientToServer.minCoinWithdraw)){
                                    goSell();
                                }
                                else {
                                    Toast.makeText(HomeScreen.this, "Your have to have more than "+ClientToServer.minCoinWithdraw+" Cr CHIPS", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    if(isNotificationResponse) {
                        isNotificationResponse = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    showNotification();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

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
                    if(currentImageResponse){
                        currentImageResponse = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Glide.with(profilepic).load(String.valueOf(ClientToServer.user.getImageLink())).into(profilepic);
                                String imglinkt = String.valueOf(ClientToServer.user.getImageLink());
                                String imglinkf = imglinkt;
                                if(imglinkt.contains(MainActivity.ServerImageLink)){
                                    System.out.println("yessssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

                                }
                                else{
                                    System.out.println("noooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                                    imglinkf = MainActivity.GuestImageLink;
                                }
                                Glide.with(profilepic).load(imglinkf).into(profilepic);

                            }
                        });
                    }
                }
            }
        };
        homeThread.start();

        //..........................................................................................
        //....................................homeThread end.........................................
        //..........................................................................................


    }

    class CustomAdapter extends BaseAdapter {
        Context context;
        int logos[];
        LayoutInflater inflter;
        public CustomAdapter(Context applicationContext, int[] logos) {
            this.context = applicationContext;
            this.logos = logos;
            inflter = (LayoutInflater.from(applicationContext));
        }
        @Override
        public int getCount() {
            return logos.length;
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
            view = inflter.inflate(R.layout.avatar, null); // inflate the layout
            CircleImageView image = (CircleImageView) view.findViewById(R.id.image); // get the reference of ImageView
            CircleImageView selectimage = (CircleImageView) view.findViewById(R.id.selectimage);
            selectimage.setImageResource(R.drawable.logo1); // set logo images
//            if(i==0){
//                Glide.with(view).load(Login.defaultimageLink).into(image);
//            }
//            else {
//                Glide.with(view).load("http://140.82.0.55:1112/image?id=" + logos[i]).into(image);
//            }

            Glide.with(view).load(MainActivity.ServerImageLink + logos[i]).into(image);

            return view;
        }
    }

    private void showNotification() throws JSONException {
        if(notifications!=null){
            for(int i=0; i<notifications.length(); i++){
                notificationText.setVisibility(View.VISIBLE);
                JSONObject jsonObject1 = notifications.getJSONObject(i);
                JSONObject jsonObject2 = jsonObject1.getJSONObject("data");

                String s = jsonObject1.getString("type")+"("+jsonObject2.getString("type")+") : CHIPS Amount: "+jsonObject2.getString("coinAmount")+", TK: "+jsonObject2.getString("price");

                notificationText.setText(s);


                timerCounter2 = 0;
                mCountDownTimer11=new CountDownTimer(4000,1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        timerCounter2++;
                    }

                    @Override
                    public void onFinish() {
                        //Do what you want
                        timerCounter2++;
                        notificationText.setVisibility(View.GONE);
                    }
                };
                mCountDownTimer11.start();
            }
        }
        homecoins.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getCurrentCoin()));
    }

    private void showFreeConSuccesful(boolean isActive){
        if(isActive){
            FreeCoinWindow.setVisibility(View.VISIBLE);
            freeCoinText.setText(BoardChoosing.modifyChipsString(freeCoinAdded)+" Added!");
            homecoins.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getCurrentCoin()));
        }
        else{
            FreeCoinWindow.setVisibility(View.GONE);
        }
    }

    private void setHomeScreenData() {
        if(ClientToServer.user!=null){
            username.setText(ClientToServer.user.getUsername());
            homecoins.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getCurrentCoin()));
            rankname.setText(ClientToServer.user.getRank());
            int rank = 0;
            for(int i=0; i< User.rankString.length; i++){
                if(User.rankString[i].equalsIgnoreCase(ClientToServer.user.getRank())){
                    rank = i;
                }
            }
            rankinhome.setText("LV "+String.valueOf(rank));
            mf_progress_bar.setProgress(rank*10);

            try {
                ClientToServer.getCurrentImage();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Glide.with(this).load(String.valueOf(ClientToServer.user.getImageLink())).into(profilepic);
        }
    }

    public void openProfile(){
        isHomeThread = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        ProfileMenu.isProThread = true;
        Intent intent = new Intent(this, ProfileMenu.class);
        startActivity(intent);
        finish();
    }

    public void OpenActivityplaywithfriend(){
        isHomeThread = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        Intent intent = new Intent(this, playwithfriend.class);
        startActivity(intent);
        finish();
    }

    public void OpenHandRules(){
        Intent intent = new Intent(this, PokerHandRank.class);
        startActivity(intent);
    }

    public void OpenBoardChooseWindow(){
        isHomeThread = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        Intent intent = new Intent(this, BoardChoosing.class);
        startActivity(intent);
        finish();
    }

    public void openAdVideo(){
        isHomeThread = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        Intent intent = new Intent(this, AdVideo.class);
        startActivity(intent);
        finish();
    }

    private void goConnectivity(){
        isHomeThread = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        Connectivity.isConnectivityThread = true;
        Intent intent = new Intent(this, Connectivity.class);
        startActivity(intent);
        finish();
    }

    private void goShop(){
        isHomeThread = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        Intent intent = new Intent(this, CoinPurchase.class);
        startActivity(intent);
        finish();
    }


    //-----------to load the adVideo for sell button click ---------
    void loadVideo(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-4862496362579786/4568442305", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        //Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }
    //-------------- ends of loadVideo function----------------


    private void goSell(){
        isHomeThread = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }

        //----------- before showing the withdraw page this ad will showed up.----------
        //----------- if the add isn't loaded we will directly go to the withdraw page----------
        if(mInterstitialAd != null){
            mInterstitialAd.show(HomeScreen.this);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Intent intent = new Intent(HomeScreen.this, CoinWithdraw.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else{
            Intent intent = new Intent(this, CoinWithdraw.class);
            startActivity(intent);
            finish();
        }
        //------------------------ends of ad before withdraw page-------------
    }

    private void goNotification(){
        isHomeThread = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        Intent intent = new Intent(this, Notification.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        isHomeThread = false;
        if(mCountDownTimer11!=null){
            mCountDownTimer11.cancel();
        }
        Login.isLoginThread = true;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }



    boolean singleBack = false;

    @Override
    public void onBackPressed() {
        if (singleBack) {
            finishAndRemoveTask();
            System.exit(0);
            return;
        }

        this.singleBack = true;
        Toast.makeText(this, "Double Back to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                singleBack=false;
            }
        }, 2000);
    }
}