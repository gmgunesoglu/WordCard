package com.mobilproje.wordcard.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilproje.wordcard.OnItemClickListener;
import com.mobilproje.wordcard.R;
import com.mobilproje.wordcard.dao.CategoryDao;
import com.mobilproje.wordcard.holder.PlayHolder;
import com.mobilproje.wordcard.model.Category;

import java.util.List;

public class PlayAdaptor extends RecyclerView.Adapter<PlayHolder> {

    private List<Category> categories;

    private Context context;

    private OnItemClickListener listener;


    public PlayAdaptor(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public PlayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.play_list_item,parent,false);
        return new PlayHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayHolder holder, int position) {
        Category category=categories.get(position);
        holder.setData(category.getName(),getCardCount(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    private int getCardCount(Category category){
        return new CategoryDao().getCardCount(category.getId());
    }
}
