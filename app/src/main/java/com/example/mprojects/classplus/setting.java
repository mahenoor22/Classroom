package com.example.mprojects.classplus;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class setting extends Activity {

    TextView code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        code=findViewById(R.id.code);
    }

}
