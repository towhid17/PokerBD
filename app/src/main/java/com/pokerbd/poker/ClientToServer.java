package com.pokerbd.poker;


import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tech.gusavila92.websocketclient.WebSocketClient;

import java.net.InetAddress;
import java.net.URI;
import java.util.Timer;
import java.util.*;

import static java.lang.StrictMath.max;

public class ClientToServer{

    //========================================================
    //
    //          THIS IS CLIENT SIDE OBJECT
    //     SOME UNNECESSARY FUNCTIONS ARE OMITTED HERE
    //
    //      INITIALIZED WITH PORT AND LINK
    //      CONNECTS TO THE SERVER
    //      THEN WAITS FOR LOGIN AND OTHER STUFFS
    //
    //========================================================

    public static ArrayList<RequestObject> requestList;

    //============================================================================
    //
    //              INITIALIZING PARAMETERS
    //
    //===========================================================================
    public static double coinPricePerCrore;
    public static int minCoinWithdraw;
    public static boolean hiddenStatus[];
    public static long coinAmountOnBuy[];
    public static double coinPriceOnBuy[];
    public static String bkashLinks[];

    private int boardTypeCount;
    public static long minCallValue[];
    public static String boardType[];
    public static long minEntryValue[];
    public static long maxEntryValue[];
    public static long mcr[];

    public static boolean isForceLogOut = false;

//    private static double coinPricePerCrore = 26.0;
//    private static long coinAmountOnBuy[] = { 30000000, 50000000, 100000000, 200000000, 500000000, 1000000000 };
//    private static long coinPriceOnBuy[] = { 100, 150, 300, 600, 1480, 2950 };
//
//    private int boardTypeCount = 10;
//    public static long minCallValue[] = {10000, 20000, 100000, 200000, 500000, 1000000, 2000000, 4000000, 10000000, 20000000};
//    public static String boardType[] = {"board1", "board2", "board3", "board4", "board5", "board6", "board7", "board8", "board9", "board10"};
//    public static long minEntryValue[] = {50000, 500000, 2000000, 5000000, 10000000, 25000000, 50000000, 100000000, 250000000, 500000000};
//    public static long maxEntryValue[] = {1000000, 5000000, 10000000, 25000000, 50000000, 100000000, 250000000, 500000000, 1000000000, 2000000000};
//    public static long mcr[] = {0, 0, 2500000, 7000000, 15000000, 40000000, 100000000, 150000000, 400000000, 1000000000};


    public static ArrayList<TransactionNumber> transactionNumbers;


    public static int buyrequestLeft = 10;

    public static boolean DISCONNECTED = false;
    public static boolean TRYCONNECTION = false;


    public static boolean isJoin = false;
    public static boolean isAbort = false;
    public static boolean isExit = false;

    public static boolean loginResponse = false;
    public static boolean logoutResponse = false;
    public static boolean buyCoinResponse = false;
    public static boolean addVideoCoinResponse = false;
    public static boolean addFreeCoinResponse = false;

    URI webSocketLink;                                      //      SOCKET LINK
    static WebSocketClient webSocketClient;
    private String received;//      SOCKET
    private static int port;                                       //      CONNECTION PORT
    private static String host;                                    //      CONNECTION LINK
    //      UNNECESSARY
    //      USED FOR LOFFING IN
    public static User user;                                      //      USER OBJECT IN CLIENT SIDE

    private JSONObject jsonIncoming;                        //      INCOMING JSON DATA
    public static boolean hasConnected;                           //      IF CONNECTION IS MADE
    private Timer connectionCheckTimer;                     //      TIMER TO CHECK IF HAS CONNECTED


    private int tryConnectionTimeCounter;                      //      SECONDS COUNTER
    private int tryCounter;
    private int connectionTimeOut;                          //      WEB SOCKET CONNECTION TIMEOUT
    private int reconnectionTimeOut;                        //      WEB SOCKET RECONNECTION TIMEOUT


    private static String curCommand;                              //      CURRENT USER COMMANDS IN GUI
    private static String msg = "";                                //      MSG STRING


    //========================================================================================
    //          DELETE THIS VARIABLES AT THE END

    //      FOR PRIVATE GAME THREADS

    public static int gameThreadId;                               //      GAME ID
    private static int gameThreadCode;                             //      GAME CODE
    private String gameRoomType;                            //      room type
    private int roomEntryValue;                             //      Entry fee of game room
    private String owner;
    public static boolean gameRunning;                            //      GAME RUNNING OR NOT

    private int tempCode;
    private String tempBoard;
    private int tempEntryValue;
    private int tempMinCallValue;

    private static String call;                                    //      CALL OF PLAYER IN CURRENT ROUND
    //private int minCallValue;                               //      MINIMUM VALUE TO CALL IN CURRENT ROUND
    private static int foldCost;                                   //      FOLD COST, FOR SMALL BLIND, BIG BLINDS
    private int boardCoin;                                  //      CURRENT COIN IN BOARD
    private int cycleCount;                                 //      CYCLE COUNT
    private static int roundCount;                                 //      ROUND COUNTS IN CURRENT GAME THREAD
    private static int turnCount;

    //============================================================================
    //              INITIALIZING DONE
    //============================================================================


    //======================================================================================================
    //
    //              JFRAME GUI SHITS
    //              IGNORE AND DELETE THIS
    //
    //======================================================================================================


    private static int inpCount = 0;                               //      INPUT COUNT
    //      USED THIS TO COMMUNICATE WITH SERVER
    //      IGNORE AND USE JSON OBJECT

    //=====================================================================================================
    //
    //=====================================================================================================


    //============================================================================
    //
    //              CONSTRUCTORS
    //
    //              INITIALIZED SOCKETS AND STREAMS HERE
    //
    //============================================================================


    public ClientToServer(URI link, int port, int tryCounter, int connectionTimeOut, int reconnectionTimeOut) {


        setUpGui();

        webSocketLink = link;
        webSocketClient = null;

        this.port = port;
        try {
            host = InetAddress.getLocalHost().getHostAddress();

        } catch (Exception e) {
            System.out.println("Exception in fetching ip -> " + e);
        }

        user = null;
        jsonIncoming = null;

        this.tryCounter = tryCounter;
        this.connectionTimeOut = connectionTimeOut;
        this.reconnectionTimeOut = reconnectionTimeOut;

        //createWebSocketClient();
        deInitializeAppData();
        tryConnection();
    }

    //=====================================================================================
    //
    //=====================================================================================


    //======================================================================
    //
    //      SETTING UP CONNECTION
    //
    //======================================================================


