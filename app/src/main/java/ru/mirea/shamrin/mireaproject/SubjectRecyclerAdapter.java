package ru.mirea.shamrin.mireaproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectRecyclerAdapter extends RecyclerView.Adapter<SubjectRecyclerAdapter.SubjectViewHolder> {
    private final List<Subject> subjects;

    SubjectRecyclerAdapter(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public SubjectRecyclerAdapter.SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject, parent, false);
        return new SubjectRecyclerAdapter.SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubjectRecyclerAdapter.SubjectViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.nameSubject.setText(subject.getNameSubject());
        holder.teacher.setText(subject.getTeacher());
        holder.typeOfControl.setText(subject.getTypeOfControl());
    }

    @Override
    public int getItemCount() { return subjects.size(); }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        final TextView nameSubject, teacher, typeOfControl;
        SubjectViewHolder(View view){
            super(view);
            nameSubject = view.findViewById(R.id.nameSubject);
            teacher = view.findViewById(R.id.teacher);
            typeOfControl = view.findViewById(R.id.typeOfControl);
        }
    }
}
