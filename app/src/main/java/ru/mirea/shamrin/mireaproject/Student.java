package ru.mirea.shamrin.mireaproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Student {
    @PrimaryKey(autoGenerate = true)
    public long id;
    private String name;
    private String age;
    private String university;
    public Student(String name, String age, String university) {
        this.name = name;
        this.age = age;
        this.university =university;
    }

    public String getName() {
        return name;
    }
    public String getAge() {
        return age;
    }
    public  String getUniversity() {
        return university;
    }
}
