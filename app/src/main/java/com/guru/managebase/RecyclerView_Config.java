package com.guru.managebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView_Config {
    private static final String TAG = "RecyclerView_Config";
    private Context mContext;
    private TasksAdapter mTasksAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, ArrayList<TaskModel> tasks, ArrayList<String> keys) {
        mContext = context;
        mTasksAdapter = new TasksAdapter(tasks, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mTasksAdapter);
    }

    class TaskItemView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitle;
        private TextView mAuthor;
        private TextView mDate;
        private TextView mCategory;

        private String key;

        public TaskItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.task_list_item, parent, false));

            mTitle = (TextView) itemView.findViewById(R.id.task_title);
            mAuthor = (TextView) itemView.findViewById(R.id.task_author);
            mDate = (TextView) itemView.findViewById(R.id.task_date);
            mCategory = (TextView) itemView.findViewById(R.id.task_category);

            mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TaskDetailsActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("title", mTitle.getText().toString());
                    intent.putExtra("category", mCategory.getText().toString());

                    mContext.startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TaskDetailsActivity.class);
                    Log.d(TAG, "TaskItem Clicked");
                    intent.putExtra("key", key);
                    intent.putExtra("title", mTitle.getText().toString());
                    intent.putExtra("category", mCategory.getText().toString());

                    mContext.startActivity(intent);
                }
            });
        }

        public void bind(TaskModel taskModel, String key) {
            mTitle.setText(taskModel.getTitle());
            mAuthor.setText(taskModel.getAuthor());
            mDate.setText(taskModel.getDate());
            mCategory.setText((taskModel.getCategory()));
            this.key = key;
        }

        @Override
        public void onClick(View view) {


        }
    }

    class TasksAdapter extends RecyclerView.Adapter<TaskItemView> {
        private List<TaskModel> mTaskList;
        private List<String> mKeys;

        public TasksAdapter(ArrayList<TaskModel> mTaskList, List<String> mKeys) {
            this.mTaskList = mTaskList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public TaskItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskItemView holder, int position) {
            holder.bind(mTaskList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mTaskList.size();
        }
    }


}
