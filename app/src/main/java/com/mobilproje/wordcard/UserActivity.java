package com.mobilproje.wordcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilproje.wordcard.adaptor.CardAdaptor;
import com.mobilproje.wordcard.adaptor.CategoryAdapter;
import com.mobilproje.wordcard.adaptor.PlayAdaptor;
import com.mobilproje.wordcard.dao.CardDao;
import com.mobilproje.wordcard.dao.CategoryDao;
import com.mobilproje.wordcard.dao.UserDao;
import com.mobilproje.wordcard.databinding.ActivityUserBinding;
import com.mobilproje.wordcard.model.Card;
import com.mobilproje.wordcard.model.Category;
import com.mobilproje.wordcard.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    public static SQLiteDatabase database;
    public static SharedPreferences preferences;
    private ActivityUserBinding binding;

    private CategoryAdapter categoryAdapter;
    private CardAdaptor cardAdaptor;

    private RecyclerView recyclerView,recyclerViewCard,recyclerViewRepeat;

    Context context;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new DataBase(this).getDatabase();
        //ilk baş preferences daki kullanıcı kontrol edilmeli
        if(preferences==null){
            preferences =this.getSharedPreferences("com.mobilproje.wordcard", Context.MODE_PRIVATE);
        }
        String userName=preferences.getString("userName","");
        String password=preferences.getString("password","");
        System.out.println(userName + " | "+password);
        //preferences boş ise
        if(userName.equals("")||password.equals("")){
            Intent externalIntent=getIntent();
            user=new User();
            user.setId(externalIntent.getLongExtra("id",0));
            //login yapılmadıysa
            if(user.getId()==0){
                Intent intent=new Intent(this,LoginUserActivity.class);
                this.finish();
                startActivity(intent);
            }
            //login den save lemeden gelindiğinde
            else{
                user.setFirstName(externalIntent.getStringExtra("firstName"));
                user.setLastName(externalIntent.getStringExtra("lastName"));
                user.setUserName(externalIntent.getStringExtra("userName"));
                user.setPassword(externalIntent.getStringExtra("password"));
                user.setEmail(externalIntent.getStringExtra("email"));
                user.setCorrectAnswer(externalIntent.getIntExtra("correctAnswer",0));
                user.setWrongAnswer(externalIntent.getIntExtra("wrongAnswer",0));
            }
        }
        //preferences doluysa oradan user ı çek
        else{
            user=new UserDao().find(userName);
        }

        binding= com.mobilproje.wordcard.databinding.ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=this;
    }

    public void clickLogout(View view){
        logout();
        Intent intent=new Intent(this,LoginUserActivity.class);
        this.finish();
        startActivity(intent);
    }

    public void clickStatistic(View view){
        showStatistic();
    }

    public void clickCategory(View view){
        showCategories();
    }

    public void clickCard(View view){
        showCards();
    }

    public void clickRepeat(View view){
        showRepeat();
    }
    private void showStatistic(){
        user=new UserDao().find(user.getUserName());
        Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(R.layout.dialog_statistic);
        TextView tvExit = dialog.findViewById(R.id.tvExitStatistic);
        TextView tvCorrect = dialog.findViewById(R.id.tvCorrectAnswer);
        TextView tvWrong = dialog.findViewById(R.id.tvWrongAnswer);
        TextView tvTotal = dialog.findViewById(R.id.tvTotalAnswer);
        tvCorrect.setText(""+user.getCorrectAnswer());
        tvWrong.setText(""+user.getWrongAnswer());
        tvTotal.setText(""+(user.getCorrectAnswer()+user.getWrongAnswer()));
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setAttributes(params);
        dialog.show();
    }

    private void showCategories(){
        Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(R.layout.dialog_category);
        Button btnNewCategory = dialog.findViewById(R.id.btnNewCategory);
        Button btnConfigureCategories = dialog.findViewById(R.id.btnConfigureCategories);
        TextView tvExit = dialog.findViewById(R.id.tvExitCategory);

        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addCategory();
            }
        });
        btnConfigureCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                configureCategories();
            }
        });

        dialog.getWindow().setAttributes(params);
        dialog.show();
    }

    private void addCategory(){
        Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_category_create);
        TextView tvExit = dialog.findViewById(R.id.tvExitCategoryCreate);
        TextView tvSave = dialog.findViewById(R.id.tvSave);
        EditText etName = dialog.findViewById(R.id.etCategoryName);

        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().length()>16){
                    Toast.makeText(getApplicationContext(),"Kategori adı en fazla 16 karakter olmalı!",Toast.LENGTH_SHORT).show();
                }else if(etName.getText().length()<5){
                    Toast.makeText(getApplicationContext(),"Kategori adı en az 5 karakter olmalı!",Toast.LENGTH_SHORT).show();
                }else{
                    Category category=new Category();
                    category.setUserId(user.getId());
                    category.setName(etName.getText().toString());
                    if(new CategoryDao().add(category)){
                        Toast.makeText(getApplicationContext(),"Kategori eklendi!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getApplicationContext(),"Kategori eklenemedi!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.getWindow().setAttributes(params);
        dialog.show();
    }
    private void configureCategories(){
        Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.category_list);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        recyclerView = dialog.findViewById(R.id.rvCategories);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        listCategories();

        dialog.getWindow().setAttributes(params);
        dialog.show();
    }

    private void listCategories(){
        List<Category> categories = new CategoryDao().getAll(user.getId());

        categoryAdapter = new CategoryAdapter(categories,this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                categories.remove(position);
                categoryAdapter.notifyItemRemoved(position);
            }
        });
    }

    private void showCards(){
        Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(R.layout.dialog_card);
        Button btnNewCard = dialog.findViewById(R.id.btnNewCard);
        Button btnConfigureCards = dialog.findViewById(R.id.btnConfigureCards);
        TextView tvExit = dialog.findViewById(R.id.tvExitCard);

        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNewCard();
            }
        });
        btnConfigureCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureCards();
            }
        });

        dialog.getWindow().setAttributes(params);
        dialog.show();
    }

    private void showRepeat(){
        Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(R.layout.play_list);
        Button btnClose = dialog.findViewById(R.id.btnCloseCardList);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        recyclerViewRepeat = dialog.findViewById(R.id.rvPlayList);
        List<Category> categories = new CategoryDao().getAll(user.getId());
        PlayAdaptor adaptor = new PlayAdaptor(categories,this);
        recyclerViewRepeat.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewRepeat.setLayoutManager(manager);
        recyclerViewRepeat.setAdapter(adaptor);
        adaptor.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Long categoryId=categories.get(position).getId();
                if(new CategoryDao().getCardCount(categoryId)==0){
                    Toast.makeText(getApplicationContext(),"Kategoriye kart eklemelisiniz!",Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                startRepeat(categoryId);
            }
        });

        dialog.getWindow().setAttributes(params);
        dialog.show();
    }


    private void openDialogNewCard(){
        Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_new_card);
        Button btnClose = dialog.findViewById(R.id.btnCloseDialogNewCard);
        EditText etTrWord = dialog.findViewById(R.id.etTrWord);
        EditText etTrUsage1 = dialog.findViewById(R.id.etTrUsage1);
        EditText etTrUsage2 = dialog.findViewById(R.id.etTrUsage2);
        EditText etTrUsage3 = dialog.findViewById(R.id.etTrUsage3);
        EditText etEngWord = dialog.findViewById(R.id.etEngWord);
        EditText etEngUsage1 = dialog.findViewById(R.id.etEngUsage1);
        EditText etEngUsage2 = dialog.findViewById(R.id.etEngUsage2);
        EditText etEngUsage3 = dialog.findViewById(R.id.etEngUsage3);
        TextView tvSaveCard = dialog.findViewById(R.id.tvSaveCardDialogNewCard);
        TextView tvExit = dialog.findViewById(R.id.tvExitDialogNewCard);
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
        tvSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kontrol et true ise kaydet
                String trWord,engWord,trUsage1,trUsage2,trUsage3,engUsage1,engUsage2,engUsage3;
                trWord = etTrWord.getText().toString();
                engWord = etEngWord.getText().toString();
                trUsage1 = etTrUsage1.getText().toString();
                trUsage2 = etTrUsage2.getText().toString();
                trUsage3 = etTrUsage3.getText().toString();
                engUsage1 = etEngUsage1.getText().toString();
                engUsage2 = etEngUsage2.getText().toString();
                engUsage3 = etEngUsage3.getText().toString();

                List<String> trUsages = new ArrayList<>();
                List<String> engUsages = new ArrayList<>();

                trUsages.add(trUsage1);
                trUsages.add(trUsage2);
                trUsages.add(trUsage3);
                engUsages.add(engUsage1);
                engUsages.add(engUsage2);
                engUsages.add(engUsage3);

                Card card = new Card();
                card.setUserId(user.getId());
                card.setTrWord(trWord);
                card.setTrUsage(trUsages);
                card.setEngWord(engWord);
                card.setEngUsage(engUsages);
                card.setCorrectAnswer(0);
                card.setWrongAnswer(0);
                card.setAdded(new Timestamp(System.currentTimeMillis()));
                card.setLastUsage(new Timestamp(System.currentTimeMillis()));

                CardDao cardDao=new CardDao();
                cardDao.save(card);
                Toast.makeText(getApplicationContext(),"Kart Kaydedildi!",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.getWindow().setAttributes(params);
        dialog.show();
    }




    private void configureCards(){
        Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams params= new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.card_list);
        Button btnClose = dialog.findViewById(R.id.btnCloseCardList);
        recyclerViewCard = dialog.findViewById(R.id.rvCardList);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        listCards();

        dialog.getWindow().setAttributes(params);
        dialog.show();
    }


    private void listCards(){
        List<Card> cards = new CardDao().getAll(user.getId());

        cardAdaptor = new CardAdaptor(cards,this);
        recyclerViewCard.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewCard.setLayoutManager(manager);
        recyclerViewCard.setAdapter(cardAdaptor);

        cardAdaptor.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CardDao cardDao=new CardDao();
                cardDao.delete(cards.get(position));
                cards.remove(position);
                cardAdaptor.notifyItemRemoved(position);
            }
        });
    }


    private void logout(){
        preferences.edit().clear().apply();
    }

    private void startRepeat(Long categoryId){
        Intent intent = new Intent(this,RepeatActivity.class);
        intent.putExtra("categoryId",categoryId);
        intent.putExtra("id",user.getId());
        intent.putExtra("firstName",user.getFirstName());
        intent.putExtra("lastName",user.getLastName());
        intent.putExtra("userName",user.getUserName());
        intent.putExtra("password",user.getPassword());
        intent.putExtra("email",user.getEmail());
        intent.putExtra("correctAnswer",user.getCorrectAnswer());
        intent.putExtra("wrongAnswer",user.getWrongAnswer());
        startActivity(intent);
        System.out.println(user.toString());
    }
}