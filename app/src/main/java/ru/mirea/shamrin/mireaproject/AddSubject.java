package ru.mirea.shamrin.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddSubject extends AppCompatActivity {
    private EditText editSubject;
    private EditText editTeacher;
    private EditText editTypeOfControl;
    private Button btnSaveNewSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);


        editSubject = findViewById(R.id.editSubject);
        editTeacher = findViewById(R.id.editTeacher);
        editTypeOfControl = findViewById(R.id.editTypeOfControl);

        btnSaveNewSubject = findViewById(R.id.btnSaveNewSubject);
        btnSaveNewSubject.setOnClickListener(this::saveSubject);
    }

    public void saveSubject(View view) {
        AppDatabase database = App.getInstance().getDatabase();
        SubjectDao subjectDao = database.subjectDao();
        subjectDao.insert(new Subject(editSubject.getText().toString(), editTeacher.getText().toString(), editTypeOfControl.getText().toString()));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
