package com.mobilproje.wordcard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private SQLiteDatabase database;
    List<String> commands;

    public DataBase(Context context) {
        database = context.openOrCreateDatabase("WordCardDB", Context.MODE_PRIVATE,null);
        //executeCommands();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }


    private void executeCommands(){
        commands = new ArrayList<>();
        deleteTables();
        createTables();
        addDemos();
        for(String cmd:commands){
            try{
                database.execSQL(cmd);
                System.out.println("[+] SQL Command success: "+cmd);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("[-] SQL Command couldn't run: "+cmd);
            }
        }
    }

    private void deleteTables(){
        commands.add("DROP TABLE IF EXISTS user;");
        commands.add("DROP TABLE IF EXISTS category;");
        commands.add("DROP TABLE IF EXISTS word;");
        commands.add("DROP TABLE IF EXISTS usage;");
        commands.add("DROP TABLE IF EXISTS card;");
        commands.add("DROP TABLE IF EXISTS connector;");
    }

    private void createTables(){
        commands.add("CREATE TABLE IF NOT EXISTS user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "firstname VARCHAR(30) NOT NULL," +
                "lastname VARCHAR(20) NOT NULL," +
                "username VARCHAR(16) NOT NULL," +
                "password VARCHAR(16) NOT NULL," +
                "email VARCHAR(30) NOT NULL," +
                "correct_answer INT NOT NULL," +
                "wrong_answer INT NOT NULL);");
        commands.add("CREATE TABLE IF NOT EXISTS category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "name VARCHAR(16) NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES user(id));");
        commands.add("CREATE TABLE IF NOT EXISTS word(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "value VARCHAR(30) NOT NULL);");
        commands.add("CREATE TABLE IF NOT EXISTS usage(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "word_id INTEGER NOT NULL," +
                "sentence VARCHAR(120) NOT NULL," +
                "FOREIGN KEY (word_id) REFERENCES word(id));");
        commands.add("CREATE TABLE IF NOT EXISTS card(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "tr_word_id INTEGER NOT NULL," +
                "eng_word_id INTEGER NOT NULL," +
                "correct_answer INT NOT NULL," +
                "wrong_answer INT NOT NULL," +
                "last_usage_time INTEGER NOT NULL," +
                "added_time INTEGER NOT NULL," +
                "FOREIGN KEY (tr_word_id) REFERENCES word(id)," +
                "FOREIGN KEY (eng_word_id) REFERENCES word(id));");
        commands.add("CREATE TABLE IF NOT EXISTS connector (" +
                "category_id INTEGER NOT NULL," +
                "card_id INTEGER NOT NULL," +
                "PRIMARY KEY (category_id, card_id)," +
                "FOREIGN KEY (category_id) REFERENCES category(id)," +
                "FOREIGN KEY (card_id) REFERENCES card(id));");
    }

    private void addDemos(){
        //kullanıcılar NOT şifreler HASH li olduğu için problem!!!
//        commands.add("INSERT INTO user " +
//                "(firstname,lastname,username,password,email,correct_answer,wrong_answer) " +
//                "VALUES ('Gokhan','Gunesoglu','gokhan123','123qweqwe','gokhangunesoglu@gmail.com',0,0);");
//        commands.add("INSERT INTO user " +
//                "(firstname,lastname,username,password,email,correct_answer,wrong_answer) " +
//                "VALUES ('Emre','Tantu','emre123','123qweqwe','emretantu@gmail.com',0,0);");

        //kategoriler
        commands.add("INSERT INTO category (user_id, name) VALUES (1, 'Science'),(2, 'History');");
        //kelimeler
        commands.add("INSERT INTO word (value) VALUES ('apple'),('elma'),('car'),('araba'),('book'),('kitap');");
        //kullanımlar
        commands.add("INSERT INTO usage (word_id, sentence) VALUES (1, 'I ate an apple today.')," +
                "(1, 'I like to eat apple.'),(2, 'Kırmızı elma tatlıdır.'),(2, 'Yeşil elma ekşidir.')," +
                "(3, 'He drives a car.'),(3, 'She cant drive car.'),(4, 'Hiç araba sürmedim.')," +
                "(4, 'Petrol zamları arabası olanları üzdü.'),(5, 'She is reading a book.')," +
                "(5, 'My sister likes reading book.'),(6, 'En sevdiğim kitap Mavi Tüy.')," +
                "(6, 'Kitaplar bir gün tarih olacak.');");
        //kartlar
        commands.add("INSERT INTO card " +
                "(user_id, tr_word_id, eng_word_id, correct_answer, wrong_answer, last_usage_time, added_time)" +
                " VALUES (1, 2, 1, 0, 0, 1622721600, 1622635200),(1, 4, 3, 0, 0, 1622817600, 1622702400)," +
                "(1, 6, 5, 0, 0, 1622817600, 1622702400);");
        //bağlayıcı
        commands.add("INSERT INTO connector (category_id, card_id) VALUES (1, 1),(2, 2),(1, 3);");
    }




}
