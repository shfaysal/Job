package com.example.job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.job.model.Apply;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ApplicantList extends AppCompatActivity {
    ListView mypdfListView;
    DatabaseReference databaseReference;
    List<Apply> applicantpdfs;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_list);

        mypdfListView = findViewById(R.id.listview);
        applicantpdfs = new ArrayList<>();

        id = Paper.book().read("UserIdentification");
        Log.i("id", id);

        viewAllFiles();

        mypdfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Apply pdf = applicantpdfs.get(position);
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setData((Uri.parse(pdf.getUrl())));
                startActivity(intent);
                Log.i("URL" , (pdf.getUrl()));
            }
        });
    }

    private void viewAllFiles() {
        databaseReference = FirebaseDatabase.getInstance().getReference("applicants");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.exists()){
                    for(DataSnapshot applysnapshot : snapshot.getChildren() ){
                        Apply applicant = snapshot.child(id).getValue(Apply.class);
                        applicantpdfs.add(applicant);
                    }

                    String[] allapplicants = new String[applicantpdfs.size()];

                    for(int i = 0;i<allapplicants.length;i++){
                        //if (allapplica)
                        allapplicants[i] = applicantpdfs.get(i).getName();
                        //Log.i("Value", String.valueOf(i));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,allapplicants);
                    mypdfListView.setAdapter(adapter);
                }else{
                    Toast.makeText(ApplicantList.this, "no Data Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}