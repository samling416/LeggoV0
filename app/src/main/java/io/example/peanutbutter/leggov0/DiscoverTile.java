package io.example.peanutbutter.leggov0;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Samuel on 2/07/2017.
 */

public class DiscoverTile implements Parcelable {
    private String mName;
    private int mPhoto;
    private ArrayList<Integer> mActivities;
    private LatLng mLocation;
    private double mLat;
    private double mLng;
    private int mOrientation; //0 is front.
    private boolean alreadyinitialized = false;

    public DiscoverTile() {
    }

    public DiscoverTile(String name) {
        mName = name;
    }


    public DiscoverTile(String name, int photo) {
        mName = name;
        mPhoto = photo;
    }

    public DiscoverTile(String name, int photo, ArrayList<Integer> activities) {
        mOrientation = 0;
        mName = name;
        mPhoto = photo;
        mActivities = activities;
        mLng = 174.775486;
        mLat = -36.858931;
    }

    public DiscoverTile(String name, int photo, ArrayList<Integer> activities, Double Lat, Double Lng) {
        mOrientation = 0;
        mName = name;
        mPhoto = photo;
        mActivities = activities;
        mLng = Lng;
        mLat = Lat;
    }

    public boolean isAlreadyinitialized() {
        return alreadyinitialized;
    }

    public void setAlreadyinitialized(boolean alreadyinitialized) {
        this.alreadyinitialized = alreadyinitialized;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPhoto() {
        return mPhoto;
    }

    public void setPhoto(int photo) {
        mPhoto = photo;
    }

    public ArrayList<Integer> getActivities() {
        return mActivities;
    }

    public void setActivities(ArrayList<Integer> activities) {
        mActivities = activities;
    }

    public int noOfActivities() {
        return mActivities.size();
    }

    public LatLng getLocation() {
        return mLocation;
    }

    public void setLocation(LatLng location) {
        mLocation = location;
        mLat = location.latitude;
        mLng = location.longitude;
    }

    public double getLat() {
        return mLat;
    }

    public double getLng() {
        return mLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mPhoto);
        dest.writeList(mActivities);
        dest.writeDouble(mLat);
        dest.writeDouble(mLng);
    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private DiscoverTile(Parcel in) {
        mPhoto = in.readInt();
        mName = in.readString();
        mActivities = in.readArrayList(null);
        mLat = in.readDouble();
        mLng = in.readDouble();
    }


    public static final Parcelable.Creator<DiscoverTile> CREATOR = new Parcelable.Creator<DiscoverTile>() {
        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public DiscoverTile createFromParcel(Parcel in) {
            return new DiscoverTile(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public DiscoverTile[] newArray(int size) {
            return new DiscoverTile[size];
        }
    };

}
