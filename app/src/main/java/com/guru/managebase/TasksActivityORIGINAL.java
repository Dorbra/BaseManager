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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TasksActivityORIGINAL extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TasksActivity";
    private Button ButtonAdd;
    private TextView TextTitle;
    private EditText TextTaskField;

    private RecyclerView mTasksList;
    private GridLayoutManager gridLayoutManager;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseTasks;

    // Tasks List and Model Arraylist
    private RecyclerView recyclerView_tasks;
    private ArrayList<TaskModel> list_tasksModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Log.d(TAG, "onCreate");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        mAuth = FirebaseAuth.getInstance();
        ButtonAdd = findViewById(R.id.BtnAddTask);
        TextTitle = findViewById(R.id.TextTitle);

        // init Firebase User
        user = mAuth.getCurrentUser();

        // Tasks RecyclerView + GridLayout
        gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mTasksList = findViewById(R.id.recyclerView_tasks);
        mTasksList.setHasFixedSize(true);
        mTasksList.setLayoutManager(gridLayoutManager);

        // NEW RECYCLER VIEW 12.10
        recyclerView_tasks = (RecyclerView) findViewById(R.id.recyclerView_tasks);
        // Test

        // -----TEST----- //

        new FirebaseDatabaseHelper().getTasks(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<TaskModel> tasks, ArrayList<String> keys) {
                Log.d(TAG, "### Tasks: " + tasks.size());
                new RecyclerView_Config().setConfig(recyclerView_tasks, TasksActivityORIGINAL.this, tasks, keys);
                Log.d(TAG, "### Tasks: " + tasks.size());
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
        // NEW RECYCLER VIEW 12.10

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        if (user == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            databaseTasks = FirebaseDatabase.getInstance().getReference("Tasks").child(user.getUid());
        }
    }

    /*private void addTask(String title) {
        String author = mAuth.getCurrentUser().getEmail();
        String date = Calendar.getInstance().getTime().toString();


        if (user != null) {
            final String id = databaseTasks.push().getKey();
            final TaskModel newTask = new TaskModel(title, author, date, category);

            //Log.d(TAG, "taskModels ArrayList size: " + list_tasksModel.size());

            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    databaseTasks.child(id).setValue(newTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "TaskModel add: " + newTask.getTitle());
                                Toast.makeText(TasksActivity.this, "TaskModel added: " + newTask.getTitle(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TasksActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            mainThread.start();

        } else {
            Toast.makeText(this, "Must be Signed-In!", Toast.LENGTH_SHORT).show();
        }
    }
*/
    @Override
    public void onClick(View view) {
        if (view == ButtonAdd) {
            String title = TextTaskField.getText().toString().trim();
            TextTaskField.setText("");
            Log.d(TAG, "ButtonAdd clicked!");
            if (!TextUtils.isEmpty(title)) {
                // addTask(title);

            } else {
                Toast.makeText(TasksActivityORIGINAL.this, "TaskModel can't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //selectedFragment = new HomeFragment();
                    Intent intentMain = new Intent(TasksActivityORIGINAL.this, MainActivity.class);
                    startActivity(intentMain);
                    break;
                case R.id.navigation_tasks:
                    //selectedFragment = new TasksFragment();
                    Intent intentTasks = new Intent(TasksActivityORIGINAL.this, TasksActivityORIGINAL.class);
                    startActivity(intentTasks);
                    break;
                case R.id.navigation_profile:
                    //selectedFragment = new ProfileFragment();
                    Intent intentProfile = new Intent(TasksActivityORIGINAL.this, LoginActivity.class);
                    startActivity(intentProfile);
                    break;
            }
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            //return true

            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasklist_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                startActivity(new Intent(this, NewTaskActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
