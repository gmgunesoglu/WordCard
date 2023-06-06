package com.mobilproje.wordcard.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mobilproje.wordcard.OnItemClickListener;
import com.mobilproje.wordcard.R;
import com.mobilproje.wordcard.holder.CategoryHolder;
import com.mobilproje.wordcard.model.Category;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
    List<Category> categories;
    Context context;
    OnItemClickListener listener;


    public CategoryAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_list_item,parent,false);
        return new CategoryHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category=categories.get(position);
        holder.setData(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
