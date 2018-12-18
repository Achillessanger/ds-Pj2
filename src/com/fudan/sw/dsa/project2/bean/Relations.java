package com.fudan.sw.dsa.project2.bean;

import java.util.ArrayList;

public class Relations {
    private int index;
    private int d;
    private ArrayList<Relations> reArrList = new ArrayList<>();
    public Relations(int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public ArrayList<Relations> getReArrList() {
        return reArrList;
    }
}
