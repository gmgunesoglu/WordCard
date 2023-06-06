package com.mobilproje.wordcard.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilproje.wordcard.R;
import com.mobilproje.wordcard.model.Card;

public class CardAddHolder extends RecyclerView.ViewHolder {
    TextView tvCardEng,tvCardTr;

    Card card;
    boolean clicked;
    Button btn;

    public CardAddHolder(@NonNull View view){
        super(view);
        tvCardEng=view.findViewById(R.id.tvEngCardAddListItem);
        tvCardTr=view.findViewById(R.id.tvTrCardAddListItem);
        btn=view.findViewById(R.id.btnCardAddListItem);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=(!clicked);
                setBtnImage();
            }
        });

    }

    private void setBtnImage(){
        if(clicked){
            btn.setBackgroundResource(R.drawable.bg_minus);
        }else{
            btn.setBackgroundResource(R.drawable.bg_plus);
        }
    }

    public void setData(Card card, boolean clicked){
        this.clicked=clicked;
        tvCardEng.setText("TR:  "+card.getTrWord());
        tvCardTr.setText("ENG: "+card.getTrWord());
        this.card=card;
        setBtnImage();
    }
}
