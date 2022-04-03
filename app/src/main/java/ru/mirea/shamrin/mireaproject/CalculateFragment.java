package ru.mirea.shamrin.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText firstNum;
    EditText secondNum;
    Button btnPlus;
    Button btnMinus;
    Button btnMult;
    Button btnDiv;
    TextView resultCalc;
    String operation = "";

    public CalculateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculateFragment newInstance(String param1, String param2) {
        CalculateFragment fragment = new CalculateFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculate, container, false);
        firstNum = view.findViewById(R.id.firstNumber);
        secondNum = view.findViewById(R.id.secondNumber);
        btnPlus = view.findViewById(R.id.buttonPlus);
        btnMinus = view.findViewById(R.id.buttonMinus);
        btnMult = view.findViewById(R.id.buttonMult);
        btnDiv = view.findViewById(R.id.buttonDiv);
        resultCalc = view.findViewById(R.id.result);
        processor(view,resultCalc,btnPlus);
        processor(view,resultCalc,btnMinus);
        processor(view,resultCalc,btnMult);
        processor(view, resultCalc, btnDiv);
        return view;
    }
    public void processor(View view,TextView textView, Button button){
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                float num1 = 0;
                float num2 = 0;
                float result = 0;
                //Заполняем переменные числами
                num1 = Float.parseFloat(firstNum.getText().toString());
                num2 = Float.parseFloat(secondNum.getText().toString());
                //Определяем нажатую кнопку и выполняем соответствующую операцию
                switch (v.getId()) {
                    case R.id.buttonPlus:
                        operation = "+";
                        result = num1 + num2;
                        break;
                    case R.id.buttonMinus:
                        operation = "-";
                        result = num1 - num2;
                        break;
                    case R.id.buttonMult:
                        operation = "*";
                        result = num1 * num2;
                        break;
                    case R.id.buttonDiv:
                        operation = "/";
                        result = num1 / num2;
                        break;
                    default:
                        break;
                }
                //Строка вывода
                resultCalc.setText(num1 + " " + operation + " " + num2 + " = " + result);
            }
        });
    }
}