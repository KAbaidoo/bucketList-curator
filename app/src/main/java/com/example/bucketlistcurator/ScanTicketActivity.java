package com.example.bucketlistcurator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ScanTicketActivity extends AppCompatActivity {
    private ScannerLiveView camera;
    private TextView scannedTV;
    CollectionReference  sales;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ticket);
        db = FirebaseFirestore.getInstance();
        sales = db.collection("sales");
//        actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Scan Ticket");
        // check permission method is to check that the
        // camera permission is granted by user or not.
        // request permission method is to request the
        // camera permission if not given.
        if (checkPermission()) {
            // if permission is already granted display a toast message
            Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        // initialize scannerLiveView and textview.
        scannedTV = findViewById(R.id.idTVscanned);
        camera = findViewById(R.id.camview);
        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                // method is called when scanner is started

            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                // method is called when scanner is stopped.
//                Toast.makeText(ScanTicketActivity.this, "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                // method is called when scanner gives some error.
                Toast.makeText(ScanTicketActivity.this, "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {
                // method is called when camera scans the
                // qr code and the data from qr code is
                // stored in data in string format.
                Intent intent = getIntent();
                String eventId = intent.getStringExtra("eventId");
                scannedTV.setText(data);

                sales.whereEqualTo("eventId",eventId).get().addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()){
                                for(QueryDocumentSnapshot doc : task.getResult()){


                                    if (!data.equals("http://" + doc.get("eventId"))) {
                                        Toast.makeText(ScanTicketActivity.this, "Validated! ", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), ValidatedActivity.class);
                                        startActivity(i);
                                        finish();
                                    }

                                }
                            }
                        }
                ).addOnFailureListener(e -> Toast.makeText(ScanTicketActivity.this, "Failed to validate! ", Toast.LENGTH_SHORT).show());



            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        // 0.5 is the area where we have
        // to place red marker for scanning.
        decoder.setScanAreaPercent(0.8);
        // below method will set decoder to camera.
        camera.setDecoder(decoder);
        camera.startScanner();
    }

    @Override
    protected void onPause() {
        // on app pause the
        // camera will stop scanning.
        camera.stopScanner();
        super.onPause();
    }

    private boolean checkPermission() {
        // here we are checking two permission that is vibrate
        // and camera which is granted by user and not.
        // if permission is granted then we are returning
        // true otherwise false.
        int camera_permission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        // this method is to request
        // the runtime permission.
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, VIBRATE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // this method is called when user
        // allows the permission to use camera.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (cameraAccepted && vibrateAccepted) {
                Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied \n You cannot use app without providing permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
