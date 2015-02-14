package com.example.austin.ex2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Austin on 2/3/2015.
 */
public class Contact extends Activity {
    public static final String PREFS_NAME = "MyPreferencesFile";
    Button mButton;
    EditText mEdit;
    TextView mText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        mButton = (Button)findViewById(R.id.button3);


        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //mEdit   = (EditText)findViewById(R.id.eContact);

                mEdit = (EditText)findViewById(R.id.eContact);
                mText = (TextView)findViewById(R.id.tvResults);
                mText.setText("Emergency Contact: "+mEdit.getText().toString());
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("eContact", mEdit.getText().toString());
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

