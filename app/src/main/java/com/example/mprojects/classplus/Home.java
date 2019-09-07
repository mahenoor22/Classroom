package com.example.mprojects.classplus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView emailh, nameh,textView1,textView2;
    FloatingActionButton fab, fb1, fb2;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    ListView lvitem;
    ItemAdapter adapter;
    Item mItem;
    static Context context;
    String name, degree, special, email;
    static SharedPreferences sp;
    static String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        context=getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);

        textView1=findViewById(R.id.ft1);
        textView2=findViewById(R.id.ft2);
        //List View
        sp = getSharedPreferences(Config.SPName, MODE_PRIVATE);
        final SharedPreferences.Editor e = sp.edit();

        lvitem = findViewById(R.id.list);
        adapter = new ItemAdapter(this, R.layout.item_layout);
        lvitem.setAdapter(adapter);
        listview();

        /*
       lvitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onclick","yes");
                String name=((TextView)view.findViewById(R.id.txtname)).getText().toString();
                String currcode=((TextView)view.findViewById(R.id.classcode)).getText().toString();
                e.putString(Config.code,currcode);
                e.putString(Config.Classname,name);
                e.commit();
                checkstatus(sp.getString(Config.cemail,"Not Found"),currcode);
                Log.e("status",status);
                if(status.equals("teacher")){
                    Intent i = new Intent(Home.this, classcreated.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i = new Intent(Home.this, joined.class);
                    startActivity(i);
                    finish();
                }
            }
        });*/

        email = sp.getString(Config.cemail, "not found");
        name = sp.getString(Config.name, "");

        //FloatingAction Button
        fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });
        fb1 = findViewById(R.id.fb1);
        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Intent i = new Intent(Home.this, createclass.class);
                startActivity(i);

            }
        });
        fb2 = findViewById(R.id.fb2);
        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Intent i = new Intent(Home.this, joinclass.class);
                startActivity(i);
            }
        });
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        //Drawer Layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Navigation View
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adapter.getSelectedItem(item);
        return super.onContextItemSelected(item);
    }

    private void animateFab() {
        if (isOpen) {
            fab.startAnimation(rotateBackward);
            fb1.startAnimation(fabClose);
            fb2.startAnimation(fabClose);
            textView1.startAnimation(fabClose);
            textView2.startAnimation(fabClose);
            fb1.setClickable(false);
            fb2.setClickable(false);
            isOpen = false;
        } else {
            fab.startAnimation(rotateForward);
            fb1.startAnimation(fabOpen);
            fb2.startAnimation(fabOpen);
            textView1.startAnimation(fabOpen);
            textView2.startAnimation(fabOpen);
            fb1.setClickable(true);
            fb2.setClickable(true);
            isOpen = true;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.classes) {
            Intent i = new Intent(Home.this, Home.class);
            startActivity(i);
        } else if (id == R.id.aboutus) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            TextView mymsg=new TextView(this);
            mymsg.setText("KC Student App Created By \nDegree CS Deptartment:\nMahenoor Mansuri(Student)");
            mymsg.setGravity(Gravity.CENTER);
            mymsg.setTextSize(18);
            mymsg.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            builder.setTitle("About Us");
            builder.setIcon(R.mipmap.appicon);
            builder.setView(mymsg)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            System.runFinalizersOnExit(true);
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
        else if (id == R.id.logout) {


            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do you want to sign out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor e = sp.edit();
                            e.putBoolean(Config.STATUS, false);
                            e.commit();
                            System.runFinalizersOnExit(true);
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void listview() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "home.php").newBuilder();
            urlBuilder.addQueryParameter("email", sp.getString(Config.cemail, "Not Found"));

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //Toast.makeText(Home.this, "Please Check ur internet", Toast.LENGTH_SHORT).show();
                    Log.e("Failure",e.getMessage());


                }

                @Override
                public void onResponse(Call call, final Response response) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //txtInfo.setText(response.body().string());

                                try {
                                    String data = response.body().string();
                                    Log.e("message", data);
                                    JSONObject jsonobject = new JSONObject(data);
                                    JSONArray jsonArray = jsonobject.getJSONArray("Json");
                                    int count = 0;
                                    while (count < jsonArray.length()) {
                                        JSONObject JO = jsonArray.getJSONObject(count);
                                        if (JO.getInt("count") > 0) {
                                            mItem = new Item(JO.getString("name"), JO.getString("details"), JO.getInt("count") + " students", JO.getString("code"), JO.getString("date"));
                                        } else {
                                            mItem = new Item(JO.getString("name"), JO.getString("details"), "No students", JO.getString("code"), JO.getString("date"));
                                        }

                                        adapter.add(mItem);
                                        count++;

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
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

    public static void checkstatus(String semail,String scode) {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "status.php").newBuilder();
            urlBuilder.addQueryParameter("email", semail);
            urlBuilder.addQueryParameter("code", scode);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

        try {
            status=client.newCall(request).execute().body().string().trim();
            Log.e("status",status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "deleteclass.php").newBuilder();
        urlBuilder.addQueryParameter("email", sp.getString((Config.cemail),"Not Found"));
        urlBuilder.addQueryParameter("code", sp.getString(Config.code,"Not Found"));

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            String delete=client.newCall(request).execute().body().string().trim();
            Toast.makeText(context, "Removed from class successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
