package com.mobilproje.wordcard.dao;

import android.database.Cursor;

import com.mobilproje.wordcard.UserActivity;

import java.util.ArrayList;
import java.util.List;

public class ConnectorDao {

    public boolean add(Long categoryId,List<Long> cardIdList){
        for(Long cardId:cardIdList){
            String cmd="INSERT INTO connector (category_id, card_id) VALUES ("+categoryId+", "+cardId+");";
            try{
                UserActivity.database.execSQL(cmd);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("[-] SQL Command couldn't run: "+cmd);
                return false;
            }
        }
        return true;
    }

    public boolean remove(Long categoryId,List<Long> cardIdList){
        for(Long cardId:cardIdList){
            String cmd="DELETE FROM connector WHERE category_id = '"+categoryId+"' AND card_id = '"+cardId+"';";
            try{
                UserActivity.database.execSQL(cmd);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("[-] SQL Command couldn't run: "+cmd);
                return false;
            }
        }
        return true;
    }

    public void remove(Long categoryId){
        String cmd="DELETE FROM connector WHERE category_id = '"+categoryId+"';";
        try{
            UserActivity.database.execSQL(cmd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
        }
    }

    public List<Long> getCardIdList(Long categoryId){
        List<Long> idList = new ArrayList<>();
        String cmd="SELECT card_id FROM connector WHERE category_id = '"+categoryId+"';";
        Cursor cursor = UserActivity.database.rawQuery(cmd,null);
        while(cursor.moveToNext()){
            idList.add(cursor.getLong(0));
        }
        return idList;
    }
}
