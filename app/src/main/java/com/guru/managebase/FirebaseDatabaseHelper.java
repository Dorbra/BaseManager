package com.guru.managebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseDatabaseHelper {
    private static final String TAG = "FirebaseDatabaseHelper";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceTasks;
    private ArrayList<TaskModel> tasks = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(ArrayList<TaskModel> tasks, ArrayList<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceTasks = mDatabase.getReference("Tasks");
    }

    public void getTasks(final DataStatus dataStatus) {
        mReferenceTasks.addValueEventListener(new ValueEventListener() {
            @Override
            // Async method
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tasks.clear();
                ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Log.d(TAG, "tasks: " + tasks.size());
                    keys.add(keyNode.getKey());
                    TaskModel task = keyNode.getValue(TaskModel.class);
                    tasks.add(task);
                }
                dataStatus.DataIsLoaded(tasks, keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addTasks(TaskModel task, final DataStatus dataStatus) {
        String key = mReferenceTasks.push().getKey();
        mReferenceTasks.child(key).setValue(task).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void updateTask(String key, TaskModel task, final DataStatus dataStatus) {
        mReferenceTasks.child(key).setValue(task).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();

            }
        });
    }

    public void deleteTask(String key, final DataStatus dataStatus) {
        mReferenceTasks.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }

}
