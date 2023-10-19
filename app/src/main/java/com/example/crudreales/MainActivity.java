package com.example.crudreales;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.DialogInterface;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TaskDBHelper dbHelper;
    private TaskAdapter taskAdapter;
    private long selectedTaskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new TaskDBHelper(this);
        ListView listView = findViewById(R.id.taskListView);
        taskAdapter = new TaskAdapter(this, getAllTasks());
        listView.setAdapter(taskAdapter);


        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTaskId = id; // Almacenar el ID de la tarea seleccionada
                showEditTaskDialog(id);
            }
        });
    }

    private Cursor getAllTasks() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return database.query(TaskContract.TaskEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private void showAddTaskDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Tarea");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        builder.setView(view);

        final EditText taskEditText = view.findViewById(R.id.taskEditText);
        final DatePicker taskDatePicker = view.findViewById(R.id.taskDatePicker);
        final TimePicker taskTimePicker = view.findViewById(R.id.taskTimePicker);
        final Spinner taskDaysSpinner = view.findViewById(R.id.taskDaysSpinner);


        List<String> daysList = new ArrayList<>();
        daysList.add("Lunes");
        daysList.add("Martes");
        daysList.add("Miércoles");
        daysList.add("Jueves");
        daysList.add("Viernes");
        daysList.add("Sábado");
        daysList.add("Domingo");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daysList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskDaysSpinner.setAdapter(adapter);

        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String task = taskEditText.getText().toString().trim();
                int year = taskDatePicker.getYear();
                int month = taskDatePicker.getMonth();
                int day = taskDatePicker.getDayOfMonth();
                int hour = taskTimePicker.getCurrentHour();
                int minute = taskTimePicker.getCurrentMinute();
                String selectedDay = taskDaysSpinner.getSelectedItem().toString();

                if (!task.isEmpty()) {
                    addTask(task, year, month, day, hour, minute, selectedDay);
                    taskAdapter.changeCursor(getAllTasks());
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    private void showEditTaskDialog(final long taskId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Tarea");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        builder.setView(view);

        final EditText taskEditText = view.findViewById(R.id.taskEditText);
        final DatePicker taskDatePicker = view.findViewById(R.id.taskDatePicker);
        final TimePicker taskTimePicker = view.findViewById(R.id.taskTimePicker);
        final Spinner taskDaysSpinner = view.findViewById(R.id.taskDaysSpinner);


        List<String> daysList = new ArrayList<>();
        daysList.add("Lunes");
        daysList.add("Martes");
        daysList.add("Miércoles");
        daysList.add("Jueves");
        daysList.add("Viernes");
        daysList.add("Sábado");
        daysList.add("Domingo");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daysList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskDaysSpinner.setAdapter(adapter);

        taskEditText.setText(getTask(taskId));
        taskDatePicker.updateDate(getYear(taskId), getMonth(taskId), getDay(taskId));
        taskTimePicker.setHour(getHour(taskId));
        taskTimePicker.setMinute(getMinute(taskId));
        String selectedDay = getDays(taskId);
        int position = daysList.indexOf(selectedDay);
        if (position >= 0) {
            taskDaysSpinner.setSelection(position);
        }

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String task = taskEditText.getText().toString().trim();
                int year = taskDatePicker.getYear();
                int month = taskDatePicker.getMonth();
                int day = taskDatePicker.getDayOfMonth();
                int hour = taskTimePicker.getCurrentHour();
                int minute = taskTimePicker.getCurrentMinute();
                String selectedDay = taskDaysSpinner.getSelectedItem().toString();

                if (!task.isEmpty()) {
                    updateTask(taskId, task, year, month, day, hour, minute, selectedDay);
                    taskAdapter.changeCursor(getAllTasks());
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    private void addTask(String task, int year, int month, int day, int hour, int minute, String days) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE, task);
        values.put(TaskContract.TaskEntry.COLUMN_COMPLETED, 0);
        values.put(TaskContract.TaskEntry.COLUMN_YEAR, year);
        values.put(TaskContract.TaskEntry.COLUMN_MONTH, month);
        values.put(TaskContract.TaskEntry.COLUMN_DAY, day);
        values.put(TaskContract.TaskEntry.COLUMN_HOUR, hour);
        values.put(TaskContract.TaskEntry.COLUMN_MINUTE, minute);
        values.put(TaskContract.TaskEntry.COLUMN_DAYS, days);
        long newRowId = database.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Tarea agregada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al agregar la tarea", Toast.LENGTH_SHORT).show();
        }
    }

    private String getTask(long taskId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry.COLUMN_TITLE},
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TITLE));
        }

        return "";
    }

    private int getYear(long taskId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry.COLUMN_YEAR},
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_YEAR));
        }

        return 0;
    }

    private int getMonth(long taskId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry.COLUMN_MONTH},
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_MONTH));
        }

        return 0;
    }

    private int getDay(long taskId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry.COLUMN_DAY},
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DAY));
        }

        return 0;
    }

    private int getHour(long taskId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry.COLUMN_HOUR},
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_HOUR));
        }

        return 0;
    }

    private int getMinute(long taskId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry.COLUMN_MINUTE},
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_MINUTE));
        }

        return 0;
    }

    private String getDays(long taskId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry.COLUMN_DAYS},
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DAYS));
        }

        return "";
    }

    private void updateTask(long taskId, String task, int year, int month, int day, int hour, int minute, String days) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE, task);
        values.put(TaskContract.TaskEntry.COLUMN_YEAR, year);
        values.put(TaskContract.TaskEntry.COLUMN_MONTH, month);
        values.put(TaskContract.TaskEntry.COLUMN_DAY, day);
        values.put(TaskContract.TaskEntry.COLUMN_HOUR, hour);
        values.put(TaskContract.TaskEntry.COLUMN_MINUTE, minute);
        values.put(TaskContract.TaskEntry.COLUMN_DAYS, days);
        int rowsAffected = database.update(TaskContract.TaskEntry.TABLE_NAME, values,
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar la tarea", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {

            if (selectedTaskId != -1) {
                showEditTaskDialog(selectedTaskId);
            } else {
                Toast.makeText(this, "Selecciona una tarea para editar", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.action_delete) {

            if (selectedTaskId != -1) {
                deleteTask(selectedTaskId);
                taskAdapter.changeCursor(getAllTasks());
                selectedTaskId = -1;
            } else {
                Toast.makeText(this, "Selecciona una tarea para eliminar", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.action_complete) {

            if (selectedTaskId != -1) {
                completeTask(selectedTaskId);
                taskAdapter.changeCursor(getAllTasks());
                selectedTaskId = -1;
            } else {
                Toast.makeText(this, "Selecciona una tarea para marcar como completada", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.action_incomplete) {

            if (selectedTaskId != -1) {
                markTaskIncomplete(selectedTaskId);
                taskAdapter.changeCursor(getAllTasks());
                selectedTaskId = -1;
            } else {
                Toast.makeText(this, "Selecciona una tarea para marcar como no completada", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void deleteTask(long taskId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted = database.delete(TaskContract.TaskEntry.TABLE_NAME,
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)});

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar la tarea", Toast.LENGTH_SHORT).show();
        }
    }

    private void completeTask(long taskId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_COMPLETED, 1);
        int rowsUpdated = database.update(TaskContract.TaskEntry.TABLE_NAME, values,
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)});

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Tarea marcada como completada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al marcar la tarea como completada", Toast.LENGTH_SHORT).show();
        }
    }

    private void markTaskIncomplete(long taskId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_COMPLETED, 0);
        int rowsUpdated = database.update(TaskContract.TaskEntry.TABLE_NAME, values,
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(taskId)});

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Tarea marcada como no completada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al marcar la tarea como no completada", Toast.LENGTH_SHORT).show();
        }
    }

}
