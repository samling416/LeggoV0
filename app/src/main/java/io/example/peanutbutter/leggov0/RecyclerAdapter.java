package io.example.peanutbutter.leggov0;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final int ScaleFactor = 28;
    public static boolean weathercallfinish = false;

    private ArrayList<DiscoverTile> mDiscoverTiles;
    private ArrayList<Boolean> mSecondClick;
    private ArrayList<Marker> mMarker;
    private Boolean misExpanded = false;
    private GoogleMap mMap;
    private MainActivity mActivity;
    private int mScreenHeight;
    private int mPosition;
    private int lastposition;
    private int positionDetach;
    public float inity;
    public float finaly;
    private ViewGroup.LayoutParams mImageviewParams;

    public int mCardviewExpandedHeight;
    public int mCardviewCollapsedHeight;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final CardView mCardView;
        public final ImageView mImageView;
        public final TextView mNameTextView;
        /*public final TextView mDescriptTextView;
        public final TextView mTimeTextView;
        public final TextView mLocTextView;
        public final TextView mDistTextView;
        public final GridLayout mGridLayoutActivity;
        public final GridLayout mGridLayoutWeather;*/


        public ViewHolder(View view) {
            super(view);
            //Log.d(TAG, "ViewHolder: ");
            mCardView = (CardView) view.findViewById(R.id.tile_cardview);
            mImageView = (ImageView) view.findViewById(R.id.location_imageview);
            mNameTextView = (TextView) view.findViewById(R.id.location_textview);
            //mDescriptTextView = (TextView) view.findViewById(R.id.description_placeholder);
            //mTimeTextView = (TextView) view.findViewById(R.id.time_textview);
            //mLocTextView = (TextView) view.findViewById(R.id.initial_loc_textview);
            //mDistTextView = (TextView) view.findViewById(R.id.distance_textview);
            //mGridLayoutWeather = (GridLayout) view.findViewById(R.id.weatherIcon_GridLayout);
            //mGridLayoutActivity = (GridLayout) view.findViewById(R.id.activityIcon_GridLayout);

            initializeCard();
            initializeMarkers();
        }

        public void initializeCard() {
            mImageviewParams = mImageView.getLayoutParams();
            int NewHeight = (mScreenHeight / 100) * ScaleFactor;
            mImageviewParams.height = NewHeight;
            mImageviewParams.width = (300 * NewHeight) / 200;
            mImageView.setLayoutParams(mImageviewParams);
        }

        public void initializeMarkers() {
            // Initialize on startup.
            mSecondClick = new ArrayList<Boolean>();
            mMarker = new ArrayList<Marker>();
            for (int i = 0; i < mDiscoverTiles.size(); i++) {
                mSecondClick.add(false);
                mMarker.add(mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mDiscoverTiles.get(i).getLat(), mDiscoverTiles.get(i).getLng()))));
                mMarker.get(i).setVisible(true);
            }
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

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Discover Tile " + mDiscoverTiles.get(mPosition).getName() + " has been loaded.");
        holder.mImageView.setImageResource(mDiscoverTiles.get(position).getPhoto());
        holder.mNameTextView.setText(mDiscoverTiles.get(mPosition).getName());
        holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                animateToTileLocation(new LatLng(mDiscoverTiles.get(mPosition).getLat(),mDiscoverTiles.get(mPosition).getLng()));
                return true;
            }
        });
        /*fetchActivities(holder, position);
        fetchWeather(holder);
        holder.mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (weathercallfinish) {
                    // TODO: IMPLEMENT ANIMATIONS
                    initializeAnimation(holder, mPosition);
                }
            }
        });*/
        //measureFinalandInitialCardHeights(holder, mPosition);

    }

    @Override
    public int getItemCount() {
        return mDiscoverTiles.size();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        mPosition = holder.getAdapterPosition();
        //animateToTileLocation(new LatLng(mDiscoverTiles.get(mPosition).getLat(),mDiscoverTiles.get(mPosition).getLng()));
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        //Log.d(TAG, "onViewDetachedFromWindow: positionDetach is " + holder.getAdapterPosition());
        positionDetach = holder.getAdapterPosition();
        super.onViewDetachedFromWindow(holder);
        //checkCameraAnimation(holder);
    }


    public void animateToTileLocation(LatLng destination) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(destination), 1500, this);
    }

    public void checkCameraAnimation(ViewHolder holder) {
        //locationName = (String) holder.mNameTextView.getText();
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

    public void elevateCard(ViewHolder vh) {
        final ViewHolder holder = vh;
    }

    public void ExpandCard(ViewHolder vh, int position) {
        final ViewHolder holder = vh;

        ValueAnimator animation = ValueAnimator.ofInt(370, 700);
        animation.setDuration(500);
        animation.start();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedvalue = (int) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: " + animatedvalue);
                ViewGroup.LayoutParams lp = holder.getCardView().getLayoutParams();
                lp.height = animatedvalue;
                holder.mCardView.setLayoutParams(lp);
            }
        });


        misExpanded = true;
        //setCardVisibility(holder);

        TransitionManager.beginDelayedTransition(mActivity.getRecyclerView());
        notifyDataSetChanged();
    }

    public void CollapseCard(ViewHolder vh, int position) {
        final ViewHolder holder = vh;

        ValueAnimator animation = ValueAnimator.ofInt(700, 400);
        animation.setDuration(500);
        animation.start();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedvalue = (int) animation.getAnimatedValue();
                //Log.d(TAG, "onAnimationUpdate: " + animatedvalue);
                ViewGroup.LayoutParams lp = holder.getCardView().getLayoutParams();
                lp.height = animatedvalue;
                holder.mCardView.setLayoutParams(lp);
            }
        });


        misExpanded = false;
        //setCardVisibility(holder);
        TransitionManager.beginDelayedTransition(mActivity.getRecyclerView());
        notifyDataSetChanged();
    }

    /*public void fetchWeather(ViewHolder holder) {
        holder.mGridLayoutWeather.setColumnCount(6);
        WeatherDownload weatherDownload = new WeatherDownload(holder);
        weatherDownload.setmContext(mActivity);
        double Lat = mDiscoverTiles.get(mPosition).getLat();
        double Lng = mDiscoverTiles.get(mPosition).getLng();
        // Open http
        weatherDownload.execute("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + Double.toString(Lat) + "&lon=" + Double.toString(Lng) + "&cnt=6&appid=a29798c8e7bcb161dcf56cca00f66a77");

    }

    public void fetchActivities(ViewHolder holder, int position) {
        // Display Activity Icons (Only needs to be done on initialization)
        int c = 0;
        holder.mGridLayoutActivity.removeAllViews();
        for (int y = 0; y < mDiscoverTiles.get(position).noOfActivities(); y++) {
            holder.mGridLayoutActivity.setRowCount(1);
            ImageView iconImageView = new ImageView(mActivity);
            iconImageView.setImageResource(mDiscoverTiles.get(position).getActivities().get(y));
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayoutManager.LayoutParams.WRAP_CONTENT;
            param.width = GridLayoutManager.LayoutParams.WRAP_CONTENT;
            /*Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, r.getDisplayMetrics());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(px, px);
            iconImageView.setLayoutParams(layoutParams);*/

            /*holder.mGridLayoutActivity.addView(iconImageView);
            c++;
        }
    }*/

    public void initializeAnimation(final ViewHolder holder, final int position) {
        // Animation logic.
        //setCardVisibility(holder);

        ViewGroup.LayoutParams lp = holder.getCardView().getLayoutParams();
        lp.height = 400;
        holder.mCardView.setLayoutParams(lp);

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d(TAG, "onTouch: ");
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        // Get initial Y position
                        final float inity1 = event.getY();
                        inity = inity1;
                        Log.d(TAG, "onTouch: init is " + inity);
                        return true;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        final float finaly1 = event.getY();
                        finaly = finaly1;
                        Log.d(TAG, "onTouch: finaly is " + finaly);
                        float deltaY = finaly - inity;
                        // Determine if up or down swipe
                        if (Math.abs(deltaY) > 5) {
                            if (deltaY >= 0) {
                                Log.d(TAG, "onTouch: Swiped down");
                                if (mActivity.getRecyclerviewState() == MainActivity.EXPANDED) {
                                    if (misExpanded) {
                                        //CollapseCard(holder, holder.getAdapterPosition());
                                    }
                                }
                                return true;
                            } else if (deltaY <= 0) {
                                Log.d(TAG, "onTouch: Swiped up");
                                if (mActivity.getRecyclerviewState() == MainActivity.EXPANDED) {
                                    Toast.makeText(mActivity, "Expandcard", Toast.LENGTH_SHORT).show();
                                    if (!misExpanded) {
                                        //ExpandCard(holder, holder.getAdapterPosition());
                                    }
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                }
                return true;
            }
        });
    }

    /*public void setCardVisibility(ViewHolder holder) {
        if (misExpanded) {
            holder.mNameTextView.setVisibility(View.VISIBLE);
            holder.mDistTextView.setVisibility(View.VISIBLE);
            holder.mLocTextView.setVisibility(View.VISIBLE);
            holder.mTimeTextView.setVisibility(View.VISIBLE);
            holder.mDescriptTextView.setVisibility(View.VISIBLE);
            holder.mGridLayoutActivity.setVisibility(View.VISIBLE);
            holder.mGridLayoutWeather.setVisibility(View.VISIBLE);
        } else {
            holder.mNameTextView.setVisibility(View.GONE);
            holder.mDistTextView.setVisibility(View.GONE);
            holder.mLocTextView.setVisibility(View.GONE);
            holder.mTimeTextView.setVisibility(View.GONE);
            holder.mDescriptTextView.setVisibility(View.GONE);
            holder.mGridLayoutActivity.setVisibility(View.GONE);
            holder.mGridLayoutWeather.setVisibility(View.GONE);
        }

        holder.itemView.setActivated(misExpanded);
    }*/

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


}
