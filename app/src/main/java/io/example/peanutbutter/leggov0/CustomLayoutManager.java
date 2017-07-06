package io.example.peanutbutter.leggov0;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Samuel on 4/07/2017.
 */

public class CustomLayoutManager extends LinearLayoutManager {

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

    public boolean isScrollEnabled() {
        return isScrollEnabled;
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }
}
