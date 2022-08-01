package com.example.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String name;
    private String details;
    private String size;
    private String date;
    private int id;
    private int qty;
    private int urgent;
    private int bought;

    // default item
    public Item(String name, String details, String size, int qty, boolean urgent) {
        this.name = name;
        this.details = details;
        this.size = size;
        this.date = "";
        this.id = 0;
        this.qty = qty;
        if (urgent) { this.urgent = 1; }
        else { this.urgent = 0; }
        this.bought = 0;
    }

    // bought item
    public Item(String name, String details, String size, int qty, boolean urgent, String date) {
        this.name = name;
        this.details = details;
        this.size = size;
        this.date = date;
        this.id = 0;
        this.qty = qty;
        if (urgent) { this.urgent = 1; }
        else { this.urgent = 0; }
        this.bought = 1;
    }

    protected Item(Parcel in) {
        name = in.readString();
        details = in.readString();
        size = in.readString();
        date = in.readString();
        id = in.readInt();
        qty = in.readInt();
        urgent = in.readInt();
        bought = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        if (urgent) { this.urgent = 1; }
        else { this.urgent = 0; }
    }

    public int getBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        if (bought) { this.bought = 1; }
        else { this.bought = 0; }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(details);
        dest.writeString(size);
        dest.writeString(date);
        dest.writeInt(id);
        dest.writeInt(qty);
        dest.writeInt(urgent);
        dest.writeInt(bought);
    }
}
