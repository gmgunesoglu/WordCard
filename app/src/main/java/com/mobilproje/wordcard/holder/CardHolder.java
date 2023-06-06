package com.mobilproje.wordcard.holder;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilproje.wordcard.OnItemClickListener;
import com.mobilproje.wordcard.R;
import com.mobilproje.wordcard.dao.CardDao;
import com.mobilproje.wordcard.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardHolder extends RecyclerView.ViewHolder {

    TextView tvCardEng,tvCardTr;

    Card card;
    Context context;
    Button btnConfigure,btnDelete;

    public CardHolder(@NonNull View view, OnItemClickListener listener){
        super(view);
        context=view.getContext();
        tvCardEng=view.findViewById(R.id.tvCardEng);
        tvCardTr=view.findViewById(R.id.tvCardTr);
        btnConfigure=view.findViewById(R.id.btnConfigureCards);
        btnConfigure=view.findViewById(R.id.btnConfigureCardListItem);
        btnDelete=view.findViewById(R.id.btnDeleteCardListItem);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(getAdapterPosition());
            }
        });
        btnConfigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureClick();
            }
        });
    }

    public void setData(Card card){
        this.card=card;
        tvCardEng.setText("ENG:  "+card.getEngWord());
        tvCardTr.setText("TR: "+card.getTrWord());
    }

    private void configureClick(){
        Dialog dialog=new Dialog(context);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(R.layout.dialog_configure_card);
        Button btnClose=dialog.findViewById(R.id.btnCloseDialogConfigureCard);
        TextView tvTrWord=dialog.findViewById(R.id.etTrWordConfigure);
        TextView tvEngWord=dialog.findViewById(R.id.etEngWordConfigure);
        TextView tvTrUsage1=dialog.findViewById(R.id.etTrUsage1Configure);
        TextView tvTrUsage2=dialog.findViewById(R.id.etTrUsage2Configure);
        TextView tvTrUsage3=dialog.findViewById(R.id.etTrUsage3Configure);
        TextView tvEngUsage1=dialog.findViewById(R.id.etEngUsage1Configure);
        TextView tvEngUsage2=dialog.findViewById(R.id.etEngUsage2Configure);
        TextView tvEngUsage3=dialog.findViewById(R.id.etEngUsage3Configure);
        TextView tvSave=dialog.findViewById(R.id.tvSaveCardDialogConfigureCard);
        TextView tvExit=dialog.findViewById(R.id.tvExitDialogConfigureCard);

        List<String> trUsages = card.getTrUsage();
        List<String> engUsages = card.getEngUsage();

        tvTrWord.setHint(card.getTrWord());
        tvEngWord.setHint(card.getEngWord());
        if(trUsages==null){
            trUsages=new ArrayList<>();
        }
        for(int i=trUsages.size();i<3;i++){
            trUsages.add("");
        }
        if(engUsages==null){
            engUsages=new ArrayList<>();
        }
        for(int i=engUsages.size();i<3;i++){
            engUsages.add("");
        }

        tvTrUsage1.setHint(trUsages.get(0));
        tvTrUsage2.setHint(trUsages.get(1));
        tvTrUsage3.setHint(trUsages.get(2));
        tvEngUsage1.setHint(engUsages.get(0));
        tvEngUsage2.setHint(engUsages.get(1));
        tvEngUsage3.setHint(engUsages.get(2));


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardDao cardDao=new CardDao();
                cardDao.delete(card);

                List<String> trUsages = new ArrayList<>();
                List<String> engUsages = new ArrayList<>();

                if(tvTrUsage1.getText().length()!=0){
                    trUsages.add(tvTrUsage1.getText().toString());
                }else{
                    trUsages.add(tvTrUsage1.getHint().toString());
                }
                if(tvTrUsage2.getText().length()!=0){
                    trUsages.add(tvTrUsage2.getText().toString());
                }else{
                    trUsages.add(tvTrUsage2.getHint().toString());
                }
                if(tvTrUsage3.getText().length()!=0){
                    trUsages.add(tvTrUsage3.getText().toString());
                }else{
                    trUsages.add(tvTrUsage3.getHint().toString());
                }
                if(tvEngUsage1.getText().length()!=0){
                    engUsages.add(tvEngUsage1.getText().toString());
                }else{
                    engUsages.add(tvEngUsage1.getHint().toString());
                }
                if(tvEngUsage2.getText().length()!=0){
                    engUsages.add(tvEngUsage2.getText().toString());
                }else{
                    engUsages.add(tvEngUsage2.getHint().toString());
                }
                if(tvEngUsage3.getText().length()!=0){
                    engUsages.add(tvEngUsage3.getText().toString());
                }else{
                    engUsages.add(tvEngUsage3.getHint().toString());
                }

                if(tvTrWord.getText().length()!=0){
                    card.setTrWord(tvTrWord.getText().toString());
                }else{
                    card.setTrWord(tvTrWord.getHint().toString());
                }
                if(tvEngWord.getText().length()!=0){
                    card.setEngWord(tvEngWord.getText().toString());
                }else{
                    card.setEngWord(tvEngWord.getHint().toString());
                }

                card.setTrUsage(trUsages);
                card.setEngUsage(engUsages);
                cardDao.save(card);

                tvCardEng.setText("TR:  "+card.getEngWord());
                tvCardTr.setText("ENG: "+card.getTrWord());

                dialog.dismiss();
            }
        });

        dialog.getWindow().setAttributes(params);
        dialog.show();
    }
}
