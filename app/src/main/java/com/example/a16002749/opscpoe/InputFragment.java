package com.example.a16002749.opscpoe;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

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
    private GraphView graph;
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
        graph = view.findViewById(R.id.graph);

        drawGraph();
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
            dateSelected = day+"/"+(month+1)+"/"+year; //Months start from 0??
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

                    // Update graph
                    drawGraph();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast myToast = Toast.makeText(getContext(), "Saving Weight Failed " + e.getMessage(), Toast.LENGTH_LONG);
                    //myToast.show();
                }
            }

        }
    };

    private void drawGraph()
    {
        // Update graph
        //Graph source:
        // http://www.android-graphview.org/
        try
        {
            //File reading source
            //https://stackoverflow.com/questions/12421814/how-can-i-read-a-text-file-in-android
            ArrayList<String[]>inputs = new ArrayList<>();
            String inFilePath = getContext().getFilesDir().getAbsolutePath();
            File inInputFile = new File(inFilePath + "/input/input.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inInputFile));
            String line= "";
            String fileOut = "";

            //IF there is a line to be read it appends it to the fileOut variable
            while((line = reader.readLine())!=null)
            {
                fileOut = line;
                inputs.add(fileOut.split(","));
            }

            //Series for the graph
            String monthForTitle = "";
            String yearForTitle = "";
            ArrayList<DataPoint> dataForGraph = new ArrayList<>();
            ArrayList<String> labelsForGraph = new ArrayList<>();

            //Collection of arrays with entries
            for (String[] weightInputs : inputs)
            {
                //Source: https://www.javatpoint.com/java-string-to-date
                String [] dateFromEntry = weightInputs[1].split("/");
                yearForTitle = dateFromEntry[2];

                switch(dateFromEntry[1])//Assigns appropriate month for title
                {
                    case "1":
                        monthForTitle = "Jan";
                        break;
                    case "2":
                        monthForTitle = "Feb";
                        break;
                    case "3":
                        monthForTitle = "Mar";
                        break;
                    case "4":
                        monthForTitle = "Apr";
                        break;
                    case "5":
                        monthForTitle = "May";
                        break;
                    case "6":
                        monthForTitle = "Jun";
                        break;
                    case "7":
                        monthForTitle = "Jul";
                        break;
                    case "8":
                        monthForTitle = "Aug";
                        break;
                    case "9":
                        monthForTitle = "Sep";
                        break;
                    case "10":
                        monthForTitle = "Oct";
                        break;
                    case "11":
                        monthForTitle = "Nov";
                        break;
                    case "12":
                        monthForTitle = "Dec";
                        break;
                    default:
                        monthForTitle = "";
                        break;
                }

                DateFormat dateForFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat stringFormatter = new SimpleDateFormat("dd/MM/yyyy");
                Date formattedDate = dateForFormat.parse(weightInputs[1]);

                //Build list for graph data points consisting of actual date values
                dataForGraph.add(new DataPoint(formattedDate,Double.parseDouble(weightInputs[0])));

                //Build list out of this reformatted date but use formattedDate Date obj for actual data
                String dateForLabel = stringFormatter.format(formattedDate);
                labelsForGraph.add(dateForLabel);
            }

            int numOfLabels = 0;

            //labelFormatter.setHorizontalLabels(allLabels);
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        //
                        SimpleDateFormat stringFormatter = new SimpleDateFormat("dd");
                        String dateForLabel = stringFormatter.format(value);
                        return dateForLabel;

                    }
                    else
                    {
                        return value+"";
                    }
                }
            });



            DataPoint[] dataPointsAsArray = new DataPoint[dataForGraph.size()];
            for(int i = 0; i < dataForGraph.size(); i++)
            {
                dataPointsAsArray[i] = dataForGraph.get(i);
            }

            //Assigns data points to ListGraphSeries
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointsAsArray);

            graph.removeAllSeries();
            graph.setTitle(monthForTitle + " " + yearForTitle);
            graph.addSeries(series);

        }
        catch(IOException e)//Catches exception on first run as file will not exist
        {
            Log.e("File Read Fail", e.getMessage() + " stack: " + e.getStackTrace());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.e("Graph or file handled exception", e.getStackTrace().toString());
            //Toast aToast = Toast.makeText(getContext(), e.getMessage() + "Done Goofed", Toast.LENGTH_LONG);
            //aToast.show();
        }

    }

}
