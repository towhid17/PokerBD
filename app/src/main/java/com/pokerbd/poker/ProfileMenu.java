package com.pokerbd.poker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

public class ProfileMenu extends AppCompatActivity {

    Button closeprofilebtn;
    TextView usernamePM;
    TextView rankinhome1;
    ProgressBar mf_progress_bar;
    ImageView propicPM;

    Button logout_button;
    GoogleSignInClient mGoogleSignInClient;
    boolean isLoggedIn;
    public AccessToken accessToken;

    Thread profileThread;
    public static boolean isProThread = false;
    public static boolean isLogOut = false;


    TextView HandsPlayed;
    TextView BestWinningHand;
    TextView WinPercentage;
    TextView biggestPotWin;
    TextView CoinWon;
    TextView winStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_menu);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        mGoogleSignInClient = Login.mGoogleSignInClient;

        //logout from account
        logout_button = (Button) findViewById(R.id.logout_button);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ClientToServer.sendLogoutRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        usernamePM = (TextView) findViewById(R.id.usernamePM);
        rankinhome1 = (TextView) findViewById(R.id.rankinhome1);
        mf_progress_bar = (ProgressBar) findViewById(R.id.mf_progress_bar);
        propicPM = (ImageView) findViewById(R.id.propicPM);

        HandsPlayed = (TextView) findViewById(R.id.handsPlayed);
        BestWinningHand = (TextView) findViewById(R.id.BestWinningHand2);
        WinPercentage = (TextView) findViewById(R.id.WinPercentage2);
        biggestPotWin = (TextView) findViewById(R.id.biggestPotWin2);
        CoinWon = (TextView) findViewById(R.id.coinWon);
        winStreak = (TextView) findViewById(R.id.winStreak);

        closeprofilebtn = (Button) findViewById(R.id.closeprofilebtn);

        closeprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

        setInfo();

        //................................................................
        // .........................proThread............................
        // ...............................................................

        profileThread = new Thread(){
            @Override
            public void run() {
                while (isProThread){
                    if(isLogOut) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogOut();
                            }
                        });
                        isLogOut = false;
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
        profileThread.start();

        //..........................................................................................
        //....................................proThread end.........................................
        //..........................................................................................
    }

    private void setInfo() {
        if(ClientToServer.user!=null){
            HandsPlayed.setText(String.valueOf(ClientToServer.user.getRoundsPlayed()));
            biggestPotWin.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getBiggestWin()));
            String wp = String.valueOf(ClientToServer.user.getWinPercentage());
            String str = "";
            if(wp.length()>=5){
                str = wp.substring(0, 5);
            }
            else{
                str = wp;
            }
            WinPercentage.setText(str+"%");
            BestWinningHand.setText(ClientToServer.user.getBestHand());
            winStreak.setText(String.valueOf(ClientToServer.user.getWinStreak()));
            CoinWon.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getCoinWon())+"/"+BoardChoosing.modifyChipsString(ClientToServer.user.getCoinLost()));
            Glide.with(this).load(ClientToServer.user.getImageLink()).into(propicPM);

            usernamePM.setText(ClientToServer.user.getUsername());
            rankinhome1.setText("Level: "+ClientToServer.user.getLevel()+" #"+ClientToServer.user.getRank());
            int rank = 0;
            for(int i=0; i< User.rankString.length; i++){
                if(User.rankString[i].equalsIgnoreCase(ClientToServer.user.getRank())){
                    rank = i;
                }
            }
            mf_progress_bar.setProgress(rank*10);
        }
    }

    //sign_out from google
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //do something to show signed out
                        System.out.println("Signed out successfully");
                        //alert
                        Toast toast = Toast.makeText(getApplicationContext(),"Signed out Successfully",Toast.LENGTH_SHORT);
                        toast.show();
                        Login.sign_in_mode = -1;
                        goLogin();
                    }
                });
    }

    //Facebook logout
    private void fb_sign_out(){
        System.out.println("Facebook Signed out successfully");
        Toast toast = Toast.makeText(getApplicationContext(),"Facebook logged out",Toast.LENGTH_SHORT);
        toast.show();
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);
        Login.sign_in_mode = -1;
        goLogin();
    }

    private void guestSingOut(){
        Login.isGuestLoggin = false;
        goLogin();
    }

    private void LogOut(){
        if(Login.sign_in_mode == 0)
            signOut();

        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn)
            fb_sign_out();

        if(Login.isGuestLoggin){
            guestSingOut();
        }
    }

    public void goHome(){
        isProThread = false;
        HomeScreen.isHomeThread = true;
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    public void goLogin(){
        isProThread = false;
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