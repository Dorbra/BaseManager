package com.guru.managebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private Button RegisterButton;
    private EditText EmailText;
    private EditText PasswordText;
    private TextView TextTitle;
    private ProgressDialog progressDialog;
    private TextView TextSignin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // if already Signed-In
        if (mAuth.getCurrentUser() != null) {
            // Profile Activity
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        RegisterButton = (Button) findViewById(R.id.BtnRegister);
        EmailText = (EditText) findViewById(R.id.TextEmail);
        PasswordText = (EditText) findViewById(R.id.TextPassword);
        TextTitle = findViewById(R.id.TextTitle);
        TextSignin = findViewById(R.id.TextSignin);

        progressDialog = new ProgressDialog(this);

        RegisterButton.setOnClickListener(this);
        TextSignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == RegisterButton) {
            // register the user
            registerUser();
        }
        // Login - will open SignIn activity
        if (view == TextSignin) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void registerUser() {
        final String email = EmailText.getText().toString().trim();
        String password = PasswordText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            // email is empty
            Toast.makeText(this, "Must provide Email Address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            // password is empty
            Toast.makeText(this, "Must provide a Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        // if validation is good, register:
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        // create the User
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // if Registered successfully and logged in:
                    Log.d(TAG, "Registered User: " + mAuth.getCurrentUser().getEmail());
                    Toast.makeText(MainActivity.this, email + " Registered Successfully.", Toast.LENGTH_SHORT).show();
                    if (mAuth.getCurrentUser() != null) {
                        // Profile Activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    }
                } else {
                    Log.w(TAG, "createUserWithEmailAndPassword : failure", task.getException());
                    Toast.makeText(MainActivity.this, "Registration Failed/nPassword is at least 6 chars", Toast.LENGTH_LONG).show();

                }
                progressDialog.dismiss();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //selectedFragment = new HomeFragment();
                    Intent intentMain = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intentMain);
                    break;
                case R.id.navigation_tasks:
                    //selectedFragment = new TasksFragment();
                    Intent intentTasks = new Intent(MainActivity.this, TasksActivity.class);
                    startActivity(intentTasks);
                    break;
                case R.id.navigation_profile:
                    //selectedFragment = new ProfileFragment();
                    Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intentProfile);
                    break;
            }
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            //return true

            return false;
        }
    };


}
