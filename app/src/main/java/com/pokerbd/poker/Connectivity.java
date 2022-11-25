package com.pokerbd.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class Connectivity extends AppCompatActivity {

    TextView message;

    public static boolean isConnectivityThread = false;
    Thread connectivityThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_connectivity);

        System.out.println("Connectivity Page");
        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        message = (TextView) findViewById(R.id.message);


        connectivityThread = new Thread(){
            @Override
            public void run() {
                while (isConnectivityThread){
                    if(ClientToServer.TRYCONNECTION){
                        if(ClientToServer.hasConnected){
                            goLogin();
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    message.setText("Connect your device with a stable internet connection and Restart the App");
                                }
                            });
                        }
                        ClientToServer.TRYCONNECTION = false;
                    }

                }
            }
        };
        connectivityThread.start();
    }

    private void goLogin(){
        isConnectivityThread = false;
        Login.isLoginThread = true;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}