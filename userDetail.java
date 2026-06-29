package csci3130.fall_2021.group1.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class userDetail extends AppCompatActivity implements View.OnClickListener{

    private TextView usernameTextView, passwordTextView, cardTextView, cvvTextView;
    private String username, password;

    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private static final String USER = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Intent intent = getIntent();
        username = intent.getStringExtra("usernameValue");

        usernameTextView = findViewById(R.id.usernameTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        cardTextView = findViewById(R.id.cardTextView);
        cvvTextView = findViewById(R.id.cvvTextView);

        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference(USER);

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot values: snapshot.getChildren()){

                    if(Objects.equals(values.getKey(), username)){
                        usernameTextView.setText(username);
                        passwordTextView.setText(values.child("0").getValue(String.class));
                        cardTextView.setText(values.child("1").getValue(String.class));
                        cvvTextView.setText(values.child("3").getValue(String.class));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view){

    }

}