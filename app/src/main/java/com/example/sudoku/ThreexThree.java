package com.example.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ThreexThree extends Fragment {
    private View view;
    private int groupId;
    private OnFragmentInteractionListener mListener;

    public ThreexThree() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_three_x_three, container, false);

        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4,
                R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};
        for (int tv : textViews) {
            TextView textView = view.findViewById(tv);
            textView.setOnClickListener(new View.OnClickListener() {
                //które poe zostalo klikniete
                @Override
                public void onClick(View view) {
                    mListener.onFragmentInteraction(groupId, Integer.parseInt(view.getTag().toString()), view);
                }
            });
        }
        return view;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    //wczytywanie tablicy
    public void setValue(int position, int value) {
        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4,
                R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};
        //curentView = kwadracik
        TextView currentView = view.findViewById(textViews[position]);
        currentView.setText(String.valueOf(value));
        currentView.setTextColor(Color.BLACK);
        currentView.setTypeface(null, Typeface.BOLD);
    }

    public boolean checkGroupCorrect() {
        ArrayList<Integer> numbers = new ArrayList<>();
        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4,
                R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};
        for (int tv : textViews) {
            TextView textView = view.findViewById(tv);
            //pobieramy z pola dana wartosc
            int number = Integer.parseInt(textView.getText().toString());
            if (numbers.contains(number)) {
                return false;
            } else {
                numbers.add(number);
            }
        }
        return true;
    }

    //automatycznie wygenerowane
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int groupId, int cellId, View view);
    }
}
