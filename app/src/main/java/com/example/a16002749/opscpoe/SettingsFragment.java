package com.example.a16002749.opscpoe;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.preference.PreferenceFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragment{

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);
        //Manages validation on user weight changes
        //Finds the preference and attaches a listener for when it is changed
        Preference weightPref = getPreferenceScreen().findPreference(getString(R.string.editWeightKey));
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
    }
}
