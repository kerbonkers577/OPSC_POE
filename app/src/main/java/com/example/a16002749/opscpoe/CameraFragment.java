package com.example.a16002749.opscpoe;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Tristan Constable on 5/9/2018.
 */



public class CameraFragment extends Fragment{

    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    public StrictMode.VmPolicy.Builder builder;
    public Button capture;
    private static final int CAMERA_REQUEST = 1888;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);
        //ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);

        //Source https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed
        //Basically handles suspicious activity
        builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        capture = (Button) view.findViewById(R.id.btnCamera);

        // source: https://stackoverflow.com/questions/14421694/taking-pictures-with-camera-on-android-programmatically#14421798
        // source: SensorGraph | Author: Daniel Erasmus
        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        //Finds external storage and appends a new directory to it
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        //Opens a file object on it
        File newdir = new File(dir);
        //Creates the new directory
        newdir.mkdirs();
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Here, the counter will be incremented each time, and the
                // picture taken by camera will be stored as 1.jpg,2.jpg
                // and likewise.
                count++;
                //Making a unique photo code
                String file = dir+count+".jpg";
                File newfile = new File(file);
                try {//Creates the new file
                    newfile.createNewFile();
                }
                catch (IOException e)
                {
                }
                //opens a stream to that new file
                Uri outputFileUri = Uri.fromFile(newfile);
                builder.detectFileUriExposure();
                //using the camera intent it writes the image to that new file
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });
        return view;
    }

    //Source https://stackoverflow.com/questions/33666071/android-marshmallow-request-permission
    //TODO: Handle it better so if user declines you don't crash the app with the photo's
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied to Camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //This method gets a response from external intents
    //Basically feedback on how the operation went
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            //TODO: Make a toast with confirmation
            Toast.makeText(getActivity(), "Picture Saved",
            Toast.LENGTH_LONG).show();
        }
    }
}
