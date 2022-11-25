package com.pokerbd.poker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    //user////////////
    public static String ID = "";
    public static String imgLink = "";
    public static String UName = "";
    /////////////////

    public static boolean isLoginThread = false;

    public static boolean isLoginSucces = false;

    public static boolean isGuestLoggin = false;
    public static boolean LoginFalseLogout = false;

    public static String defaultimageLink = MainActivity.GuestImageLink;

    private LottieAnimationView guest;

    private LottieAnimationView gmail;
    private ImageView imageView;

    //signed in with what flag?
    // 0 -> google
    // 1 -> facebook
    //-1 -> neither
    public static int sign_in_mode = -1;

    //google variables
    //Sign-in Client
    static GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    //facebook variables
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton loginButton;
    boolean isLoggedIn;
    AccessToken accessToken;

    Thread LOGinThread;
    public static boolean loginRespose = false;
    public static boolean loginSucess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //UncaughtExceptionHandler/////////////////////////////
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        /////////////////////////////////////////////////////////

        //check already facebook logged-in
        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn)
        {
            sign_in_mode = 1;
            RequestFacebookLoagin(accessToken);
        }

        //initialize sign_in_mode with -1 as not signed in with google or facebook yet
        sign_in_mode = -1;

        guest = (LottieAnimationView) findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                GuestLoginRequest();
            }
        });

        //click on the google icon to sign_in using gmail
        gmail = (LottieAnimationView) findViewById(R.id.gmail_login);
        gmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //do all the sign-in stuff
                google_sign_in();
            }
        });


        //facebook sign-in
        //Facebook log in part
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_friends"));
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Facebook login success
                System.out.println("Facebook Login Success!");
                accessToken = AccessToken.getCurrentAccessToken();
                isLoggedIn = accessToken != null && !accessToken.isExpired();
                if(isLoggedIn){
                    sign_in_mode = 1;
                    Toast toast = Toast.makeText(getApplicationContext(),"Facebook Login success",Toast.LENGTH_SHORT);
                    toast.show();
                    RequestFacebookLoagin(accessToken);
                }

            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("Facebook Login Canceled!");
                Toast toast = Toast.makeText(getApplicationContext(),"Facebook login failed!",Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("Facebook Login Failed!");
                Toast toast = Toast.makeText(getApplicationContext(),"Facebook login error!",Toast.LENGTH_SHORT);
                toast.show();
            }
        });




        //................................................................
        // .........................hostThread............................
        // ...............................................................

        LOGinThread = new Thread(){
            @Override
            public void run() {
                while (isLoginThread){
                    if(loginRespose){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(loginSucess) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Signed in Successfully", Toast.LENGTH_SHORT);
                                    goHome();
                                }
                                else{
                                    isGuestLoggin = false;
                                    Toast toast = Toast.makeText(getApplicationContext(), "Signed in failed, Try again!", Toast.LENGTH_SHORT);
                                }
                            }
                        });
                        loginRespose = false;
                    }
                    if(LoginFalseLogout){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogOut();
                            }
                        });
                        LoginFalseLogout = false;
                    }

                }
            }
        };
        LOGinThread.start();

        //..........................................................................................
        //....................................hostThreadEnd.........................................
        //..........................................................................................


    }

    public void GuestLoginRequest(){
        if(ClientToServer.hasConnected) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        MainActivity.clientToServer.requestLogin("", "guest", "", MainActivity.GuestImageLink);
                        isGuestLoggin = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }.start();

        }
        else{
            Toast.makeText(Login.this, "Guest: You are not Connected, Please check your internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void goHome(){
        isLoginThread = false;
        HomeScreen.isHomeThread = true;
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }


    public void google_sign_in(){
        System.out.println("in gmail login");
        //go to google sign in activity
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn();

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null)
        {
            System.out.println("Gmail: Already Signed-in");
            sign_in_mode = 0;
            //update UI to not show the login page
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            //alert
            Toast toast = Toast.makeText(getApplicationContext(),"Google Signed in Successfully",Toast.LENGTH_SHORT);
            toast.show();

            RequestGoogleLogin();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    AccessTokenTracker t =  new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                sign_in_mode = -1;
                //Toast.makeText(Login.this, "logged out", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("here in else");
                loaduserProfile(currentAccessToken);
            }
        }
    };

    public void loaduserProfile (AccessToken newAccessToken)
    {
        System.out.println("in loadprofile");
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                System.out.println("In oncompleted");
                if(object != null)
                {
                    System.out.println("In object not null");
                    try{
                        String email = object.getString("email");
                        String id = object.getString("id");
                        //System.out.println(object.getJSONObject("user_friends"));
                        //String friends = object.getString("user_friends");
                        //String pp = object.getString("public_profile");

                        System.out.println(email);
                        System.out.println(id);
                        //System.out.println(friends);
                        //System.out.println(pp);
                    }
                    catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields"," email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    //google sign-in success or failed
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            //updateUI(account); // do something show sign-in was successful
            System.out.println("Gmail Signed in successfully");

            //alert
            Toast toast = Toast.makeText(getApplicationContext(),"Gmail Login success",Toast.LENGTH_SHORT);
            toast.show();

            sign_in_mode = 0;
            RequestGoogleLogin();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);//do something to show sign-in failed
            //alert
            Toast toast = Toast.makeText(getApplicationContext(),"Gmail Login failed",Toast.LENGTH_SHORT);
            toast.show();
            System.out.println("Gmail Signed in failed");
        }
    }

    private void RequestFacebookLoagin(AccessToken accessToken){
        //if logged in with facebook

        //If the user is LoggedIn then continue
        Bundle params = new Bundle();
        //params.putString("fields", "id,name,email,gender,birthday,cover,picture.type(large)");
        params.putString("fields", "id,name,picture.type(small)");

        new GraphRequest(accessToken, "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
//                                if(data.has("email"))
//                                {
//                                    System.out.println("email:");
//                                    String email = data.getString("email");
//                                    System.out.println(email);
//                                }
                                if(data.has("name"))
                                {
                                    System.out.println("name:");
                                    String name = data.getString("name");
                                    UName = name;
                                    System.out.println(name);
                                }
                                if(data.has("id"))
                                {
                                    System.out.println("id:");
                                    String id = data.getString("id");
                                    ID = id;
                                    System.out.println(id);
                                }
//                                if(data.has("birthday"))
//                                {
//                                    System.out.println("birthday:");
//                                    String birthday = data.getString("birthday");
//                                    System.out.println(birthday);
//                                }
                                if (data.has("picture")) {
                                    String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");

                                    defaultimageLink = profilePicUrl;

                                    imgLink = profilePicUrl;
                                    //profilepic.setImageBitmap(profilePic);
                                    //Glide.with(HomeScreen.this).load(profilePicUrl).into(profilepic);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(ClientToServer.hasConnected) {
                                try {
                                    MainActivity.clientToServer.requestLogin(ID, "facebook", UName, MainActivity.GuestImageLink);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                Toast.makeText(Login.this, "Facebook: You are not Connected, Please check your internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).executeAsync();

    }

    private void RequestGoogleLogin(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            ID = personId;
            UName = personName;

            System.out.println("Person Name:" + personName);
            System.out.println("Person Email:" + personEmail);
            System.out.println("person id:" + personId);

            if (personPhoto != null) {
                System.out.println("In profile pic");
                imgLink = personPhoto.toString();
                defaultimageLink = imgLink;
                //profilepic.setImageURI(personPhoto);
                //Glide.with(this).load(String.valueOf(personPhoto)).into(profilepic);
            }
            else{
                imgLink = MainActivity.GuestImageLink;
            }

            if(ClientToServer.hasConnected) {
                try {
                    MainActivity.clientToServer.requestLogin(ID, "google", UName, MainActivity.GuestImageLink);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(Login.this, "Google: You are not Connected, Please check your internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(Login.this, "Google: Log in Failed", Toast.LENGTH_SHORT).show();
        }

    }



    ///////////log out if false/////////////////////

    //sign_out from google
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //do something to show signed out
                        System.out.println("Signed out successfully");
                        //alert
                        Toast toast = Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT);
                        toast.show();
                        Login.sign_in_mode = -1;
                    }
                });
    }

    //Facebook logout
    private void fb_sign_out(){
        System.out.println("Facebook Signed out successfully");
        Toast toast = Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT);
        toast.show();
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);
        Login.sign_in_mode = -1;
    }

    private void guestSingOut(){
        Login.isGuestLoggin = false;
        Toast toast = Toast.makeText(getApplicationContext(),"No more Guests are allowed",Toast.LENGTH_SHORT);
        toast.show();
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

    @Override
    public void onBackPressed(){
        finishAndRemoveTask();
        System.exit(0);
    }
}