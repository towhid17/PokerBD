package com.pokerbd.poker;

public class Model {

    private int image;
    private String table_name;
    private String blind;
    private String buyin;

    public Model(int image,String table_name, String blind, String buyin) {
        this.image = image;
        this.table_name = table_name;
        this.blind = blind;
        this.buyin = buyin;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String get_tableName() {
        return table_name;
    }

    public void set_tableName(String table_name) {
        this.table_name = table_name;
    }


    public String getBlind() {
        return blind;
    }

    public void setBlind(String blind) {
        this.blind = blind;
    }

    public String getBuyin() {
        return buyin;
    }

    public void setBuyin(String buyin) {
        this.buyin = buyin;
    }
}
