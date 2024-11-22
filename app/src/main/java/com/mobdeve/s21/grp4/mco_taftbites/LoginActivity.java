package com.mobdeve.s21.grp4.mco_taftbites;
//JASPER
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button createAccountButton;  // New button for "Create an Account"

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);  // Link to your layout file

        // Find the views from the XML layout
        emailInput = findViewById(R.id.email_input);  // The EditText for email
        passwordInput = findViewById(R.id.password_input);  // The EditText for password
        loginButton = findViewById(R.id.loginBTN);  // The Login button
        createAccountButton = findViewById(R.id.create_account_button);  // "Create an Account" button

        // Set up click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the input from the EditText fields
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Perform a basic validation check
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Add your login logic here (e.g., authentication)
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up click listener for the "Create an Account" button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SignupActivity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
