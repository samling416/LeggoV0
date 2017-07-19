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
    private ArrayList<String> mActivityNames;
    private LatLng mLocation;
    private double mLat;
    private double mLng;
    private String mDescription;
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
        mName = name;
        mPhoto = photo;
        mActivities = activities;
        mLng = 174.775486;
        mLat = -36.858931;
    }

    public DiscoverTile(String name, int photo, ArrayList<Integer> activities, ArrayList<String> activityNames, Double Lat, Double Lng) {
        this.mName = name;
        this.mPhoto = photo;
        this.mActivities = activities;
        this.mActivityNames = activityNames;
        this.mLng = Lng;
        this.mLat = Lat;
        this.mDescription = "(No description has been entered.)";
        this.mLocation = new LatLng(Lat,Lng);
    }

    public DiscoverTile(String name, int photo, ArrayList<Integer> activities, ArrayList<String> activityNames, Double Lat, Double Lng, String mDescription) {
        this.mName = name;
        this.mPhoto = photo;
        this.mActivities = activities;
        this.mActivityNames = activityNames;
        this.mLng = Lng;
        this.mLat = Lat;
        this.mDescription = mDescription;
        this.mLocation = new LatLng(Lat,Lng);
    }

    public boolean isAlreadyinitialized() {
        return alreadyinitialized;
    }

    public void setAlreadyinitialized(boolean alreadyinitialized) {
        this.alreadyinitialized = alreadyinitialized;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
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

    public ArrayList<String> getActivityNames() {
        return mActivityNames;
    }

    public void setActivityNames(ArrayList<String> activityNames) {
        mActivityNames = activityNames;
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
