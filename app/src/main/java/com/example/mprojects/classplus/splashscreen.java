
package com.example.mprojects.classplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class splashscreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        SharedPreferences sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);
        final Boolean loginstatus=sp.getBoolean(Config.STATUS,false);

        Button b1=(Button)findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Config.STATUS,loginstatus+"");
                if(loginstatus) {
                    Intent i = new Intent(splashscreen.this, Home.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(splashscreen.this, Login.class);
                    startActivity(i);
                }
                finish();
            }
        });

    }
}
