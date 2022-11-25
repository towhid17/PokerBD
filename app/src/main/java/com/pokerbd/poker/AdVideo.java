package com.pokerbd.poker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class AdVideo extends AppCompatActivity {

    private RewardedAd mRewardedAd;
    private Button watchNow;
    public static int videoLeft = 10;
    public static long coinAdded = 0;
    private boolean isEarnReward;
    private ViewGroup viewGroup;
    private ImageView returnHome;
    private TextView infoMsg;
    private TextView wonMsg;
    private TextView leftMsg;

    Thread adThread;
    public static boolean isAdThread = false;
    public static boolean rewardResponse = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_watch_video);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        isAdThread = true;

        watchNow = (Button) findViewById(R.id.watchNow);
        watchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAd();
            }
        });

        returnHome = (ImageView) findViewById(R.id.returnHome);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });

        infoMsg = (TextView) findViewById(R.id.infoMsg);
        wonMsg = (TextView) findViewById(R.id.wonMsg);
        leftMsg = (TextView) findViewById(R.id.leftMsg);

        videoLeft = ClientToServer.user.getCoinVideoCount();

        infoMsg.setText("Your video is being loaded.Please wait...");
        wonMsg.setText("EARN FREE CHIPS");
        leftMsg.setText(videoLeft+" videos left today");

        // the value will come form the database..
        isEarnReward = false;

        viewGroup = (ViewGroup) watchNow.getParent();
        viewGroup.removeView(watchNow);
        //watchNow.setEnabled(false);
        loadVideo();
        LoadThread load = new LoadThread();
        new Thread(load).start();

        //................................................................
        // ........................adThread............................
        // ...............................................................

        adThread = new Thread(){
            @Override
            public void run() {
                while (isAdThread) {
                    // System.out.println("in HostThread");
                    if (rewardResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wonMsg.setText("YOU WON "+BoardChoosing.modifyChipsString(coinAdded)+" CHIPS");
                            }
                        });
                        rewardResponse = false;
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
        adThread.start();

        //..........................................................................................
        //....................................adThreadEnd.........................................
        //..........................................................................................
    }
    public void showAd() {
        if (mRewardedAd != null) {
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    //Log.d("TAG", "Ad was shown.");
                    mRewardedAd = null;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    //Log.d("TAG", "Ad failed to show.");
                    Button temp = (Button) findViewById(R.id.watchNow);
                    if(temp != null){
                        viewGroup.removeView(watchNow);
                        infoMsg.setText("Your video is being loaded.Please wait...");
                    }
                    if(videoLeft > 0){
                        loadVideo();
                        LoadThread load = new LoadThread();
                        new Thread(load).start();
                    }
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    //Log.d("TAG", "Ad was dismissed.");
                    //watchNow.setEnabled(false);
                    viewGroup.removeView(watchNow);
                    infoMsg.setText("Your video is being loaded.Please wait...");
                    leftMsg.setText(videoLeft+" videos left today");

                    if(!isEarnReward){
                        wonMsg.setText("DIDN'T GET THE REWARD");
                    }
                    if(videoLeft > 0){
                        loadVideo();
                        LoadThread load = new LoadThread();
                        new Thread(load).start();
                    }
                }
            });

            Activity activityContext = AdVideo.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    //Log.d("TAG", "The user earned the reward.");

                    // the value of rewardAmount will be send to the database....
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    //wonMsg.setText("YOU WON $"+BoardChoosing.modifyChipsString(coinAdded)+" CHIPS");
                    //wonMsg.setText("YOU WON 0.5 L CHIPS");


                    isEarnReward = true;
                    try {
                        ClientToServer.addCoinByVideoRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            //Log.d("TAG", "The rewarded ad wasn't ready yet.");
        }
    }

    class LoadThread implements Runnable{

        @Override
        public void run() {
            while(mRewardedAd == null){
                continue;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //watchNow.setEnabled(true);
                    viewGroup.addView(watchNow);
                    infoMsg.setText("Watch a short video and earn $500,000 chips");
                }
            });
        }
    }


    void loadVideo(){
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-4862496362579786/7443675249",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        //Log.d("TAG", loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        //Log.d("TAG", "onAdLoad");
                    }
                });
    }

    void goToHome(){
        isAdThread = false;
        HomeScreen.isHomeThread = true;
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        isAdThread = false;
        Login.isLoginThread = true;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goToHome();
    }

}

