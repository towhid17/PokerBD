package com.pokerbd.poker;

public class PlayerDetails {
    String username;
    int seatPosition;
    long totalCoin;
    long boardPlayerCoin;
    long playerCallValue;
    long presentCallValue;
    String playerCall;
    String card1;
    String card2;
    String imgLink;
    String loginMethod;
    int id;

    public PlayerDetails(String username, int seatPosition, long totalCoin, long boardPlayerCoin, long playerCallValue, String playerCall) {
        this.username = username;
        this.seatPosition = seatPosition;
        this.totalCoin = totalCoin;
        this.boardPlayerCoin = boardPlayerCoin;
        this.playerCallValue = playerCallValue;
        this.playerCall = playerCall;
    }

    public void setEmpty(){
        this.username = "Empty";
        this.seatPosition = 0;
        this.totalCoin = 0;
        this.boardPlayerCoin = 0;
        this.playerCallValue = 0;
        this.playerCall = "Empty";
        this.imgLink = "Empty";
        this.loginMethod = "Empty";
        this.id = 0;
        this.presentCallValue = 0;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public PlayerDetails(String username, int seatPosition){
        this.username = username;
        this.seatPosition = seatPosition;
        this.totalCoin = 0;
        this.boardPlayerCoin = 0;
        this.playerCallValue = 0;
        this.playerCall = "Empty";
    }

    public PlayerDetails(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSeatPosition() {
        return seatPosition;
    }

    public void setSeatPosition(int seatPosition) {
        this.seatPosition = seatPosition;
    }

    public long getTotalCoin() {
        return totalCoin;
    }

    public void setTotalCoin(long totalCoin) {
        this.totalCoin = totalCoin;
    }

    public long getBoardPlayerCoin() {
        return boardPlayerCoin;
    }

    public void setBoardPlayerCoin(long boardPlayerCoin) {
        this.boardPlayerCoin = boardPlayerCoin;
    }

    public long getPlayerCallValue() {
        return playerCallValue;
    }

    public void setPlayerCallValue(long playerCallValue) {
        this.playerCallValue = playerCallValue;
    }

    public String getPlayerCall() {
        return playerCall;
    }

    public void setPlayerCall(String playerCall) {
        this.playerCall = playerCall;
    }

    public String getCard1() {
        return card1;
    }

    public void setCard1(String card1) {
        this.card1 = card1;
    }

    public String getCard2() {
        return card2;
    }

    public void setCard2(String card2) {
        this.card2 = card2;
    }

    public long getPresentCallValue() {
        return presentCallValue;
    }

    public void setPresentCallValue(long presentCallValue) {
        this.presentCallValue = presentCallValue;
    }
}
