package com.example.job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    EditText username,password,repassword,phone;
    Button register;
    String extra;
    TextView seekerOrNot;
    String saveCurrentDate,saveCurrentTime,userRandomKey;
    String parentDBName="Seekers";
    Boolean is_seeker = true;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        register = findViewById(R.id.register);
        seekerOrNot = findViewById(R.id.tv_seekerOrNot);
        phone = findViewById(R.id.phone);
        loadingBar = new ProgressDialog(this);

        extra = getIntent().getStringExtra("type");

        seekerOrNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_seeker){
                    register.setText("LOGIN JOB CREATOR");
                    seekerOrNot.setText("I am a Job Seeker");
                    parentDBName="Creators";
                    is_seeker = false;
                }else{
                    register.setText("LOGIN JOB SEEKER");
                    seekerOrNot.setText("I am a Job Creator");
                    parentDBName="Seekers";
                    is_seeker=true;
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,pass,repass,mobile;

                name = username.getText().toString();
                pass = password.getText().toString().trim();
                repass = password.getText().toString().trim();
                mobile = phone.getText().toString().trim();

                if(name.isEmpty()){
                    username.setError("Please Enter your name");
                    return;
                }

                if(mobile.isEmpty()){
                    phone.setError("Please Enter your phone Number");
                    return;
                }

                if(pass.isEmpty() ){
                    password.setError("Please Enter your password");
                    return;
                }
                Log.i("test","test");

                if(pass.length() < 4){
                    password.setError("Password must be at eight letters");
                    return;
                }
                Log.i("test","test1");
                if(repass.isEmpty()){
                    repassword.setError("Please enter your password");
                    return;
                }


                if(!pass.equals(repass)){
                    repassword.setError("Please confirm your password");
                    return;
                }

                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please wait, while we are checking credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                validateEmail(name,pass,mobile);
            }
        });
    }

    private void validateEmail(String name, String pass, String mobile) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("Seekers").child(mobile).exists()){
                    if(!snapshot.child("Seekers").child(mobile).exists()){
                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat currentDate = new SimpleDateFormat("MM,dd,yyyy");
                        saveCurrentDate = currentDate.format(calendar.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        saveCurrentTime = currentTime.format(calendar.getTime());

                        userRandomKey = saveCurrentDate+saveCurrentTime+mobile;

                        HashMap<String, Object> userDataMap = new HashMap<>();

                        userDataMap.put("name",name);
                        userDataMap.put("phone",mobile);
                        userDataMap.put("password",pass);
                        userDataMap.put("user_id",userRandomKey);

                        RootRef.child(parentDBName).child(mobile).updateChildren(userDataMap).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SignIn.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                            if(parentDBName.equals("Seekers")){
                                                Intent intent = new Intent(SignIn.this, HomeActivity.class);
                                                startActivity(intent);
                                            }else if(parentDBName.equals("Creators")){
                                                Intent intent = new Intent(SignIn.this, JobCreatorsHome.class);
                                                startActivity(intent);
                                            }

                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(SignIn.this, "Exist Email in Creators", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }


                }else {
                    Toast.makeText(SignIn.this, "Exists Email in Seekers", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}