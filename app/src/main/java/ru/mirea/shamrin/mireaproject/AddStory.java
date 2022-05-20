package ru.mirea.shamrin.mireaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddStory extends AppCompatActivity {
    private EditText nameEditStory;
    private EditText ageEditStory;
    private EditText universityEditStory;
    private Button btnSaveNewStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        nameEditStory = findViewById(R.id.editStudentName);
        ageEditStory = findViewById(R.id.editStudentAge);
        universityEditStory = findViewById(R.id.editUniversityName);
        btnSaveNewStory = findViewById(R.id.btnSaveNewStory);
        btnSaveNewStory.setOnClickListener(this::saveStory);
    }

    public void saveStory(View view) {
        AppDatabase db = App.getInstance().getDatabase();
        StudentDao studentDao = db.studentDao();

        studentDao.insert(new Student(nameEditStory.getText().toString(), ageEditStory.getText().toString(), universityEditStory.getText().toString()));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}