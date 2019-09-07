package com.example.mprojects.classplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class createclass extends AppCompatActivity {
    TextInputLayout name,details,special,year;
    Button create;
    String mypref;
    Date d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_class);

        name=findViewById(R.id.name);
        details=findViewById(R.id.details);
        special=findViewById(R.id.specialization);
        year=findViewById(R.id.year);

        Calendar cal =Calendar.getInstance();
        final String current=DateFormat.getDateInstance().format(cal.getTime());
        create=findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);
                SharedPreferences.Editor e=sp.edit();
                e.putString("class",name.getEditText().getText().toString());
                e.commit();
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"createclass.php").newBuilder();
                    urlBuilder.addQueryParameter("class",name.getEditText().getText().toString());
                    urlBuilder.addQueryParameter("details",details.getEditText().getText().toString());
                    urlBuilder.addQueryParameter("special",special.getEditText().getText().toString());
                    urlBuilder.addQueryParameter("year",year.getEditText().getText().toString());
                    urlBuilder.addQueryParameter("email",sp.getString(Config.cemail,"Not Found"));
                    urlBuilder.addQueryParameter("name",sp.getString(Config.name,""));
                    Toast.makeText(createclass.this, name.getEditText().getText().toString()+" "+sp.getString(Config.name,""), Toast.LENGTH_SHORT).show();

                    String url = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        String s1=response.body().string();
                                        Toast.makeText(createclass.this, "CLass Created", Toast.LENGTH_SHORT).show();
                                        if(s1.trim().contains("Class Created"))
                                        {
                                            Intent i=new Intent(createclass.this,Home.class);
                                            startActivity(i);
                                            finish();
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        };
                    });
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static class jclasswork extends Fragment {
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.jclasswork, container, false);
            return rootView;
        }
    }
}
