package csci3130.fall_2021.group1.project;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;



public class employeeActivity extends AppCompatActivity implements View.OnClickListener{

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Button viewButton = findViewById(R.id.switchToView);
        viewButton.setOnClickListener(this);

        Button locationButton = findViewById(R.id.switchToLocation);
        locationButton.setOnClickListener(this);

        Button userButton = findViewById(R.id.userDetailButton);
        userButton.setOnClickListener(this);

        Button userJobButton = findViewById(R.id.userJobButton);
        userJobButton.setOnClickListener(this);

        Button ratingButton = findViewById(R.id.ratingButton);
        ratingButton.setOnClickListener(this);

        Intent intent = getIntent();
        userName = intent.getStringExtra("usernameValue");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.switchToView:
                Intent view_jobs_page = new Intent(this, jobViewActivity.class);
                view_jobs_page.putExtra("usernameValue", userName);
                startActivity(view_jobs_page);
                break;

            case R.id.switchToLocation:
                Intent view_location_page = new Intent(this, LocationService.class);
                startActivity(view_location_page);
                break;

            case R.id.userDetailButton:
                Intent view_detail_page = new Intent(this, userDetail.class);
                view_detail_page.putExtra("usernameValue", userName);
                startActivity(view_detail_page);
                break;

            case R.id.userJobButton:
                Intent user_jobs_page = new Intent(this, employeeAcceptedJobsActivity.class);
                user_jobs_page.putExtra("usernameValue", userName);
                startActivity(user_jobs_page);
                break;

            case R.id.ratingButton:
                Intent view_rating_page = new Intent(this, userRating.class);
                startActivity(view_rating_page);
                break;
        }
    }
}
