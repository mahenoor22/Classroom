package com.example.mprojects.classplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class joinclass extends AppCompatActivity {
    TextInputLayout code;
    Button joinb;
    SharedPreferences sp;
    String email,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinclass);
        code=findViewById(R.id.joinc);
        joinb=findViewById(R.id.join);
        sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);
        email=sp.getString(Config.cemail,"Not Found");
        name=sp.getString(Config.name,"Not Found");

        joinb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"joinresult.php").newBuilder();
                    urlBuilder.addQueryParameter("code", code.getEditText().getText().toString());
                    urlBuilder.addQueryParameter("email",email );
                    urlBuilder.addQueryParameter("username", name);

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
                                            String data = response.body().string();
                                            if (data.trim().equals("Class Joined")) {
                                                Toast.makeText(joinclass.this, "Classroom Joined", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), Home.class);
                                                startActivity(i);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(joinclass.this, "No Classroom found with this code", Toast.LENGTH_SHORT).show();
                                            }


                                    }
                                        catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                }
                            });
                        }

                        ;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}