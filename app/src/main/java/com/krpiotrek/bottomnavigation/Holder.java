package com.krpiotrek.bottomnavigation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class Holder extends RecyclerView.ViewHolder {

    private TextView text;

    public Holder(View itemView) {
        super(itemView);
        text = (TextView) itemView.findViewById(android.R.id.text1);
    }

    public TextView getText() {
        return text;
    }

    public void setText(String text) {
        this.text.setText(text);
    }
}
