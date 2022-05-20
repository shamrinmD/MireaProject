package ru.mirea.shamrin.mireaproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class StoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton btnAddStory;
    private List<Student> students;
    private StudentDao studentDao;
    private AppDatabase db;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StoriesFragment() {
        // Required empty public constructor
    }

    public static StoriesFragment newInstance(String param1, String param2) {
        StoriesFragment fragment = new StoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stories, container, false);
        students = new ArrayList<>();
        btnAddStory = view.findViewById(R.id.btnAddStory);
        btnAddStory.setOnClickListener(this::onClickAddStory);
        db = App.getInstance().getDatabase();
        studentDao = db.studentDao();
        students = studentDao.getAll();
        students.add(new Student("Ivan", "20", "Mirea"));
        students.add(new Student("Petya", "19", "MSU"));
        students.add(new Student("Artem", "21", "Mirea"));
        recyclerView = view.findViewById(R.id.recycleView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), students);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void onClickAddStory(View view) {
        Intent intent = new Intent(getActivity(), AddStory.class);
        startActivity(intent);
    }
}