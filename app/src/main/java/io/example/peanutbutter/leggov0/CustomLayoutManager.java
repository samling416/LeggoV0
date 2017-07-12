package io.example.peanutbutter.leggov0;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Switch;

/**
 * Created by Samuel on 4/07/2017.
 */

public class CustomLayoutManager extends LinearLayoutManager {

    // Shrink the cards around the center up to x%
    private final float mShrinkAmount = 0.10f;
    // The cards will be at x% when they are mShrinkDistance% of the way between the
    // center and the edge.
    private final float mShrinkDistance = 0.1f;

    private Context mContext;
    private boolean isScrollEnabled = true;

    public CustomLayoutManager(Context context) {
        super(context);
    }

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.mContext = context;
    }

    public CustomLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout, boolean isScrollEnabled) {
        super(context, orientation, reverseLayout);
        this.mContext = context;
        this.isScrollEnabled = isScrollEnabled;
        canScrollHorizontally();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled = super.scrollHorizontallyBy(dx, recycler, state);

        float midpoint = getWidth() / 2.f;
        float d0 = 0.f;
        float d1 = mShrinkDistance * midpoint;
        float s0 = 1.f;
        float s1 = 1.f - mShrinkAmount;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMidpoint =
                    (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
            float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
            float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
            child.setScaleX(scale);
            child.setScaleY(scale);
        }

        return scrolled;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scrollVerticallyBy(0, recycler, state);
    }

    @Override
    public int findFirstCompletelyVisibleItemPosition() {
        return super.findFirstCompletelyVisibleItemPosition();
    }

    public boolean isScrollEnabled() {
        return isScrollEnabled;
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }
}
