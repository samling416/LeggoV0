package io.example.peanutbutter.leggov0;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Samuel on 3/07/2017.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }


    // Specifies direction of drags and swipes which are supported.
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // No drags in all direction.
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        // Allowleft and right swipes
        int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    // OnMove and OnSwiped notify anything in charge of updating underlying data.
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemSwiped(viewHolder.getAdapterPosition());
    }

}
