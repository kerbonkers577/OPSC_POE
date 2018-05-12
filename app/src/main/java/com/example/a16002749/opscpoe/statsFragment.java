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



        /*Testing
        String choices = preferences.getString(getString(R.string.metricsPref),"Failed");
        weight.setText(choices);*/
        //Set values to text for startup
        /*
        weight.setText(preferences.getString(getString(R.string.editWeightKey),""));
        weightGoal.setText(preferences.getString(getString(R.string.editWeightGoal),""));
        stepsGoal.setText(preferences.getString(getString(R.string.editStepsGoal),""));
        height.setText(preferences.getString(getString(R.string.editHeightKey),""));
        */

        //Return inflated view for display
        return view;
    }

    //Updates UI based on preference changes
    //To Metric
    private void convertImperialHereticsToMetric()
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
    private void convertMetricHereticsToImperial()
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
