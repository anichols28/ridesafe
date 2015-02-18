package com.example.austin.ex2;

/**
 * Created by Austin on 2/4/2015.
 */
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendSMS extends Activity {
    public static final String PREFS_NAME = "MyPreferencesFile";
    Button sendButton;
    EditText phoneTextField;
    EditText msgTextField;


    // this is the function that gets called when you click the button
    public void send(String Number)
    {
        // get the phone number from the phone number text field
        String phoneNumber = Number;
        // get the message from the message text box
        String msg = "test";
        GPS myGPS = new GPS(this);
        myGPS.getLocation();
        String address = myGPS.getLocationAddress();
        myGPS.closeGPS();

        // make sure the fields are not empty
        if (phoneNumber.length()>0 && msg.length()>0)
        {
            // call the sms manager
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, SendSMS.class), 0);
            SmsManager sms = SmsManager.getDefault();
            // this is the function that does all the magic
            sms.sendTextMessage(phoneNumber, null, address, pi, null);
        }
        else
        {
            // display message if text fields are empty
            Toast.makeText(getBaseContext(), "All field are required", Toast.LENGTH_SHORT).show();
        }


    }
}