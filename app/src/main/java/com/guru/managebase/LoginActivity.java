package com.guru.managebase;

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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "LoginActivity";
    private Button SigninButton;
    private EditText EmailText;
    private EditText PasswordText;
    private TextView TextTitle;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        SigninButton = (Button) findViewById(R.id.BtnSignin);
        EmailText = (EditText) findViewById(R.id.TextEmail);
        PasswordText = findViewById(R.id.TextPassword);
        TextTitle = findViewById(R.id.TextTitle);

        SigninButton.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

    }

    @Override
    public void onClick(View view) {
        if (view == SigninButton) {
            userLogin();
        }
        if (view == TextTitle) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void userLogin() {
        String email = EmailText.getText().toString().trim();
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

        // if email & password correct =
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Logged In: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    // Start Profile Activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail : failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //selectedFragment = new HomeFragment();
                    Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intentMain);
                    break;
                case R.id.navigation_tasks:
                    //selectedFragment = new TasksFragment();
                    Intent intentTasks = new Intent(LoginActivity.this, TasksActivity.class);
                    startActivity(intentTasks);
                    break;
                case R.id.navigation_profile:
                    //selectedFragment = new ProfileFragment();
                    //Intent intentProfile = new Intent(LoginActivity.this, LoginActivity.class);
                    //startActivity(intentProfile);
                    break;
            }
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            //return true

            return false;
        }
    };

}
