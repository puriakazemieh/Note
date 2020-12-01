package com.kazemieh.www.note;

public class DataModel {
    String title;
    String des;
    int id;
    int fav;

    DataModel (String dtitle,String ddes,int did,int dfav){
        this.title=dtitle;
        this.des=ddes;
        this.id=did;
        this.fav=dfav;
    }

    public int getId() {
        return id;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }
}
