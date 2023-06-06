package com.mobilproje.wordcard.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilproje.wordcard.OnItemClickListener;
import com.mobilproje.wordcard.R;
import com.mobilproje.wordcard.holder.CardHolder;
import com.mobilproje.wordcard.model.Card;
import java.util.List;

public class CardAdaptor extends RecyclerView.Adapter<CardHolder> {

    List<Card> cards;
    Context context;
    OnItemClickListener listener;


    public CardAdaptor(List<Card> cards, Context context) {
        this.cards = cards;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_list_item,parent,false);
        return new CardHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        Card card=cards.get(position);
        holder.setData(card);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
