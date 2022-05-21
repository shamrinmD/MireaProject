package ru.mirea.shamrin.mireaproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class SubjectsFragment extends Fragment {

    private FloatingActionButton btnNewSubject;
    private RecyclerView subjectRecycler;
    private SubjectViewModel subjectViewModel;
    private SubjectRecyclerAdapter subjectRecyclerAdapter;
    private ActivityResultLauncher<Intent> launcher;
    private static List<Subject> subjects;
    public static final int ADD_CAT_RESULT_CODE = 1;
    public static final String SUBJECT_LABEL="SUBJECT";
    public static final String TEACHER_LABEL="TEACHER";
    public static final String TYPE_LABEL="TYPE_OF_CONTROL";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects, container, false);
        subjects = new ArrayList<>();
        subjectRecyclerAdapter = new SubjectRecyclerAdapter(subjects);

        if (getActivity() != null) {
            subjectViewModel = new ViewModelProvider(getActivity()).
                    get(SubjectViewModel.class);
            subjectViewModel.getSubjectsLiveData().observe(getActivity(), this::onChanged);
        }

        btnNewSubject = view.findViewById(R.id.btnAddSubject);
        btnNewSubject.setOnClickListener(this::onCLickNewSubject);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        subjectRecycler = view.findViewById(R.id.subjectView);
        subjectRecycler.setLayoutManager(mLayoutManager);
        subjectRecycler.setItemAnimator(new DefaultItemAnimator());
        subjectRecycler.setAdapter(subjectRecyclerAdapter);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                (result) -> {
                    if (result.getResultCode() == ADD_CAT_RESULT_CODE && result.getData() != null){
                        Subject subject = new Subject(result.getData().getStringExtra(SUBJECT_LABEL), result.getData().getStringExtra(TEACHER_LABEL), result.getData().getStringExtra(TYPE_LABEL));
                        subjectViewModel.addSubject(subject);
                        subjectRecyclerAdapter.notifyDataSetChanged();
                    }
                });
        return view;
    }

    public void onChanged(List<Subject> subjectDB) {
        if (!subjects.isEmpty()){
            subjects.clear();
        }
        subjects.addAll(subjectDB);
        subjectRecyclerAdapter.notifyDataSetChanged();
    }

    private void onCLickNewSubject(View view){
        Intent intent = new Intent(getActivity(), AddSubject.class);
        launcher.launch(intent);
    }
}