package com.example.mprojects.classplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class feedback extends AppCompatActivity {
    TextInputLayout name,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        Toolbar toolbar=findViewById(R.id.ftoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FeedBack");

        name=findViewById(R.id.fname);
        message=findViewById(R.id.message);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.feedback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.fsure){
            givefeedback();
            Intent i=new Intent(feedback.this,Home.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void givefeedback(){
        SharedPreferences sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"feedback.php").newBuilder();
            urlBuilder.addQueryParameter("name", name.getEditText().getText().toString());
            urlBuilder.addQueryParameter("message", message.getEditText().getText().toString());
            urlBuilder.addQueryParameter("email", sp.getString(Config.cemail,"Not Found"));
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
                    Log.e("register",response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String s1=response.body().string().trim();
                                Log.e("register",s1);
                                if(s1.equals("success"))
                                {
                                    Toast.makeText(feedback.this, "FeedBack Submitted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                };
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
