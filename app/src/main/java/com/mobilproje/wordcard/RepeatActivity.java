package com.mobilproje.wordcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobilproje.wordcard.dao.CardDao;
import com.mobilproje.wordcard.dao.ConnectorDao;
import com.mobilproje.wordcard.dao.UserDao;
import com.mobilproje.wordcard.databinding.ActivityRepeatBinding;
import com.mobilproje.wordcard.model.Card;
import com.mobilproje.wordcard.model.User;

import java.util.List;

public class RepeatActivity extends AppCompatActivity {

    TextView tvQuestCount,tvCorrecntCount,tvWrongCount,tvTrWordRepeat;
    TextView tvTrUsage1Repeat,tvTrUsage2Repeat,tvTrUsage3Repeat,tvEngUsage1Repeat,tvEngUsage2Repeat,tvEngUsage3Repeat;
    EditText etEngWordRepeat;
    Button btnCheckAnswer;
    private int questIndex,totalQuest,correctAnswer,wrongAnswer;
    private List<Card> cards;
    private Card currentCard;

    private User user;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat);

        tvQuestCount= findViewById(R.id.tvQuestCount);
        tvCorrecntCount= findViewById(R.id.tvCorrecntCount);
        tvWrongCount= findViewById(R.id.tvWrongCount);
        tvTrWordRepeat= findViewById(R.id.tvTrWordRepeat);
        tvTrUsage1Repeat= findViewById(R.id.tvTrUsage1Repeat);
        tvTrUsage2Repeat= findViewById(R.id.tvTrUsage2Repeat);
        tvTrUsage3Repeat= findViewById(R.id.tvTrUsage3Repeat);
        tvEngUsage1Repeat= findViewById(R.id.tvEngUsage1Repeat);
        tvEngUsage2Repeat= findViewById(R.id.tvEngUsage2Repeat);
        tvEngUsage3Repeat= findViewById(R.id.tvEngUsage3Repeat);
        etEngWordRepeat= findViewById(R.id.etEngWordRepeat);
        btnCheckAnswer= findViewById(R.id.btnCheckAnswer);

        userDao=new UserDao();
        Intent externalIntent=getIntent();
        user=new User();
        user.setId(externalIntent.getLongExtra("id",0));
        user.setFirstName(externalIntent.getStringExtra("firstName"));
        user.setLastName(externalIntent.getStringExtra("lastName"));
        user.setUserName(externalIntent.getStringExtra("userName"));
        user.setPassword(externalIntent.getStringExtra("password"));
        user.setEmail(externalIntent.getStringExtra("email"));
        user.setCorrectAnswer(externalIntent.getIntExtra("correctAnswer",0));
        user.setWrongAnswer(externalIntent.getIntExtra("wrongAnswer",0));


        Long categoryId = externalIntent.getLongExtra("categoryId",0);
        List<Long> cardIdList = new ConnectorDao().getCardIdList(categoryId);
        cards = new CardDao().getFromCategory(cardIdList);
        questIndex=0;
        totalQuest=cards.size();
        correctAnswer=0;
        wrongAnswer=0;
        currentCard=cards.get(0);       //buna gerek olmamalı
        tvQuestCount.setText("Kalan Soru: "+(totalQuest-questIndex));
        tvCorrecntCount.setText("Doğru Cevap: 0");
        tvWrongCount.setText("Yanlış Cevap: 0");
        loadNextQuest();
    }


    private void loadNextQuest(){
        etEngWordRepeat.setText("");
        etEngWordRepeat.setTextColor(Color.BLACK);

        List<String> trUsages = currentCard.getTrUsage();
        for(int i = trUsages.size();i<3;i++){
            trUsages.add("");
        }
        tvTrUsage1Repeat.setText(trUsages.get(0));
        tvTrUsage2Repeat.setText(trUsages.get(1));
        tvTrUsage3Repeat.setText(trUsages.get(2));
        tvEngUsage1Repeat.setText("");
        tvEngUsage2Repeat.setText("");
        tvEngUsage3Repeat.setText("");
        tvTrWordRepeat.setText(currentCard.getTrWord());
    }

    public void clickCheckAnswer(View view){
        if(etEngWordRepeat.getText().toString().equals(currentCard.getEngWord())){
            correctAnswer++;
            etEngWordRepeat.setTextColor(Color.GREEN);
            user.setCorrectAnswer(user.getCorrectAnswer()+1);
            userDao.updateCorrectAnswer(user.getId(),user.getCorrectAnswer());
        }else{
            wrongAnswer++;
            etEngWordRepeat.setText(currentCard.getEngWord());
            etEngWordRepeat.setTextColor(Color.RED);
            user.setWrongAnswer(user.getWrongAnswer()+1);
            userDao.updateWrongAnswer(user.getId(),user.getWrongAnswer());
        }
        List<String> engUsages = currentCard.getEngUsage();
        for(int i = engUsages.size();i<3;i++){
            engUsages.add("");
        }
        tvQuestCount.setText("Kalan Soru: "+(totalQuest-questIndex-1));
        tvCorrecntCount.setText("Doğru Cevap: "+correctAnswer);
        tvWrongCount.setText("Yanlış Cevap: "+wrongAnswer);
        tvEngUsage1Repeat.setText(engUsages.get(0));
        tvEngUsage2Repeat.setText(engUsages.get(1));
        tvEngUsage3Repeat.setText(engUsages.get(2));

        if(questIndex+1==totalQuest){
            btnCheckAnswer.setBackgroundResource(R.drawable.btn_x_bg);
            btnCheckAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickExit(v);
                }
            });
            return;
        }

        btnCheckAnswer.setBackgroundResource(R.drawable.bg_go_next);
        btnCheckAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGoNextQuest(v);
            }
        });
    }

    public void clickGoNextQuest(View view){
        questIndex++;
        currentCard=cards.get(questIndex);
        btnCheckAnswer.setBackgroundResource(R.drawable.bg_quest);
        btnCheckAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCheckAnswer(v);
            }
        });
        loadNextQuest();
    }

    public void clickExit(View view){
        this.finish();
    }


}