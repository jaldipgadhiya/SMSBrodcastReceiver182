package com.android.acadgild.smsbrodcastreceiver182;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

//Main activity class to check & grant all permissions

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if  permission is granted or not
        if (!checkPermission()) {
            //If not then request permission
            requestPermission();

        } else {
            //Else display this message.
            Toast.makeText(MainActivity.this, "Permission already granted.", Toast.LENGTH_LONG).show();

        }


    }

    //checkPermission method
    private boolean checkPermission() {
        //Take result for each permision by calling ContextCompat.checkSelfPermission method
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        //Return result
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    //requestPermission method
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{RECEIVE_SMS, READ_SMS, SEND_SMS}, PERMISSION_REQUEST_CODE);

    }

    //onRequestPermissionsResult method
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // Check for permission request code
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //Check if length of grantResults array greater than 0
                if (grantResults.length > 0) {
                    //Take result of all 3 requests
                    boolean receiveSms = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readSms = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean sendSms = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    // If all 3 requests are granted then display this message
                    if (receiveSms && readSms && sendSms)
                        Toast.makeText(MainActivity.this, "Permission Granted, Now you can access external storage.", Toast.LENGTH_LONG).show();
                    else {
                        //Else display denied permission message.
                        Toast.makeText(MainActivity.this, "Permission Denied, You cannot access external storage.", Toast.LENGTH_LONG).show();
                        // Check if version is greater than Marshmallow or not
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(RECEIVE_SMS)) {
                                //call showMessageOKCancel method if all permissions are not granted
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    //Again call request permission
                                                    requestPermissions(new String[]{RECEIVE_SMS, READ_SMS, SEND_SMS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }
    //Method to show message in dialog
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
