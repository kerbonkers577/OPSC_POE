package com.example.a16002749.opscpoe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.preference.PreferenceFragment;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragment{

    private EditTextPreference editWeight;
    private EditTextPreference editWeightGoal;
    private EditTextPreference editHeight;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        //TODO:Set values of weight and height based on metric being used
        //TODO: Set default values
        //TODO:Retrieve values using get and put in shared preferences object

        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);
        //Manages validation on user weight changes
        //Finds the preference and attaches a listener for when it is changed
        editWeight = (EditTextPreference) findPreference(getString(R.string.editWeightKey));
        editWeightGoal = (EditTextPreference) findPreference(getString(R.string.editWeightGoal));
        editHeight = (EditTextPreference) findPreference(getString(R.string.editHeightKey));

        final Preference weightPref = getPreferenceScreen().findPreference(getString(R.string.editWeightKey));
        weightPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean testPassed = false;
                try
                {
                    //If it can format it will save it as successful
                    Double.parseDouble(newValue.toString());
                    testPassed = true;
                }
                catch(NumberFormatException e)
                {
                    AlertDialog.Builder formatError = new AlertDialog.Builder(getActivity());
                    formatError.setTitle("Incorrect input type");
                    formatError.setMessage("Only a number with a decimal can be entered");
                    formatError.show();
                }
                return testPassed;
            }
        });
        //Editing height info and validating user input
        Preference heightPref = getPreferenceScreen().findPreference(getString(R.string.editHeightKey));
        heightPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean testPassed = false;
                try
                {
                    //If it can format it will save it as successful
                    Double.parseDouble(newValue.toString());
                    testPassed = true;
                }
                catch(NumberFormatException e)
                {
                    AlertDialog.Builder formatError = new AlertDialog.Builder(getActivity());
                    formatError.setTitle("Incorrect input type");
                    formatError.setMessage("Only a number with a decimal can be entered");
                    formatError.show();
                }
                return testPassed;
            }
        });

        //Editing weight goal info and validating user input
        Preference weightGoalPref = getPreferenceScreen().findPreference(getString(R.string.editWeightGoal));
        weightGoalPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean testPassed = false;
                try
                {
                    //If it can format it will save it as successful
                    Double.parseDouble(newValue.toString());
                    testPassed = true;
                }
                catch(NumberFormatException e)
                {
                    AlertDialog.Builder formatError = new AlertDialog.Builder(getActivity());
                    formatError.setTitle("Incorrect input type");
                    formatError.setMessage("Only a number with a decimal can be entered");
                    formatError.show();
                }
                return testPassed;
            }
        });

        //Editing steps goal info and validating user input
        Preference stepsGoalPref = getPreferenceScreen().findPreference(getString(R.string.editStepsGoal));
        stepsGoalPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean testPassed = false;
                try
                {
                    //If it can format it will save it as successful
                    Integer.parseInt(newValue.toString());
                    testPassed = true;
                }
                catch(NumberFormatException e)
                {
                    AlertDialog.Builder formatError = new AlertDialog.Builder(getActivity());
                    formatError.setTitle("Incorrect input type");
                    formatError.setMessage("Only a number can be entered (No Decimals)");
                    formatError.show();
                }
                return testPassed;
            }
        });

        Preference metricsPref = getPreferenceScreen().findPreference(getString(R.string.metricsPref));
        metricsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                try
                {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                    //get preference values to change
                    double weight = Double.parseDouble(sharedPreferences.getString(getString(R.string.editWeightKey), ""));
                    double weightGoal = Double.parseDouble(sharedPreferences.getString(getString(R.string.editWeightGoal), ""));
                    double height = Double.parseDouble(sharedPreferences.getString(getString(R.string.editHeightKey), ""));

                    //If user selects metric it will convert his preferences to metric
                    if(newValue.equals(R.string.metArrPref)) {
                        //Convert weight to metric
                        weight = convertWeightToMetric(weight);
                        String convertedWeight = weight + "";
                        weightGoal = convertWeightToMetric(weightGoal);
                        String convertedWeightGoal = weightGoal + "";
                        //Convert height to metric
                        height = convertHeightToMetric(height);
                        String convertedHeight = height + "";

                        editWeight.setText(convertedWeight);
                        editHeight.setText(convertedHeight);
                        editWeightGoal.setText(convertedWeightGoal);

                        /*SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.editWeightKey), convertedWeight);
                        editor.putString(getString(R.string.editWeightGoal), convertedWeightGoal);
                        editor.putString(getString(R.string.editHeightKey), convertedHeight);
                        editor.commit();*/


                    }
                    else if(newValue.equals(R.string.impArrPref))
                    {
                        //Convert weight to Imperial
                        weight = convertWeightToImperial(weight);
                        String convertedWeight = weight + "";
                        weightGoal = convertWeightToImperial(weightGoal);
                        String convertedWeightGoal = weightGoal + "";

                        //Convert height to Imperial
                        height = convertHeightToImperial(height);
                        String convertedHeight = height + "";

                        editWeight.setText(convertedWeight);
                        editHeight.setText(convertedHeight);
                        editWeightGoal.setText(convertedWeightGoal);

                        /*
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.editWeightKey), convertedWeight);
                        editor.putString(getString(R.string.editWeightGoal), convertedWeightGoal);
                        editor.putString(getString(R.string.editHeightKey), convertedHeight);
                        editor.commit();*/
                    }
                }
                catch(Exception e)
                {
                    AlertDialog.Builder formatError = new AlertDialog.Builder(getActivity());
                    formatError.setTitle("Metrics Option Issues");
                    formatError.setMessage(e.getMessage());
                    formatError.show();
                }
                return true;
            }
        });


    }

    //Conversion methods for app
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
