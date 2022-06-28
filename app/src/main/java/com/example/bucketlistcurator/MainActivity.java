package com.example.bucketlistcurator;


import static java.lang.Long.getLong;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //    Views
    EditText mTitle, mDescription, mVenue, mPrice, mDate, mTime;
    Button mSaveBtn, mListBtn;
    String category, rating;

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
        assert actionBar != null;
        actionBar.setTitle("Add Event");
        initializeViews();

    }

    private void initializeViews() {
//        grab the views
        Spinner categorySpinner = findViewById(R.id.category_spinner);
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

        Spinner ratingSpinner = findViewById(R.id.rating_spinner);
        ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(this,
                R.array.ratings, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(mAdapter);

        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          rating =  parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTitle = findViewById(R.id.editText_title);
        mDescription = findViewById(R.id.editText_description);
        mSaveBtn = findViewById(R.id.button_save);
        mListBtn = findViewById(R.id.button_list);
        mVenue = findViewById(R.id.et_venue);
        mPrice = findViewById(R.id.et_price);
        mDate = findViewById(R.id.et_date);
        mTime = findViewById(R.id.et_time);


//        create progress dialog
        pd = new ProgressDialog(this);

//        Firestore
        db = FirebaseFirestore.getInstance();

//        click button to upload
        mSaveBtn.setOnClickListener(v -> {
            String title = mTitle.getText().toString().trim();
            String description = mDescription.getText().toString().trim();
            String venue = mVenue.getText().toString().trim();
            String price = mPrice.getText().toString().trim();
            float floatPrice = Float.parseFloat(price);
            String date = mDate.getText().toString().trim();
            String time = mTime.getText().toString().trim();




            uploadData(title, description, venue, category, floatPrice, date, time);
        });

        mListBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ListActivity.class);
            startActivity(i);
            finish();

        });

    }


    private void uploadData(String title, String description, String venue,String category, float price, String date, String time) {

//        set title of progress bar
        pd.setTitle("Adding Data ....");
//        show progress bar when user click save button
        pd.show();

        String id = UUID.randomUUID().toString();
        Map<String, Object> doc = new HashMap<>();

        doc.put("id", id);
        doc.put("title", title);
        doc.put("info", description);
        doc.put("venue", venue);
        doc.put("curator", "Eunyfred events");
        doc.put("time", time);
        doc.put("date", date);
        doc.put("posted",new Timestamp(new Date()) );
//        doc.put("tags", tags);
        doc.put("rating",Double.parseDouble(rating));
//        doc.put("imageResource", "https://firebasestorage.googleapis.com/v0/b/bucketlist-10f69.appspot.com/o/images%2Fplaceholder.jpg?alt=media&token=7b4942c7-a66e-4e13-bda5-bcc10c605207");
        doc.put("price", price);
        doc.put("category", category);

//        add this to data
        db.collection("documents")
                .add(doc)
                .addOnSuccessListener(documentReference -> {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
//                  this will be called when there is any error adding data

                    pd.dismiss();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}