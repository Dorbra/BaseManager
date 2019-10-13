package com.guru.managebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TasksActivity";
    private Button ButtonAdd;

    private RecyclerView mTasksList;

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    // Tasks List and Model Arraylist
    private RecyclerView recyclerView_tasks;

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

        // init Firebase User
        user = mAuth.getCurrentUser();

        // NEW RECYCLER VIEW 12.10
        recyclerView_tasks = (RecyclerView) findViewById(R.id.recyclerView_tasks);
        recyclerView_tasks.setNestedScrollingEnabled(false);
        new FirebaseDatabaseHelper().getTasks(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<TaskModel> tasks, ArrayList<String> keys) {
                Log.d(TAG, "### Tasks: " + tasks.size());
                new RecyclerView_Config().setConfig(recyclerView_tasks, TasksActivity.this, tasks, keys);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        if (user == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
            startActivity(new Intent(TasksActivity.this, NewTaskActivity.class));
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentMain = new Intent(TasksActivity.this, MainActivity.class);
                    startActivity(intentMain);
                    break;
                case R.id.navigation_tasks:
                    Intent intentTasks = new Intent(TasksActivity.this, TasksActivity.class);
                    startActivity(intentTasks);
                    break;
                case R.id.navigation_profile:
                    Intent intentProfile = new Intent(TasksActivity.this, LoginActivity.class);
                    startActivity(intentProfile);
                    break;
            }
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
