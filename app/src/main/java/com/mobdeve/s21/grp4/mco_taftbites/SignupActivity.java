package com.mobdeve.s21.grp4.mco_taftbites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText signupUsername, signupEmail, signupPassword, confirmPassword;
    private Button signUpButton, haveAccountButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);  // Ensure this links to your signup layout

        // Reference UI elements
        signupUsername = findViewById(R.id.signup_username);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.confirm_password);
        signUpButton = findViewById(R.id.signupBTN);
        haveAccountButton = findViewById(R.id.have_accountBTN);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // SignUp Button logic
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = signupUsername.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                // Validate the inputs
                if (validateInputs(username, email, password, confirmPass)) {
                    // Firebase-friendly key for userId (replace '.' with ',')
                    String userId = email.replace(".", ",");

                    // Create a new user object
                    SignupLoginHelper helper = new SignupLoginHelper(username, email, password);

                    // Save user data to Firebase
                    reference.child(userId).setValue(helper);

                    // Show success message
                    Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));  // Go to Login screen
                    finish();
                }
            }
        });

        // Redirect to Login Activity if the user already has an account
        haveAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Validate the input fields
    private boolean validateInputs(String username, String email, String password, String confirmPass) {
        if (username.isEmpty()) {
            signupUsername.setError("Username cannot be empty");
            return false;
        }
        if (email.isEmpty()) {
            signupEmail.setError("Email cannot be empty");
            return false;
        }
        if (password.isEmpty()) {
            signupPassword.setError("Password cannot be empty");
            return false;
        }
        if (!password.equals(confirmPass)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}
