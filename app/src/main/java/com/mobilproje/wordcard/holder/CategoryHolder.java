package com.mobilproje.wordcard.holder;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilproje.wordcard.OnItemClickListener;
import com.mobilproje.wordcard.R;
import com.mobilproje.wordcard.adaptor.CardAddAdaptor;
import com.mobilproje.wordcard.dao.CardDao;
import com.mobilproje.wordcard.dao.CategoryDao;
import com.mobilproje.wordcard.dao.ConnectorDao;
import com.mobilproje.wordcard.model.Card;
import com.mobilproje.wordcard.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryHolder extends RecyclerView.ViewHolder {
    TextView tvName;
    Category category;

    boolean clicked;
    RecyclerView recyclerView;

    TextView tvSave,tvExit;
    Context context;
    Button btnMines,btnDelete,btnPlus;

    CardAddAdaptor adaptor;
    public CategoryHolder(@NonNull View view, OnItemClickListener listener){
        super(view);
        context=view.getContext();
        tvName=view.findViewById(R.id.tvCategoryName);
        btnMines=view.findViewById(R.id.btnMinesCategoryListItem);
        btnDelete=view.findViewById(R.id.btnDeleteCategoryListItem);
        btnPlus=view.findViewById(R.id.btnPlusCategoryListItem);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Silinecek olan kategori: "+tvName.getText().toString());
                deleteCategory();
                listener.onItemClick(getAdapterPosition());
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Ekleme yapılacak kategori: "+tvName.getText().toString());
                clicked=false;
                clickPlus();
            }
        });
        btnMines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Çıkartma yapılacak kategori: "+tvName.getText().toString());
                clicked=true;
                clickMines();
            }
        });
    }

    public void setData(Category category){
        this.category=category;
        tvName.setText(category.getName());
    }

    private void clickPlus(){
        Dialog dialog=new Dialog(context);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(R.layout.card_add_list);
        Button btn=dialog.findViewById(R.id.btnCloseCardAddList);
        recyclerView = dialog.findViewById(R.id.rvCardAddList);
        tvSave=dialog.findViewById(R.id.tvSaveCardAddList);
        tvExit=dialog.findViewById(R.id.tvExitCardAddList);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
                dialog.dismiss();
            }
        });
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        listCardsPlus();
        dialog.getWindow().setAttributes(params);
        dialog.show();
    }

    private void clickMines(){
        Dialog dialog=new Dialog(context);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(R.layout.card_add_list);
        Button btn=dialog.findViewById(R.id.btnCloseCardAddList);
        recyclerView = dialog.findViewById(R.id.rvCardAddList);
        tvSave=dialog.findViewById(R.id.tvSaveCardAddList);
        tvExit=dialog.findViewById(R.id.tvExitCardAddList);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
                dialog.dismiss();
            }
        });
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        listCardsMines();
        dialog.getWindow().setAttributes(params);
        dialog.show();
    }

    private void saveChanges(){
        List<CardAddHolder> hodlers=adaptor.getHolders();
        List<Long> idList=new ArrayList<>();
        for(CardAddHolder holder:hodlers){
            if(holder.clicked!=clicked){
                idList.add(holder.card.getId());
            }
        }
        ConnectorDao connectorDao = new ConnectorDao();
        //eklerken false çıkarırken true
        if(!clicked){
            if(connectorDao.add(category.getId(),idList)){
                System.out.println("Ekleme Başarılı!");
            }else{
                System.out.println("Ekleme Başarısız!");
            }
        }else{
            if(connectorDao.remove(category.getId(),idList)){
                System.out.println("Silme Başarılı");
            }else{
                System.out.println("Silme Başarısız!");
            }
        }
    }



    private void listCardsMines(){
        List<Long> cardIdList = new CategoryDao().getCardIdList(category.getId());
        List<Card> cards = new CardDao().getFromCategory(cardIdList);

        adaptor = new CardAddAdaptor(cards,context,clicked);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adaptor);
    }

    private void listCardsPlus(){
        List<Long> cardIdList = new CategoryDao().getCardIdList(category.getId());
        List<Card> cards = new CardDao().getAll(category.getUserId());
        for(Long id:cardIdList){
            for(Card card:cards){
                if(card.getId()==id){
                    cards.remove(card);
                    break;
                }
            }
        }

        adaptor = new CardAddAdaptor(cards,context,clicked);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adaptor);
    }

    private void deleteCategory(){
        ConnectorDao connectorDao=new ConnectorDao();
        CategoryDao categoryDao=new CategoryDao();
        connectorDao.remove(category.getId());
        categoryDao.remove(category.getId());
        System.out.println("Kategori silindi!");
    }
}
