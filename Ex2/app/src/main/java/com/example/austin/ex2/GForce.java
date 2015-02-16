package com.example.austin.ex2;

/**
 * Created by Austin on 2/13/2015.
 */
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

public class GForce extends Activity implements SensorEventListener {

    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;
    private double GMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private double cG = 0;

    private float vibrateThreshold = 0;
    public static final String PREFS_NAME = "MyPreferencesFile";
    private TextView currentX, currentY, currentZ, currentG, maxG,maxX, maxY, maxZ;

    public Vibrator v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accel);
        initializeViews();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            // fai! we dont have an accelerometer!
        }

        //initialize vibration
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
        currentG = (TextView) findViewById(R.id.currentG);

        maxX = (TextView) findViewById(R.id.maxX);
        maxY = (TextView) findViewById(R.id.maxY);
        maxZ = (TextView) findViewById(R.id.maxZ);
        maxG = (TextView) findViewById(R.id.maxG);
    }

    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // clean current values
        displayCleanValues();
        // display the current x,y,z accelerometer values
        displayCurrentValues();
        // display the max x,y,z accelerometer values
        displayMaxValues();

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
        if (deltaZ < 2)
            deltaZ = 0;

        // set the last know values of x,y,z
        lastX = event.values[0];
        lastY = event.values[1];
        lastZ = event.values[2];

        vibrate();
    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));

        cG = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)/SensorManager.STANDARD_GRAVITY;

        //cG = (deltaX + deltaY + deltaZ)/SensorManager.STANDARD_GRAVITY;

        currentG.setText(Double.toString(cG));
    }

    // display the max x,y,z accelerometer values
    public void displayMaxValues() {

        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
            maxX.setText(Float.toString(deltaXMax));
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            maxY.setText(Float.toString(deltaYMax));
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
            maxZ.setText(Float.toString(deltaZMax));
        }
        if (cG > GMax) {
            GMax = cG;
            maxG.setText(Double.toString(GMax));
            if(GMax >=2.5){
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                send(settings.getString("eContact", ""));
            }
        }
    }

    public void vibrate() {
        if ((deltaX > vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
            v.vibrate(50);
        }
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
                    new Intent(this, GForce.class), 0);
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