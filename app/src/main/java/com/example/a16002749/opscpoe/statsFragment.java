package com.example.a16002749.opscpoe;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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



        //Initial launch
        //Fragment "dies" when heading to main screen so this runs every start up
        //Instead of this senseless killing, I might pause it
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        //Get if set to metric or imperial
        String choices = preferences.getString(getString(R.string.metricsPref),"Failed");





        //Shitty way of converting between the two which does not allow the user to work in imperial or metric only metric then having the option to convert
        if(choices.equalsIgnoreCase("Metric"))
        {
            String iniWeight = (preferences.getString(getString(R.string.editWeightKey),""));
            String iniWeightGoal = (preferences.getString(getString(R.string.editWeightGoal),""));
            String iniStepsGoal = (preferences.getString(getString(R.string.editStepsGoal),""));
            String iniHeight = (preferences.getString(getString(R.string.editHeightKey),""));
            setInitialUserValues(iniWeight, iniWeightGoal, iniStepsGoal, iniHeight);
        }
        else
        {
            String iniWeight = convertWeightToImperial(Double.parseDouble(preferences.getString(getString(R.string.editWeightKey),""))) + "";
            String iniWeightGoal = convertWeightToImperial(Double.parseDouble(preferences.getString(getString(R.string.editWeightGoal),""))) + "";
            String iniStepsGoal = preferences.getString(getString(R.string.editStepsGoal),"");
            String iniHeight = convertHeightToImperial(Double.parseDouble(preferences.getString(getString(R.string.editHeightKey),""))) + "";
            setInitialUserValues(iniWeight, iniWeightGoal, iniStepsGoal, iniHeight);
        }


        //Return inflated view for display
        return view;
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
