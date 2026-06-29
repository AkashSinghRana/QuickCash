package csci3130.fall_2021.group1.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button registerButton;
    Button loginButton;
    Spinner userType;
    ArrayList<String> jobType = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    EditText userName;
    EditText password;
    ArrayList<String> userInfo = new ArrayList<String>();
    FirebaseDatabase db;
    DatabaseReference reference;
    boolean validUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("users");

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        userType = findViewById(R.id.typeUser);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.passWord);

        jobType.add("Employee");
        jobType.add("Employer");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jobType);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userType.setAdapter(adapter);


    }

    public void loginCheck(CallBack callBack){


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfo.clear();
                DataSnapshot dataSnapshot = snapshot.child(userName.getText().toString());
                for (DataSnapshot values : dataSnapshot.getChildren()){
                    String value = values.getValue(String.class);
                    userInfo.add(value);
                }
                callBack.onCallBack(userInfo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void switch2AppWindow(){
        Intent login_page = new Intent(this, employeeActivity.class);
        login_page.putExtra("usernameValue", userName.getText().toString());
        startActivity(login_page);
    }

    public void switch2EmployerWindow(){
        Intent employer_page = new Intent(this, employerActivity.class);
        employer_page.putExtra("user", userName.getText().toString());
        startActivity(employer_page);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerButton:
                Intent register_page = new Intent(this, registrationActivity.class);
                startActivity(register_page);
                break;
            case R.id.loginButton:
                loginCheck(new CallBack() {
                    @Override
                    public void onCallBack(ArrayList<String> values) {
                        if(values.size() == 0){
                            validUser = false;
                        }
                        else{
                            validUser = password.getText().toString().equals(values.get(0));
                        }


                        if(validUser && userType.getSelectedItem().toString().equals(values.get(4))){
                            if(userType.getSelectedItem().toString().equals("Employee")){
                                switch2AppWindow();
                            }
                            else{
                                switch2EmployerWindow();
                            }

                        }
                        else{
                            Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        }
    }
}