    private void createWebSocketClient() {

        webSocketClient = new WebSocketClient(webSocketLink) {
            @Override
            public void onOpen() {

                hasConnected = true;
                received = "";
            }

            @Override
            public void onTextReceived(String s) {
                System.out.println("OnTextReceived: "+s);
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    boolean done = jsonObject.getBoolean("done");
                    String data = jsonObject.getString("data");
                    received += data;
                    if(done == true){
                        incomingMsg(received);
                        received = "";
                    }
                }catch (Exception e){
                    System.out.println("Exception in converting json in websocket, data length " + received.length());
                    System.out.println(e);
                }
            }

            @Override
            public void onBinaryReceived(byte[] bytes) {

            }

            @Override
            public void onPingReceived(byte[] bytes) {

            }

            @Override
            public void onPongReceived(byte[] bytes) {

            }

            @Override
            public void onException(Exception e) {

                System.out.println("error ashchhe " + e);
                System.out.println("closed");

                if(hasConnected){
                    webSocketClient.close();
                    DISCONNECTED = true;
                    tryConnection();
                    TRYCONNECTION = true;
                }
            }

            @Override
            public void onCloseReceived() {
                if(hasConnected){
                    webSocketClient.close();
                    DISCONNECTED = true;
                    tryConnection();
                    TRYCONNECTION = true;
                }
            }
        };
        webSocketClient.setConnectTimeout(connectionTimeOut);
        webSocketClient.enableAutomaticReconnection(reconnectionTimeOut);

    }

    private void tryConnection() {
        hasConnected = false;
        tryConnectionTimeCounter = tryCounter;
        connectionCheckTimer = new Timer();

        createWebSocketClient();
        webSocketClient.connect();
        connectionCheckTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                connectionChecker();
            }
        }, 0, 1000);

    }

    private void connectionChecker() {

        if (hasConnected) {
            tryConnectionTimeCounter = -1;
            connectionCheckTimer.cancel();

            System.out.println("Connection established with server");

        } else tryConnectionTimeCounter--;

        if (tryConnectionTimeCounter == 0) {

            tryConnectionTimeCounter = -1;
            connectionCheckTimer.cancel();
            webSocketClient.close();

            System.out.println("Cannot connect to server");
        }

    }

    //=====================================================================================
    //
    //=====================================================================================


    //======================================================================
    //
    //      COMMUNICATION
    //
    //======================================================================

    private static JSONObject initiateJson() throws JSONException {

        JSONObject temp = new JSONObject();

        temp.put("sender", "Client");
        temp.put("ip", host);
        temp.put("port", port);

        return temp;
    }

    public static void sendBoardDataRequest() throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "BoardData");

        sendMessage(send.toString());
    }

    private static void sendMessage(String temp) {
        try {
            System.out.println("sending length -> " + temp.toString().getBytes("UTF-8").length);

            String[] splitted = FluentIterable.from(Splitter.fixedLength(8000).split(temp)).toArray(String.class);

            for(int i=0; i<splitted.length-1; i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("done", false);
                jsonObject.put("data", splitted[i]);

                webSocketClient.send(jsonObject.toString());
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("done", true);
            jsonObject.put("data", splitted[splitted.length-1]);

            System.out.println("Sending-> "+jsonObject.toString());
            webSocketClient.send(jsonObject.toString());
            System.out.println("sending done");

        } catch (Exception e) {
            System.out.println("Error in sending msg to server -> " + e);
        }
    }

    private void incomingMsg(String temp) throws JSONException {
        System.out.println("Incoming Message: "+temp);
        try {
            jsonIncoming = new JSONObject(temp);

        } catch (Exception e) {
            System.out.println("Error in getting json in client side\n" + e);
            jsonIncoming = null;
            return;
        }

        if (jsonIncoming.get("requestType").equals("LoginResponse")) {
            HomeScreen.isBuyButtonSuccess = jsonIncoming.getBoolean("transactionButtons");
            loginRequestResponse(jsonIncoming.getBoolean("response"), jsonIncoming.getJSONObject("data"));
            Login.loginRespose = true;
        }
        else if (jsonIncoming.get("requestType").equals("LogoutResponse")) {

            logoutRequestResponse(jsonIncoming.getBoolean("success"));
        }
        else if (jsonIncoming.get("requestType").equals("ForceLogout")) {

            forceLogout(jsonIncoming);
            isForceLogOut = true;
        }
        else if (jsonIncoming.get("requestType").equals("AddTransactionResponse")){

            transactionRequestResponse(jsonIncoming);
        }
        else if (jsonIncoming.get("requestType").equals("AllTransactionsResponse")){

            allTransactionsRequestResponse(jsonIncoming);
        }
        else if (jsonIncoming.get("requestType").equals("AddCoinVideoResponse")) {

            //requesting to buy coins

            receiveAddCoinVideoResponse(jsonIncoming);
            //gameStartIfInAGame();
        }
        else if (jsonIncoming.get("requestType").equals("AddFreeCoinResponse")) {

            //requesting to buy coins

            receiveAddFreeCoinResponse(jsonIncoming);
            HomeScreen.freecoinResponse = true;
            //gameStartIfInAGame();
        }
        else if (jsonIncoming.get("requestType").equals("FriendsList")) {

            //requesting friends list
            JSONArray friends = jsonIncoming.getJSONArray("data");

            showFriends(friends);
        }
        else if (jsonIncoming.get("requestType").equals("JoinResponse")) {

            requestJoinResponse(jsonIncoming);
        }
        else if (jsonIncoming.get("requestType").equals("AbortResponse")) {

            requestAbortResponse(jsonIncoming);
        }
        else if (jsonIncoming.get("requestType").equals("UpdateOwnResponse")) {

            loadUpdateOwnResponse(jsonIncoming);
        }
        else if (jsonIncoming.get("requestType").equals("UpdateOwnInGameResponse")) {

            loadUpdateOwnInGameResponse(jsonIncoming);
        }
        else if (jsonIncoming.get("requestType").equals("GameRoom")) {

            JSONObject tempJson = jsonIncoming.getJSONObject("gameData");

            if (tempJson.get("gameRequest").equals("InitializeGameData")) {

                initializeGameData(jsonIncoming);
                isExit = true;
                isAbort = false;
            }
            else if (tempJson.get("gameRequest").equals("LoadPlayersData")) {

                loadPlayersData(jsonIncoming);
                LobbyScreen.loadRoomData = true;
                System.out.println("ClinettoServer: LoadPlayerData");
            }
            else if (tempJson.get("gameRequest").equals("WelcomeGameMessage")) {

                showWelcomeGameMessage(jsonIncoming);
                isExit = true;
            }
            else if (tempJson.get("gameRequest").equals("RoundStartMessage")) {

                gameRoundStart(jsonIncoming);
                LobbyScreen.showRoundStartMessage = true;
            }
            else if (tempJson.get("gameRequest").equals("LoadPlayerCards")) {

                loadCards(jsonIncoming);
                LobbyScreen.LoadPlayerCards = true;
            }
            else if (tempJson.get("gameRequest").equals("LoadBoardCards")) {

                loadCards(jsonIncoming);
                LobbyScreen.LoadBoardCards = true;
            }
            else if (tempJson.get("gameRequest").equals("ShowBoardInfo")) {

                showBoardInfo(jsonIncoming);

            }
            else if (tempJson.get("gameRequest").equals("ShowCards")) {

                showCards();
            }
            else if (tempJson.get("gameRequest").equals("ShowNextTurnInfo")) {

                showNextTurnInfo(jsonIncoming);
                LobbyScreen.PlayerWithTurn = true;
            }
            else if (tempJson.get("gameRequest").equals("DisableGameButtons")) {

                disableGameButtons();
                LobbyScreen.disableButton = true;
            }
            else if (tempJson.get("gameRequest").equals("EnableGameButtons")) {

                enableGameButtons(jsonIncoming);
                LobbyScreen.enableButton = true;
            }
            else if (tempJson.get("gameRequest").equals("TurnInfo")) {

                showTurnInfo(jsonIncoming);
                LobbyScreen.showTurnInfo = true;
            }
            else if (tempJson.get("gameRequest").equals("CycleEnd")) {

                showEndCycle(jsonIncoming);
                LobbyScreen.showEndCycle = true;
            }
            else if (tempJson.get("gameRequest").equals("Result")) {

                processResult(jsonIncoming);
                LobbyScreen.proceesResult = true;

            }
            else if (tempJson.get("gameRequest").equals("ShowAllCards")) {

                showAllCards((jsonIncoming));
                LobbyScreen.showAllCards = true;
            }
            else if (tempJson.get("gameRequest").equals("WaitForPlayersToBuy")) {

                waitForPlayersToBuy();
                LobbyScreen.waitForPlayersToBuy = true;
            }
            else if (tempJson.get("gameRequest").equals("AddBoardCoinResponse")) {

                addBoardCoinInGameResponse(jsonIncoming);
            }
            else if (tempJson.get("gameRequest").equals("LeaveGame")) {

                leaveGameRoom();

                disableGameButtons();
                isExit = false;
                isJoin = true;
                LobbyScreen.kickMeResponse = true;
            }
            else if (tempJson.get("gameRequest").equals("AskJoinGameThreadByCodeResponse")){
                joinGameThreadByCodeResponse(jsonIncoming);
            }
            else if(tempJson.get("gameRequest").equals("AskBoardCoin")){
                askBoardCoinForGame(jsonIncoming);
                JoinFriends.askBoardCoin = true;
            }
            else if(tempJson.get("gameRequest").equals("DeductBlindCoins")){

                deductBlindCoins(jsonIncoming);
                LobbyScreen.deductBlind = true;
            }

        }
        else if (jsonIncoming.get("requestType").equals("WaitingRoom")) {

            JSONObject tempJson = jsonIncoming.getJSONObject("waitingRoomData");

            if(tempJson.get("requestType").equals("CreateWaitingRoomResponse")){

                createWaitingRoomResponse(jsonIncoming);
            }
            else if(tempJson.get("requestType").equals("AskBoardCoin")){

                askBoardCoin(jsonIncoming);
                JoinFriends.askBoardCoin = true;
                playwithfriend.setBoardCoin = true;
                System.out.println("AskBoarCoin");
            }
            else if(tempJson.get("requestType").equals("InitializeUser")){

                initializeWaitingRoomData(jsonIncoming);
                playwithfriend.initializeRoom = true;
                JoinFriends.initializeData = true;
            }
            else if(tempJson.get("requestType").equals("AskJoinWaitingRoomByCodeResponse")) {

                askJoinWaitingRoomByCodeResponse(jsonIncoming);

            }
            else if(tempJson.get("requestType").equals("RemoveFromWaitingRoomResponse")) {

                removedFromWaitingRoom(jsonIncoming);
            }
            else if(tempJson.get("requestType").equals("StartGameResponse")) {

                showStartGameResponse(jsonIncoming);
                HostFriends.gameStart = true;
                JoinFriends.gameStart = true;
            }
            else if(tempJson.get("requestType").equals("LoadPlayersData")){

                loadPlayersDataWaitingRoom(jsonIncoming);
                HostFriends.loadData = true;
                JoinFriends.loadData = true;
            }
            else if(tempJson.get("requestType").equals("EditBoardCoinResponse")){

                editBoardCoinInWaitingRoomResponse(jsonIncoming);
            }
        }
        else if (jsonIncoming.get("requestType").equals("CheckConnection")){

            sendConnectionCheckResponse();
        }
        else if (jsonIncoming.get("requestType").equals("LoadBoardData")){

            loadBoardData(jsonIncoming);
            HomeScreen.isBoardDataLoaded = true;
            HomeScreen.loadBoardDataCount++;
        }
        else if (jsonIncoming.get("requestType").equals("LoadShopData")){

            loadShopData(jsonIncoming);
            HomeScreen.isShopDataLoaded = true;
        }
        else if (jsonIncoming.get("requestType").equals("ShowNotifications")){

            showNotifications(jsonIncoming);
        }
        else if (jsonIncoming.get("requestType").equals("TokenRequestResponse")){

            showToken(jsonIncoming);
        }
        else if (jsonIncoming.get("requestType").equals("Version")){

            versionResponse(jsonIncoming);
            MainActivity.isVersionRespose = true;
        }
        else if (jsonIncoming.get("requestType").equals("CurrentImage")){

            currentImageResponse(jsonIncoming);
            HomeScreen.currentImageResponse = true;
        }

    }

    //=====================================================================================
    //
    //=====================================================================================


    private void initializeAppData(JSONObject jsonObject) throws JSONException {

        JSONArray array;

        minCoinWithdraw = jsonObject.getInt("minCoinWithdraw");

        coinPricePerCrore = jsonObject.getDouble("coinPricePerCrore");

        array = jsonObject.getJSONArray("coinAmountOnBuy");
        coinAmountOnBuy = new long[array.length()];
        for(int i=0; i<array.length(); i++) coinAmountOnBuy[i] = array.getLong(i);

        array = jsonObject.getJSONArray("hiddenStatus");
        hiddenStatus = new boolean[array.length()];
        for(int i=0; i<array.length(); i++) hiddenStatus[i] = array.getBoolean(i);

        array = jsonObject.getJSONArray("coinPriceOnBuy");
        coinPriceOnBuy = new double[array.length()];
        for(int i=0; i<array.length(); i++) coinPriceOnBuy[i] = array.getDouble(i);

        boardTypeCount = jsonObject.getInt("boardTypeCount");

        array = jsonObject.getJSONArray("minCallValue");
        minCallValue = new long[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) minCallValue[i] = array.getLong(i);

        array = jsonObject.getJSONArray("boardType");
        boardType = new String[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) boardType[i] = array.getString(i);

        array = jsonObject.getJSONArray("minEntryValue");
        minEntryValue = new long[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) minEntryValue[i] = array.getLong(i);

        array = jsonObject.getJSONArray("maxEntryValue");
        maxEntryValue = new long[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) maxEntryValue[i] = array.getLong(i);

        array = jsonObject.getJSONArray("mcr");
        mcr = new long[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) mcr[i] = array.getLong(i);

        User.setExpIncrease(jsonObject.getInt("expIncrease"));

        array = jsonObject.getJSONArray("rankString");
        String[] temp = new String[array.length()];
        for(int i=0; i<array.length(); i++) temp[i] = array.getString(i);
        User.setRankString(temp);

        array = jsonObject.getJSONArray("ranksValue");
        long[] temp2 = new long[array.length()];
        for(int i=0; i<array.length(); i++) temp2[i] = array.getLong(i);
        User.setRanksValue(temp2);
    }

    private void deInitializeAppData(){

        coinPricePerCrore = 0.0;
        coinAmountOnBuy = null;
        coinPriceOnBuy = null;

        boardTypeCount = 0;
        minCallValue = null;
        boardType = null;
        minEntryValue = null;
        maxEntryValue = null;
        mcr = null;

        User.setExpIncrease(0);
        User.setRankString(null);
        User.setRanksValue(null);
    }
    //=============================================================================
    //
    //          LOGIN FUNCTIONS, UPDATE OWN FUNCTION
    //
    //          (FB_ID, "facebook"), (GMAIL_ID, "google") ( empty, "guest")
    //
    //=============================================================================

    public static void requestLogin(String account_data, String account_type, String username, String imageLink) throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "LoginRequest");

        JSONObject tempJson = new JSONObject();

        tempJson.put("account_id", account_data);
        tempJson.put("account_type", account_type);
        tempJson.put("account_username", username);
        tempJson.put("imageLink", imageLink);

        send.put("data", tempJson);
        sendMessage(send.toString());
    }

    private void loginRequestResponse(boolean success, JSONObject data) throws JSONException {

        if (success) {

            //logged in successfully
            isJoin = true;
            loginResponse = true;
            Login.loginSucess = true;
            Login.LoginFalseLogout = false;
            //making new users

            initializeAppData(data.getJSONObject("appData"));
            loadUser(data);
            System.out.println("Welcome " + user.getUsername());
        } else {
            loginResponse = false;
            Login.loginSucess = false;
            Login.LoginFalseLogout = true;
            System.out.println("Login failed! No More guests allowed");
        }
    }

    private void loadUser(JSONObject temp) throws JSONException {
        user = User.JSONToUser(temp);
        user.setLoggedIn(true);
    }

    public static void shopDataRequest() throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "ShopData");

        sendMessage(send.toString());
    }

    private void requestUpdateOwn() throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "UpdateOwn");

        sendMessage(send.toString());
    }

    private void loadUpdateOwnResponse(JSONObject jsonObject) throws JSONException {

        boolean response = jsonObject.getBoolean("response");

        if (response) user = User.JSONToUser(jsonObject.getJSONObject("data"));
    }

    private static void requestUpdateOwnInGame() throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "UpdateOwnInGame");

        sendMessage(send.toString());
    }

    private void loadUpdateOwnInGameResponse(JSONObject jsonObject) throws JSONException {

        boolean response = jsonObject.getBoolean("response");

        if (response) user = User.JSONToUserInGame(jsonObject.getJSONObject("data"));
    }



    private void logoutUser() {

        //logged out successfully
        isJoin = false;
        logoutResponse = true;

        System.out.println("Logged out user " + user.getUsername());
        user = null;
    }

    private void logoutRequestResponse(boolean success) {

        if (success) {
            logoutUser();
            ProfileMenu.isLogOut = true;
        } else{
            logoutResponse = false;
            System.out.println("Logout request failed");
        }
    }

    public static void sendLogoutRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "LogoutRequest");

        sendMessage(send.toString());
    }

    private static void closeEverything() {

        try {
            user = null;
            webSocketClient.close();
        } catch (Exception e) {
            System.out.println("Error in closing connection in Client side, error -> " + e);
        }
    }

    //=====================================================================================
    //
    //=====================================================================================


    //=====================================================================================
    //
    //                  COIN ADDING FUNCTIONS
    //
    //=====================================================================================

    private void getCoinBuyWithdrawDataRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("id", user.getId());
        send.put("username", user.getUsername());
        send.put("requestType", "CoinBuyWithdrawDataRequest");

        sendMessage(send.toString());
    }

    private void getCoinBuyWithdrawDataResponse(JSONObject jsonObject) throws JSONException {

        int sz = jsonObject.getJSONArray("buyCoinData").length();

        double withdrawPerCrore = jsonObject.getDouble("withdrawPerCrore");
        long buyCoinAmount[] = new long[sz];
        double buyCoinPrice [] = new double[sz];

        JSONArray buyCoinData = jsonObject.getJSONArray("buyCoinData");
        for(int i=0; i<sz; i++){

            JSONObject temp = buyCoinData.getJSONObject(i);
            buyCoinAmount[i] = temp.getLong("amount");
            buyCoinPrice[i] = temp.getDouble("price");
        }
    }

    public double getCurrencyAmount(long coinAmount, String req){

        double price = 0.0;

        if(req.equals("buy")) {

            for(int i=0; i<coinAmountOnBuy.length; i++){
                if(coinAmount == coinAmountOnBuy[i]){
                    price = coinPriceOnBuy[i];
                    break;
                }
            }
        }
        else if(req.equals("withdraw")){

            price = coinPricePerCrore * ( coinAmount / 10000000 );
        }
        return price;
    }


    private void receiveBuyCoinResponse(JSONObject temp) throws JSONException {

        boolean success = jsonIncoming.getBoolean("success");
        long value = jsonIncoming.getLong("currentCoin");
        double price = jsonIncoming.getDouble("price");

        if (success) user.setCurrentCoin(value);
       // addTextInGui(jsonIncoming.getString("message") + " price " + price);
    }

    public static void coinBuyRequest(long coinAmount, String method, String trId, String sender, String receiver, String invoice, String total, String price, String fee) throws JSONException {

        JSONObject send = initiateJson();

        send.put("id", user.getId());
        send.put("username", user.getUsername());
        send.put("requestType", "Transaction");

        JSONObject tempJson = new JSONObject();

        tempJson.put("request", "BuyCoin");
        tempJson.put("method", method);
        tempJson.put("transactionId", trId);
        tempJson.put("coinAmount", coinAmount);
        tempJson.put("time", Calendar.getInstance().getTime());
        tempJson.put("sender", sender);
        tempJson.put("receiver", receiver);
        tempJson.put("invoice", invoice);
        tempJson.put("total", Double.parseDouble(total));
        tempJson.put("price", Double.parseDouble(price));
        tempJson.put("fee", Double.parseDouble(fee));


        send.put("data", tempJson);

        sendMessage(send.toString());
    }

    private void receiveWithdrawCoinResponse(JSONObject jsonObject) throws JSONException {

        boolean success = jsonIncoming.getBoolean("success");
        long value = jsonIncoming.getLong("currentCoin");
        double price = jsonIncoming.getDouble("price");
        String transactionId = jsonIncoming.getString("transactionId");

        if (success) user.setCurrentCoin(value);
        //addTextInGui(jsonIncoming.getString("message") + " price " + price);
    }

    private void getTransactionsRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("id", user.getId());
        send.put("username", user.getUsername());
        send.put("requestType", "Transaction");

        JSONObject tempJson = new JSONObject();

        tempJson.put("request", "ShowTransactions");
        send.put("data", tempJson);

        sendMessage(send.toString());
    }

    public static void addCoinByVideoRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("id", user.getId());
        send.put("username", user.getUsername());
        send.put("requestType", "AddCoinVideoRequest");
        send.put("requestTime", Calendar.getInstance().getTime());

        sendMessage(send.toString());
    }

    private void receiveAddCoinVideoResponse(JSONObject temp) throws JSONException {

        boolean success = jsonIncoming.getBoolean("success");
        long value = jsonIncoming.getLong("currentCoin");
        Date d = User.stringToDate(jsonIncoming.getString("lastCoinVideoAvailableTime"));
        long added = jsonIncoming.getLong("coinAdded");
        int count = jsonIncoming.getInt("coinVideoCount");

        if (success) {
            user.setCurrentCoin(value);
            user.setLastCoinVideoAvailableTime(d);
            addVideoCoinResponse = true;
            user.setCoinVideoCount(count);
            AdVideo.videoLeft = count;
            AdVideo.coinAdded = added;
            AdVideo.rewardResponse = true;
        }
        else addVideoCoinResponse = false;
        System.out.println(jsonIncoming.getString("message"));
    }

    public static void addFreeCoinRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("id", user.getId());
        send.put("username", user.getUsername());
        send.put("requestType", "AddFreeCoinRequest");
        send.put("requestTime", Calendar.getInstance().getTime());

        sendMessage(send.toString());

    }

    private void receiveAddFreeCoinResponse(JSONObject temp) throws JSONException {

        boolean success = jsonIncoming.getBoolean("success");
        long value = jsonIncoming.getLong("currentCoin");
        Date d = User.stringToDate(jsonIncoming.getString("lastFreeCoinTime"));
        long coinAdded = jsonIncoming.getLong("coinAdded");

        if (success) {
            user.setCurrentCoin(value);
            user.setLastFreeCoinTime(d);
            HomeScreen.isFreeCoinAdded = 1;
            HomeScreen.freeCoinAdded = coinAdded;
        }
        else{
            HomeScreen.isFreeCoinAdded = 2;
        }
        System.out.println(jsonIncoming.getString("message"));
    }


    //=====================================================================================
    //
    //=====================================================================================


    //=====================================================================================
    //
    //                  JOIN/EXIT GAME FUNCTIONS
    //
    //=====================================================================================

    public static void requestJoin(int gameId, int gameCode, String boardType, long minEntryValue, long minCallValue, int owner_id, int seatPosition, long boardCoin) throws JSONException {

        user.initializeGameData(gameId, gameCode, boardType, minEntryValue, minCallValue, owner_id, seatPosition, boardCoin);
        sendJoinRequest();
    }

    private static void sendJoinRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "JoinRequest");

        JSONObject tempJson = new JSONObject();

        tempJson.put("gameId", user.getGameId());
        tempJson.put("gameCode", user.getGameCode());
        tempJson.put("boardType", user.getBoardType());
        tempJson.put("minEntryValue", user.getMinEntryValue());
        tempJson.put("minCallValue", user.getMinCallValue());
        tempJson.put("owner_id", user.getOwner_id());
        tempJson.put("seatPosition", user.getSeatPosition());
        tempJson.put("boardCoin", user.getBoardCoin());

        send.put("data", tempJson);

        sendMessage(send.toString());
    }

    private void requestJoinResponse(JSONObject temp) throws JSONException {

        System.out.println(jsonIncoming.get("data").toString());
        System.out.println("Abort");
        isAbort = true;
    }


    public static void requestAbort() throws JSONException {

        user.deInitializeGameData();
        sendRequestAbort();
    }

    private static void sendRequestAbort() throws JSONException {

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "AbortRequest");

        sendMessage(send.toString());
    }

    private void requestAbortResponse(JSONObject temp) throws JSONException {

        System.out.println(jsonIncoming.get("data").toString());
        System.out.println("Join");
        isJoin = true;
        LobbyScreen.abortResponse = true;
    }


    public static void requestExit() throws JSONException {

        sendExitRequest();
       // leaveGameRoom();

        disableGameButtons();
        isExit = false;
        isJoin = true;
    }

    private static void sendExitRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();

        tempJson.put("gameId", gameThreadId);
        tempJson.put("gameCode", gameThreadCode);
        tempJson.put("requestType", "ExitGame");

        send.put("gameData", tempJson);

        sendMessage(send.toString());
    }

    private static void leaveGameRoom() {

        user.deInitializeGameData();
    }

    //=====================================================================================
    //
    //=====================================================================================








    //=======================================================================================
    //
    //              GAME THREAD FUNCTIONALITIES
    //
    //=======================================================================================


    //=======================================================================================
    //              INITIALIZING GAME DATA
    //=======================================================================================

    private void initializeGameData(JSONObject jsonObject) throws JSONException {

        JSONObject gameData = jsonObject.getJSONObject("gameData");

        int playerCount = gameData.getInt("playerCount");
        int gameId = gameData.getInt("gameId");
        int gameCode = gameData.getInt("gameCode");
        int ownerId = gameData.getInt("ownerId");
        int seatPosition = gameData.getInt("seatPosition");
        int maxPlayerCount = gameData.getInt("maxPlayerCount");

        user.joinedAGame(gameId, gameCode, ownerId, seatPosition, maxPlayerCount, playerCount);
    }

    private void loadPlayersData(JSONObject temp) throws JSONException {

        JSONArray data = temp.getJSONArray("data");

        for(int i=0; i<user.getInGamePlayers().length; i++){
            user.getInGamePlayers()[i] = null;
        }

        for (int i = 0; i < data.length(); i++) {

            User tempUser = User.JSONToUserInGame((JSONObject) data.get(i));
            user.getInGamePlayers()[tempUser.getSeatPosition()] = tempUser;
        }
        User.loadOwnSelfFromInGamePlayers(user);

        User[] users = user.getInGamePlayers();

        if(users!=null){
            for(int i=0; i<users.length; i++){
                if(users[i]!=null)
                    System.out.println(users[i].getUsername());
            }
        }
    }

    private void showWelcomeGameMessage(JSONObject jsonObject) throws JSONException {             //GAME STARTING E

        JSONObject gameData = jsonObject.getJSONObject("gameData");

        String show = gameData.getString("message");
        LobbyScreen.Messeage = show;
    }


    //=======================================================================================
    //              INITIALIZING GAME DATA
    //=======================================================================================


    //=======================================================================================
    //              INOMINGS FROM GAME THREAD
    //=======================================================================================

    private void gameRoundStart(JSONObject jsonObject) throws JSONException {                     //ROUND STARTING E

        String message = jsonObject.getString("message");
        LobbyScreen.Messeage = "New Round Started";
    }


    private void loadCards(JSONObject jsonObject) throws JSONException {

        int loc = -1;
        JSONArray array = jsonObject.getJSONArray("data");
        String req = jsonObject.getJSONObject("gameData").getString("gameRequest");

        if (req.equals("LoadPlayerCards")) loc = 1;
        else if (req.equals("LoadBoardCards")) loc = 0;


        ArrayList location;
        if (loc == 1) {
            location = user.getPlayerCards();
            location.clear();
        }
        else {
            location = user.getBoardCards();
            if(array.length() == 0) location.clear();
        }

        for (int i = 0; i < array.length(); i++) {

            String x[] = array.getString(i).split("\\.");

            Card card = new Card(Integer.valueOf(x[0]) - 1, Integer.valueOf(x[1]) - 2, loc);
            location.add(card);
        }
    }




    //  BOTH OF THEM UNNECESSARY, SHOW BOARD INFO CALLED FROM
    //  AROUND LINE 443

    private void showCards() {

        String bleh = "";

        bleh = "Board: ";
        for (int i = 0; i < user.getBoardCards().size(); i++)
            bleh += ((Card) user.getBoardCards().get(i)).toStringWithoutType() + "\n";



        bleh = "Player: ";
        for (int i = 0; i < user.getPlayerCards().size(); i++)
            bleh += ((Card) user.getPlayerCards().get(i)).toStringWithoutType() + "\n";

    }

    private void showBoardInfo(JSONObject jsonObject) throws JSONException {

        JSONObject data = jsonObject.getJSONObject("data");

        int roundCount = data.getInt("roundCount");
        long roundCoins = data.getLong("roundCoins");
        int turnCount = data.getInt("turnCount");
        int cycleCount = data.getInt("cycleCount");
        long roundCall = data.getLong("roundCall");

        String show = "round " + roundCount + " cycle " + cycleCount + " turn " + turnCount + "\n";
        show += "round coin " + roundCoins + " round minimum call " + roundCall;

    }

    private void showNextTurnInfo(JSONObject jsonObject) throws JSONException {

        JSONObject temp = jsonObject.getJSONObject("data");

        String username = temp.getString("username");
        int pos = temp.getInt("seatPosition");

        LobbyScreen.PlayerWithTurnID = getId(pos, user.getSeatPosition());

        String show = username + "'s turn, seat position " + pos;
    }



    //===================================================================================
    //
    //      GAME BUTTONS ENABLING
    //
    //===================================================================================

    private void enableGameButtons(JSONObject JsonObject) throws JSONException {
        JSONArray temp = JsonObject.getJSONArray("data");

        String show = "Your current coin: " + user.getBoardCoin() + "\n";
        long cost;

        for (int i = 0; i < temp.length(); i++) {

            JSONObject jsonObject = temp.getJSONObject(i);

            if (jsonObject.getString("name").equals("Fold")) {

                LobbyScreen.isFoldAvailable = true;

                show += "You can fold";
                show += "\n";

            }
            else if (jsonObject.getString("name").equals("Call")) {

                cost = jsonObject.getLong("cost");
                LobbyScreen.callCost = cost;
                show += "You can call, minimum value: " + cost + "\n";
                LobbyScreen.isCallAvailable = true;
            }
            else if (jsonObject.getString("name").equals("Raise")) {

                cost = jsonObject.getLong("cost");
                LobbyScreen.raiseCost = cost;
                long maxcost = jsonObject.getLong("maxCost");
                LobbyScreen.raiseMaxCost = maxcost;
                long stepSize = jsonObject.getLong("stepSize");
                LobbyScreen.raiseStepSize = stepSize;
                show += "You can raise, minimum value: " + cost + "\n";
                LobbyScreen.isRaiseAvailable = true;
            }
            else if (jsonObject.getString("name").equals("Check")) {

                show += "You can check\n";
                LobbyScreen.isCheckAvailable = true;
            }
            else if (jsonObject.getString("name").equals("AllIn")) {

                show += "You can go all in\n";
                LobbyScreen.isAllinAvailable = true;
            }
        }

    }

    private static void disableGameButtons() {
        LobbyScreen.isAllinAvailable = false;
        LobbyScreen.isCallAvailable = false;
        LobbyScreen.isFoldAvailable = false;
        LobbyScreen.isRaiseAvailable = false;
        LobbyScreen.isCheckAvailable = false;
    }

    //=================================================================================





    //=====================================================================================
    //
    //              GAMETHREAD OUTCOMING
    //
    //
    //=====================================================================================

    public static void sendGameThreadCallRequest() throws JSONException {

        user.setTotalCallCount(user.getTotalCallCount()+1);
        user.setCallCount(user.getCallCount()+1);
        user.setBoardCoin(user.getBoardCoin() - user.getRoundCall());
        user.setCallValue(user.getRoundCall());
        user.setTotalCallValue(user.getTotalCallValue() + user.getRoundCall());
        user.setCall("Call");
        user.setRoundCoins(user.getRoundCoins() + user.getRoundCall());

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();

        tempJson.put("requestType", "GameCall");
        tempJson.put("call", "Call");

        send.put("gameData", tempJson);

        sendMessage(send.toString());
    }

    public static void sendGameThreadRaiseRequest(long value) throws JSONException {

        //      RAISE COIN SLIDER BETWEEN
        //      user.getRoundCall()     to      user.getBoardCoin()

        if( !(value > 0 && value <= user.getBoardCoin()) ){

            return ;
        }

        user.setTotalCallCount(user.getTotalCallCount()+1);
        user.setRaiseCount(user.getRaiseCount()+1);
        user.setBoardCoin(user.getBoardCoin()-value);
        user.setCallValue(value);
        user.setTotalCallValue(user.getTotalCallValue() + value);
        user.setCall("Raise");
        user.setRoundCoins(user.getRoundCoins() + value);
        user.setRoundCall(value);

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();

        tempJson.put("requestType", "GameCall");
        tempJson.put("call", "Raise");
        tempJson.put("cost", value);

        send.put("gameData", tempJson);

        sendMessage(send.toString());
    }

    public static void sendGameThreadAllInRequest() throws JSONException {

        user.setTotalCallCount(user.getTotalCallCount()+1);
        user.setAllInCount(user.getAllInCount()+1);
        user.setCallValue(user.getBoardCoin());
        user.setTotalCallValue(user.getTotalCallValue()+user.getBoardCoin());
        user.setCall("AllIn");
        user.setRoundCoins(user.getRoundCoins()+user.getBoardCoin());
        if(user.getBoardCoin() > user.getRoundCall()) user.setRoundCall(user.getBoardCoin());
        user.setBoardCoin(0);

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();
        tempJson.put("requestType", "GameCall");
        tempJson.put("call", "AllIn");

        send.put("gameData", tempJson);

        sendMessage(send.toString());
    }

    public static void sendGameThreadCheckRequest() throws JSONException {

        user.setTotalCallCount(user.getTotalCallCount()+1);
        user.setCheckCount(user.getCheckCount()+1);
        user.setCall("Check");

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();
        tempJson.put("requestType", "GameCall");
        tempJson.put("call", "Check");

        send.put("gameData", tempJson);

        sendMessage(send.toString());
    }

    public static void sendGameThreadFoldRequest() throws JSONException {

        user.setTotalCallCount(user.getTotalCallCount() + 1);
        user.setFoldCount(user.getFoldCount()+1);
        user.setBoardCoin(user.getBoardCoin() - user.getFoldCost());
        user.setCall("Fold");
        user.setRoundCoins(user.getRoundCoins() + user.getFoldCost());

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();
        tempJson.put("requestType", "GameCall");
        tempJson.put("call", "Fold");

        send.put("gameData", tempJson);

        sendMessage(send.toString());
    }


    public void showTurnInfo(JSONObject jsonObject) throws JSONException {

        JSONObject temp = jsonObject.getJSONObject("data");

        String username = temp.getString("username");
        int seatPosition = temp.getInt("seatPosition");
        String call = temp.getString("call");
        long value = temp.getLong("value");

        if(user.getInGamePlayers()[seatPosition]!=null){
            long t = user.getInGamePlayers()[seatPosition].getTotalCallValue();
            user.getInGamePlayers()[seatPosition].setTotalCallValue(t+value);
            user.getInGamePlayers()[seatPosition].setCall(call);
        }

        LobbyScreen.prevPlayerWithTurnID = getId(seatPosition, user.getSeatPosition())-1;
        LobbyScreen.prevPlayerCall = call;
        LobbyScreen.prevPlayerCallValue = value;

        String show = username + " " + call + "ed with value " + value + ", seatPosition " + seatPosition;

    }

    //=====================================================================================
    //
    //
    //=====================================================================================
















    //=====================================================================================
    //
    //              ROUND ENDING RELATED STUFF
    //
    //
    //=====================================================================================

    private void showEndCycle(JSONObject jsonObject) throws JSONException {

        String msg = jsonObject.getString("data");
        LobbyScreen.Messeage = msg;
    }

    private void showAllCards(JSONObject jsonObject) throws JSONException {

        String show;
        JSONObject data = jsonObject.getJSONObject("data");

        JSONArray temp;

        show = "All board cards: \n";
        temp = data.getJSONArray("allBoardCards");

        for (int i = 0; i < temp.length(); i++) show += temp.getString(i) + "\n";
        show += "\n";


        show += "All player cards: \n\n";
        temp = data.getJSONArray("allPlayerCards");

        for (int i = 0; i < temp.length(); i++) {

            JSONObject tempObject = temp.getJSONObject(i);
            JSONArray tempArray = tempObject.getJSONArray("cards");

            int sp = tempObject.getInt("seatPosition");
            int id = getId(sp, user.getSeatPosition())-1;
            String c1 = tempArray.getString(0);
            String c2 = tempArray.getString(1);
            String[] x1 = c1.split(" ");
            String[] x2 = c2.split(" ");

            if(x1.length==2 && x2.length==2){
                String card1 = getCardDotSuit(x1[0], x1[1]);
                String card2 = getCardDotSuit(x2[0], x2[1]);

                LobbyScreen.playerDetails[id].setCard1(card1);
                LobbyScreen.playerDetails[id].setCard2(card2);
            }
        }

    }

    public static String getCardDotSuit(String value, String suit){
        String card = "";
        if(suit.equalsIgnoreCase("HEART")){
            card+="1.";
        }
        else if(suit.equalsIgnoreCase("DIAMOND")){
            card+="2.";
        }
        else if(suit.equalsIgnoreCase("CLUBS")){
            card+="3.";
        }
        else if(suit.equalsIgnoreCase("SPADE")){
            card+="4.";
        }

        if(value.equalsIgnoreCase("J")){
            card += "11";
        }
        else if(value.equalsIgnoreCase("Q")){
            card += "12";
        }
        else if(value.equalsIgnoreCase("K")){
            card += "13";
        }
        else if(value.equalsIgnoreCase("A")){
            card += "14";
        }
        else{
            card += value;
        }

        return card;
    }


    private void loadWinnerData(JSONObject jsonObject) throws JSONException {


        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray eachPlayerData = data.getJSONArray("eachPlayerData");

        boolean showCardsAtEnd = data.getBoolean("showCards");

        for(int i=0; i<eachPlayerData.length(); i++){

            JSONObject jsonObject1 = eachPlayerData.getJSONObject(i);

            int seat = jsonObject1.getInt("seat");
            long winAmount = jsonObject1.getLong("winAmount");
            boolean hasWon = jsonObject1.getBoolean("hasWon");
            long coinBack = jsonObject1.getLong("coinBack");
            String power = jsonObject1.getString("power");


            User temp = user.getInGamePlayers()[seat];

            if(temp == null) continue;
            temp.setRoundsPlayed(temp.getRoundsPlayed() + 1);
            temp.setBoardCoin(temp.getBoardCoin() + coinBack);

            if(hasWon){
                temp.setRoundsWon(temp.getRoundsWon() + 1);
                temp.setWinStreak(temp.getWinStreak() + 1);
                temp.setCoinWon(temp.getCoinWon() + winAmount);
                temp.setBiggestWin( max(temp.getBiggestWin() , winAmount) );
                if(showCardsAtEnd) temp.setBestHand( Card.compareHand(temp.getBestHand(), power) );
                temp.setBoardCoin(temp.getBoardCoin() + winAmount);
            }
            else{
                temp.setWinStreak(0);
                temp.setCoinLost(temp.getCoinLost() + temp.getTotalCallValue());
            }
        }
        User.loadOwnSelfFromInGamePlayers(user);




//        JSONObject data = jsonObject.getJSONObject("data");
//        JSONArray winnerData = data.getJSONArray("winnerData");
//
//        boolean showCardsAtEnd = data.getBoolean("showCards");
//        long winAmount = data.getLong("winAmount");
//
//        int kk = 0;
//        int winners[] = new int[user.getMaxPlayerCount()];
//        String[] results = new String[winnerData.length()];
//
//        for(int i=0; i<user.getMaxPlayerCount(); i++) winners[i] = 0;
//        for(int i=0; i<winnerData.length(); i++){
//
//            int k = winnerData.getJSONObject(i).getInt("seatPosition");
//            winners[k] = 1;
//            results[i] = winnerData.getJSONObject(i).getString("resultString");
//        }
//
//        for(int i=0; i<user.getMaxPlayerCount(); i++){
//
//            if(user.getInGamePlayers()[i] == null) continue;
//
//            User temp = user.getInGamePlayers()[i];
//            temp.setRoundsPlayed(temp.getRoundsPlayed() + 1);
//
//            if(winners[i] == 1){
//                temp.setRoundsWon(temp.getRoundsWon() + 1);
//                temp.setWinStreak(temp.getWinStreak() + 1);
//                temp.setCoinWon(temp.getCoinWon() + winAmount);
//                temp.setBiggestWin( Math.max(temp.getBiggestWin() , winAmount) );
//                if(showCardsAtEnd) temp.setBestHand( Card.compareHand(temp.getBestHand(), results[kk]) );
//                temp.setBoardCoin(temp.getBoardCoin() + winAmount);
//
//                kk++;
//            }
//            else{
//                temp.setWinStreak(0);
//                temp.setCoinLost(temp.getCoinLost() + temp.getTotalCallValue());
//            }
//        }
//        User.loadOwnSelfFromInGamePlayers(user);
    }

    private void processResult(JSONObject jsonObject) throws JSONException {

        System.out.println("Winner result -> " + jsonObject);

        loadWinnerData(jsonObject);

        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray eachPlayerData = data.getJSONArray("eachPlayerData");

        boolean showCardsAtEnd = data.getBoolean("showCards");

        for(int i=0; i<5; i++){
            LobbyScreen.winLevel[i].clear();
        }

        for(int i=0; i<eachPlayerData.length(); i++){

            JSONObject jsonObject1 = eachPlayerData.getJSONObject(i);

            int seat = jsonObject1.getInt("seat");
            int id = getId(seat, user.getSeatPosition())-1;
            long winAmount = jsonObject1.getLong("winAmount");
            boolean hasWon = jsonObject1.getBoolean("hasWon");
            long coinBack = jsonObject1.getLong("coinBack");
            user.getInGamePlayers()[seat].setBoardCoin(user.getInGamePlayers()[seat].getBoardCoin()+coinBack);
            String power = jsonObject1.getString("power");

            if( ! hasWon ) continue;

            JSONArray winningData = jsonObject1.getJSONArray("winLevel");
            for(int j=0; j<winningData.length(); j++){

                JSONObject jsonObject2 = winningData.getJSONObject(j);

                long amount = jsonObject2.getLong("amount");
                int level = jsonObject2.getInt("level");
                int resultFoundAtLevel = jsonObject2.getInt("resultFoundAtLevel");

                user.getInGamePlayers()[seat].setBoardCoin(user.getInGamePlayers()[seat].getBoardCoin()+amount);

                WinnerData winnerData = new WinnerData();
                winnerData.setSeat(id);
                winnerData.setWinamount(amount);

                if(showCardsAtEnd){
                    System.out.println( Card.suitMessage(power, resultFoundAtLevel) );
                    winnerData.setResultString(Card.suitMessage(power, resultFoundAtLevel));
                }else{
                    winnerData.setResultString(" ");
                }
                System.out.println("Process Result:::::::::::::::::::::: \n Winner Result::::::::::::::");
                System.out.println(winnerData.getSeat()+" : "+winnerData.getResultString());
                LobbyScreen.winLevel[level-1].add(winnerData);
            }
        }




//        System.out.println("ClinettoServer Processresult start");
//
//        loadWinnerData(jsonObject);
//
//        JSONObject data = jsonObject.getJSONObject("data");
//
//        String show = "\nWinners: ";
//
//        int winnerCount = data.getJSONArray("winnerData").length();
//        long winAmount = data.getLong("winAmount");
//
//
//        if (winnerCount == 0) {
//            LobbyScreen.Messeage = ("Everyone folded, no winners!");
//            LobbyScreen.proceesResult = true;
//
//            for(int i=0; i<5; i++){
//                LobbyScreen.WinnerSeats[i] = false;
//                LobbyScreen.winAmmount[i] = 0;
//                LobbyScreen.winnerSuit[i] = "no";
//            }
//            return;
//        }
//
//        String[] winners = new String[winnerCount];
//        String[] winnerCards = new String[winnerCount];
//
//        for(int i=0; i<5; i++){
//            LobbyScreen.WinnerSeats[i] = false;
//        }
//
//        boolean showCards = data.getBoolean("showCards");
//
//        for (int i = 0; i < winnerCount; i++) {
//
//            JSONObject temp = data.getJSONArray("winnerData").getJSONObject(i);
//
//            winners[i] = temp.getString("username");
//            winnerCards[i] = temp.getString("resultString");
//            int sp = temp.getInt("seatPosition");
//            int id = getId(sp, user.getSeatPosition())-1;
//            LobbyScreen.WinnerSeats[id] = true;
//            LobbyScreen.winAmmount[id] = winAmount;
//            if(showCards) {
//                LobbyScreen.winnerSuit[id] = Card.suitMessage(winnerCards[i], data.getInt("showWinLevel"));
//            }
//            else{
//                LobbyScreen.winnerSuit[id] = "Others Folded";
//            }
//        }
//
//
//        show += "Winning amount each: " + winAmount;
//        show += winnerCount + " winners\n";
//
//        for (int i = 0; i < winnerCount; i++) {
//            show += winners[i];
//            show += "\n";
//
//            if (showCards == true) show += " -> " + Card.suitMessage(winnerCards[i], data.getInt("showWinLevel"));
//        }
//        if (winnerCount > 1) show += "\nTied between " + winnerCount + " players";
//
//
//
//        //LobbyScreen.Messeage = (show);
//        LobbyScreen.proceesResult = true;
//
//        User[] users = user.getInGamePlayers();
//        if(users!=null) {
//            for (int i = 0; i < users.length; i++) {
//                if (users[i]!=null){
//                    users[i].setTotalCallValue(0);
//                }
//            }
//        }
//        user.setTotalCallValue(0);
//
//        System.out.println("ClinettoServer Processresult End");

    }

    //======================================================================================
    //
    //======================================================================================







    //======================================================================================
    //
    //              WAIT FOR PLAYERS
    //              OR
    //              WAIT FOR PLAYERS TO BUY COINS
    //
    //======================================================================================

    private void waitForPlayersToBuy() {

        disableGameButtons();

        user.setGameRunning(false);

        String show = "Waiting for players to buy coins" ;
        LobbyScreen.Messeage = (show);
    }

    public static void sendAddBoardCoinInGameRequest(long value) throws JSONException {

        if(value > user.getCurrentCoin()) {
            return ;
        }

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();

        tempJson.put("gameId", gameThreadId);
        tempJson.put("gameCode", gameThreadCode);
        tempJson.put("requestType", "AddBoardCoin");
        tempJson.put("amount", value);

        send.put("gameData", tempJson);

        sendMessage(send.toString());
    }

    private void addBoardCoinInGameResponse(JSONObject jsonObject) throws JSONException {

        JSONObject gameData = jsonObject.getJSONObject("gameData");
        boolean success = gameData.getBoolean("success");
        long amount = gameData.getLong("amount");

        if(success){
            user.setCurrentCoin(user.getCurrentCoin() - amount);
            user.setBoardCoin(user.getBoardCoin() + amount);

            tryStartCurrentGame();
            LobbyScreen.addBoardCoinsuccess = true;
        }
    }


    private void tryStartCurrentGame() throws JSONException {

        if(user.isInGame() == false) return ;
        else if(user.isGameRunning() == true) return ;
        else sendTryStartCurrentGameRequest();
    }

    private void sendTryStartCurrentGameRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();

        tempJson.put("gameId", gameThreadId);
        tempJson.put("gameCode", gameThreadCode);
        tempJson.put("requestType", "StartNewRound");

        send.put("gameData", tempJson);

        sendMessage(send.toString());
    }






    //===================================================================================
    //
    //              GUI ON CLICK BUTTONS
    //
    //===================================================================================

    //=====================================================================================
    //      REQUESTS FRIENDLIST TO SERVER
    //
    //      SAVE FRIENDLIST FROM SERVER WHEN LOGGING IN
    //      THEN IF ANY CHANGE MADE, SEND DATA TO SERVER
    //
    //=====================================================================================


    //====================================================================================
    //      REQUEST TO JOIN A GAME
    //
    //
    //===================================================================================
    //===========================================================================================
    //
    //      GUI SHITS, IGNORE
    //
    //===========================================================================================


    public static void logUserClick() throws JSONException {

        msg = "";
        if (user != null) {
            msg += "Logout";
            sendLogoutRequest();
            curCommand = "Logout";
        } else {
            msg += "Login ";
            inpCount = 2;
            curCommand = "Login";

            System.out.println("Enter username and then password");
        }
    }

    public static void infoUserClick() {
        curCommand = "UserInfo";
        System.out.println(user.toString());
    }

    public static void joinClick() throws JSONException {

//        if (isJoin) {
//            requestJoin(-1, -1, boardType[0], minEntryValue[0], minCallValue[0], -1, -1, 100000);
//            curCommand = "Join";
//        } else if (isAbort) {
//            requestAbort();
//            curCommand = "Abort";
//        }
    }

    public static void inviteButtonClick() throws JSONException {

        requestUpdateOwnInGame();

        //addFreeCoinRequest();

        /*
        ArrayList temp = new ArrayList<String>();

        temp.add("b");
        temp.add("c");
        temp.add("d");

        createWaitingRoom(temp, "board1", 100000, 100000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startGameFromWaitingRoom();
            }
        }, 15000);
        */
    }

    public static void friendsButtonClicked() throws JSONException {
        curCommand = "Friends";
        requestFriendsList();
    }

    public static void closeButtonClicked() {
        String ret = "";
        ret = "Close";
        closeEverything();
    }

    private void buyCoinClick() {
        curCommand = "Buy";
    }

    private void setUpGui() {
        LobbyScreen.isCheckAvailable = false;
        LobbyScreen.isRaiseAvailable = false;
        LobbyScreen.isFoldAvailable = false;
        LobbyScreen.isCallAvailable = false;
        LobbyScreen.isAllinAvailable = false;
        isExit = false;
        isJoin = false;
    }

    //=====================================================================================


    //=====================================================================================
    //
    //              JSON CODES
    //
    //
    //=====================================================================================

    private static void requestFriendsList() throws JSONException {

        JSONObject send = initiateJson();

        send.put("username", user.getUsername());
        send.put("requestType", "FriendsListRequest");

        sendMessage(send.toString());
    }

    private void showFriends(JSONArray friends) throws JSONException {

        String bleh = "Friends: \n";

        for (int i = 0; i < friends.length(); i++) {

            JSONObject tempJson = friends.getJSONObject(i);
            String tempString = "";

            tempString += tempJson.getString("username") + " ";
            tempString += tempJson.getInt("coinWon") + " ";
            tempString += tempJson.getInt("currentCoin") + " ";
            tempString += tempJson.getInt("level") + " ";


            bleh += tempString + "\n";
        }

        System.out.println(bleh);
    }

    //=====================================================================================
    //
    //              JSON CODES
    //              FOR GAME THREAD
    //
    //=====================================================================================







    private void gameStartIfInAGame() throws JSONException {

        //KONO GAME E NAI
        if (gameThreadId == -1) return;
        if (gameRunning == true) return;

        sendTryStartCurrentGameRequest();
    }






    private void roundInitialize() {

        user.getPlayerCards().clear();
        user.getBoardCards().clear();
    }


    private void showEndCycle(String msg) {

        LobbyScreen.Messeage = msg;
    }



    //====================================================================================
    //
    //      GAME BUTTON ON CLICK FUNCTIONS
    //
    //      SENDS GAME TURN REQUESTS TO SERVER
    //
    //      WHICH BUTTONS WILL BE ENABLED IS SENT BY THE SERVER
    //
    //      CHECKED IF VALID INPUTS WERE GIVEN IN RAISE BUTTON CALL
    //      OTHERWISE ALL BASIC
    //
    //====================================================================================

    public static void clickedCallButton() throws JSONException {
        String send = "";
        call = "Call";
        //user.setCurrentCoin(user.getCurrentCoin() - minCallValue);

        sendGameThreadCallRequest();
    }

    public static void clickedFoldButton() throws JSONException {
        String send = "";
        call = "Fold";
        //user.setCurrentCoin(user.getCurrentCoin() - foldCost);

        sendGameThreadFoldRequest();
    }

    public static void clickedRaiseButton(int y) throws JSONException {
        call = "Raise";
        sendGameThreadRaiseRequest(y);
    }

    public static void clickedCheckButton() throws JSONException {
        call = "Check";
        sendGameThreadCheckRequest();
    }

    public static void clickedAllInButton() throws JSONException {
        call = "AllIn";
        long v = user.getCurrentCoin();

        //user.setCurrentCoin(0);
        sendGameThreadAllInRequest();
    }

    public static void clickedExitButton() throws JSONException {

        requestExit();
    }








    //=================================================================================
    //
    //          WAITING ROOM
    //
    //=================================================================================



    //=================================================================================
    //
    //          CREATE WAITING ROOM
    //
    //=================================================================================

    public static void createWaitingRoom(String boardType, long minEntryValue, long minCallValue) throws JSONException {

        // OWNER ER CURRENT COIN minEntryValue
        // ER BESHI HOILEI CREATE KORTE PARBE

        sendCreateWaitingRoomRequest(boardType, minEntryValue, minCallValue);
    }

    private static void sendCreateWaitingRoomRequest(String boardType, long minEntryValue, long minCallValue) throws JSONException {

        JSONObject send = initiateJson();

        send.put("ownerId", user.getId());
        send.put("requestType", "WaitingRoom");

        JSONObject tempJson = new JSONObject();

        tempJson.put("requestType", "Create");

        tempJson.put("boardType", boardType);
        tempJson.put("minEntryValue", minEntryValue);
        tempJson.put("minCallValue", minCallValue);

        send.put("waitingRoomData", tempJson);
        sendMessage(send.toString());
    }

    private void createWaitingRoomResponse(JSONObject jsonObject) throws JSONException {

        String message = jsonObject.getString("data");

    }


    //=================================================================================
    //
    //=================================================================================



    //=================================================================================
    //
    //          JOIN REQUESTS
    //
    //=================================================================================

    private void askBoardCoin(JSONObject jsonObject) throws JSONException {

        JSONObject waitingRoomData = jsonObject.getJSONObject("waitingRoomData");

        long minEntryValue = waitingRoomData.getLong("minEntryValue");
        long minCallValue = waitingRoomData.getLong("minCallValue");

        playwithfriend.popUpblind = minCallValue;
        playwithfriend.popUpbuyin = minEntryValue;

        String show = "Min entry value " + minEntryValue + " min call value " + minCallValue + "\n";
        show += "Enter entry amount: " ;


//        addTextInGui(show);
    }

    public static void sendJoinAmount(long value) throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "WaitingRoom");

        JSONObject tempJson = new JSONObject();

        tempJson.put("requestType", "JoinAmount");
        tempJson.put("amount", value);

        send.put("waitingRoomData", tempJson);
        sendMessage(send.toString());
    }


    private void initializeWaitingRoomData(JSONObject jsonObject) throws JSONException {

        JSONObject waitingRoomData = jsonObject.getJSONObject("waitingRoomData");

        int id = waitingRoomData.getInt("gameId");
        int code = waitingRoomData.getInt("gameCode");
        String boardType = waitingRoomData.getString("boardType");
        long minEntryValue = waitingRoomData.getLong("minEntryValue");
        long minCallValue = waitingRoomData.getLong("minCallValue");
        long boardCoin = waitingRoomData.getLong("boardCoin");
        int seatPosition = waitingRoomData.getInt("seatPosition");
        int owner_id = waitingRoomData.getInt("ownerId");
        int maxPlayerCount = waitingRoomData.getInt("maxPlayerCount");

        user.initializeInvitationData(id, code, maxPlayerCount, boardType, minEntryValue, minCallValue, owner_id, seatPosition, boardCoin);
    }



    public static void joinWaitingRoomByCode(int code) throws JSONException {

        sendJoinWaitingRoomByCodeRequest(code);
    }

    private static void sendJoinWaitingRoomByCodeRequest(int code) throws JSONException {

        JSONObject send = initiateJson();

        send.put("requestType", "WaitingRoom");

        JSONObject tempJson = new JSONObject();

        tempJson.put("requestType", "AskJoinWaitingRoomByCode");
        tempJson.put("gameCode", code);

        send.put("waitingRoomData", tempJson);
        sendMessage(send.toString());
    }

    private void askJoinWaitingRoomByCodeResponse(JSONObject temp) throws JSONException {

//        JSONObject waitingRoomData = temp.getJSONObject("waitingRoomData");
//        String msg = waitingRoomData.getString("message");
//
//        String[] x = msg.split(" ");
//        int xx = x.length;
//        if(x[xx-1].equalsIgnoreCase("found.")){
//            JoinFriends.invalidCode = true;
//        }
//        else JoinFriends.invalidCode = false;
//
//        JoinFriends.responseCode = true;
//        //addTextInGui(msg);

        JSONObject waitingRoomData = temp.getJSONObject("waitingRoomData");
        String msg = waitingRoomData.getString("message");
        int code = waitingRoomData.getInt("gameCode");
        boolean success = waitingRoomData.getBoolean("success");

       // addTextInGui(msg);

        waitingRoomJoinDone(success, code);

    }

    //=================================================================================
    //
    //          JOIN GAME BY CODE
    //
    //=================================================================================

    private void joinByCode(int code) throws JSONException {

        joinWaitingRoomByCode(code);
    }

    private void waitingRoomJoinDone(boolean success, int code) throws JSONException {

        if(success == false){
            joinGameThreadByCode(code);
            JoinFriends.joinGameThread = true;

        }
        else
        {
            JoinFriends.joinWaitingRoom = true;
            JoinFriends.responseCode = true;
            JoinFriends.invalidCode = false;
        }
    }

    //=================================================================================
    //
    //=================================================================================



    public static void cancelJoiningRequest() throws JSONException {

        sendCancelJoinRequest();
    }

    private static void sendCancelJoinRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("requestType", "WaitingRoom");

        JSONObject tempJson = new JSONObject();

        tempJson.put("requestType", "CancelJoinRequest");

        send.put("waitingRoomData", tempJson);
        sendMessage(send.toString());

    }

    //=================================================================================
    //
    //=================================================================================




    //=================================================================================
    //
    //          ADD BOARD COIN IN WAITING ROOM
    //
    //=================================================================================

    private void sendEditBoardCoinInWaitingRoomRequest(long value) throws JSONException {

        if(value > user.getCurrentCoin() + user.getBoardCoin()) {
            //addTextInGui("Invalid Request");
            return ;
        }

        JSONObject send = initiateJson();

        send.put("requestType", "WaitingRoom");

        JSONObject tempJson = new JSONObject();

        tempJson.put("requestType", "EditBoardCoin");
        tempJson.put("amount", value);

        send.put("waitingRoomData", tempJson);

        sendMessage(send.toString());
    }

    private void editBoardCoinInWaitingRoomResponse(JSONObject jsonObject) throws JSONException {

        JSONObject waitingRoomData = jsonObject.getJSONObject("waitingRoomData");
        boolean success = waitingRoomData.getBoolean("success");
        long boardCoin = waitingRoomData.getLong("boardCoin");
        long currentCoin = waitingRoomData.getLong("currentCoin");

        if(success){
            user.setCurrentCoin(currentCoin);
            user.setBoardCoin(boardCoin);
        }
    }

    //=================================================================================
    //
    //=================================================================================






    //=================================================================================
    //
    //          ALL PLAYERS DATA
    //
    //=================================================================================

    private void showPlayersData(JSONObject jsonObject) throws JSONException {

        JSONArray array = jsonObject.getJSONArray("data");
        System.out.println("in user " + user.getUsername() + " -> " + array);
    }

    //=================================================================================
    //
    //=================================================================================










    //=================================================================================
    //
    //          REMOVE FROM WAITING ROOM
    //
    //=================================================================================

    public static void removeMeFromWaitingRoom() throws JSONException {

        int loc = user.getSeatPosition();
        user.deInitializeInvitationData();
        sendRemoveMeFromWaitingRoom(loc);
    }

    private static void sendRemoveMeFromWaitingRoom(int loc) throws JSONException {

        JSONObject send = initiateJson();

        send.put("requestType", "WaitingRoom");

        JSONObject tempJson = new JSONObject();
        tempJson.put("requestType", "RemoveMeFromWaitingRoom");
        tempJson.put("seatPosition", loc);

        send.put("waitingRoomData", tempJson);
        sendMessage(send.toString());

    }

    //  OWNER ONLY
    public static void removeFromWaitingRoom(int[] seat) throws JSONException {

        JSONObject send = initiateJson();

        send.put("owner", user.getUsername());
        send.put("requestType", "WaitingRoom");

        JSONObject tempJson = new JSONObject();
        tempJson.put("requestType", "RemoveFromWaitingRoom");

        JSONArray array = new JSONArray();
        for(int x : seat) array.put(x);

        send.put("data", array);

        send.put("waitingRoomData", tempJson);
        sendMessage(send.toString());
    }


    private void removedFromWaitingRoom(JSONObject jsonObject) throws JSONException {

        JSONObject waitingRoomData = jsonObject.getJSONObject("waitingRoomData");
        String message = waitingRoomData.getString("message");

        if(message != null) {
            JoinFriends.removeMessage = message;
            JoinFriends.isRemoved = true;
            HostFriends.isRemoved = true;
            //addTextInGui(message);
        }

        user.deInitializeInvitationData();
    }

    //=================================================================================
    //
    //=================================================================================













    //=================================================================================
    //
    //          START GAME FROM WAITING ROOM
    //
    //=================================================================================


    //=================================================================================
    //
    //=================================================================================









    //=================================================================================
    //
    //=================================================================================













    //=================================================================================
    //
    //          JOIN GAME BY CODE
    //
    //=================================================================================

    public static void sendStartGameRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("requestType", "WaitingRoom");

        JSONObject tempJson = new JSONObject();
        tempJson.put("requestType", "StartGame");

        send.put("waitingRoomData", tempJson);
        sendMessage(send.toString());
    }

    private void showStartGameResponse(JSONObject temp) throws JSONException {

        JSONObject waitingRoomData = temp.getJSONObject("waitingRoomData");
        String msg = waitingRoomData.getString("message");

       // addTextInGui(msg);
    }

    private void loadPlayersDataWaitingRoom(JSONObject temp) throws JSONException {

        JSONArray data = temp.getJSONArray("data");

        for(int i=0; i<user.getInWaitingRoomPlayers().length; i++){
            user.getInWaitingRoomPlayers()[i] = null;
        }

        for (int i = 0; i < data.length(); i++) {

            User tempUser = User.JSONToUserInGame((JSONObject) data.get(i));
            user.getInWaitingRoomPlayers()[tempUser.getSeatPosition()] = tempUser;
            System.out.println(tempUser.getUsername());
        }
    }

    //=================================================================================
    //
    //=================================================================================




    //=====================================================================================
    //
    //
    //=====================================================================================


    private static int getId(int id, int myId){
        if(id==myId) return 1;
        else if(id<myId){
            return (6-(myId-id));
        }
        else{
            return (1+(id-myId));
        }
    }




    //=================================================================================
    //
    //          JOIN GAME BY CODE
    //
    //=================================================================================

    private void joinGameThreadByCode(int code) throws JSONException {

        sendJoinGameThreadByCodeRequest(code);
    }

    private void sendJoinGameThreadByCodeRequest(int code) throws JSONException {

        JSONObject send = initiateJson();

        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();

        tempJson.put("requestType", "AskJoinGameThreadByCode");
        tempJson.put("gameCode", code);

        send.put("gameData", tempJson);
        sendMessage(send.toString());
    }


    private void joinGameThreadByCodeResponse(JSONObject temp) throws JSONException {

        JSONObject gameData = temp.getJSONObject("gameData");
        String msg = gameData.getString("message");
        boolean success = gameData.getBoolean("success");

        if(success == false){
            user.deInitializeGameData();
            JoinFriends.invalidCode = true;
            JoinFriends.responseCode = true;
            System.out.println("joinGameThreadByCodeResponse");
        }
        else{
            JoinFriends.invalidCode = false;
            System.out.println(" not joinGameThreadByCodeResponse");
        }

        //addTextInGui(msg);
    }


    private void cancelJoiningGameRequest() throws JSONException {

        user.deInitializeGameData();
        sendCancelJoiningGameRequest();
    }

    private void sendCancelJoiningGameRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();

        tempJson.put("requestType", "CancelJoinRequest");

        send.put("gameData", tempJson);
        sendMessage(send.toString());

    }




    private void askBoardCoinForGame(JSONObject jsonObject) throws JSONException {

        JSONObject gameData = jsonObject.getJSONObject("gameData");

        int gameId = gameData.getInt("gameId");
        int gameCode = gameData.getInt("gameCode");
        int ownerId = gameData.getInt("ownerId");
        long minEntryValue = gameData.getLong("minEntryValue");
        long minCallValue = gameData.getLong("minCallValue");
        String boardType = gameData.getString("boardType");

        user.initializeGameData(gameId, gameCode, boardType, minEntryValue, minCallValue, ownerId, -1, 0 );

        String show = "Min entry value " + minEntryValue + " min call value " + minCallValue + " boardType " + boardType + "\n";
        show += "Enter entry amount: " ;

        //addTextInGui(show);
    }



    private void joinAmountForGame(long value) throws JSONException {

        user.setBoardCoin(value);
        sendJoinAmountForGame(value);
    }

    private void sendJoinAmountForGame(long value) throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "GameThread");

        JSONObject tempJson = new JSONObject();

        tempJson.put("gameCode", user.getGameCode());
        tempJson.put("requestType", "JoinAmount");
        tempJson.put("amount", value);

        send.put("gameData", tempJson);
        sendMessage(send.toString());
    }

    //=================================================================================
    //
    //=================================================================================


    private void deductBlindCoins(JSONObject jsonObject) throws JSONException {

        JSONArray array = jsonObject.getJSONArray("data");
        String msg = jsonObject.getString("message");

        long roundCoin = jsonObject.getLong("roundCoins");
        LobbyScreen.deductRoundCoin = roundCoin;
        user.setRoundCoins(roundCoin);

        JSONObject temp = array.getJSONObject(0);
        int smallBlindSeat = temp.getInt("seatPosition");
        long smallBlindDeduct = temp.getLong("amount");
        LobbyScreen.smallBlindSeat = getId(smallBlindSeat, user.getSeatPosition())-1;
        LobbyScreen.smallBlindDeduct = smallBlindDeduct;

        temp = array.getJSONObject(1);
        int bigBlindSeat = temp.getInt("seatPosition");
        long bigBlindDeduct = temp.getLong("amount");
        LobbyScreen.bigBlindDeduct = bigBlindDeduct;
        LobbyScreen.bigBlindSeat = getId(bigBlindSeat, user.getSeatPosition())-1;

        System.out.println("roundCoin: " + roundCoin + " small blind: " + smallBlindSeat + " " + smallBlindDeduct + " big blind: " + bigBlindSeat + " " + bigBlindDeduct + " " + msg);
    }





    private void forceLogout(JSONObject jsonObject) throws JSONException {

        System.out.println("ekhon force logout korbe!");
        System.out.println("Reason "  + jsonObject.getString("message"));

    }

    private void sendConnectionCheckResponse() throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "CheckConnection");
        send.put("isConnected", true);
        sendMessage(send.toString());
    }

    public static void loadBoardData(JSONObject data) throws JSONException {

        minCallValue = null;
        boardType = null;
        minEntryValue = null;
        maxEntryValue = null;
        hiddenStatus = null;
        mcr = null;

        JSONArray array;
        JSONObject jsonObject = data.getJSONObject("data");

        int boardTypeCount = jsonObject.getInt("boardTypeCount");

        array = jsonObject.getJSONArray("minCallValue");
        minCallValue = new long[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) minCallValue[i] = array.getLong(i);

        array = jsonObject.getJSONArray("hiddenStatus");
        hiddenStatus = new boolean[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) hiddenStatus[i] = array.getBoolean(i);

        array = jsonObject.getJSONArray("boardType");
        boardType = new String[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) boardType[i] = array.getString(i);

        array = jsonObject.getJSONArray("minEntryValue");
        minEntryValue = new long[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) minEntryValue[i] = array.getLong(i);

        array = jsonObject.getJSONArray("maxEntryValue");
        maxEntryValue = new long[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) maxEntryValue[i] = array.getLong(i);

        array = jsonObject.getJSONArray("mcr");
        mcr = new long[boardTypeCount];
        for(int i=0; i<boardTypeCount; i++) mcr[i] = array.getLong(i);
    }

    public static void loadShopData(JSONObject data) throws JSONException {

        coinPriceOnBuy = null;
        coinAmountOnBuy = null;
        transactionNumbers = null;

        JSONObject jsonObject = data.getJSONObject("data");
        JSONArray array;

        coinPricePerCrore = jsonObject.getDouble("coinPricePerCrore");

        minCoinWithdraw = jsonObject.getInt("minCoinWithdraw");

        array = jsonObject.getJSONArray("coinAmountOnBuy");
        coinAmountOnBuy = new long[array.length()];
        for(int i=0; i<array.length(); i++) coinAmountOnBuy[i] = array.getLong(i);

        array = jsonObject.getJSONArray("coinPriceOnBuy");
        coinPriceOnBuy = new double[array.length()];
        for(int i=0; i<array.length(); i++) coinPriceOnBuy[i] = array.getDouble(i);

        array = jsonObject.getJSONArray("bkashLinks");
        bkashLinks = new String[array.length()];
        for(int i=0; i<array.length(); i++) bkashLinks[i] = array.getString(i);

        array = data.getJSONArray("Numbers");
        transactionNumbers = new ArrayList<TransactionNumber>();
        for(int i=0; i<array.length(); i++) transactionNumbers.add(new TransactionNumber(array.getJSONObject(i)));

        buyrequestLeft = data.getInt("requestLeft");

    }

    private void transactionRequestResponse(JSONObject jsonObject) throws JSONException {

        String msg = "";

        boolean success = jsonObject.getBoolean("success");
        long currentCoin = jsonObject.getLong("currentCoin");
        int reqLeft = jsonObject.getInt("requestLeft");
        CoinPurchase.reqLeft = reqLeft;
        buyrequestLeft = reqLeft;

        user.setCurrentCoin(currentCoin);


        msg += jsonObject.getString("message");
        //msg += "\n";
        //msg += "Request left: " + reqLeft + "\n";

        CashOut.isResponse = true;
        CashOut.success = success;
        CashIn.isResponse = true;
        CashIn.success = true;

    }

    public static void getNotifications() throws JSONException {

        JSONObject send = initiateJson();

        send.put("id", user.getId());
        send.put("username", user.getUsername());
        send.put("requestType", "NotificationRequest");

        sendMessage(send.toString());
    }

    private void showNotifications(JSONObject jsonObject) throws JSONException {

        JSONArray notifications = jsonObject.getJSONArray("data");
        for(int i=0; i<notifications.length(); i++){

            JSONObject jsonObject1 = notifications.getJSONObject(i);

            long coinAdded = jsonObject1.getLong("coinAdded");
            user.setCurrentCoin(user.getCurrentCoin() + coinAdded);

            System.out.println("Notification " + i + " -> ");
            System.out.println(jsonObject1);
        }
        System.out.println();
        HomeScreen.notifications = notifications;
        HomeScreen.isNotificationResponse = true;
        BkashActivity.isPaymentNotification = true;
    }

    public static void withdrawCoinRequest(long coinAmount, String method, String receiver) throws JSONException {

        JSONObject send = initiateJson();

        send.put("id", user.getId());
        send.put("username", user.getUsername());
        send.put("requestType", "Transaction");

        JSONObject tempJson = new JSONObject();

        tempJson.put("request", "WithdrawCoin");
        tempJson.put("method", method);
        tempJson.put("transactionId", "");
        tempJson.put("coinAmount", coinAmount);
        tempJson.put("requestTime", Calendar.getInstance().getTime());
        tempJson.put("sender", "");
        tempJson.put("receiver", receiver);
        send.put("data", tempJson);

        sendMessage(send.toString());
    }


    public static void getAllTransactionsRequest() throws JSONException {

        JSONObject send = initiateJson();

        send.put("id", user.getId());
        send.put("username", user.getUsername());
        send.put("requestType", "Transaction");

        JSONObject tempJson = new JSONObject();

        tempJson.put("request", "ShowTransactions");
        send.put("data", tempJson);

        sendMessage(send.toString());
    }

    private void allTransactionsRequestResponse(JSONObject jsonObject) throws JSONException {

        requestList.clear();

        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray transactions = data.getJSONArray("transactions");
        JSONArray pendingTransactions = data.getJSONArray("pendingTransactions");
        JSONArray pendingRefunds = data.getJSONArray("pendingRefunds");
        JSONArray refunds = data.getJSONArray("refunds");

        for(int i=0; i<pendingTransactions.length(); i++){

            JSONObject j = pendingTransactions.getJSONObject(i);

            int id = j.getInt("id");
            int account_id = j.getInt("account_id");
            String type = j.getString("type");
            String method = j.getString("method");
            String transactionId = j.getString("transactionId");
            long coinAmount = j.getLong("coinAmount");
            double price = j.getDouble("price");
            String receiver = j.getString("receiver");
            String sender = j.getString("sender");
            Date requestTime = User.stringToDate(j.getString("requestTime"));
            String reqdate = j.getString("requestTime");

            RequestObject requestObject = new RequestObject();
            requestObject.setBType("Pending Transaction");
            requestObject.setType(type);
            requestObject.setMethod(method);
            requestObject.setReqdate(reqdate);
            requestObject.setResdate(" ");
            requestObject.setNotification(" Chips: "+BoardChoosing.modifyChipsString(coinAmount)+"     Price: "+price+"\n TransactionId: "+transactionId+"    Sender: "+sender);
            requestList.add(requestObject);
        }

        for(int i=0; i<transactions.length(); i++){

            JSONObject j = transactions.getJSONObject(i);

            int id = j.getInt("id");
            int account_id = j.getInt("account_id");
            String type = j.getString("type");
            String method = j.getString("method");
            String transactionId = j.getString("transactionId");
            long coinAmount = j.getLong("coinAmount");
            double price = j.getDouble("price");
            String receiver = j.getString("receiver");
            String sender = j.getString("sender");
            Date requestTime = User.stringToDate(j.getString("requestTime"));
            Date approvalTime = User.stringToDate(j.getString("approvalTime"));
            String reqdate = j.getString("requestTime");
            String resdate = j.getString("approvalTime");

            RequestObject requestObject = new RequestObject();
            requestObject.setBType("Transaction");
            requestObject.setType(type);
            requestObject.setMethod(method);
            requestObject.setReqdate(reqdate);
            requestObject.setResdate(resdate);
            requestObject.setNotification(" Chips: "+BoardChoosing.modifyChipsString(coinAmount)+"     Price: "+price+"\n TransactionId: "+transactionId+"    Sender: "+sender);
            requestList.add(requestObject);
        }

        for(int i=0; i<pendingRefunds.length(); i++){

            JSONObject j = pendingRefunds.getJSONObject(i);

            int id = j.getInt("id");
            int account_id = j.getInt("account_id");
            String type = j.getString("type");
            String method = j.getString("method");
            String transactionId = j.getString("transactionId");
            long coinAmount = j.getLong("coinAmount");
            double refundAmount = j.getDouble("refundAmount");
            String receiver = j.getString("receiver");
            String sender = j.getString("sender");
            Date requestTime = User.stringToDate(j.getString("requestTime"));
            Date refundRequestTime = User.stringToDate(j.getString("refundRequestTime"));
            String reqdate = j.getString("requestTime");
            String resdate = j.getString("refundRequestTime");
            String reason = j.getString("reason");

            RequestObject requestObject = new RequestObject();
            requestObject.setBType("Pending Refund");
            requestObject.setType(type);
            requestObject.setMethod(method);
            requestObject.setReqdate(reqdate);
            requestObject.setResdate(resdate);
            requestObject.setNotification(" Chips: "+BoardChoosing.modifyChipsString(coinAmount)+"    Pending Refund: "+refundAmount+"\n TransactionId: "+transactionId+"    Sender: "+sender+"\n Reason: "+reason);
            requestList.add(requestObject);
        }

        for(int i=0; i<refunds.length(); i++){

            JSONObject j = refunds.getJSONObject(i);

            int id = j.getInt("id");
            int account_id = j.getInt("account_id");
            String type = j.getString("type");
            String method = j.getString("method");
            String prevTransactionId = j.getString("prevTransactionId");
            String refundTransactionId = j.getString("refundTransactionId");
            long coinAmount = j.getLong("coinAmount");
            double refundAmount = j.getDouble("refundAmount");
            String receiver = j.getString("receiver");
            String sender = j.getString("sender");
            Date requestTime = User.stringToDate(j.getString("requestTime"));
            Date refundRequestTime = User.stringToDate(j.getString("refundRequestTime"));
            Date refundTime = User.stringToDate(j.getString("refundTime"));
            String reason = j.getString("reason");

            String reqdate = j.getString("requestTime");
            String resdate = j.getString("refundRequestTime");

            RequestObject requestObject = new RequestObject();
            requestObject.setBType("Refund");
            requestObject.setType(type);
            requestObject.setMethod(method);
            requestObject.setReqdate(reqdate);
            requestObject.setResdate(resdate);
            requestObject.setNotification(" Chips: "+BoardChoosing.modifyChipsString(coinAmount)+"    Pending Refund: "+refundAmount+"\n Prev. TransactionId: "+prevTransactionId +"/Refund TransactionId: "+refundTransactionId+"    Sender: "+sender+"\n Reason: "+reason);
            requestList.add(requestObject);
        }

        Notification.isRequestResponse = true;
    }

    public static void requestToken() throws JSONException {

        JSONObject send = initiateJson();

        send.put("id", user.getId());
        send.put("username", user.getUsername());
        send.put("requestType", "TokenRequest");

        sendMessage(send.toString());
    }

    private void showToken(JSONObject jsonObject) throws JSONException {

        String token = jsonObject.getString("token");
        String link = "http://66.42.55.46:1112/request?id=" + user.getId() + "&token=" + token ;

        //boolean successBuy = jsonObject.getBoolean("success");

        //HomeScreen.isBuyButtonSuccess = successBuy;
    }


    //version

    public static void requestVersion() throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "Version");
        sendMessage(send.toString());
    }

    private void versionResponse(JSONObject jsonObject) throws JSONException {

        MainActivity.version = jsonObject.getInt("version");
        System.out.println("Version: "+MainActivity.version);
    }

    //Avatar

    public static void getCurrentImage() throws JSONException {

        JSONObject send = initiateJson();
        send.put("requestType", "CurrentImage");
        sendMessage(send.toString());
    }

    public static void requestImageChange(String link) throws JSONException {

        user.setImageLink(link);

        JSONObject send = initiateJson();
        send.put("requestType", "ChangeImage");
        send.put("imageLink", link);
        sendMessage(send.toString());
    }

    private void currentImageResponse(JSONObject jsonObject) throws JSONException {

        String link = jsonObject.getString("imageLink");
        user.setImageLink(link);

    }


}
