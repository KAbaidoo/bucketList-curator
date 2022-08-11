package com.example.bucketlistcurator;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<Event> eventList = new ArrayList<>();
    //Layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;

    //firestore instance
    FirebaseFirestore db;
    //adapter
    CustomAdapter adapter;
    ProgressDialog pd;

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        // init firestore
        db = FirebaseFirestore.getInstance();
        //initialize views
        mRecyclerView = findViewById(R.id.recycler_view);

        //get recyclerView properties
        mRecyclerView.setHasFixedSize(true);

        fab = findViewById(R.id.fab);

        //init progress dialog
        pd = new ProgressDialog(this);


        //adapter

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        //set adapter to recyclerView


        //show data in recyclerView
        showData();

        fab.setOnClickListener(view -> {
            startActivity(new Intent(ListActivity.this, MainActivity.class));
            finish();
        });
    }

    private void showData() {
        //set title of progress dialog
        pd.setTitle("Loading Data ...");
        //show progress dialog
        pd.show();


        db.collection("events").get().addOnCompleteListener(task -> {
            //called when data is received
            pd.dismiss();
            if(task.isSuccessful()){
                for (DocumentSnapshot doc : task.getResult()) {
                    eventList.add(doc.toObject(Event.class));
                    adapter = new CustomAdapter(ListActivity.this, eventList, this);
                    mRecyclerView.setAdapter(adapter);

                }
            }


// some comment
        }).addOnFailureListener(e -> {
            //called when there is any error in retrieving data
            pd.dismiss();
        });
    }
}