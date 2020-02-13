package com.example.android.terremotos;

public class Terremoto {

    private double mMagnitude;
    private String mLocation;
    private long mTime;
    private String mUrl;

    //Construtor

    public Terremoto (double magnitude, String location, long date, String url){

        mMagnitude = magnitude;
        mLocation = location;
        mTime = date;
        mUrl = url;


    }

    public double getMagnitude(){

        return mMagnitude;
    }

    public String getLocation(){

        return mLocation;
    }

    public long getTime(){

        return mTime;
    }

    public String getUrl(){

        return mUrl;
    }

}
