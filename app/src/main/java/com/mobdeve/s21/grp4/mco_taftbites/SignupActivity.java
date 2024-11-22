package com.mobdeve.s21.grp4.mco_taftbites;
//JASPER
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);  // Link to your signup layout

        // Find the "Already have an account" button by its ID
        // Reference to "Already have an account" button
        Button haveAccountButton = findViewById(R.id.have_accountBTN);

        // Set an OnClickListener on the button
        haveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate back to LoginActivity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();  // Finish SignupActivity so it's removed from the back stack
            }
        });
    }
}
