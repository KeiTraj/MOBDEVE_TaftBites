package com.mobdeve.s21.grp4.mco_taftbites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText signupUsername, signupEmail, signupPassword, confirmPassword;
    private Button signUpButton, haveAccountButton;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    ProgressDialog pd;

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
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        // Redirect to Login Activity if the user already has an account
        haveAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // SignUp Button logic
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = signupUsername.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                if (validateInputs(username, email, password, confirmPass)) {
                    registerUser(username, email, password);
                }
            }
        });
    }

    // Validate the input fields
    private boolean validateInputs(String username, String email, String password, String confirmPass) {
        if (TextUtils.isEmpty(username)) {
            signupUsername.setError("Username cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            signupEmail.setError("Email cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            signupPassword.setError("Password cannot be empty");
            return false;
        }
        if (password.length() < 6) {
            signupPassword.setError("Password must be at least 6 characters long");
            return false;
        }
        if (!password.equals(confirmPass)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    // Register the user using Firebase Authentication and save to Firebase Database
    private void registerUser(final String username, final String email, final String password) {
        pd.setMessage("Signing up...");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String userId = mAuth.getCurrentUser().getUid();

                // Create user data map
                HashMap<String, Object> map = new HashMap<>();
                map.put("username", username);
                map.put("email", email);
                map.put("id", userId);
                map.put("bio", "");
                map.put("imageurl", "default");

                // Save to database
                mRootRef.child("Users").child(userId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(SignupActivity.this, "Signup successful! Update your profile for a better experience.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, MainPageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SignupActivity.this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
