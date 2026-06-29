package csci3130.fall_2021.group1.project;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class jobAddingActivity extends AppCompatActivity implements View.OnClickListener {

    Button addJobButton;
    Button switchBackToJob;
    ArrayList<String> listUrgency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_adding_interface);

        addJobButton = findViewById(R.id.addJob);
        addJobButton.setOnClickListener(this);

        switchBackToJob = findViewById(R.id.JobsbackToMain2);
        switchBackToJob.setOnClickListener(this);

        createSpinnerForUrgency();
    }

    public void createSpinnerForUrgency(){
        listUrgency = new ArrayList<>();
        listUrgency.add("High");
        listUrgency.add("Medium");
        listUrgency.add("Low");
        Spinner jobs = findViewById(R.id.addUrgency);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listUrgency);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobs.setAdapter(adapter);
    }

    public String getJobType(){
        EditText job = findViewById(R.id.addJobType);
        return job.getText().toString().trim();
    }

    public String getLocation(){
        EditText location = findViewById(R.id.addLocation);
        return location.getText().toString().trim();
    }

    public String getPay(){
        EditText pay = findViewById(R.id.addPay);
        return pay.getText().toString().trim();
    }

    public String getDate() {
        EditText date = findViewById(R.id.addDate);
        return date.getText().toString().trim();
    }

    public String getDuration(){
        EditText duration = findViewById(R.id.addDuration);
        return duration.getText().toString().trim();
    }


    public String getUrgency() {
        Spinner jobs = findViewById(R.id.addUrgency);
        return jobs.getSelectedItem().toString();
    }

    protected boolean isEmptyInfo( String job, String location, String date, String pay, String duration) {
        return job.trim().isEmpty() || location.trim().isEmpty() || date.trim().isEmpty() || pay.trim().isEmpty() || duration.trim().isEmpty();
    }

    protected boolean checkPay(String pay){
        return pay.matches("^[0-9]{1,5}$");
    }

    protected boolean checkDuration(String duration ){
        return duration.matches("^[0-9]{1,5}$");
    }

    protected boolean checkDate( String date){
        return date.matches("^[0-3][0-9]-[0-3][0-9]-(?:[0-9][0-9])?[0-9][0-9]$");
    }

    @Override
    public void onClick(View view) {
        String jobType = getJobType();
        String location = getLocation();
        String pay = getPay();
        String date = getDate();
        String duration = getDuration();
        String urgency = getUrgency();
        String errorMessage;
        List<Object> jobsInfo = new ArrayList<Object>();
        Bundle value = getIntent().getExtras();
        String user = value.getString("user");

        switch (view.getId()){
            case R.id.JobsbackToMain2:
                Intent employer_Activity = new Intent(this, employerActivity.class);
                startActivity(employer_Activity);
                break;
            case R.id.addJob:
                if (isEmptyInfo(jobType, location, pay, date, duration)){
                    errorMessage = "Fill all fields";
                    Toast.makeText(jobAddingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                } else if (!checkPay(pay)){
                    errorMessage = "Enter a number (Pay)";
                    Toast.makeText(jobAddingActivity.this, errorMessage, Toast.LENGTH_LONG ).show();
                } else if (!checkDate(date)){
                    errorMessage = "Improper Date Format";
                    Toast.makeText(jobAddingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                } else if (!checkDuration(duration)){
                    errorMessage = "Enter a number (Hours)";
                    Toast.makeText(jobAddingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
                if (!isEmptyInfo(jobType, location, pay, date, duration) && checkPay(pay) && checkDuration(duration) && checkDate(date)){
                    errorMessage = "Successfully added a job";
                    Toast.makeText(jobAddingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    jobsInfo.add(location);
                    jobsInfo.add(pay);
                    jobsInfo.add(date);
                    jobsInfo.add(duration);
                    jobsInfo.add(urgency);
                    jobsInfo.add("0");
                    jobsInfo.add(user);
                    jobsInfo.add("none");
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference reference = db.getReference("jobs");
                    reference.child(getJobType()).setValue(jobsInfo);
                    employer_Activity = new Intent(this, employerActivity.class);
                    startActivity(employer_Activity);
                }
                break;
            default:
                break;
        }

    }
}