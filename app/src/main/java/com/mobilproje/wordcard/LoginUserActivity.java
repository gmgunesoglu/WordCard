package com.mobilproje.wordcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mobilproje.wordcard.dao.UserDao;
import com.mobilproje.wordcard.databinding.ActivityLoginUserBinding;
import com.mobilproje.wordcard.model.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class LoginUserActivity extends AppCompatActivity {

    ActivityLoginUserBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void btnLoginClick(View view){
        String userName=binding.etUserName.getText().toString();
        String password=binding.etPassword.getText().toString();
        if(checkLoginUser(userName,password)){
            User user=new UserDao().find(userName);
            //kullanıcıyı kontrol et
            if(user==null){
                Toast.makeText(getApplicationContext(),"Kullanıcı adı veya şifre hatalı!",Toast.LENGTH_SHORT).show();
                //bunu sonra sil
                printAllUsers();
            }else{
                //şifreyi kontrol et
                if(checkHash(password,user.getPassword())){
                    System.out.println(user.toString());
                    Intent intent=new Intent(this,UserActivity.class);
                    if(binding.cbSave.isChecked()){
                        UserActivity.preferences.edit().putString("userName",userName).apply();
                        UserActivity.preferences.edit().putString("password",password).apply();
                    }
                    //save lemeden login yapılıyorsa ...
                    else{
                        intent.putExtra("id",user.getId());
                        intent.putExtra("firstName",user.getFirstName());
                        intent.putExtra("lastName",user.getLastName());
                        intent.putExtra("userName",user.getUserName());
                        intent.putExtra("password",user.getPassword());
                        intent.putExtra("email",user.getEmail());
                        intent.putExtra("correctAnswer",user.getCorrectAnswer());
                        intent.putExtra("wrongAnswer",user.getWrongAnswer());
                    }
                    this.finish();
                    startActivity(intent);
                }else{
                    //şifre veya kullanıcı adı hatalı, ipucu verme
                    Toast.makeText(getApplicationContext(),"Kullanıcı adı veya şifre hatalı!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void btnCreateUserClick(View view){
        goToActivity(CreateUserActivity.class);
    }

    private boolean checkLoginUser(String userName, String password){
        if(userName.length()<6){
            Toast.makeText(getApplicationContext(),"Kullanıcı en az 6 karakter olmalı!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(userName.length()>16){
            Toast.makeText(getApplicationContext(),"Kullanıcı en fazla 16 karakter olmalı!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length()<6){
            Toast.makeText(getApplicationContext(),"Şifre en az 6 karakter olmalı!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length()>16){
            Toast.makeText(getApplicationContext(),"Şifre en fazla 16 karakter olmalı!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void printAllUsers(){
        List<User> users=new UserDao().getAll();
        for(User user:users){
            System.out.println(user.toString());
        }
    }

    private void goToActivity(Class<?> activity){
        Intent intent=new Intent(this,activity);
        //extra ekelme
        //intent.putExtra("kullaniciadi","value");
        this.finish();
        startActivity(intent);
    }

    private boolean checkHash(String pure,String hashed){
        return BCrypt.checkpw(pure, hashed);
    }
}