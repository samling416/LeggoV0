package io.example.peanutbutter.leggov0;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by Samuel on 17/07/2017.
 */

public class CustomGridLayoutItem extends LinearLayout {

    public CustomGridLayoutItem(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            inflater.inflate(R.layout.gridlayout_item, this);
        }
    }
}
