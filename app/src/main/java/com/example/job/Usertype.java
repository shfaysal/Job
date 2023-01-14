package com.example.job;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Usertype extends AppCompatActivity implements View.OnClickListener{

    Button creator,seeker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);

        creator = findViewById(R.id.btn_creator);
        seeker = findViewById(R.id.btn_jobseeker);

        creator.setOnClickListener(this);
        seeker.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_creator:
                Intent intent = new Intent(Usertype.this, MainActivity.class);
                intent.putExtra("type","creator");
                startActivity(intent);
                break;
            case R.id.btn_jobseeker:
                Intent intent1 = new Intent(Usertype.this, MainActivity.class);
                intent1.putExtra("type","seeker");
                startActivity(intent1);
                break;
        }
    }
}