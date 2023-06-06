package com.mobilproje.wordcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mobilproje.wordcard.dao.UserDao;
import com.mobilproje.wordcard.databinding.ActivityCreateUserBinding;
import com.mobilproje.wordcard.model.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserActivity extends AppCompatActivity {

    ActivityCreateUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_create_user);
        binding= ActivityCreateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void btnSaveUserClick(View view){
        addNewUserFromFormToDB();
    }

    @Override
    public void onBackPressed() {
        goToActivity(LoginUserActivity.class);
    }

    private void goToActivity(Class<?> activity){
        Intent intent=new Intent(CreateUserActivity.this,activity);
        //extra ekelme
        //intent.putExtra("kullaniciadi","value");
        finish();
        startActivity(intent);
    }

    private boolean formIsOkey(){
        int lenghtFirstName=binding.etFirstName.getText().length();
        int lenghtLastName=binding.etLastName.getText().length();
        int lenghtUserName=binding.etUsername.getText().length();
        int lenghtPassword=binding.etPassword.getText().length();
        int lenghtEmail=binding.etEmail.getText().length();

        if(lenghtFirstName < 3){
            Toast.makeText(getApplicationContext(),"İsminiz en az 3 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lenghtFirstName > 30){
            Toast.makeText(getApplicationContext(),"İsminiz en fazla 30 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lenghtLastName < 3){
            Toast.makeText(getApplicationContext(),"Soy isminiz en az 3 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lenghtLastName > 20){
            Toast.makeText(getApplicationContext(),"Soy isminiz en fazla 20 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lenghtUserName < 6){
            Toast.makeText(getApplicationContext(),"Kullanıcı adınız en az 6 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lenghtUserName > 16){
            Toast.makeText(getApplicationContext(),"Kullanıcı adınız en fazla 16 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        //hash password gelecek
        if(lenghtPassword < 6){
            Toast.makeText(getApplicationContext(),"Şifreniz en az 6 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lenghtPassword > 16){
            Toast.makeText(getApplicationContext(),"Şifreniz en fazla 16 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lenghtEmail < 10){
            Toast.makeText(getApplicationContext(),"Email en az 10 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lenghtEmail > 30){
            Toast.makeText(getApplicationContext(),"Email en fazla 30 karakter olabilir!",Toast.LENGTH_SHORT).show();
            return false;
        }
        //garip bir bu var true dınunce if e girmiyor false donunce giriyor...
        if(!emailFormatCorrect(binding.etEmail.getText().toString())){
            Toast.makeText(getApplicationContext(),"Geçerli bir email adresi girmelisiniz!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean emailFormatCorrect(String email){
        Matcher matcher = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email);
        return matcher.find();
    }

    private String getHash(String password){
        String hashed= BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("hashsiz sifre: "+password);
        System.out.println("hassli sifre: "+hashed);
        return hashed;
    }

    private void addNewUserFromFormToDB(){
        if(formIsOkey()){
            UserDao userDao=new UserDao();
            if(!userDao.userExists((binding.etUsername.getText().toString()))){
                User newUser=new User(binding.etFirstName.getText().toString()
                        ,binding.etLastName.getText().toString()
                        ,binding.etUsername.getText().toString()
                        ,binding.etPassword.getText().toString()
                        ,binding.etEmail.getText().toString());

//                String cmd="INSERT INTO user (firstname,lastname,username,password,email,correct_answer,wrong_answer) VALUES (";
//                cmd+="'"+binding.etFirstName.getText()+"',";
//                cmd+="'"+binding.etLastName.getText()+"',";
//                cmd+="'"+binding.etUsername.getText()+"',";
//                cmd+="'"+binding.etPassword.getText()+"',";
//                cmd+="'"+binding.etEmail.getText()+"',";
//                cmd+="0,0);";

                try {
                    newUser.setPassword(getHash(newUser.getPassword()));
                    if(userDao.add(newUser)){
                        Toast.makeText(getApplicationContext(),"Kayıt başarılı!",Toast.LENGTH_SHORT).show();
                        Thread.sleep(1000);
                        goToActivity(LoginUserActivity.class);
                    }else{
                        System.out.println("[-] User couldn't add to database!");
                        Toast.makeText(getApplicationContext(),"Sistem bakımda, daha sonra tekrar deneyin!",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Bu kullanıcı adı kullanılıyor!",Toast.LENGTH_SHORT).show();
                System.out.println("Bu kullanıcı adı kullanılıyor: "+binding.etUsername.getText().toString());
            }
        }
    }
}