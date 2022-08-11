package com.example.bucketlistcurator;


import static java.lang.Long.getLong;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    public static String TAG = ".MainActivity";
    //    Views
    EditText mTitle, mDescription, mVenue, mPrice;
    static Button mDateBtn, mTimeBtn;
    TextView imgUrl;
    Button mSaveBtn, mListBtn, mUploadBtn;
    String category, rating, downloadUri;
    ChipGroup mChipGroup;

    //    Progress dialog
    ProgressDialog pd;

    //    Firestore instance
    FirebaseFirestore db;
   static Calendar cal;
    static Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date= new Date();
         cal = Calendar.getInstance();


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
                rating = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTitle = findViewById(R.id.editText_title);
        imgUrl = findViewById(R.id.tv_imageUrl);
        mDescription = findViewById(R.id.editText_description);
        mSaveBtn = findViewById(R.id.button_save);
        mListBtn = findViewById(R.id.button_list);
        mUploadBtn = findViewById(R.id.btn_upload);
        mVenue = findViewById(R.id.et_venue);
        mPrice = findViewById(R.id.et_price);
        mDateBtn = findViewById(R.id.btn_date);
        mTimeBtn = findViewById(R.id.btn_time);
        mChipGroup = findViewById(R.id.chip_group);

        mChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

            }
        });
        ActivityResultLauncher<Intent> mUpload = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        downloadUri = intent.getStringExtra("downloadUri");
                        imgUrl.setText("image url :" + downloadUri);
                    }
                });


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



            uploadData(title, description, venue, category, floatPrice, date);
        });
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpload.launch(new Intent(MainActivity.this, UploadActivity.class));
            }
        });

        mListBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ListActivity.class);
            startActivity(i);
            finish();

        });

        // Date and Time Picker
        mDateBtn.setOnClickListener(v -> showDatePickerDialog(v));
        mTimeBtn.setOnClickListener(v -> showTimePickerDialog(v));

    }


    private void uploadData(String title, String description, String venue, String category, float price, Date date) {

        List<String> tags = new ArrayList<>();
        for (int id : mChipGroup.getCheckedChipIds()) {
            Chip chip = findViewById(id);
            tags.add(chip.getText().toString().toLowerCase(Locale.ROOT));
            Log.d("MainActivity", String.valueOf(tags));
        }

//        set title of progress bar
        pd.setTitle("Adding Data ....");
//        show progress bar when user click save button
        pd.show();

//        String id = UUID.randomUUID().toString();
        Map<String, Object> doc = new HashMap<>();

//        doc.put("id", id);
        doc.put("title", title);
        doc.put("info", description);
        doc.put("venue", venue);
        doc.put("curator", "Eunyfred events");
        doc.put("dateTime", new Timestamp(date));
        doc.put("posted", new Timestamp(new Date()));
        doc.put("tags", tags);
        doc.put("rating", Double.parseDouble(rating));
        doc.put("imageResource", downloadUri);
        doc.put("price", price);
        doc.put("category", category.toLowerCase(Locale.ROOT));

//        add this to data
        db.collection("events")
                .add(doc)
                .addOnSuccessListener(documentReference -> {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                    finish();

                })
                .addOnFailureListener(e -> {
//                  this will be called when there is any error adding data

                    pd.dismiss();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    //Date and time Picker
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {


//            cal.setTime(todayDate);
//
            cal.set(Calendar.YEAR,year);
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.DAY_OF_MONTH,day);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);






            StringBuilder str = new StringBuilder();
            str.append("Date: ");
            str.append(day);
            str.append("-");
            str.append(month);
            str.append("-");
            str.append(year);
            mDateBtn.setText(str);



        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity())
            );
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            date = cal.getTime();


            Log.d(TAG,date.toString());
            StringBuilder str = new StringBuilder();
            str.append("Time: ");
            str.append(hourOfDay);
            str.append(":");
            str.append(minute);
            mTimeBtn.setText(str);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}

