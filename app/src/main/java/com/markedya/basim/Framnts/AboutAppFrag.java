package com.markedya.basim.Framnts;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.markedya.basim.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutAppFrag extends Fragment {
    private TextView textView;
    private StringBuilder text = new StringBuilder();


    public AboutAppFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);

        BufferedReader reader = null;

        setTextBar(view);

        try {
            reader = new BufferedReader(
                    new InputStreamReader(view.getContext().getAssets().open("about.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append(mLine);
                text.append('\n');
            }
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error reading file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }

            TextView output = (TextView) view.findViewById(R.id.summtext);
            output.setText((CharSequence) text);

            return view;
        }

    }

    @SuppressLint("SetTextI18n")
    public void setTextBar(View mView){
        View getActView = getActivity().findViewById(R.id.textBar);
        if (getActView instanceof TextView){
            TextView textView = (TextView)getActView ;

            textView.setText(getString(R.string.choosePart));

        }
    }

}
