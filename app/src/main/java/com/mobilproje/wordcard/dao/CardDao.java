package com.mobilproje.wordcard.dao;

import android.database.Cursor;

import com.mobilproje.wordcard.UserActivity;
import com.mobilproje.wordcard.model.Card;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CardDao {

    public CardDao() {
    }

    public List<Card> getAll(Long userId){
        List<Card> cards=new ArrayList<>();

        String cmd="SELECT * FROM card WHERE user_id = '"+userId+"';";
        Cursor cursor = UserActivity.database.rawQuery(cmd,null);
        Cursor cur;
        Card card;
        while(cursor.moveToNext()){
            card = new Card();
            card.setId(cursor.getLong(0));
            card.setUserId(cursor.getLong(1));
            card.setTrWordId(cursor.getLong(2));
            card.setEngWordId(cursor.getLong(3));
            card.setCorrectAnswer(cursor.getInt(4));
            card.setWrongAnswer(cursor.getInt(5));
            card.setLastUsage(new Timestamp(cursor.getLong(6)));
            card.setAdded(new Timestamp(cursor.getLong(7)));

            cmd="SELECT * FROM word WHERE id = '"+card.getTrWordId()+"';";
            cur = UserActivity.database.rawQuery(cmd,null);
            if(cur.moveToNext()){
                card.setTrWord(cur.getString(1));
            }
            cmd="SELECT * FROM word WHERE id = '"+card.getEngWordId()+"';";
            cur = UserActivity.database.rawQuery(cmd,null);
            if(cur.moveToNext()){
                card.setEngWord(cur.getString(1));
            }

            cmd="SELECT * FROM usage WHERE word_id = '"+card.getTrWordId()+"';";
            cur = UserActivity.database.rawQuery(cmd,null);
            List<String> usagesTr=new ArrayList<>();
            while(cur.moveToNext()){
                usagesTr.add(cur.getString(2));
            }
            card.setTrUsage(usagesTr);
            List<String> usagesEng=new ArrayList<>();
            cmd="SELECT * FROM usage WHERE word_id = '"+card.getEngWordId()+"';";
            cur = UserActivity.database.rawQuery(cmd,null);
            while(cur.moveToNext()){
                usagesEng.add(cur.getString(2));
            }
            card.setEngUsage(usagesEng);
            cards.add(card);
        }
        return cards;
    }

    public List<Card> getFromCategory(List<Long> cardIdList){
        List<Card> cards=new ArrayList<>();
        for(Long cardId:cardIdList){
            String cmd="SELECT * FROM card WHERE id = '"+cardId+"';";
            Cursor cursor = UserActivity.database.rawQuery(cmd,null);
            Cursor cur;
            Card card;
            if(cursor.moveToNext()){
                card = new Card();
                card.setId(cursor.getLong(0));
                card.setUserId(cursor.getLong(1));
                card.setTrWordId(cursor.getLong(2));
                card.setEngWordId(cursor.getLong(3));
                card.setCorrectAnswer(cursor.getInt(4));
                card.setWrongAnswer(cursor.getInt(5));
                card.setLastUsage(new Timestamp(cursor.getLong(6)));
                card.setAdded(new Timestamp(cursor.getLong(7)));

                cmd="SELECT * FROM word WHERE id = '"+card.getTrWordId()+"';";
                cur = UserActivity.database.rawQuery(cmd,null);
                if(cur.moveToNext()){
                    card.setTrWord(cur.getString(1));
                }
                cmd="SELECT * FROM word WHERE id = '"+card.getEngWordId()+"';";
                cur = UserActivity.database.rawQuery(cmd,null);
                if(cur.moveToNext()){
                    card.setEngWord(cur.getString(1));
                }

                cmd="SELECT * FROM usage WHERE word_id = '"+card.getTrWordId()+"';";
                cur = UserActivity.database.rawQuery(cmd,null);
                List<String> usagesTr=new ArrayList<>();
                while(cur.moveToNext()){
                    usagesTr.add(cur.getString(2));
                }
                card.setTrUsage(usagesTr);
                List<String> usagesEng=new ArrayList<>();
                cmd="SELECT * FROM usage WHERE word_id = '"+card.getEngWordId()+"';";
                cur = UserActivity.database.rawQuery(cmd,null);
                while(cur.moveToNext()){
                    usagesEng.add(cur.getString(2));
                }
                card.setEngUsage(usagesEng);
                cards.add(card);
            }
        }
        return cards;
    }

    public void save(Card card){
        String cmd;
        Cursor cursor;
        cmd="INSERT INTO word (value) VALUES ('"+card.getTrWord()+"');";
        UserActivity.database.execSQL(cmd);
        cmd="INSERT INTO word (value) VALUES ('"+card.getEngWord()+"');";
        UserActivity.database.execSQL(cmd);

        cmd="SELECT id FROM word WHERE value = '"+card.getTrWord()+"'";
        cursor = UserActivity.database.rawQuery(cmd,null);
        if(cursor.moveToNext()){
            card.setTrWordId(cursor.getLong(0));
        }
        cmd="SELECT id FROM word WHERE value = '"+card.getEngWord()+"'";
        cursor = UserActivity.database.rawQuery(cmd,null);
        if(cursor.moveToNext()){
            card.setEngWordId(cursor.getLong(0));
        }
        cmd="INSERT INTO card " +
                "(user_id, tr_word_id, eng_word_id, correct_answer, wrong_answer, last_usage_time, added_time) " +
                "VALUES ("+card.getUserId()+", "+card.getTrWordId()+", "+card.getEngWordId()+
                ", "+card.getCorrectAnswer()+", "+card.getWrongAnswer()+", "+card.getLastUsage().getTime()+
                ", "+card.getAdded().getTime()+");";
        System.out.println("Command: "+cmd);
        UserActivity.database.execSQL(cmd);

        for(String usage:card.getTrUsage()){
            cmd="INSERT INTO usage (word_id, sentence) VALUES ("+card.getTrWordId()+", '"+usage+"');";
            UserActivity.database.execSQL(cmd);
        }
        for(String usage:card.getEngUsage()){
            cmd="INSERT INTO usage (word_id, sentence) VALUES ("+card.getEngWordId()+", '"+usage+"');";
            UserActivity.database.execSQL(cmd);
        }
    }

    public void delete(Card card){
        String cmd;
        cmd="DELETE FROM word WHERE id='"+card.getTrWordId()+"';";
        try{
            UserActivity.database.execSQL(cmd);
            System.out.println("[+] SQL Command ruined: "+cmd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
        }
        cmd="DELETE FROM word WHERE id='"+card.getEngWordId()+"';";
        try{
            UserActivity.database.execSQL(cmd);
            System.out.println("[+] SQL Command ruined: "+cmd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
        }
        cmd="DELETE FROM usage WHERE word_id='"+card.getTrWordId()+"';";
        try{
            UserActivity.database.execSQL(cmd);
            System.out.println("[+] SQL Command ruined: "+cmd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
        }
        cmd="DELETE FROM usage WHERE word_id='"+card.getEngWord()+"';";
        try{
            UserActivity.database.execSQL(cmd);
            System.out.println("[+] SQL Command ruined: "+cmd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
        }
        cmd="DELETE FROM card WHERE id='"+card.getId()+"';";
        try{
            UserActivity.database.execSQL(cmd);
            System.out.println("[+] SQL Command ruined: "+cmd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
        }
    }

}
