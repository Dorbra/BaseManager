package com.guru.managebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    private FirebaseAuth mAuth;
    private Button BtnLogout;
    private Button BtnMoveTasks;
    private TextView TextUserEmail;

    // Database Objects
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        BtnLogout = findViewById(R.id.BtnLogout);
        BtnMoveTasks = findViewById(R.id.BtnGoTasks);
        TextUserEmail = findViewById(R.id.TextUserEmail);

        BtnLogout.setOnClickListener(this);

        TextUserEmail.setText(user.getEmail());

    }

    @Override
    public void onClick(View view) {
        if (view == BtnLogout) {
            mAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (view == BtnMoveTasks) {
            finish();
            startActivity(new Intent(this, TasksActivity.class));
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //selectedFragment = new HomeFragment();
                    Intent intentMain = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intentMain);
                    break;
                case R.id.navigation_tasks:
                    //selectedFragment = new TasksFragment();
                    Intent intentTasks = new Intent(ProfileActivity.this, TasksActivity.class);
                    startActivity(intentTasks);
                    break;
                case R.id.navigation_profile:
                    //selectedFragment = new ProfileFragment();
                    Intent intentProfile = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intentProfile);
                    break;
            }
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            //return true

            return false;
        }
    };

}
