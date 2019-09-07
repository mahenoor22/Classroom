package com.example.mprojects.classplus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class match_key extends AppCompatActivity {

        EditText t1;
        String s1;
        int otp;
        Button b1;
        @Override
        protected void onCreate (Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_match_key);
            t1 = (EditText) findViewById(R.id.otp);
            b1 = (Button) findViewById(R.id.sendotp);
            Intent i = getIntent();
            otp = i.getIntExtra("OTP", 0);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkkey(view);
                }
            });
        }

     public boolean validate ()
        {


            return false;
        }


        public void checkkey (View v)
        {

            if (t1.getText().length() == 0) {
                Toast.makeText(this, "pleasse enter one time password", Toast.LENGTH_SHORT).show();
                //  return false;
            } else {
                s1 = t1.getText().toString();
                int n = Integer.parseInt(s1);
            }
        }
    }

