package com.mobilproje.wordcard.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilproje.wordcard.OnItemClickListener;
import com.mobilproje.wordcard.R;

public class PlayHolder extends RecyclerView.ViewHolder{

    private int cardCount;
    private String categoryName;
    private TextView tvcategoryName;
    private LinearLayout llItem;

    public PlayHolder(@NonNull View view, OnItemClickListener listener) {
        super(view);
        tvcategoryName = view.findViewById(R.id.tvCategoryNamePlayListItem);
        llItem = view.findViewById(R.id.llCategoryPlayListItem);
        llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(getAdapterPosition());
            }
        });
    }

    public void setData(String categoryName,int cardCount){
        this.cardCount=cardCount;
        this.categoryName=categoryName;
        tvcategoryName.setText("[ "+categoryName+" ]    [ "+cardCount+" Kart ]");
    }
}
