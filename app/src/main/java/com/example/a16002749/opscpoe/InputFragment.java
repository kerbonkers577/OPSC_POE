package com.example.a16002749.opscpoe;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Tristan Constable on 5/9/2018.
 */

public class InputFragment extends Fragment
{


    public InputFragment()
    {

    }

    private DatePicker datePicker;
    private EditText weight;
    private FloatingActionButton add;

    private TextView test;
    private final long WEEK_TO_MILISECONDS = 604800000;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.input_fragment, container, false);
        datePicker = view.findViewById(R.id.dtPicker);
        weight = view.findViewById(R.id.inputWeight);
        add = view.findViewById(R.id.btnAdd);
        test = view.findViewById(R.id.test);


        datePicker.setMinDate(System.currentTimeMillis() - WEEK_TO_MILISECONDS);
        //weight.addTextChangedListener(weightWatcher);
        add.setOnClickListener(addWeight);

        return view;
    }





    View.OnClickListener addWeight = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            //Write to memory
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            String date = day+"/"+month+"/"+year;
            //test.setText(stupid.getText() + " " +date);

            String weightAchieved = weight.getText().toString();
            String fileContents = weightAchieved+","+date+"\n";
            FileOutputStream outputStream;


            String filePath = getContext().getFilesDir().getAbsolutePath();
            File newfile = new File(filePath + "/input");
            File inputFile = new File(filePath + "/input/input.txt");

            try {
                if(!newfile.isDirectory())//If input dir doesn't exists
                {
                    newfile.mkdir();
                    if(!inputFile.isFile())//Check if file exists
                    {
                        newfile.createNewFile();
                        outputStream = new FileOutputStream(inputFile, true);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();
                    }
                    else {
                        outputStream = new FileOutputStream(inputFile, true);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();
                    }
                }
                else//If input exist
                {

                    if(!inputFile.isFile())//Check if file exists
                    {
                        newfile.createNewFile();
                        outputStream = new FileOutputStream(inputFile, true);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();
                    }
                    else {
                        outputStream = new FileOutputStream(inputFile, true);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
