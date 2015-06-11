package com.example.austin.ex2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Austin on 2/3/2015.
 */
public class Contact extends Activity {
    private static final String PREFS_NAME = "MyPreferencesFile";
    Button mButton;
    EditText mEdit,mEdit2, mEdit3;
    TextView mText,mText2,mText3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        mButton = (Button)findViewById(R.id.button3);


        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //mEdit   = (EditText)findViewById(R.id.eContact);


                    mEdit = (EditText) findViewById(R.id.eContact);
                    mEdit2 = (EditText) findViewById(R.id.eMessage);
                    mEdit3 = (EditText) findViewById(R.id.eTimer);
                    mText = (TextView) findViewById(R.id.tvResults);
                    mText.setText("Emergency Contact: " + mEdit.getText().toString());
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("eContact", mEdit.getText().toString());
                    editor.putString("eMessage", mEdit2.getText().toString());
                    editor.putString("eTimer", mEdit3.getText().toString());
                    editor.commit();
                    startG();


            }
        });
    }

    public void startG() {

        boolean valid = false;
        do {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

            if(isPhoneNumberValid(settings.getString("eContact", "")) && isInt(settings.getString("eTimer", "")))
            {

                valid = true;

                Intent intent = new Intent(getApplicationContext(), GForce.class);
                startActivity(intent);



        }else{
                mText.setText("Not a valid number");
        }
    }while(valid = false);
    }


    public static boolean isInt(String timer){
        try
        {
            Integer.parseInt(timer);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
    public static boolean isPhoneNumberValid(String phoneNumber){
        boolean isValid = false;

        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()){
            isValid = true;
        }

        return isValid;
    }
}

