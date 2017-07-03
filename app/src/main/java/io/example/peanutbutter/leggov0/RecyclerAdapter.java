package io.example.peanutbutter.leggov0;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;

/**
 * Created by Samuel on 1/07/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements GoogleMap.CancelableCallback,
        ItemTouchHelperAdapter {

    public static final String TAG = "RecyclerAdapter";
    public static final int ScaleFactor = 27;
    private ArrayList<DiscoverTile> mDiscoverTiles;
    private GoogleMap mMap;
    private MainActivity mActivity;
    private int mScreenHeight;
    private int position;
    private int lastposition;
    private ViewGroup.LayoutParams mImageviewParams;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final CardView mCardView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mCardView = (CardView) view.findViewById(R.id.tile_cardview);
            mImageView = (ImageView) view.findViewById(R.id.location_imageview);
            initializeCard();
        }

        public void initializeCard() {
            mImageviewParams = mImageView.getLayoutParams();
            int NewHeight = (mScreenHeight / 100) * ScaleFactor;
            mImageviewParams.height = NewHeight;
            mImageviewParams.width = (300 * NewHeight) / 200;
            mImageView.setLayoutParams(mImageviewParams);
        }


    }

    RecyclerAdapter(ArrayList<DiscoverTile> mDiscoverTiles) {
        this.mDiscoverTiles = mDiscoverTiles;
    }

    RecyclerAdapter(ArrayList<DiscoverTile> mDiscoverTiles, MainActivity activity) {
        this.mDiscoverTiles = mDiscoverTiles;
        this.mMap = activity.getMap();
        this.mActivity = activity;
        this.mScreenHeight = activity.getScreenHeight();
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        //Log.d(TAG, "onBindViewHolder: Discover Tile " + mDiscoverTiles.get(position).getName() + " has been loaded.");
        holder.mImageView.setImageResource(mDiscoverTiles.get(position).getPhoto());
        //mMap.animateCamera(CameraUpdateFactory.newLatLng();
    }

    @Override
    public int getItemCount() {
        return mDiscoverTiles.size();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        //locationName = (String) holder.mTextView.getText();
        position = holder.getAdapterPosition();
        animateToTileLocation(new LatLng(mDiscoverTiles.get(position).getLat(), mDiscoverTiles.get(position).getLng()));
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        checkCameraAnimation(holder);
    }

    public void animateToTileLocation(LatLng destination) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(destination), 1500, this);
    }

    public void checkCameraAnimation(ViewHolder holder) {
        //locationName = (String) holder.mTextView.getText();
        if (position == holder.getAdapterPosition()) {
            if (position == 0) {
                LatLng correctedposition = new LatLng(mDiscoverTiles.get(position + 1).getLat(), mDiscoverTiles.get(position + 1).getLng());
                animateToTileLocation(correctedposition);
            } else if (position == (mDiscoverTiles.size() - 1)) {
                LatLng correctedposition = new LatLng(mDiscoverTiles.get(position - 1).getLat(), mDiscoverTiles.get(position - 1).getLng());
                animateToTileLocation(correctedposition);
            } else {
                LatLng correctedposition = new LatLng(mDiscoverTiles.get(lastposition).getLat(), mDiscoverTiles.get(lastposition).getLng());
                animateToTileLocation(correctedposition);
            }
        } else {
            lastposition = position;
        }
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "onCancel: ");
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    public void onItemSwiped(int position) {
    }
}
