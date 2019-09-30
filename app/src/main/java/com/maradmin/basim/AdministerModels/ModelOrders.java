package com.maradmin.basim.AdministerModels;

public class ModelOrders {
    private String key;
    private String name;
    private String mobile1;
    private String mobile2;
    private String adress;
    private String notes;
    private String total;
    private String read_state;


    public ModelOrders() {
    }


    public ModelOrders(String key, String name, String mobile1, String mobile2, String adress, String notes, String total, String read_state) {
        this.key = key;
        this.name = name;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.adress = adress;
        this.notes = notes;
        this.total = total;
        this.read_state = read_state;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRead_state() {
        return read_state;
    }

    public void setRead_state(String read_state) {
        this.read_state = read_state;
    }
}