package com.pokerbd.poker;

public class WinnerData {
    int seat;
    long winamount;
    String resultString;

    public WinnerData(int seat, long winamount, String resultString) {
        this.seat = seat;
        this.winamount = winamount;
        this.resultString = resultString;
    }

    public WinnerData() {
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public long getWinamount() {
        return winamount;
    }

    public void setWinamount(long winamount) {
        this.winamount = winamount;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }
}
