package com.example.bucketlistcurator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //    Views
    EditText mTitle, mDescription, mLocation, mVenue;
    Button mSaveBtn,mListBtn;
    String category;

    //    Progress dialog
    ProgressDialog pd;

    //    Firestore instance
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Event");



        initializeViews();


    }

    private void initializeViews() {
//        grab the views
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTitle = findViewById(R.id.editText_title);
        mDescription = findViewById(R.id.editText_description);
        mSaveBtn = findViewById(R.id.button_save);
        mListBtn = findViewById(R.id.button_list);
        mLocation = findViewById(R.id.editText_location);
        mVenue = findViewById(R.id.editText_venue);

//        create progress dialog
        pd = new ProgressDialog(this);

//        Firestore
        db = FirebaseFirestore.getInstance();

//        click button to upload
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString().trim();
                String description = mDescription.getText().toString().trim();
                String location = mLocation.getText().toString().trim();
                String venue = mVenue.getText().toString().trim();
                uploadData(title, description, location,venue);
            }
        });

        mListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                startActivity(i);
                finish();

            }
        });

    }


    private void uploadData(String title, String description, String location, String venue) {

//        set title of progress bar
        pd.setTitle("Adding Data ....");
//        show progress bar when user click save button
        pd.show();

        String id = UUID.randomUUID().toString();
        Map<String, Object> doc = new HashMap<>();

        doc.put("id", id);
        doc.put("title", title);
        doc.put("description", description);
        doc.put("location", location);
        doc.put("venue", venue);
        doc.put("rating", 4.0);
        doc.put("curator", "curator name");
        doc.put("imageResource", "https://firebasestorage.googleapis.com/v0/b/bucketlist-10f69.appspot.com/o/images%2Fplaceholder.jpg?alt=media&token=7b4942c7-a66e-4e13-bda5-bcc10c605207");
        doc.put("price", description);
        doc.put("tags", id);
        doc.put("category", category);

//        add this to data
        db.collection("documents").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                  this will be called when data is added successfully


                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Uploading...", Toast.LENGTH_SHORT);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                  this will be called when there is any error adding data

                pd.dismiss();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }
}