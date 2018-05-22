package com.example.a16002749.opscpoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

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
    private ArrayList<String> metImpSwitch = new ArrayList();


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

        //Initial launch
        //Fragment "dies" when heading to main screen so this runs every start up
        //Instead of this senseless killing, I might pause it
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        //Get if set to metric or imperial
        String choices = preferences.getString(getString(R.string.metricsPref),"Failed");

        //Terrible way of converting between the two which does not allow the user to work in imperial or metric only metric then having the option to convert
        if(choices.equalsIgnoreCase("Metric"))
        {
            String iniWeight = (preferences.getString(getString(R.string.editWeightKey),"0.0")) + "Kg";
            String iniWeightGoal = (preferences.getString(getString(R.string.editWeightGoal),"0.0")) + "Kg";
            String iniStepsGoal = (preferences.getString(getString(R.string.editStepsGoal),"0"));
            String iniHeight = (preferences.getString(getString(R.string.editHeightKey),"0.0")) + "m";
            setInitialUserValues(iniWeight, iniWeightGoal, iniStepsGoal, iniHeight);
        }
        else
        {
            String iniWeight = convertWeightToImperial(Double.parseDouble(preferences.getString(getString(R.string.editWeightKey),"0.0"))) + "lbs";
            String iniWeightGoal = convertWeightToImperial(Double.parseDouble(preferences.getString(getString(R.string.editWeightGoal),"0.0"))) + "lbs";
            String iniStepsGoal = preferences.getString(getString(R.string.editStepsGoal),"0");
            String iniHeight = convertHeightToImperial(Double.parseDouble(preferences.getString(getString(R.string.editHeightKey),"0.0"))) + " Feet";
            setInitialUserValues(iniWeight, iniWeightGoal, iniStepsGoal, iniHeight);
        }

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

        }
        catch(IOException e)//Catches exception on first run as file will not exist
        {
            Log.e("File Read Fail", e.getMessage() + " stack: " + e.getStackTrace());
        }
        //Return inflated view for display
        return view;
    }

    //TODO: Reference site that implementation was carried from
    //Source for step counting
    //Source: http://www.edumobile.org/android/stepcounter-app-with-android-kitkat-4-4/
    @Override
    public void onResume()
    {
        super.onResume();
        //Creates a new sensor that hanles step counting
        Sensor stepCountSensor = stepCountManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Check that not null and register sensor to sensor manager
        if(stepCountSensor != null)
        {
            //Registers step counter to sensor manager an in this method assigns it a listener
            stepCountManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    steps.setText(String.valueOf(event.values[0]));
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            }, stepCountSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            Toast.makeText(getActivity(), "Sensor not available", Toast.LENGTH_SHORT).show();
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
