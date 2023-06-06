package com.mobilproje.wordcard.dao;

import android.database.Cursor;
import com.mobilproje.wordcard.UserActivity;
import com.mobilproje.wordcard.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public UserDao() {
    }

    public boolean add(User user){
        String cmd="INSERT INTO user (firstname,lastname,username,password,email,correct_answer,wrong_answer) VALUES (";
        cmd+="'"+user.getFirstName()+"',";
        cmd+="'"+user.getLastName()+"',";
        cmd+="'"+user.getUserName()+"',";
        cmd+="'"+user.getPassword()+"',";
        cmd+="'"+user.getEmail()+"',";
        cmd+="0,0);";
        try{
            UserActivity.database.execSQL(cmd);
            return  true;

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
            return false;
        }
    }

    public User find(String userName,String password){
        String cmd="SELECT * FROM user WHERE username='"+userName+"' AND password='"+password+"';";
        Cursor cursor = UserActivity.database.rawQuery(cmd,null);
        if(cursor.getCount()==1){
            cursor.moveToNext();
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setFirstName(cursor.getString(1));
            user.setLastName(cursor.getString(2));
            user.setUserName(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setEmail(cursor.getString(5));
            user.setCorrectAnswer(cursor.getInt(6));
            user.setWrongAnswer(cursor.getInt(7));
            return user;
        }if(cursor.getCount()==0){
            return null;
        }
        System.out.println("[-] Dublicated Row In user table!");
        System.out.println("[-] Dublicated username: "+userName);
        return null;
    }

    public User find(String userName){
        String cmd="SELECT * FROM user WHERE username='"+userName+"';";
        Cursor cursor = UserActivity.database.rawQuery(cmd,null);
        if(cursor.getCount()==1){
            cursor.moveToNext();
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setFirstName(cursor.getString(1));
            user.setLastName(cursor.getString(2));
            user.setUserName(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setEmail(cursor.getString(5));
            user.setCorrectAnswer(cursor.getInt(6));
            user.setWrongAnswer(cursor.getInt(7));
            return user;
        }if(cursor.getCount()==0){
            return null;
        }
        System.out.println("[-] Dublicated Row In user table!");
        System.out.println("[-] Dublicated username: "+userName);
        return null;
    }

    public boolean userExists(String userName){
        String cmd="SELECT * FROM user WHERE username = '"+userName+"';";
        Cursor cursor = UserActivity.database.rawQuery(cmd, null);
        if(cursor.getCount() == 0){
            return false;
        }
        return true;
    }

    public List<User> getAll(){
        List<User> users=new ArrayList<>();
        String cmd="SELECT * FROM user;";
        Cursor cursor = UserActivity.database.rawQuery(cmd,null);
        User user;
        while(cursor.moveToNext()){
            user = new User();
            user.setId(cursor.getLong(0));
            user.setFirstName(cursor.getString(1));
            user.setLastName(cursor.getString(2));
            user.setUserName(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setEmail(cursor.getString(5));
            user.setCorrectAnswer(cursor.getInt(6));
            user.setWrongAnswer(cursor.getInt(7));
            users.add(user);
        }
        return users;
    }

    public void updateWrongAnswer(Long userId, int wrongAnswer){
        String cmd = "UPDATE user SET wrong_answer = "+wrongAnswer+" WHERE id = "+userId+";";
        try{
            UserActivity.database.execSQL(cmd);
            System.out.println("[+] SQL Command Ruined: "+cmd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
        }
    }

    public void updateCorrectAnswer(Long userId, int correctAnswer){
        String cmd = "UPDATE user SET correct_answer = "+correctAnswer+" WHERE id = "+userId+";";
        try{
            UserActivity.database.execSQL(cmd);
            System.out.println("[+] SQL Command Ruined: "+cmd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[-] SQL Command couldn't run: "+cmd);
        }
    }

}
