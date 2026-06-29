package csci3130.fall_2021.group1.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class userRating extends AppCompatActivity implements View.OnClickListener {

    Button button;
    RatingBar ratingBar;
    TextView totalRatings;

    DatabaseReference databaseReference;
    float ratingCalculation;
    int countVariable;
    float overallRating;
    float totalValue;

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rating);

        button =findViewById(R.id.btn);
        ratingBar =findViewById(R.id.rb);
        totalRatings = (TextView) findViewById(R.id.totalRating);

        Button switchBack = findViewById(R.id.backButton);
        switchBack.setOnClickListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float ratingValue= ratingBar.getRating();

                Toast.makeText(getApplicationContext(),String.valueOf(ratingValue)+" ratings",Toast.LENGTH_SHORT).show();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Ratings");

                databaseReference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        overallRating = Float.parseFloat(snapshot.child("values").getValue().toString());
                        countVariable = Integer.valueOf(snapshot.child("count").getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                extracted(ratingValue);

                FirebaseDatabase.getInstance().getReference().child("Ratings").child("values").setValue(totalValue);
                FirebaseDatabase.getInstance().getReference().child("Ratings").child("count").setValue(countVariable);

            }

            private void extracted(float ratingValue) {
                countVariable +=1;

                totalValue = (ratingValue + overallRating);

                ratingCalculation = totalValue/ countVariable;

                totalRatings.setText("App Rating: "+ (decimalFormat.format(ratingCalculation)));
            }

        });

    }

    public float getRatingCalculation() {
        return ratingCalculation;
    }

    public void setRatingCalculation(float ratingCalculation) {
        this.ratingCalculation = ratingCalculation;
    }

    public int getCountVariable() {
        return countVariable;
    }

    public void setCountVariable(int countVariable) {
        this.countVariable = countVariable;
    }

    public float getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(float overallRating) {
        this.overallRating = overallRating;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.backButton:
                Intent app_Activity = new Intent(this, employeeActivity.class);
                startActivity(app_Activity);
                break;
            default:
                break;
        }

    }
}