package com.example.a16002749.opscpoe;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Preference variables that will be manipulated through the preferences
    private double userWeight = 0.0;
    private double userWeightGoal = 0.0;
    private int userStepGoal = 0;

    private BottomNavigationView bottomNav;
    private boolean PrefChaned = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //First load
        //Fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        statsFragment sf = new statsFragment();
        transaction.add(R.id.fragment, sf);
        transaction.addToBackStack(null);
        transaction.commit();
        //Preferences loaded via preference manager
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    //Handles creation of options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Handles selection of options menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent preferenceActivity = new Intent(this, Settings.class);
        startActivity(preferenceActivity);
        return super.onOptionsItemSelected(item);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(MenuItem item)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //txtTest.setText(item.getTitle());

            switch(item.getItemId())
            {
                case R.id.tabStats:
                    //Switch to this fragment

                    statsFragment sf = new statsFragment();


                    transaction.replace(R.id.fragment, sf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case R.id.tabInput:
                    //Switch to this fragment
                    InputFragment inputFrag = new InputFragment();

                    transaction.replace(R.id.fragment, inputFrag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case R.id.tabCamera:
                    //Switch to this fragment
                    CameraFragment camFrag = new CameraFragment();

                    transaction.replace(R.id.fragment, camFrag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
            }
            return true;
        }
    };

    private SharedPreferences.OnSharedPreferenceChangeListener prefChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    };
}
