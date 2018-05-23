package com.example.a16002749.opscpoe;

import android.app.AlertDialog;
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
import android.widget.Toast;

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

    private EditText weight;
    private FloatingActionButton add;
    private Button dateSelectButton;
    private String dateSelected = "";

    private TextView test;
    private final long WEEK_TO_MILISECONDS = 604800000;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.input_fragment, container, false);
        weight = view.findViewById(R.id.inputWeight);
        weight.setText("");
        add = view.findViewById(R.id.btnAdd);
        test = view.findViewById(R.id.test);
        dateSelectButton = view.findViewById(R.id.btnDate);
        add.setOnClickListener(addWeight);
        dateSelectButton.setOnClickListener(dateButtonClick);
        return view;
    }

    View.OnClickListener dateButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            DatePickerDialog dateSelector = new DatePickerDialog(getContext());
            dateSelector.setOnDateSetListener(userChangeDate);
            dateSelector.setTitle("Select Date For Weight Entry");
            dateSelector.show();
        }
    };

    DatePickerDialog.OnDateSetListener userChangeDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            dateSelected = day+"/"+month+"/"+year;
            Toast.makeText(getContext(), dateSelected, Toast.LENGTH_LONG).show();
        }
    };

    View.OnClickListener addWeight = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            //Flags to check if values are null
            boolean weightEntered = false;
            boolean dateEntered = false;
            String date = "";

            if(dateSelected != null && !dateSelected.isEmpty())
            {
                date = dateSelected;
                dateEntered = true;
            }
            else
            {
                AlertDialog.Builder formatError = new AlertDialog.Builder(getActivity());
                formatError.setTitle("No Date Selected");
                formatError.setMessage("Please Select a date");
                formatError.show();
            }


            String weightAchieved = "";
            if(weight.getText() != null && !weight.getText().toString().equals(""))
            {
                weightAchieved = weight.getText().toString();
                weightEntered = true;
            }
            else
            {
                AlertDialog.Builder formatError = new AlertDialog.Builder(getActivity());
                formatError.setTitle("No Weight Entered");
                formatError.setMessage("Please enter a weight achieved for the selected date");
                formatError.show();
            }

            if(weightEntered == true && dateEntered == true)
            {
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


                    Toast myToast = Toast.makeText(getContext(), "Weight saved", Toast.LENGTH_SHORT);
                    myToast.show();
                    weight.setText("");
                    weightEntered = false;
                    dateEntered = false;

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast myToast = Toast.makeText(getContext(), "Saving Weight Failed " + e.getMessage(), Toast.LENGTH_LONG);
                    myToast.show();
                }
            }

        }
    };

}
