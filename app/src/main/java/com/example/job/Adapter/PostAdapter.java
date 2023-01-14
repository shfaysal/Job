package com.example.job.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.job.ApplyActivity;
import com.example.job.MainActivity;
import com.example.job.Post;
import com.example.job.R;
import com.example.job.model.PostModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.HashMap;

public class PostAdapter extends FirebaseRecyclerAdapter<PostModel,PostAdapter.myViewHolder>{

    Context context;
    private SelectListener listener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostAdapter(@NonNull FirebaseRecyclerOptions<PostModel> options,Context context,SelectListener listener) {
        super(options);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull PostModel model) {
        final String pid = getRef(position).getKey();
        final String userID = model.getUser_id();


        holder.company_name.setText(model.getCompany_Name());
        holder.position.setText(model.getPost());
        holder.description.setText(model.getPost_details());



        holder.apply_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.onItemClicked(holder.getPosition());
//
//                Toast.makeText(context, holder.getPosition(), Toast.LENGTH_SHORT).show();
//
//                HashMap<String , String > hashMap = new HashMap<>();
//                hashMap.put("userID",model.getUser_id());
//                hashMap.put("pid",model.getPid());
//                Intent intent = new Intent(context,ApplyActivity.class);
//                intent.putExtra("map",hashMap);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
                Log.i("UserID",userID);
                Log.i("productid",pid);

                HashMap<String , String > hashMap = new HashMap<>();
                hashMap.put("userID",userID);
                hashMap.put("pid",pid);


                Intent intent = new Intent(context,ApplyActivity.class);
                intent.putExtra("map",hashMap);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView company_name,position,description;
        Button apply_Button;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            company_name = itemView.findViewById(R.id.companyName);
            position = itemView.findViewById(R.id.job_position);
            description = itemView.findViewById(R.id.details);
            apply_Button = itemView.findViewById(R.id.btn_apply);

        }
    }
}
