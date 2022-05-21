package ru.mirea.shamrin.mireaproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SubjectViewModel extends ViewModel {
    private final LiveData<List<Subject>> subjects;
    private final AppDatabase db = App.instance.getDatabase();
    private final SubjectDao subjectsDao = db.subjectDao();

    public SubjectViewModel(){ subjects = subjectsDao.getAll(); }

    public void addSubject(Subject subject){
        subjectsDao.insert(subject);
    }

    public LiveData<List<Subject>> getSubjectsLiveData(){
        return subjects;
    }

    public List<Subject> getSubjects(){
        return subjects.getValue();
    }
}
