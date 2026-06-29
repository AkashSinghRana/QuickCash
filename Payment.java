package csci3130.fall_2021.group1.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

public class Payment extends AppCompatActivity {

    Button payButton;
    EditText amount;
    private int requestCode =12;
    public static final String YOUR_CLIENT_ID ="AcR2xwXqrOxTqkDlaGX2KD41BBO2lIiBNpuA2cWkDKIZGEs42dRfj0fqI-n06M7tIPitURjYMkwJKcYV";
    private static PayPalConfiguration paypal_config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(YOUR_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypal_config);
        startService(intent);
        payButton = findViewById(R.id.Button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentpage();
            }
        });
    }

    public void paymentpage(){
        amount = findViewById(R.id.editTextNumberDecimal);
        PayPalPayment payment = new PayPalPayment(new BigDecimal(100),"CAD", "Quickcash", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent= new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypal_config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode){
            if (resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Payment made succesfully", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Payment was unsuccesfully", Toast.LENGTH_LONG).show();
            }
        }
    }
}