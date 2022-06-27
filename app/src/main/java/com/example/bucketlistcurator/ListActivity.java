package com.example.bucketlistcurator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

        fab  = findViewById(R.id.fab);

        //init progress dialog
        pd = new ProgressDialog(this);




        //adapter

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false );
        mRecyclerView.setLayoutManager(layoutManager);
        //set adapter to recyclerView



        //show data in recyclerView
        showData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(ListActivity.this, MainActivity.class));
            finish();
            }
        });
    }

    private void showData() {
        //set title of progress dialog
        pd.setTitle("Loading Data ...");
        //show progress dialog
        pd.show();


        db.collection("documents").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            //called when data is received
                pd.dismiss();
                for (DocumentSnapshot doc: task.getResult()){
                    Event event = new Event(doc.getString("id"),doc.getString("title"),doc.getString("description"),"","",4,"","",40,"");
                    eventList.add(event);
                    adapter = new CustomAdapter(ListActivity.this, eventList);
                    mRecyclerView.setAdapter(adapter);

                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            //called when there is any error in retrieving data
                pd.dismiss();
            }
        });
    }
}