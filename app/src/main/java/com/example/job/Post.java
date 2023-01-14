package com.example.job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.job.model.PostModel;
import com.example.job.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class Post extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    EditText post_details,companyName,post_position;
    Button btn_post;
    String id,date,time,postRandomKey;
    String selected_category = "Computer";
    ProgressDialog loadingBar;
    private DatabaseReference postRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        spinner = findViewById(R.id.sp_category);
        spinner.setOnItemSelectedListener(this);
        post_details = findViewById(R.id.job_details);
        btn_post = findViewById(R.id.btn_post);
        companyName = findViewById(R.id.et_comanyName);
        post_position = findViewById(R.id.et_post);
        //postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(selected_category);

        //HashMap<String, String> hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("map");

        //name = hashMap.get("username");
        id = Paper.book().read("UserIdentification");
        

        loadingBar = new ProgressDialog(this);




        Adapter adapter = ArrayAdapter.createFromResource(this,
                R.array.spin_array,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter((SpinnerAdapter) adapter);



        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category,name,post,details;

                Toast.makeText(Post.this, selected_category, Toast.LENGTH_SHORT).show();

                category = selected_category;
                name = companyName.getText().toString();
                post = post_position.getText().toString();
                details = post_details.getText().toString();

                if(name.isEmpty()){
                    companyName.setError("Please Write your Company Name");
                    return;
                }else if(post.isEmpty()){
                    post_position.setError("Please Write your Post Postion");
                    return;
                }else if(details.isEmpty()){
                    post_details.setError("Plsease Write your Job Details");
                    return;
                }

                loadingBar.setTitle("Adding new Post");
                loadingBar.setMessage("Dear Admin, Please wait while we are adding the new product");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                
                putPostDataIntoFirebase(category,name,post,details);

        
            }
        });

    }

    private void putPostDataIntoFirebase(String category, String name, String post, String details) {
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(selected_category);
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM, dd , yyyy");
        date = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        time = currentTime.format(calendar.getTime());
        
        postRandomKey = date+time+id;

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("pid",postRandomKey);
        postMap.put("user_id",id);
        postMap.put("category",category);
        postMap.put("company_name",name);
        postMap.put("post",post);
        postMap.put("post_details",details);
        
        postRef.child(postRandomKey).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    clearData();
                    loadingBar.dismiss();
                    Toast.makeText(Post.this, "Successfully Added Post", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.dismiss();
                    Toast.makeText(Post.this, "Failed to Add Post", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void clearData() {
        companyName.setText("");
        post_position.setText("");
        post_details.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selected_category = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, selected_category, Toast.LENGTH_SHORT).show();
        Log.i("name",selected_category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}