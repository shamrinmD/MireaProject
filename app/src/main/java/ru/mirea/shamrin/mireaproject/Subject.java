package ru.mirea.shamrin.mireaproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subject {
    @PrimaryKey(autoGenerate = true)
    public long id;
    private String nameSubject;
    private String teacher;
    private String typeOfControl;

    public Subject(String nameSubject, String teacher, String typeOfControl) {
        this.nameSubject = nameSubject;
        this.teacher = teacher;
        this.typeOfControl = typeOfControl;
    }
    public String getNameSubject() { return nameSubject; }

    public String getTeacher() { return teacher; }

    public String getTypeOfControl() { return typeOfControl; }
}
