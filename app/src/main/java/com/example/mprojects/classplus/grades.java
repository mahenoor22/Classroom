package com.example.mprojects.classplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.example.mprojects.classplus.Home.context;
import static com.example.mprojects.classplus.Home.sp;

public class grades extends AppCompatActivity {
    String pdfName, pdfUrl,pdfDesc,pdfPoints,pdfDue,pdfPost,pdfTopic,pdfqid;
    ArrayList<Pdf> pdfList;
    ListView lvitem;
    gradeadapter adapter;
    gradesitem mItem;
    static Context context;
    static SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades2);
        getSupportActionBar().setTitle("Answers");
        context=getApplicationContext();
        sp = getSharedPreferences(Config.SPName, MODE_PRIVATE);
        pdfList = new ArrayList<Pdf>();
        lvitem = findViewById(R.id.anslist);
        adapter = new gradeadapter(this, R.layout.answer);
        lvitem.setAdapter(adapter);

        display();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adapter.getSelectedItem(item);
        return super.onContextItemSelected(item);
    }

    public void display(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"getans.php").newBuilder();
            urlBuilder.addQueryParameter("code",sp.getString(Config.code,"Not Found"));
            urlBuilder.addQueryParameter("qid",sp.getString(Config.qid,"Not Found"));

            String url = urlBuilder.build().toString();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();

                }

                @Override
                public void onResponse(Call call, final okhttp3.Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String data=response.body().string();
                                JSONObject obj = new JSONObject(data);
                                Log.e("grades", data.trim());
                                JSONArray jsonArray = obj.getJSONArray("pdfs");
                                int count=0;
                                while(count< jsonArray.length()) {
                                    //Declaring a json object corresponding to every pdf object in our json Array
                                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                                   //Declaring a Pdf object to add it to the ArrayList  pdfList
                                    mItem=new gradesitem(jsonObject.getString("filename"),
                                            jsonObject.getString("postdate"),
                                            jsonObject.getString("fileurl"),
                                            jsonObject.getString("ansid"));
                                    count++;
                                    adapter.add(mItem);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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