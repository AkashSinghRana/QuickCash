package csci3130.fall_2021.group1.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class employerActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

        Button addJobButton = findViewById(R.id.addJobButton);
        addJobButton.setOnClickListener(this);

        Button viewJobs = findViewById(R.id.viewEmployerJobs);
        viewJobs.setOnClickListener(this);

        Button payment = findViewById(R.id.payment);
        payment.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Bundle value = getIntent().getExtras();
        String user = value.getString("user");
        switch (view.getId()){
            case R.id.addJobButton:
                Intent jobAddPage = new Intent(this, jobAddingActivity.class);
                jobAddPage.putExtra("user", user);
                startActivity(jobAddPage);
                break;

            case R.id.viewEmployerJobs:
                Intent jobViewPage = new Intent(this, employerJobViewActivity.class);
                jobViewPage.putExtra("user", user);
                startActivity(jobViewPage);
                break;
            case R.id.payment:
                Intent PaymentPage = new Intent(this, Payment.class);
                PaymentPage.putExtra("user", user);
                startActivity(PaymentPage);
                break;
        }
    }
}