package csci3130.fall_2021.group1.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class registrationActivity extends AppCompatActivity implements View.OnClickListener{
    Button backToLogin;
    Button buttonRegister;
    ArrayList<String> listUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backToLogin = findViewById(R.id.backToLogin);
        backToLogin.setOnClickListener(this);

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);

        createSpinnerForUserType();
    }

    public void createSpinnerForUserType(){
        listUserType = new ArrayList<>();
        listUserType.add("Employee");
        listUserType.add("Employer");
        Spinner jobs = findViewById(R.id.userTypeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listUserType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobs.setAdapter(adapter);
    }

    public String getUserName() {
        EditText userName = findViewById(R.id.usernameRegister);
        return userName.getText().toString().trim();
    }

    protected String getPassword(){
        EditText password = findViewById(R.id.passwordRegister);
        return password.getText().toString().trim();
    }

    protected String getPasswordConfirm(){
        EditText confirmPassword = findViewById(R.id.confirmPassword);
        return confirmPassword.getText().toString().trim();
    }

    protected String getCreditCardNumber(){
        EditText creditCard = findViewById(R.id.creditCard);
        return creditCard.getText().toString().trim();
    }

    protected String getCreditCardDate(){
        EditText date = findViewById(R.id.creditDate);
        return date.getText().toString().trim();
    }

    protected String getCVV(){
        EditText cvv = findViewById(R.id.cvv);
        return cvv.getText().toString().trim();
    }

    protected void setStatusMessage(String message){
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message);
    }

    protected String getUserType() {
        Spinner jobs = findViewById(R.id.userTypeSpinner);
        return jobs.getSelectedItem().toString();
    }


    protected boolean isEmptyInfo(String userName,String password,String confirmPassword,String creditCard,String date,String cvv ) {
        return userName.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty() || creditCard.trim().isEmpty() || date.trim().isEmpty() || cvv.trim().isEmpty();
    }

    protected boolean isValidPassWord(String password) {
        return (Pattern.matches("^[a-zA-Z0-9]{6,32}[\\W]$", password));
    }

    protected boolean isValidUserName(String userName) {
        return (Pattern.matches("^[a-zA-Z0-9\\W]{3,20}$", userName));
    }

    protected boolean isConfirmedPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    protected boolean isValidCreditCard(String creditCard) {
        return (Pattern.matches("^[0-9]{16}$", creditCard));
    }

    protected boolean isValidCVV(String cvv) {
        return (Pattern.matches("^[0-9]{3}$", cvv));
    }

    protected boolean isValidCreditDate(String date) {
        return (Pattern.matches("^[0-9]{4}$", date));
    }

    @Override
    public void onClick(View view) {
        String userName = getUserName();
        String password = getPassword();
        String cvv = getCVV();
        String date = getCreditCardDate();
        String creditCard = getCreditCardNumber();
        String confirmPassword = getPasswordConfirm();
        String errorMessage = new String("");
        switch (view.getId()){
            case R.id.backToLogin:
                Intent login_page = new Intent(this, MainActivity.class);
                startActivity(login_page);
                break;
            case R.id.buttonRegister:
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference reference = db.getReference("users");
                List<String> userInfo = new ArrayList<String>();
                if(isEmptyInfo(userName,password,cvv,date,creditCard,confirmPassword)){
                    errorMessage = "Fill all information";
                    setStatusMessage(errorMessage);
                    Toast.makeText(registrationActivity.this, "Fill all fields", Toast.LENGTH_LONG).show();
                } else if(!isValidUserName(userName)){
                    errorMessage = "Username must be at least 3 characters long";
                    setStatusMessage(errorMessage);
                    Toast.makeText(registrationActivity.this, "Username must be at least 3 characters long", Toast.LENGTH_LONG).show();
                } else if(!isValidPassWord(password)){
                    errorMessage = "Password must be 6 characters at least with a special character at the end";
                    setStatusMessage(errorMessage);
                    Toast.makeText(registrationActivity.this, "Password must be 6 characters at least with a special character at the end", Toast.LENGTH_LONG).show();
                } else if(!isConfirmedPassword(password, confirmPassword)){
                    errorMessage = "Confirmed password must be the same as password";
                    setStatusMessage(errorMessage);
                    Toast.makeText(registrationActivity.this, "Confirmed password must be the same as password", Toast.LENGTH_LONG).show();
                } else if(!isValidCreditCard(creditCard)){
                    errorMessage = "Mastercard must be 16 digits long";
                    setStatusMessage(errorMessage);
                    Toast.makeText(registrationActivity.this, "Mastercard must be 16 digits long", Toast.LENGTH_LONG).show();
                } else if(!isValidCreditDate(date)){
                    errorMessage = "MM-YY no space";
                    setStatusMessage(errorMessage);
                    Toast.makeText(registrationActivity.this, "MM-YY no space", Toast.LENGTH_LONG).show();
                } else if(!isValidCVV(cvv)){
                    errorMessage = "cvv must be 3 digits";
                    setStatusMessage(errorMessage);
                    Toast.makeText(registrationActivity.this, "cvv must be 3 digits", Toast.LENGTH_LONG).show();
                }

                if(!isEmptyInfo(userName,password,cvv,date,creditCard,confirmPassword) && isValidPassWord(password) && isValidUserName(userName)
                        && isConfirmedPassword(password, confirmPassword) && isValidCreditCard(creditCard) && isValidCVV(cvv) && isValidCreditDate(date)){
                    userInfo.add(getPassword());
                    userInfo.add(getCreditCardNumber());
                    userInfo.add(getCreditCardDate());
                    userInfo.add(getCVV());
                    userInfo.add(getUserType());
                    reference.child(getUserName()).setValue(userInfo);
                    Toast.makeText(registrationActivity.this, "You have been registered", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
