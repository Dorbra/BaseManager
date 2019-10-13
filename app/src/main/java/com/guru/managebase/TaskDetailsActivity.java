package com.guru.managebase;

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

public class TaskDetailsActivity extends AppCompatActivity {

    private EditText mTitle;
    private Spinner mCategory;
    private Button update_button;
    private Button delete_button;
    private Button back_button;

    private String key;
    private String title;
    private String category;
    private String author;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        mAuth = FirebaseAuth.getInstance();
        key = getIntent().getStringExtra("key");
        title = getIntent().getStringExtra("title");
        category = getIntent().getStringExtra("category");
        author = mAuth.getCurrentUser().getEmail();
        
        mTitle = findViewById(R.id.title_editText);
        mCategory = findViewById(R.id.task_categories_spinner);
        update_button = findViewById(R.id.button_updateTask);
        delete_button = findViewById(R.id.button_deleteTask);
        back_button = findViewById(R.id.button_back);

        mCategory.setSelection(getIndexSpinnerItems(mCategory, category));

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskModel task = new TaskModel();
                task.setTitle(mTitle.getText().toString().trim());
                task.setCategory(mCategory.getSelectedItem().toString());
                task.setAuthor(author);

                Calendar calendar = Calendar.getInstance();
                String date = DateFormat.getDateInstance().format(calendar.getTime());
                task.setDate(date);

                new FirebaseDatabaseHelper().updateTask(key, task, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(ArrayList<TaskModel> tasks, ArrayList<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(TaskDetailsActivity.this, "Updated Task..", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FirebaseDatabaseHelper().deleteTask(key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(ArrayList<TaskModel> tasks, ArrayList<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(TaskDetailsActivity.this, "Task Deleted!", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                });
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }


    private int getIndexSpinnerItems(Spinner spinner, String item) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(item)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
