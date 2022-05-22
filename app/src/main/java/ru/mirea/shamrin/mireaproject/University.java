package ru.mirea.shamrin.mireaproject;

import androidx.annotation.NonNull;

public class University {
    String name;
    String date;
    String address;

    University(String name, String date, String address){
        this.name = name;
        this.date = date;
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return  name + " " + date + " " + address;
    }
}
