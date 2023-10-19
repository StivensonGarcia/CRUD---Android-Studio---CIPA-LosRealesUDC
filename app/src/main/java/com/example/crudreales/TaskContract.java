package com.example.crudreales;

import android.provider.BaseColumns;

public final class TaskContract {
    private TaskContract() {
    }

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String COLUMN_YEAR = "year"; // Año
        public static final String COLUMN_MONTH = "month"; // Mes
        public static final String COLUMN_DAY = "day"; // Día
        public static final String COLUMN_HOUR = "hour"; // Hora
        public static final String COLUMN_MINUTE = "minute"; // Minuto
        public static final String COLUMN_DAYS = "days"; // Días de la semana
    }
}
