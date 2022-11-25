package com.pokerbd.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    ListView requestList;
    RequestAdapter requestAdapter;
    Button btnReq, btnbackn;

    Thread notifThread;
    public static boolean isNotifThread = false;
    public static boolean isRequestResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        ClientToServer.requestList = new ArrayList<>();

        requestList = (ListView) findViewById(R.id.requestList);


        requestAdapter = new RequestAdapter();
        requestList.setAdapter(requestAdapter);

       // btnNotify = (Button) findViewById(R.id.btnNotify);
        btnReq = (Button) findViewById(R.id.btnReq);
        btnbackn = (Button) findViewById(R.id.btnbackn);

        btnbackn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

        requestList.setVisibility(View.VISIBLE);

        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClientToServer.getAllTransactionsRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestList.setVisibility(View.VISIBLE);
            }
        });

        try {
            ClientToServer.getAllTransactionsRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        isNotifThread = true;

        notifThread = new Thread(){
            @Override
            public void run() {
                while (isNotifThread) {
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

                    if(isRequestResponse){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(requestAdapter!=null) {
                                    requestAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                        isRequestResponse = false;
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
        notifThread.start();
    }

    class RequestAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ClientToServer.requestList.size();
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
            View view1 = getLayoutInflater().inflate(R.layout.request, null);

            TextView ntype = (TextView) view1.findViewById(R.id.ntype1);
            TextView notif = (TextView) view1.findViewById(R.id.ndetails1);
            TextView btype = (TextView) view1.findViewById(R.id.nbtype);
            TextView nmethod = (TextView) view1.findViewById(R.id.nmethod1);
            TextView reqdate = (TextView) view1.findViewById(R.id.nreqdate);
            TextView resdate = (TextView) view1.findViewById(R.id.nresdate);


            ntype.setText(ClientToServer.requestList.get(i).getType());
            btype.setText(ClientToServer.requestList.get(i).getBType());
            notif.setText(ClientToServer.requestList.get(i).getNotification());
            nmethod.setText(ClientToServer.requestList.get(i).getMethod());
            reqdate.setText("Request Date: "+ClientToServer.requestList.get(i).getReqdate());
            resdate.setText("Response Date: "+ClientToServer.requestList.get(i).getResdate());

            if(ClientToServer.requestList.get(i).getResdate().length()==0){
                resdate.setText("");
            }

            return view1;
        }
    }

    private void goConnectivity(){
        isNotifThread = false;
        Connectivity.isConnectivityThread = true;
        Intent intent = new Intent(this, Connectivity.class);
        startActivity(intent);
        finish();
    }

    private void goHome(){
        isNotifThread = false;
        HomeScreen.isHomeThread = true;
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        isNotifThread = false;
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