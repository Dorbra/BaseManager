package com.guru.managebase;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    View mView;
    TextView textTitle, textDate;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
        textTitle = mView.findViewById(R.id.task_title);
        textDate = mView.findViewById(R.id.task_date);

    }

    public void setTaskTitle(String title) {
        textTitle.setText(title);
    }

    public void setTaskDate(Date date) {
        textDate.setText(date.toString().trim());
    }


}
