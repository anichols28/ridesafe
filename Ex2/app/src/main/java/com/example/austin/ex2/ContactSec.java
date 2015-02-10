package com.example.austin.ex2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Austin on 2/9/2015.
 */
public class ContactSec extends Activity{
    public static final String PREFS_NAME = "MyPreferencesFile";
    Button mButton;

    TextView mText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        mButton = (Button)findViewById(R.id.button4);
        final EditText contactEdit = (EditText)findViewById(R.id.editContact);
        //mText = (TextView)findViewById(R.id.tvResults);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //mEdit   = (EditText)findViewById(R.id.eContact);


                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("eContact", contactEdit.getText().toString());
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), SendSMS.class);
                startActivity(intent);
            }
        });
    }
}