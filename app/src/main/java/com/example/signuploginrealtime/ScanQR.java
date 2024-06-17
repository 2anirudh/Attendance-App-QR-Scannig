package com.example.signuploginrealtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.Objects;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQR extends AppCompatActivity implements View.OnClickListener {

    Button scanBtn;
    TextView headRollno, headName,lecture, attended;
    Intent intent;
    FirebaseDatabase database;
    DatabaseReference reference;
    String name;
    String Rollno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        lecture = findViewById(R.id.textLecture);
        scanBtn = findViewById(R.id.scanBtn);
        headName = findViewById(R.id.headName);
        headRollno = findViewById(R.id.headRollno);
        attended = findViewById(R.id.Leccount);
        intent = getIntent();
        headRollno.setText(intent.getStringExtra("rollno"));
        headName.setText(intent.getStringExtra("name"));

        scanBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "Attendance Marked", Toast.LENGTH_LONG).show();
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Attendance");
                Rollno = intent.getStringExtra("rollno");

                String lectureinfo = intentResult.getContents();
                String lectureinfoarray[] = lectureinfo.split("~");
                String slot = lectureinfoarray[1]+" "+lectureinfoarray[2];
                AttendanceClass attendace = new AttendanceClass(intent.getStringExtra("name"), Rollno);
                reference.child(lectureinfoarray[0]).child(slot).child(Rollno).setValue(attendace);
                lecture.setText(lectureinfoarray[0]);
                final int[] lec_count = {0};
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                            for (DataSnapshot snapshot2 : snapshot.getChildren()){
                                for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                    String key = snapshot3.getKey();
                                    if(Objects.equals(key, Rollno)){
                                        lec_count[0]++;
                                    }
                                    Log.i("","");

                                }
                            }
                        }
                        attended.setText(String.valueOf(lec_count[0]));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}