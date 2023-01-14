package com.example.job;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.job.model.Apply;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import io.paperdb.Paper;

public class ApplyActivity extends AppCompatActivity {

    EditText pdfUri;
    Button choosePDF,submit;
    ActivityResultLauncher<Intent> resultLauncher;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri sUri;
    String pid,creator_ID,currentUserID,name;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        HashMap<String, String> hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("map");

        pid = hashMap.get("pid");
        creator_ID = hashMap.get("userID");
        currentUserID = Paper.book().read("currentOnlineUsers");
        loadingBar = new ProgressDialog(this);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("applicants");



        pdfUri = findViewById(R.id.pdfName);
        choosePDF = findViewById(R.id.choose_pdf);
        submit = findViewById(R.id.btn_submit);

        //Toast.makeText(this,name,Toast.LENGTH_SHORT).show();



        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();

                        if (data != null){
                            sUri = data.getData();
                            //pdfUri.setText(sUri.toString());
                        }
                    }
                });

        choosePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(
                        ApplyActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // When permission is not granted
                    // Result permission
                    ActivityCompat.requestPermissions(
                            ApplyActivity.this, new String[] {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            }, 1);
                }
                else {
                    // When permission is granted
                    // Create method
                    selectPDF();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loadingBar.setTitle("Uploading Your CV");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                name = pdfUri.getText().toString();

                uploadCV();
            }
        });

    }

    private void uploadCV() {
        StorageReference reference = storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(sUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri url = uriTask.getResult();
                //uplaod in firebase
                Apply apply = new Apply(pid,creator_ID,currentUserID,url.toString(),name);
                Log.i("Name",name);
                databaseReference.child(pid).setValue(apply);
                loadingBar.dismiss();
                Toast.makeText(ApplyActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });

    }

    private void selectPDF() {
        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // set type
        intent.setType("application/pdf");
        // Launch intent
        resultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

        // check condition
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            selectPDF();
        }
        else {
            // When permission is denied
            // Display toast
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    void upLoadPdf(){

    }
}