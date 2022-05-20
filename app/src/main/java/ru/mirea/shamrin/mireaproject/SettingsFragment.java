package ru.mirea.shamrin.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class SettingsFragment extends Fragment {
    private EditText nameText;
    private EditText ageText;
    private EditText universityText;
    private Button saveButton;
    private SharedPreferences preferences;
    final String SAVED_NAME = "saved_name";
    final String SAVED_AGE = "saved_age";
    final String SAVED_UNIVERSITY = "saved_university";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        nameText = view.findViewById(R.id.editName);
        ageText = view.findViewById(R.id.editAge);
        universityText = view.findViewById(R.id.editUniversity);
        saveButton = view.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(this::onCLickSaveSettings);
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        loadSettings();
        return view;
    }

    private void onCLickSaveSettings(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVED_NAME, nameText.getText().toString());
        editor.putString(SAVED_AGE, ageText.getText().toString());
        editor.putString(SAVED_UNIVERSITY, universityText.getText().toString());
        editor.apply();
    }

    private void loadSettings () {
        try {
            nameText.setText(preferences.getString(SAVED_NAME, null));
            ageText.setText(preferences.getString(SAVED_AGE, null));
            universityText.setText(preferences.getString(SAVED_UNIVERSITY, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}