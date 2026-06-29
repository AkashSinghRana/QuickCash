package csci3130.fall_2021.group1.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class employeeAcceptedJobsActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase db;
    DatabaseReference reference;
    ArrayList<String> listJobs = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<String> jobInfoList = new ArrayList<>();
    Button viewJobButton;
    Spinner jobs;
    String user;
    TextView jobName;
    TextView locationLabel;
    TextView payLabel;
    TextView dateLabel;
    TextView durationLabel;
    TextView urgencyLabel;
    Bundle value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_accepted_jobs);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("jobs");

        viewJobButton = findViewById(R.id.employeeViewJobButton);
        viewJobButton.setOnClickListener(this);

        value = getIntent().getExtras();

        jobName = findViewById(R.id.employeeJobNameLabel);
        locationLabel = findViewById(R.id.employeeLocationLabel);
        payLabel = findViewById(R.id.employeePayLabel);
        dateLabel = findViewById(R.id.employeeDateLabel);
        durationLabel = findViewById(R.id.employeeDurationLabel);
        urgencyLabel = findViewById(R.id.employeeUrgencyLabel);

        jobAdder(new CallBack() {
            @Override
            public void onCallBack(ArrayList<String> values) {
                setJobView(values);
            }
        });
    }

    public void jobAdder(CallBack callBack){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = value.getString("usernameValue");
                for (DataSnapshot values : snapshot.getChildren()){
                    String value = values.child("7").getValue(String.class);

                    if(user != null){
                        if(user.equals(value)){
                            listJobs.add(String.valueOf(values.getKey()));
                        }
                    }

                }

                callBack.onCallBack(listJobs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void setJobView(ArrayList<String> abc){
        abc.add(0, "Jobs");

        jobs = findViewById(R.id.employeeJobs);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, abc);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        jobs.setAdapter(adapter);
        abc = new ArrayList<>();
    }

    public void jobInfo(CallBack callBack){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(jobs.getSelectedItem().toString().equals("Jobs")){
                    Toast.makeText(employeeAcceptedJobsActivity.this, "Please select a job", Toast.LENGTH_LONG).show();
                }
                else {
                    DataSnapshot dataSnapshot = snapshot.child(jobs.getSelectedItem().toString());

                    for (DataSnapshot values : dataSnapshot.getChildren()) {
                        String value = values.getValue(String.class);
                        jobInfoList.add(value);
                    }
                    callBack.onCallBack(jobInfoList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.employeeViewJobButton:
                adapter.notifyDataSetChanged();
                jobInfoList.clear();
                jobInfo(new CallBack() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCallBack(ArrayList<String> values) {
                        jobName.setText(jobs.getSelectedItem().toString());
                        locationLabel.setText("Location: " + values.get(0));
                        payLabel.setText("Pay: " + values.get(1));
                        dateLabel.setText("Date: " + values.get(2));
                        durationLabel.setText("Duration: " + values.get(3));
                        urgencyLabel.setText("Urgency: " + values.get(4));
                    }
                });
                break;
        }
    }
}