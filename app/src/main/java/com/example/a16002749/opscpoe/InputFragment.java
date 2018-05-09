package com.example.a16002749.opscpoe;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tristan Constable on 5/9/2018.
 */

public class InputFragment extends Fragment
{
    public InputFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.input_fragment, container, false);
    }
}
