package com.mobilproje.wordcard.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilproje.wordcard.R;
import com.mobilproje.wordcard.holder.CardAddHolder;
import com.mobilproje.wordcard.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardAddAdaptor extends RecyclerView.Adapter<CardAddHolder> {

    List<Card> cards;

    List<CardAddHolder> holders;
    boolean clicked;
    Context context;

    public CardAddAdaptor(List<Card> cards, Context context, boolean clicked) {
        this.cards = cards;
        this.context = context;
        this.clicked = clicked;
        holders=new ArrayList<>();
    }

    @NonNull
    @Override
    public CardAddHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_add_list_item,parent,false);
        CardAddHolder holder = new CardAddHolder(view);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAddHolder holder, int position) {
        Card card=cards.get(position);
        holder.setData(card,clicked);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public List<CardAddHolder> getHolders() {
        return holders;
    }


}
