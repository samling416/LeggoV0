package io.example.peanutbutter.leggov0;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Samuel on 4/07/2017.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int vertical = 0, horizontal = 1;

    public static final String TAG = "DividerItemDecoration";
    private int mode;
    private int mScreenWidth;
    private int mScreenHeight;
    private int verticalSpaceHeight;
    private int horizontalSpaceWidth;

    public DividerItemDecoration(int spacing, int mode, int mScreenWidth, int mScreenHeight) {
        this.mode = mode;
        if (mode == vertical) {
            this.verticalSpaceHeight = spacing;
        } else if (mode == horizontal) {
            this.horizontalSpaceWidth = spacing;
        }
        this.mScreenWidth = mScreenWidth;
        this.mScreenHeight = mScreenHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Adds offset between items.

        if (mode == vertical) {

        } else if (mode == horizontal) {

            /*if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = ((mScreenWidth/2) - 16) + ;
            } else {
                int imageWidth = (((mScreenHeight / 100) * RecyclerAdapter.ScaleFactor) * 300 / 200);
                outRect.left = mScreenWidth / 2 - imageWidth / 2 - 16;
            }*/

            int imageWidth = (((mScreenHeight / 100) * RecyclerAdapter.ScaleFactor) * 300 / 200);
            outRect.left = (mScreenWidth / 2 - imageWidth / 2) - 26;
        }
    }


}
