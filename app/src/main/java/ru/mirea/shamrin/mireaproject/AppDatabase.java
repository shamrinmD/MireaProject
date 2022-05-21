package ru.mirea.shamrin.mireaproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Student.class, Subject.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();
    public abstract SubjectDao subjectDao();
}
