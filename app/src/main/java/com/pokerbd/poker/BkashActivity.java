package com.pokerbd.poker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class BkashActivity extends AppCompatActivity {
    WebView wvBkashPayment;
    ProgressBar progressBar;
    TextView contentView;
    String debugBkashUrl = "https://shop.bkash.com/ms-bhuiya-traders01914957284/pay/bdt1/yMOTMu";

    private static String trxMsg = "";
    private boolean processContentDone = false;

    public static boolean isPaymentNotification = false;

    Thread bkashThread;
    public static boolean isBkashThread = false;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash);

        wvBkashPayment = findViewById(R.id.wvBkashPayment);
        progressBar = findViewById(R.id.progressBar);

        contentView = (TextView) findViewById(R.id.contentView);

        /* An instance of this class will be registered as a JavaScript interface */
        class MyJavaScriptInterface
        {
            private TextView contentView;

            public MyJavaScriptInterface(TextView aContentView)
            {
                contentView = aContentView;
            }

            @SuppressWarnings("unused")

            @JavascriptInterface
            public void processContent(String aContent){
                final String content = aContent;
                contentView.post(new Runnable(){
                    @Override
                    public void run() {
                        System.out.println("Pay Info: "+content);
                        trxMsg = content;
                        checkPaymentSuccess();
                    }
                });
            }
        }

        WebSettings webSettings = wvBkashPayment.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        /*
         * Below part is for enabling webview settings for using javascript and accessing html files and other assets
         */
        wvBkashPayment.setClickable(true);
        wvBkashPayment.getSettings().setDomStorageEnabled(true);
        wvBkashPayment.getSettings().setAppCacheEnabled(false);
        wvBkashPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvBkashPayment.clearCache(true);
        wvBkashPayment.getSettings().setAllowFileAccessFromFileURLs(true);
        wvBkashPayment.getSettings().setAllowUniversalAccessFromFileURLs(true);


        wvBkashPayment.addJavascriptInterface(new MyJavaScriptInterface(contentView), "INTERFACE");

        wvBkashPayment.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("Pay Info : called started");
                progressBar.setVisibility(view.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(view.GONE);
                System.out.println("Pay Info : called finished");
                if (url.contains("payment-success")) {
                    System.out.println("Pay Info: Start getting success");
                    //view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
                }
                else{
                    System.out.println("Pay Info: start getting others");
                    //view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");

                }
            }
        });

        wvBkashPayment.loadUrl(ClientToServer.bkashLinks[CoinPurchase.buyitemno]);   // api host link .
        //wvBkashPayment.loadUrl(debugBkashUrl);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("################# Click done now check #############");
                loadWebviewText();
            }
        });



        //thread
        isPaymentNotification = false;
        isBkashThread = true;

        bkashThread = new Thread(){
            @Override
            public void run() {
                while (isBkashThread) {
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

                    if(isPaymentNotification){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                System.out.println("go notificatio called");
                                goNotification();

                                Toast toast = Toast.makeText(getApplicationContext(),"Transaction success",Toast.LENGTH_SHORT);
                                toast.show();

                            }
                        });
                        isPaymentNotification = false;
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
        bkashThread.start();
    }


    @Override
    public void onBackPressed() {
        loadWebviewText();  //people can press backBtn and payment may cancel. so alert he really want to cancel payment or not
    }

    private void loadWebviewText() {
        processContentDone = false;
        wvBkashPayment.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");

    }

    private void checkPaymentSuccess(){

        String[] strings = trxMsg.split("\n");

        if(strings[0].equalsIgnoreCase("Payment Successful")){
            String[] strs1 = strings[2].split(" ");
            String Trxid = strs1[2];

            strs1 = strings[1].split(" ");
            String InvcNum = strs1[2];

            strs1 = strings[12].split(" ");
            String date = strs1[1];

            strs1 = strings[15].split(" ");
            String price = strs1[1];
            //String price = priceX.substring(1, priceX.length());

            strs1 = strings[16].split(" ");
            String fee = strs1[1];
            //String fee = priceX.substring(1, feeX.length());

            strs1 = strings[18].split(" ");
            String total = strs1[1];
            //String total = priceX.substring(1, totalX.length());

            strs1 = strings[6].split(" ");
            String agentNumber = strs1[1];

            strs1 = strings[10].split(" ");
            String custNumber = strs1[1];

            System.out.println("Trxid: "+Trxid+"  agentNum: "+agentNumber+"  custNum: "+custNumber+ "  invc: "+InvcNum+ "  total: "+total+ "  price: "+price+"  fee: "+fee);

            try {
                ClientToServer.coinBuyRequest(CoinPurchase.buychips, "bkash", Trxid, custNumber, agentNumber, InvcNum,total, price, fee);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Payment is not completed. Want to cancel payment process?");
            alert.setCancelable(false);
            alert.setIcon(R.drawable.ic_launcher_background);
            alert.setTitle("Alert!");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(BkashActivity.this, "Payment cancelled", Toast.LENGTH_SHORT).show();
                    goPurchase();
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            final AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }




    }

    private void goPurchase(){
        isBkashThread = false;
        Intent intent = new Intent(this, CoinPurchase.class);
        startActivity(intent);
        finish();
    }


    private void goConnectivity(){
        isBkashThread = false;
        Connectivity.isConnectivityThread = true;
        Intent intent = new Intent(this, Connectivity.class);
        startActivity(intent);
        finish();
    }

    private void goNotification(){
        isBkashThread = false;
        Intent intent = new Intent(this, Notification.class);
        startActivity(intent);
        System.out.println("go notificatio started");
        finish();
    }

    private void goLogin(){
        isBkashThread = false;
        Login.isLoginThread = true;
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
