package com.pokerbd.poker;

public class RequestObject {
    String notification;
    String Method;
    String Type;
    String BType;
    String reqdate;
    String resdate;

    public RequestObject()
    {
        this.notification = "";
        Method = "";
        Type = "";
        this.BType = "";
        this.reqdate = "";
        this.resdate = "";
    }
    public RequestObject(String notification, String method, String type, String BType, String reqdate, String resdate) {
        this.notification = notification;
        Method = method;
        Type = type;
        this.BType = BType;
        this.reqdate = reqdate;
        this.resdate = resdate;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getBType() {
        return BType;
    }

    public void setBType(String BType) {
        this.BType = BType;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getResdate() {
        return resdate;
    }

    public void setResdate(String resdate) {
        this.resdate = resdate;
    }
}
