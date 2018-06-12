package com.example.a16002749.opscpoe;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class statsFragment extends Fragment {

    public statsFragment() {
    }

    private TextView weight;
    private TextView weightGoal;
    private TextView stepsGoal;
    private TextView height;
    private SensorManager stepCountManager;
    private TextView steps;
    private FloatingActionButton fabAddSteps;
    private String stepsToday = "0";
    private String selectedDate = "";
    private Button setDate;
    private boolean dateIsSet = false;
    private Sensor stepCountSensor;
    private SharedPreferences colourPref;
    private double currentStepGoal= 0.0;
    private GraphView graph;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //This allows us to reference widgets attached to the fragment's layout
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        weight = view.findViewById(R.id.txtWeight);
        weightGoal = view.findViewById(R.id.txtWeightGoal);
        stepsGoal = view.findViewById(R.id.txtStepsGoal);
        height = view.findViewById(R.id.txtHeight);
        stepCountManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        steps = view.findViewById(R.id.txtSteps);
        fabAddSteps = view.findViewById(R.id.fabAddSteps);
        setDate = view.findViewById(R.id.btnDate);
        setDate.setOnClickListener(datePicker);
        fabAddSteps.setOnClickListener(fabStepAddClick);
        graph = view.findViewById(R.id.graph);


        drawGraph();
        //Initial launch
        //Fragment "dies" when heading to main screen so this runs every start up
        //Instead of this senseless killing, I might pause it
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //Get if set to metric or imperial
        String choices = preferences.getString(getString(R.string.metricsPref),"Failed");
        colourPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


        //Terrible way of converting between the two which does not allow the user to work in imperial or metric only metric then having the option to convert
        if(choices.equalsIgnoreCase("Metric"))
        {
            String iniWeight = "Weight: " +(preferences.getString(getString(R.string.editWeightKey),"0.0")) + "Kg";
            String iniWeightGoal = "Weight Goal: " + (preferences.getString(getString(R.string.editWeightGoal),"0.0")) + "Kg";
            String iniStepsGoal = "Steps Goal: "+ (preferences.getString(getString(R.string.editStepsGoal),"0"));
            String iniHeight = "Height: " + (preferences.getString(getString(R.string.editHeightKey),"0.0")) + "m";
            setInitialUserValues(iniWeight, iniWeightGoal, iniStepsGoal, iniHeight);

        }
        else
        {
            String iniWeight = "Weight: " + convertWeightToImperial(Double.parseDouble(preferences.getString(getString(R.string.editWeightKey),"0.0"))) + "lbs";
            String iniWeightGoal = "Weight Goal: " + convertWeightToImperial(Double.parseDouble(preferences.getString(getString(R.string.editWeightGoal),"0.0"))) + "lbs";
            String iniStepsGoal = "Steps Goal: " + preferences.getString(getString(R.string.editStepsGoal),"0");
            String iniHeight = "Height: " + convertHeightToImperial(Double.parseDouble(preferences.getString(getString(R.string.editHeightKey),"0.0"))) + " Feet";
            setInitialUserValues(iniWeight, iniWeightGoal, iniStepsGoal, iniHeight);

        }

        currentStepGoal = Double.parseDouble((preferences.getString(getString(R.string.editStepsGoal),"0")));

        //TODO: Check values from input.txt and draw graph
        try
        {
            //File reading source
            //https://stackoverflow.com/questions/12421814/how-can-i-read-a-text-file-in-android

            String filePath = getContext().getFilesDir().getAbsolutePath();
            File inputFile = new File(filePath + "/input/input.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line= "";
            String fileOut = "";

            //IF there is a line to be read it appends it to the fileOut variable
            while((line = reader.readLine())!=null)
            {
                fileOut += line + "\n";
            }

            //Testing toast
            //Toast.makeText(getContext(), fileOut, Toast.LENGTH_LONG).show();

            //TODO: Explode lines in fileOut
            //TODO: Possibly put them in a collection within above while loop
            //TODO: Setup graph to display user's weight progress from file information

        }
        catch(IOException e)//Catches exception on first run as file will not exist
        {
            Log.e("File Read Fail", e.getMessage() + " stack: " + e.getStackTrace());
        }
        //Return inflated view for display
        return view;
    }

    //Source for step counting
    //Source: http://www.edumobile.org/android/stepcounter-app-with-android-kitkat-4-4/
    @Override
    public void onResume()
    {

        super.onResume();
        //Creates a new sensor that handles step counting
        stepCountSensor = stepCountManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Check that not null and register sensor to sensor manager
        if(stepCountSensor != null)
        {
            //Registers step counter to sensor manager an in this method assigns it a listener
            stepCountManager.registerListener(stepEvent, stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else
        {
            Toast.makeText(getActivity(), "Sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    SensorEventListener stepEvent = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            try
            {
                colourPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                steps.setText(String.valueOf(sensorEvent.values[0]));
                if((sensorEvent.values[0] / currentStepGoal ) < 0.33)
                {
                    steps.setTextColor(getResources().getColor(R.color.oneThirdComplete, null));
                }
                else if (sensorEvent.values[0] < currentStepGoal)
                {
                    steps.setTextColor(getResources().getColor(R.color.twoThirdComplete, null));
                }
                else if (sensorEvent.values[0] >= currentStepGoal)
                {
                    steps.setTextColor(getResources().getColor(R.color.complete, null));
                }
                stepsToday = String.valueOf(sensorEvent.values[0]);
            }
            catch(NullPointerException e)
            {
                Log.e("ColourCrash", e.getStackTrace().toString());
            }
            catch(Exception e)
            {
                Log.e("ColourCrash", e.getStackTrace().toString());
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    View.OnClickListener datePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog dateSelector = new DatePickerDialog(getContext());
            dateSelector.setOnDateSetListener(userChangeDate);
            dateSelector.setTitle("Select Date For Steps Entry");
            dateSelector.show();
        }
    };

    DatePickerDialog.OnDateSetListener userChangeDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            selectedDate = day+"/"+(month+1)+"/"+year; //Months start from 0??
            Toast.makeText(getContext(), selectedDate, Toast.LENGTH_LONG).show();
            dateIsSet = true;
        }
    };

    View.OnClickListener fabStepAddClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {

            if(dateIsSet == true)
            {
                //TODO: Save steps for today and update graph
                if(stepsToday == null)
                {
                    stepsToday = "0";
                }
                String fileContents = stepsToday + "," + selectedDate + "\n";
                FileOutputStream outputStream;
                String filePath = getContext().getFilesDir().getAbsolutePath();
                File newfile = new File(filePath + "/input");
                File inputFile = new File(filePath + "/input/steps.txt");

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


                    Toast myToast = Toast.makeText(getContext(), "Steps saved", Toast.LENGTH_SHORT);
                    myToast.show();



                    // Update graph
                    drawGraph();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast myToast = Toast.makeText(getContext(), "Saving Steps Failed " + e.getMessage(), Toast.LENGTH_LONG);
                    myToast.show();
                }
            }
            else
            {
                Toast myToast = Toast.makeText(getContext(), "No Date Set", Toast.LENGTH_SHORT);
                myToast.show();
            }

        }
    };



    //Drawing graph from values in file
    public void drawGraph()
    {
        //TODO: Fetch graph info and draw it
        try
        {
            //File reading source
            //https://stackoverflow.com/questions/12421814/how-can-i-read-a-text-file-in-android
            ArrayList<String[]>inputs = new ArrayList<>();
            String inFilePath = getContext().getFilesDir().getAbsolutePath();
            File inInputFile = new File(inFilePath + "/input/steps.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inInputFile));
            String line= "";
            String fileOut = "";
            String lastSteps = "";
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

                lastSteps = weightInputs[0];
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

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            double laststepsCon = Double.parseDouble(lastSteps);
            double stepsGoal = Double.parseDouble(pref.getString(getString(R.string.editStepsGoal), "0"));

            if((laststepsCon - stepsGoal) <= 50)
            {
                series.setColor(Color.RED);
            }
            else if ((laststepsCon - stepsGoal)  <= 25)
            {
                series.setColor(Color.rgb(255, 127, 80));//Orange
            }
            else if(laststepsCon - stepsGoal >= stepsGoal)
            {
                series.setColor(Color.GREEN);
            }
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

    //Set Initial values for text
    public void setInitialUserValues(String weight, String weightGoal, String stepsGoal, String height)
    {
        this.weight.setText(weight);
        this.weightGoal.setText(weightGoal);
        this.stepsGoal.setText(stepsGoal);
        this.height.setText(height);

    }
    //Updates UI based on preference changes
    //To Metric
    public void convertImperialHereticsToMetric()
    {
        String weightConvert = weight.getText().toString();
        String weightGoalConvert = weightGoal.getText().toString();
        String heightConvert = height.getText().toString();

        weightConvert = convertWeightToMetric(Double.parseDouble(weightConvert)) + "";
        weightGoalConvert = convertWeightToMetric(Double.parseDouble(weightGoalConvert)) + "";
        heightConvert = convertHeightToMetric(Double.parseDouble(heightConvert)) + "";

        weight.setText(weightConvert);
        weightGoal.setText(weightGoalConvert);
        height.setText(heightConvert);
    }
    //To Imperial
    public void convertMetricHereticsToImperial()
    {
        String weightConvert = weight.getText().toString();
        String weightGoalConvert = weightGoal.getText().toString();
        String heightConvert = height.getText().toString();

        weightConvert = convertWeightToImperial(Double.parseDouble(weightConvert)) + "";
        weightGoalConvert = convertWeightToImperial(Double.parseDouble(weightGoalConvert)) + "";
        heightConvert = convertHeightToImperial(Double.parseDouble(heightConvert)) + "";

        weight.setText(weightConvert);
        weightGoal.setText(weightGoalConvert);
        height.setText(heightConvert);
    }
    //Conversion methods based on the preference changes
    private double convertWeightToMetric(double imperialHeretic)
    {
        final double LBS_IN_KG = 0.453592;
        double conversion = imperialHeretic * LBS_IN_KG;
        return  conversion;
    }

    private double convertHeightToMetric(double imperialHeretic)
    {
        final double FEET_IN_METERS = 0.3048;
        double conversion = imperialHeretic * FEET_IN_METERS;
        return conversion;
    }

    private double convertWeightToImperial(double metricHeretic)
    {
        final double KG_IN_LBS = 2.20462;
        double conversion = metricHeretic * KG_IN_LBS;
        return conversion;
    }

    private double convertHeightToImperial(double metricHeretic)
    {
        final double METERS_IN_FEET = 3.28084;
        double conversion = metricHeretic * METERS_IN_FEET;
        return conversion;
    }


}
