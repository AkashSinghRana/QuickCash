package csci3130.fall_2021.group1.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Struct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class jobViewActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseDatabase db;
    DatabaseReference reference;
    ArrayList<String> listJobs = new ArrayList<>();
    Button viewJobInfoButton;
    TextView jobName;
    Spinner jobs;
    ArrayAdapter<String> adapter;
    ArrayList<String> jobInfoList = new ArrayList<>();
    TextView locationLabel;
    TextView payLabel;
    TextView dateLabel;
    TextView durationLabel;
    TextView urgencyLabel;
    Button clearPreferencesButton;
    Button addPreferencesButton;
    EditText maxDistancePref;
    EditText minPayPref;
    EditText dateAfterPref;
    EditText maxDurationPref;
    ArrayList<String> correctPreferenceJobs = new ArrayList<>();
    ArrayList<Integer> preferences = new ArrayList<>();
    ArrayList<String> clearSpinner = new ArrayList<>();
    Button acceptJobButton;
    Bundle bundle;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobviewactivity);
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("jobs");
        jobs = findViewById(R.id.jobsSpinner);
        viewJobInfoButton = findViewById(R.id.viewJobButton);
        viewJobInfoButton.setOnClickListener(this);
        jobName = findViewById(R.id.jobNameLabel);
        locationLabel = findViewById(R.id.locationLabel);
        payLabel = findViewById(R.id.payLabel);
        dateLabel = findViewById(R.id.dateLabel);
        durationLabel = findViewById(R.id.durationLabel);
        urgencyLabel = findViewById(R.id.urgencyLabel);

        clearPreferencesButton = findViewById(R.id.clearPrefButton);
        clearPreferencesButton.setOnClickListener(this);
        addPreferencesButton = findViewById(R.id.addPrefButton);
        addPreferencesButton.setOnClickListener(this);
        maxDistancePref = findViewById(R.id.locationPreference);
        minPayPref = findViewById(R.id.payPreference);
        dateAfterPref = findViewById(R.id.datePreference);
        maxDurationPref = findViewById(R.id.durationPreference);
        acceptJobButton = findViewById(R.id.acceptJobButton);
        acceptJobButton.setOnClickListener(this);

        bundle = getIntent().getExtras();

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

                for (DataSnapshot values : snapshot.getChildren()){
                    String value = values.child("5").getValue(String.class);

                    if(!listJobs.contains(String.valueOf(values.getKey())) && value.equals("0")){
                        listJobs.add(String.valueOf(values.getKey()));
                    }

                }
                callBack.onCallBack(listJobs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void jobInfo(CallBack callBack){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(jobs.getSelectedItem().toString().equals("Jobs")){
                    Toast.makeText(jobViewActivity.this, "Please select a job", Toast.LENGTH_LONG).show();
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

    public void jobAdderPreferences(CallBack callBack, String distance, String pay, String date, String duration){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!(distance.equals("") || distance.equals("Max distance (km)"))){
                    preferences.add(0);
                }
                if(!(pay.equals("") || pay.equals("Minimum pay ($)"))){
                    preferences.add(1);
                }
                if(!(date.equals("") || date.equals("Date after(YYYY-MM-DD)"))){
                    preferences.add(2);
                }
                if(!(duration.equals("") || duration.equals("Longest duration (hours)"))){
                    preferences.add(3);
                }

                for (DataSnapshot values : snapshot.getChildren()){
                    boolean validJob = true;
                    String temp_value = "";
                    String info = String.valueOf(values.getValue());
                    info = info.substring(1, info.length()-1);
                    String[] infoValues = info.split(",");
                    for(int pref : preferences){
                        switch(pref){
                            case 0:
                                // do something that compares distance of location of task with the location of task
                                break;
                            case 1:
                                if(validJob) {
                                    temp_value = values.child("1").getValue(String.class);
                                    validJob = Integer.parseInt(pay) <= Integer.parseInt(temp_value);
                                }
                                break;
                            case 2:
                                if(validJob){
                                    temp_value = values.child("2").getValue(String.class);
                                    validJob = convertToDate(date).before(convertToDate(temp_value));
                                }
                                break;
                            case 3:
                                if(validJob){
                                    temp_value = values.child("3").getValue(String.class);
                                    validJob = Integer.parseInt(duration) >= Integer.parseInt(temp_value);
                                }
                        }
                    }
                    if(validJob){
                        correctPreferenceJobs.add(String.valueOf(values.getKey()));
                    }

                }
                preferences.clear();
                callBack.onCallBack(correctPreferenceJobs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setJobView(ArrayList<String> abc){
        if(abc.size() > 0 && !abc.get(0).equals("Jobs")){
            abc.add(0, "Jobs");
        }
        if(abc.size() == 0){
            abc.add(0, "Jobs");
        }

        jobs = findViewById(R.id.jobsSpinner);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, abc);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        jobs.setAdapter(adapter);
        abc = new ArrayList<>();
    }

    public Date convertToDate(String dateString) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateString);
            return date;
        }
        catch (Exception ignored){
            return date;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewJobButton:
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
            case R.id.addPrefButton:
                String distancePref = maxDistancePref.getText().toString();
                String payPref = minPayPref.getText().toString();
                String datePref = dateAfterPref.getText().toString();
                String durationPref = maxDurationPref.getText().toString();

                correctPreferenceJobs.clear();
                jobAdderPreferences(new CallBack() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCallBack(ArrayList<String> values) {
                        setJobView(clearSpinner);
                        setJobView(values);
                    }
                } , distancePref, payPref, datePref, durationPref);

                break;
            case R.id.clearPrefButton:
                maxDistancePref.setText("Max distance (km)");
                minPayPref.setText("Minimum pay ($)");
                dateAfterPref.setText("Date after(YYYY-MM-DD)");
                maxDurationPref.setText("Longest duration (hours)");

                listJobs.clear();
                jobAdder(new CallBack() {
                    @Override
                    public void onCallBack(ArrayList<String> values) {
                        setJobView(clearSpinner);
                        setJobView(values);
                    }
                });
                break;

            case R.id.acceptJobButton:
                if(jobs.getSelectedItem().toString().equals("Jobs")){
                    Toast.makeText(jobViewActivity.this, "Please select a job to accept", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(jobViewActivity.this, "You have successfully accepted the job!", Toast.LENGTH_LONG).show();
                    user = bundle.getString("usernameValue");
                    reference.child(jobs.getSelectedItem().toString()).child("5").setValue("1");
                    reference.child(jobs.getSelectedItem().toString()).child("7").setValue(user);
                    listJobs.clear();
                    jobAdder(new CallBack() {
                        @Override
                        public void onCallBack(ArrayList<String> values) {
                            setJobView(clearSpinner);
                            setJobView(values);
                        }
                    });
                    break;
                }
                break;
        }
    }

}