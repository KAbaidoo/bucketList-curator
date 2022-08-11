package com.example.bucketlistcurator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

public class ValidatedActivity extends AppCompatActivity {
    Button mListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validated);
        initializeViews();
    }

    private void initializeViews() {
        mListBtn = findViewById(R.id.button_list);
        mListBtn.setOnClickListener(v -> {
            Intent i = new Intent(ValidatedActivity.this, ListActivity.class);
            startActivity(i);
            finish();

        });
    }


}