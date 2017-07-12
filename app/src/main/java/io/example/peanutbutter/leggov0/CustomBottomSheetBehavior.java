package io.example.peanutbutter.leggov0;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * Created by Samuel on 6/07/2017.
 */

public class CustomBottomSheetBehavior extends BottomSheetBehavior {

    public static final String TAG = "CusBottomSheetBehavior";

    private MainActivity mActivity;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    public CustomBottomSheetBehavior() {
        super();
        Log.d(TAG, "CustomBottomSheetBehavior: Created");
    }

    public CustomBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "CustomBottomSheetBehavior: created");
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
        super.onInterceptTouchEvent(parent, child, event);

        if (child instanceof NestedScrollView) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    //Log.d(TAG, "onInterceptTouchEvent: TOUCHED");
                    final int pointerIndex = MotionEventCompat.getActionIndex(event);
                    // Record starting positions.
                    final float y = event.getY();

                    // Remember where we started dragging.
                    mLastTouchY = y;

                    //Save ID of this pointer.
                    mActivePointerId = event.getPointerId(0);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    //Log.d(TAG, "onInterceptTouchEvent: MOVING...");
                    final int pointerIndex = MotionEventCompat.getActionIndex(event);

                    // Record starting positions.
                    final float y = event.getY();

                    // Calculate distance moved
                    final float dy = mLastTouchY - y;

                    // If distance is greater than 30dp, Expand bottomsheet. If distance dragged less than -30dp, collapse.
                    if (dy > 30) {
                        //Log.d(TAG, "onInterceptTouchEvent: UP");
                        if (mActivity.getRecyclerviewState() == MainActivity.COLLAPSED) {
                            Log.d(TAG, "onInterceptTouchEvent: Expand RECYCLERVIEW");
                            //setState(BottomSheetBehavior.STATE_EXPANDED);
                            mActivity.setRecyclerviewState(MainActivity.EXPANDED);
                            return true;
                        } else if (mActivity.getRecyclerviewState() == MainActivity.EXPANDED){
                            //Log.d(TAG, "onInterceptTouchEvent: Expand CARD");
                            //At this point, it's an UPWARDS swipe on a collapsed card
                            return false;
                        }
                        return false;
                    } else if (dy < -30) {
                        //Log.d(TAG, "onInterceptTouchEvent: DOWN");
                        if (mActivity.getRecyclerviewState() == MainActivity.EXPANDED) {
                            Log.d(TAG, "onInterceptTouchEvent: Collapse RECYCLERVIEW");
                            //setState(BottomSheetBehavior.STATE_COLLAPSED);
                            mActivity.setRecyclerviewState(MainActivity.COLLAPSED);
                            return true;

                        } else if (mActivity.getRecyclerviewState() == MainActivity.COLLAPSED){
                            //Log.d(TAG, "onInterceptTouchEvent: Collapse CARD");
                            return false;
                        }
                        return false;
                    }
                }
            }
        }

        return false;
    }

    public void setActivity(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

}