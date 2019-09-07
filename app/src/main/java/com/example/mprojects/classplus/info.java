package com.example.mprojects.classplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import org.w3c.dom.Text;

public class info extends Activity {
    TextView t1;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);

        String currcode=sp.getString(Config.code,"Not Found");
        t1=findViewById(R.id.code);
        t1.setText(currcode);
    }

}
