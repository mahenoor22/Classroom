package com.example.mprojects.classplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class announcement extends AppCompatActivity {
    TextInputLayout title,details;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Announcement");

        title=findViewById(R.id.ntitle);
        details=findViewById(R.id.ndetails);

        sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);
        SharedPreferences.Editor e=sp.edit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem a=menu.findItem(R.id.mattach);
        a.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }
        if(id==R.id.msure){
            notice();
        }

        return super.onOptionsItemSelected(item);
    }
    public void notice(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"notice.php").newBuilder();
            urlBuilder.addQueryParameter("title", title.getEditText().getText().toString());
            urlBuilder.addQueryParameter("details", details.getEditText().getText().toString());
            urlBuilder.addQueryParameter("code", sp.getString(Config.code,"Not Found"));

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
                    Log.e("notice",response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String s1=response.body().string().trim();
                                if(s1.equals("success"))
                                {
                                    Toast.makeText(announcement.this, "Announcement successfully updated", Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(announcement.this,classcreated.class);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Toast.makeText(announcement.this, "Please try again", Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
