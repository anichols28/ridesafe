package com.example.austin.ex2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    int counter;
    Button add, sub;
    TextView display;
    public static final String PREFS_NAME = "MyPreferencesFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = 0;
        add = (Button) findViewById(R.id.button);
        sub = (Button) findViewById(R.id.button2);
        display = (TextView) findViewById(R.id.textView);
        add.setOnClickListener(new View.OnClickListener(){

            public void onClick(View y){
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                counter ++;
                display.setText("Your total is " + counter);
                if(counter >=3){

                    send(settings.getString("eContact", ""));
                }

            }
        });
        sub.setOnClickListener(new View.OnClickListener(){

            public void onClick(View y){
                counter --;
                display.setText("Your total is " + counter);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void send(String Number)
    {
        // get the phone number from the phone number text field
        String phoneNumber = Number;
        // get the message from the message text box
        String msg = "test";

        // make sure the fields are not empty
        if (phoneNumber.length()>0 && msg.length()>0)
        {
            // call the sms manager
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);
            SmsManager sms = SmsManager.getDefault();
            // this is the function that does all the magic
            sms.sendTextMessage(phoneNumber, null, msg, pi, null);
        }
        else
        {
            // display message if text fields are empty
            Toast.makeText(getBaseContext(), "All field are required", Toast.LENGTH_SHORT).show();
        }


    }
}
