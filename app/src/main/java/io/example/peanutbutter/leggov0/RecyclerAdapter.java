package io.example.peanutbutter.leggov0;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Samuel on 1/07/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements GoogleMap.CancelableCallback,
        ItemTouchHelperAdapter {

    public static final String TAG = "RecyclerAdapter";
    public static final int ScaleFactor = 27;
    private ArrayList<DiscoverTile> mDiscoverTiles;
    private ArrayList<Boolean> mSecondClick;
    private ArrayList<Marker> mMarker;
    private GoogleMap mMap;
    private MainActivity mActivity;
    private int mScreenHeight;
    private int mPosition;
    private int lastposition;
    private int positionDetach;
    private ViewGroup.LayoutParams mImageviewParams;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final CardView mCardView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            //Log.d(TAG, "ViewHolder: ");
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

        public CardView getCardView() {
            return mCardView;
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

        // Initialize on startup.
        mSecondClick = new ArrayList<Boolean>();
        mMarker = new ArrayList<Marker>();
        for (int i = 0; i < mDiscoverTiles.size(); i++) {
            mSecondClick.add(false);
            mMarker.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mDiscoverTiles.get(i).getLat(), mDiscoverTiles.get(i).getLng()))));
            mMarker.get(i).setVisible(false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Discover Tile " + mDiscoverTiles.get(mPosition).getName() + " has been loaded.");
        holder.mImageView.setImageResource(mDiscoverTiles.get(position).getPhoto());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity.getRecyclerviewState() == MainActivity.EXPANDED) {
                    // Onclick logic. If Marker does not exit, add it. Else, remove pre-existing marker.
                    double getLat = mDiscoverTiles.get(holder.getAdapterPosition()).getLat();
                    double getLng = mDiscoverTiles.get(holder.getAdapterPosition()).getLng();
                    if (!(mSecondClick.get(holder.getAdapterPosition()))) {

                        // Animate tile to position
                        LatLng coordinates = new LatLng(mDiscoverTiles.get(holder.getAdapterPosition()).getLat(), mDiscoverTiles.get(holder.getAdapterPosition()).getLng());
                        animateToTileLocation(coordinates);

                        // Elevate and light up tile.
                        holder.mCardView.setCardElevation(8);

                        // Make Marker visible on click.
                        mMarker.get(holder.getAdapterPosition()).setVisible(true);
                        mSecondClick.set(holder.getAdapterPosition(), true);
                    } else {
                        //Log.d(TAG, "onClick: remove");

                        // Collapse and unlight tile.
                        holder.mCardView.setCardElevation(2);

                        mMarker.get(holder.getAdapterPosition()).setVisible(false);
                        mSecondClick.set(holder.getAdapterPosition(), false);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiscoverTiles.size();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        mPosition = holder.getAdapterPosition();
        Log.d(TAG, "onViewAttachedToWindow: Position is " + mPosition);
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        Log.d(TAG, "onViewDetachedFromWindow: positionDetach is " + holder.getAdapterPosition());
        positionDetach = holder.getAdapterPosition();
        super.onViewDetachedFromWindow(holder);
        checkCameraAnimation(holder);
        /*if (positionDetach == 1 && mPosition == 0 && mActivity.getRecyclerviewState() == MainActivity.EXPANDED) {
            mActivity.enableCollapseRecycleView();
        }*/
    }

    public void animateToTileLocation(LatLng destination) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(destination), 1500, this);
    }

    public void checkCameraAnimation(ViewHolder holder) {
        //locationName = (String) holder.mTextView.getText();
        if (mPosition == holder.getAdapterPosition()) {
            if (mPosition == 0) {
                LatLng correctedposition = new LatLng(mDiscoverTiles.get(mPosition + 1).getLat(), mDiscoverTiles.get(mPosition + 1).getLng());
                animateToTileLocation(correctedposition);
            } else if (mPosition == (mDiscoverTiles.size() - 1)) {
                LatLng correctedposition = new LatLng(mDiscoverTiles.get(mPosition - 1).getLat(), mDiscoverTiles.get(mPosition - 1).getLng());
                animateToTileLocation(correctedposition);
            } else {
                LatLng correctedposition = new LatLng(mDiscoverTiles.get(lastposition).getLat(), mDiscoverTiles.get(lastposition).getLng());
                animateToTileLocation(correctedposition);
            }
        } else {
            lastposition = mPosition;
        }
    }

    @Override
    public void onFinish() {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 1500, null);
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
        Log.d(TAG, "onItemSwiped: Item swiped " + position);
    }

    public int getholderCardview() {
        return mPosition;
    }
}
