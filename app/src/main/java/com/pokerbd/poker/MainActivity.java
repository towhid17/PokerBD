 package com.pokerbd.poker;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    public static ClientToServer clientToServer;
    public static boolean isUpdateAvailable = false;
    public static boolean isVersionRespose = false;
    public static boolean isThread = true;
    public static int version = 0;

    private static int timerCounter = 0;
    private static boolean isStop = false;

    public static String Server_port = "1112";

    public static String ServerHTTPLink = "http://140.82.0.55:"+Server_port;

    public static String ServerLink = "ws://140.82.0.55:"+Server_port+"/WebSocketServerEnd";
    public static String GuestImageLink = "http://140.82.0.55:"+Server_port+"/image?id=1";
    public static String ServerImageLink = "http://140.82.0.55:"+Server_port+"/image?id=";



    //variables
    Animation splashanim;
    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //UncaughtExceptionHandler/////////////////////////////

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_LONG).show();
        }

        URI uri = null;
        try {
            uri = new URI(ServerLink);
            System.out.println(ServerLink);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        clientToServer = new ClientToServer(uri, Integer.parseInt(Server_port), 60, 1000, 10000);


        /////////////////////////////////////////////////////////

        new Thread(){
            @Override
            public void run() {

                while (isThread){
                    if(isVersionRespose){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int ver = BuildConfig.VERSION_CODE;

                                try {
                                    URL url = new URL("http://140.82.0.55:"+Server_port+"/allOkay");
                                    String ok = URLReader(url);
                                    String[] strs = ok.split(" ");
                                    boolean isok = false;
                                    for(int i=0; i<strs.length; i++) {
                                        if (strs[i].substring(0, 7).equalsIgnoreCase("allOkay")) {
                                            isok = true;
                                        }
                                    }

                                    if(isok && ver>=version) {
                                        Login.isLoginThread = true;
                                        isThread = false;
                                        Intent intent = new Intent(MainActivity.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        UpdateApp();
                                    }


                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        isVersionRespose = false;
                    }
                }

            }
        }.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(ClientToServer.hasConnected) {
                    try {
                        ClientToServer.requestVersion();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isStop = true;
                }
                else{
                    Toast.makeText(MainActivity.this, "You are not Connected, Please check your INTERNET Connection and RESTART the APP", Toast.LENGTH_SHORT).show();
                }
            }
        }, SPLASH_SCREEN);


    }


    public void UpdateApp(){
        android.view.ContextThemeWrapper ctw = new android.view.ContextThemeWrapper(this, R.style.Theme_AppCompat_Dialog_Alert);
        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctw);
        alertDialogBuilder.setTitle("Update App");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setIcon(R.drawable.logof);
        alertDialogBuilder.setMessage("you have to update app. New Features are added");
        alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
        alertDialogBuilder.show();

    }



    public static String URLReader(URL url) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        InputStream in = url.openStream();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } finally {
            in.close();
        }

        return sb.toString();
    }



}