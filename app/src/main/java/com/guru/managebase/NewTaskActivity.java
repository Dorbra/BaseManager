package com.guru.managebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity {

    private EditText mTitle;
    private Spinner mTask_categories_spinner;
    private Button mAdd_btn;
    private Button mBack_btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mAuth = FirebaseAuth.getInstance();
        mTitle = findViewById(R.id.title_editText);
        mTask_categories_spinner = findViewById(R.id.task_categories_spinner);
        mBack_btn = findViewById(R.id.button_back);
        mAdd_btn = findViewById(R.id.button_addTask);

        mAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskModel task = new TaskModel();
                Calendar calendar = Calendar.getInstance();
                String date = DateFormat.getDateInstance().format(calendar.getTime());
                String author = mAuth.getCurrentUser().getEmail();

                task.setTitle(mTitle.getText().toString());
                task.setAuthor(author);
                task.setDate(date);
                task.setCategory(mTask_categories_spinner.getSelectedItem().toString());

                new FirebaseDatabaseHelper().addTasks(task, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(ArrayList<TaskModel> tasks, ArrayList<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        mTitle.setText("");
                        Toast.makeText(NewTaskActivity.this, "Task was added!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(NewTaskActivity.this, TasksActivity.class));

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

        mBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });

    }
}
