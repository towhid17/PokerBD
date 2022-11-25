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

public class CashOut extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static String SelectedType = "Bkash";
    private static String SendToNumber = "";
    private static String SendFromNumber = "";
    private static String TransactionId = "";
    public static long amountChips = 0;
    public static double amountTK = 0;

    public static boolean isResponse = false;
    public static boolean success = false;

    private TextView textChips, textTK, typetwo, sendtono, textMethod;
    private Button btnsubmit, btnbackco;
    private TextInputLayout transid, cashoutnumber;


    Thread cashoutThread;
    public static boolean isCashoutThread = false;

    private Spinner spinner;
    private static ArrayList<String> paths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cash_out);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        paths.clear();
        for(int i=0; i<ClientToServer.transactionNumbers.size(); i++){
            String s = ClientToServer.transactionNumbers.get(i).getType();
            paths.add(s);
        }

        textChips = (TextView) findViewById(R.id.textChips);
        textTK = (TextView) findViewById(R.id.textTK);
        typetwo = (TextView) findViewById(R.id.typetwo);
        sendtono = (TextView) findViewById(R.id.sendtono);
        textMethod = (TextView) findViewById(R.id.textMethod);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        transid = (TextInputLayout) findViewById(R.id.transid);
        cashoutnumber = (TextInputLayout) findViewById(R.id.cashoutnumber);
        btnbackco = (Button) findViewById(R.id.btnbackco);


        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CashOut.this, android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        System.out.println("Cash out opeed");

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean one = false;
                boolean two = false;
                String code = cashoutnumber.getEditText().getText().toString().trim();
                SendFromNumber = code;
                if(code.isEmpty() || code.length()<11){
                    Toast.makeText(CashOut.this, "Enter Valid number", Toast.LENGTH_SHORT).show();
                }
                else{
                    one = true;
                }
                String code1 = transid.getEditText().getText().toString().trim();
                TransactionId = code1;
                if(code1.isEmpty()){
                    Toast.makeText(CashOut.this, "Enter Valid TransID", Toast.LENGTH_SHORT).show();
                }
                else{
                    two = true;
                }

                if(one && two){
//                    try {
//                        ClientToServer.coinBuyRequest(amountChips, SelectedType, TransactionId, SendFromNumber, ClientToServer.transactionNumbers.get(0).getJSON().toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });

        btnbackco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPurchase();
            }
        });

        setInfo();

        isCashoutThread = true;

        cashoutThread = new Thread(){
            @Override
            public void run() {
                while (isCashoutThread) {
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
                                    toast.show();
                                }
                            }
                        });
                        isResponse = false;
                        CashIn.isResponse = false;
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
        cashoutThread.start();
    }

    private void setInfo(){
        String[] ss = ClientToServer.transactionNumbers.get(0).getNumber().split(" ");
        String type2 = "";
        for(int i=0; i<ss.length-1; i++){
            type2+=ss[i]+" ";
        }
        typetwo.setText(type2);
        SelectedType = ClientToServer.transactionNumbers.get(0).getType();
        SendToNumber = ss[ss.length-1];
        textMethod.setText(SelectedType);
        sendtono.setText(SendToNumber);
        textTK.setText(String.valueOf(amountTK)+" TK");
        textChips.setText(BoardChoosing.modifyChipsString(amountChips));
    }

    private void goConnectivity(){
        isCashoutThread = false;
        Connectivity.isConnectivityThread = true;
        Intent intent = new Intent(this, Connectivity.class);
        startActivity(intent);
        finish();
    }

    private void goNotification(){
        isCashoutThread = false;
        Intent intent = new Intent(this, Notification.class);
        startActivity(intent);
        finish();
    }

    private void goPurchase(){
        isCashoutThread = false;
        Intent intent = new Intent(this, CoinPurchase.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        isCashoutThread = false;
        Login.isLoginThread = true;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setSelectedInfo(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setSelectedInfo(int i){
        String[] ss = ClientToServer.transactionNumbers.get(i).getNumber().split(" ");
        String type2 = "";
        for(int j=0; j<ss.length-1; j++){
            type2+=ss[j]+" ";
        }
        typetwo.setText(type2);
        SelectedType = ClientToServer.transactionNumbers.get(i).getType();
        textMethod.setText(SelectedType);
        SendToNumber = ss[ss.length-1];
        sendtono.setText(SendToNumber);
    }

    @Override
    public void onBackPressed() {
        goPurchase();
    }
}

