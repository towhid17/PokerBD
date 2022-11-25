package com.pokerbd.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class CashIn extends AppCompatActivity {

    private static String SelectedType = "Bkash";
    private static String Accno = "";
    public static long amountChips = 0;
    public static double amountTK = 0;

    public static boolean isResponse = false;
    public static boolean success = false;

    private TextView textChips1, textTK1, textMethod1, AccountText;
    private Button btnsubmit1;
    private TextInputLayout cashoutnumber1;
    private Button btnbackci;

    private String accountText = "";


    Thread cashinThread;
    public static boolean isCashinThread = false;

    private Spinner spinner1;
    private static ArrayList<String> paths = new ArrayList<>();
    HashMap<String, Integer> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cash_in);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        paths.clear();
        accountText = "";
        for(int i=0; i<ClientToServer.transactionNumbers.size(); i++){
            String s = ClientToServer.transactionNumbers.get(i).getType();
            if(!hashMap.containsKey(s)){
                paths.add(s);
                hashMap.put(s, i);
            }
        }

        textChips1 = (TextView) findViewById(R.id.textChips1);
        textTK1 = (TextView) findViewById(R.id.textTK1);
        textMethod1 = (TextView) findViewById(R.id.textMethod1);
        btnsubmit1 = (Button) findViewById(R.id.btnsubmit1);
        cashoutnumber1 = (TextInputLayout) findViewById(R.id.cashoutnumber1);
        btnbackci = (Button) findViewById(R.id.btnbackci);
        AccountText = (TextView) findViewById(R.id.accounttext1);

        spinner1 = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CashIn.this, android.R.layout.simple_spinner_item, paths);

        btnbackci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWithdraw();
            }
        });

        btnsubmit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean one = false;
                String code = cashoutnumber1.getEditText().getText().toString().trim();
                Accno = code;
                if(code.isEmpty() || code.length()<11){
                    Toast.makeText(CashIn.this, "Enter Valid number", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        ClientToServer.withdrawCoinRequest(amountChips, SelectedType, Accno);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedType = paths.get(i);
                textMethod1.setText(SelectedType);
                AccountText.setText(paths.get(i)+ " personal no.");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setInfo();

        isCashinThread = true;

        cashinThread = new Thread(){
            @Override
            public void run() {
                while (isCashinThread) {
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

                    if(isResponse){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(success){
                                    goNotification();
                                }
                                else{
                                    Toast toast = Toast.makeText(getApplicationContext(),"Transaction Failed. Try again",Toast.LENGTH_SHORT);
                                }
                            }
                        });
                        isResponse = false;
                        CashOut.isResponse = false;
                    }

                    if(ClientToServer.isForceLogOut){
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
        cashinThread.start();
    }

    private void setInfo(){
        SelectedType = ClientToServer.transactionNumbers.get(0).getType();
        textMethod1.setText(SelectedType);
        textTK1.setText(String.valueOf(amountTK)+" TK");
        textChips1.setText(BoardChoosing.modifyChipsString(amountChips));

        AccountText.setText(paths.get(0)+ " personal no.");

    }

    private void goConnectivity(){
        isCashinThread = false;
        Connectivity.isConnectivityThread = true;
        Intent intent = new Intent(this, Connectivity.class);
        startActivity(intent);
        finish();
    }

    private void goNotification(){
        isCashinThread = false;
        Intent intent = new Intent(this, Notification.class);
        startActivity(intent);
        finish();
    }

    private void goWithdraw(){
        isCashinThread = false;
        Intent intent = new Intent(this, CoinWithdraw.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        isCashinThread = false;
        Login.isLoginThread = true;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goWithdraw();
    }
}