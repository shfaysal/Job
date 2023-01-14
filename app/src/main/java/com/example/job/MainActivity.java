package com.example.job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.job.model.Prevelant;
import com.example.job.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import java.util.HashMap;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputPhone,inputPassword;
    Button login;
    String extra;
    TextView gotoRegister,seekerOrCreator;
    CheckBox cb_remember;
    String parentDBName = "Seekers";
    Boolean is_job_seeker = true;

    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPhone = findViewById(R.id.inputPhone);
        inputPassword = findViewById(R.id.inputPassword);
        gotoRegister = findViewById(R.id.gotoRegister);
        cb_remember = (CheckBox) findViewById(R.id.cb_remember);
        loadingBar = new ProgressDialog(this);
        seekerOrCreator = findViewById(R.id.job_seekerOrNot);
        Paper.init(this);


        login = findViewById(R.id.btnLogin);

        extra = getIntent().getStringExtra("type");
        //Toast.makeText(MainActivity.this, extra.toString(), Toast.LENGTH_SHORT).show();
        login.setOnClickListener(this);
        gotoRegister.setOnClickListener(this);
        seekerOrCreator.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                login();
                break;
            case R.id.gotoRegister:
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                intent.putExtra("type",extra);
                startActivity(intent);
                break;
            case R.id.job_seekerOrNot:
                if(is_job_seeker){
                    login.setText("LOGIN JOB CREATOR");
                    seekerOrCreator.setText("I am a Job Seeker");
                    parentDBName="Creators";
                    is_job_seeker = false;
                }else{
                    login.setText("LOGIN JOB SEEKER");
                    seekerOrCreator.setText("I am a Job Creator");
                    parentDBName="Seekers";
                    is_job_seeker=true;
                }
        }
    }

    void login(){
        String mobile,password;

        mobile = inputPhone.getText().toString().trim();
        password = inputPassword.getText().toString();

        if(mobile.isEmpty()){
            inputPhone.setError("Please Enter Your Phone number");
            return;
        }

        if(password.isEmpty() ){
            inputPassword.setError("Please Enter your name");
            return;
        }

        Log.i("test","test");

        if(password.length() < 4){
            inputPassword.setError("Password must be at eight letters");
            return;
        }
        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Please wait, while we are checking the creadentials");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if(cb_remember.isChecked()){
            Paper.book().write(Prevelant.UserPhoneKey, mobile);
            Paper.book().write(Prevelant.UserPasswordKey, password);
        }

        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDBName).child(mobile).exists()){
                    Users userData = snapshot.child(parentDBName).child(mobile).getValue(Users.class);
                    if(userData.getPhone().equals(mobile)){
                        if(userData.getPassword().equals(password)){
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("username",userData.getName());
                            hashMap.put("UserID",userData.getUser_id());
                            if(parentDBName.equals("Seekers")){
                                loadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra("map",hashMap);
                                startActivity(intent);
                            }else if (parentDBName.equals("Creators")){
                                loadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this, JobCreatorsHome.class);
                                Paper.book().write("UserIdentification",userData.getUser_id());
                                startActivity(intent);
                            }
                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Phone Number does not Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}