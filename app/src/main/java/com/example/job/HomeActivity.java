package com.example.job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.job.Adapter.PostAdapter;
import com.example.job.Adapter.SelectListener;
import com.example.job.model.PostModel;
import com.example.job.model.Prevelant;
import com.example.job.model.Users;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements SelectListener{

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    String name, UserID;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    String category="Computer";
    FirebaseRecyclerOptions<PostModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retriveData();



        HashMap<String, String> hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("map");

        name = hashMap.get("username");
        UserID = hashMap.get("UserID");

        Paper.init(this);

        Paper.book().write(Prevelant.currentOnlineUsers, UserID);



        Toast.makeText(this, name +" = "+ UserID, Toast.LENGTH_SHORT).show();


        nav = findViewById(R.id.home_nav_menu);
        drawerLayout = findViewById(R.id.home_drawer);

        View view= nav.getHeaderView(0);
        TextView header_user_name = view.findViewById(R.id.tv_headerName);

        header_user_name.setText(name);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();




        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                category = item.getTitle().toString();

                if(category.equals("Logout")){
                    Paper.book().destroy();
                    Toast.makeText(HomeActivity.this, "Successfully Login out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    retriveData();
                    onStart();
                    Toast.makeText(HomeActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }

    public void retriveData(){
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = FirebaseDatabase.getInstance().getReference().child("Posts").child(category);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(HomeActivity.this, "no Data " + category, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<PostModel> options = new FirebaseRecyclerOptions.
                Builder<PostModel>()
                .setQuery(query,PostModel.class)
                .build();
        postAdapter = new PostAdapter(options,getApplicationContext(), this);
        recyclerView.setAdapter(postAdapter);

    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        postAdapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        postAdapter.stopListening();
    }

    @Override
    public void onItemClicked(int position) {

    }
}