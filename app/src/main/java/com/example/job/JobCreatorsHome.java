package com.example.job;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JobCreatorsHome extends AppCompatActivity {

    Button post,applicantlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_creators_home);

        post = findViewById(R.id.postjob);
        applicantlist = findViewById(R.id.applicantlist);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobCreatorsHome.this,Post.class);
                startActivity(intent);
            }
        });

        applicantlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobCreatorsHome.this, ApplicantList.class);
                startActivity(intent);
            }
        });
    }
}