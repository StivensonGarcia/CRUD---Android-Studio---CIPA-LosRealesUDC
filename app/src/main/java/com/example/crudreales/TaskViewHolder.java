package com.example.crudreales;

import android.view.View;
import android.widget.TextView;

public class TaskViewHolder {
    TextView titleView;
    TextView completedView;

    public TaskViewHolder(View view) {
        titleView = view.findViewById(R.id.taskTitle);
        completedView = view.findViewById(R.id.taskCompleted);
    }
}
