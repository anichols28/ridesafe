package com.example.austin.ex2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Austin on 2/26/2015.
 */
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;


public class Timer extends Activity  implements TextToSpeech.OnInitListener {


    TextView text1;
    public static final String PREFS_NAME = "MyPreferencesFile";
    Button falseButton, trueButton;
    private static final String FORMAT = "%02d:%02d:%02d";
    private TextToSpeech mTts;
    int seconds , minutes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        falseButton = (Button)findViewById(R.id.falseButton);
        trueButton = (Button)findViewById(R.id.helpButton);
        mTts = new TextToSpeech(this, this);
        final GPS myGPS = new GPS(this);
        myGPS.getLocation();
        String timer = settings.getString("eTimer", "");
        int time = 0;

        if(!parseWithFallback(timer)){
            time = 10000;
        }
        else{
            time = Integer.parseInt(timer.toString());
            time *= 1000;
        }
        text1=(TextView)findViewById(R.id.textView1);


        final CountDownTimer myCount = new CountDownTimer(time, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                text1.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                send(settings.getString("eContact", ""), myGPS);



                //
            }
        }.start();
        falseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                myCount.cancel();
                Intent intent = new Intent(getApplicationContext(), GForce.class);
                startActivity(intent);


            }
        });
        trueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                myCount.cancel();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                send(settings.getString("eContact", ""), myGPS);


            }
        });

    }
    public void onInit(int i)
    {
        mTts.speak("Ride Safe has Detected an impact",
                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                null);
    }

    public void send(String Number, GPS myGPS)
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        // get the phone number from the phone number text field
        String phoneNumber = Number;
        // get the message from the message text box
        String defaultMessage = "AUTOMATED MESSAGE - Ride Safe has detected a motorcyclist has been in an accident";

        String address = " Location: " + myGPS.getLocationAddress();

        StringBuilder msg = new StringBuilder();

        String customMessage = settings.getString("eMessage", "");
        if(customMessage == ""){

            msg.append(defaultMessage);
            msg.append(address);
        }
        else{
            msg.append(customMessage);
            msg.append(address);
        }


        // make sure the fields are not empty
        myGPS.closeGPS();
        kill_activity();
        // call the sms manager
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, SOSSent.class), 0);
        SmsManager sms = SmsManager.getDefault();
        // this is the function that does all the magic
        sms.sendTextMessage(phoneNumber, null, msg.toString(), pi, null);





    }
    @Override
    public void onDestroy()
    {
// Don’t forget to shutdown!
        if (mTts != null)
        {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    void kill_activity()
    {
        finish();
    }

    public boolean parseWithFallback(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}