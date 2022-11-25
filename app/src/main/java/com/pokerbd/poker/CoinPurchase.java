package com.pokerbd.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CoinPurchase extends AppCompatActivity {

    ListView coinList;
    CustomAdapter customAdapter;

    public static long buychips= 0;
    public static double buytk = 0;
    public static int buyitemno = 0;

    Button closeprofilebtn2;
    TextView joincoins;

    public static int reqLeft = 10;

    Thread cpt;
    public static boolean isbct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_coin_purchase);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        joincoins = (TextView) findViewById(R.id.joincoins);
        joincoins.setText(BoardChoosing.modifyChipsString(ClientToServer.user.getCurrentCoin()));
        coinList = (ListView) findViewById(R.id.coinList);
        closeprofilebtn2 = (Button) findViewById(R.id.btnCloseBuy);

        isbct = true;

        cpt = new Thread(){
            @Override
            public void run() {
                while (isbct) {
                    // System.out.println("in HostThread");
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
        cpt.start();

        closeprofilebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

        customAdapter = new CustomAdapter();
        coinList.setAdapter(customAdapter);

        coinList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setCoinPurchaseItem(i);
            }
        });
    }

    private void setCoinPurchaseItem(int id){
        System.out.println(id);
        CashOut.amountChips = ClientToServer.coinAmountOnBuy[id];
        CashOut.amountTK = ClientToServer.coinPriceOnBuy[id];
        buychips = ClientToServer.coinAmountOnBuy[id];
        buytk = ClientToServer.coinPriceOnBuy[id];
        buyitemno = id;

        if(ClientToServer.buyrequestLeft>0) {
            goPaymentOption();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),"Your Daily Transaction Request Limit is Over, Try next day",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ClientToServer.coinPriceOnBuy.length;
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
            View view1 = getLayoutInflater().inflate(R.layout.coinrow, null);

            TextView textView = (TextView) view1.findViewById(R.id.tk);
            TextView textView1 = (TextView) view1.findViewById(R.id.chips2);

            textView.setText(String.valueOf(ClientToServer.coinPriceOnBuy[i])+" TK");
            textView1.setText(BoardChoosing.modifyChipsString(ClientToServer.coinAmountOnBuy[i])+" Chips");

            return view1;
        }
    }

    private void goHome(){
        HomeScreen.isHomeThread = true;
        isbct = false;
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void goPaymentOption(){
        isbct = false;
        Intent intent = new Intent(this, PaymentOption.class);
        startActivity(intent);
        finish();
    }

    private void goLogin(){
        Login.isLoginThread = true;
        isbct = false;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
       goHome();
    }
}