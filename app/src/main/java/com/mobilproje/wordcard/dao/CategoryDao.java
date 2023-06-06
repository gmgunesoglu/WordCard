package com.mobilproje.wordcard.dao;

import android.database.Cursor;
import com.mobilproje.wordcard.UserActivity;
import com.mobilproje.wordcard.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public CategoryDao() {
    }

    public List<Category> getAll(Long userId){
        List<Category> categories=new ArrayList<>();

        String cmd="SELECT * FROM category WHERE user_id = '"+userId+"';";
        Cursor cursor = UserActivity.database.rawQuery(cmd,null);
        Category category;
        while(cursor.moveToNext()){
            category = new Category();
            category.setId(cursor.getLong(0));
            category.setUserId(cursor.getLong(1));
            category.setName(cursor.getString(2));
            categories.add(category);
        }
        return categories;
    }

    public boolean add(Category category){
        String cmd="INSERT INTO category (user_id, name) VALUES (";
        cmd+="'"+category.getUserId()+"',";
        cmd+="'"+category.getName()+"');";
        try{
            UserActivity.database.execSQL(cmd);
            return  true;

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
            return false;
        }
    }

    public List<Long> getCardIdList(Long categoryId){
        List<Long> idList=new ArrayList<>();
        String cmd="SELECT * FROM connector WHERE category_id = '"+categoryId+"';";
        Cursor cursor = UserActivity.database.rawQuery(cmd,null);
        while(cursor.moveToNext()){
            idList.add(cursor.getLong(1));
        }
        return idList;
    }

    public void remove(Long categoryId){
        String cmd="DELETE FROM category WHERE id = '"+categoryId+"';";
        try{
            UserActivity.database.execSQL(cmd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
        }
    }

    public int getCardCount(Long categoryId){
        String cmd="SELECT * FROM connector WHERE category_id = '"+categoryId+"'";
        Cursor cursor = UserActivity.database.rawQuery(cmd,null);
        int result = cursor.getCount();
        if(result==0){
            return 0;
        }else{
            return  result-1;
        }

//        String cmd="SELECT COUNT (*) FROM connector WHERE category_id = '"+categoryId+"'";
//        Cursor cursor = UserActivity.database.rawQuery(cmd,null);
//
//        if (cursor.moveToNext()){
//            return cursor.getInt(0);
//        }
//        return 0;
    }
}
