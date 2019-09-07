package com.example.mprojects.classplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class Login extends AppCompatActivity{
    TextInputLayout lemail,lpass;
    Button signin,cancel;
    TextView forgot,register;
    String passInput,emailInput;
    final Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        lemail=findViewById(R.id.username);
        lpass=findViewById(R.id.lpass);

        signin=findViewById(R.id.signin);
        cancel=findViewById(R.id.cancel);


        register=findViewById(R.id.register);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,register.class);
                startActivity(i);
            }
        });


    }
    private boolean validateEmail() {
        emailInput =lemail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            lemail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            lemail.setError("Please enter a valid email address");
            return false;
        } else {
            lemail.setError(null);
            return true;
        }
    }
    public void login(View view){
        validateEmail();
        passInput = lpass.getEditText().getText().toString().trim();

        if (passInput.isEmpty()) {
            lpass.setError("Field can't be empty");
        }
        else{
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"login.php").newBuilder();
            urlBuilder.addQueryParameter("emailid",emailInput);
            urlBuilder.addQueryParameter("password",passInput);

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
                                //txtInfo.setText(response.body().string());

                                try {
                                    String data = response.body().string();
                                    JSONArray jsonArray = new JSONArray(data);
                                    JSONObject jsonObject= jsonArray.getJSONObject(0);
                                    if(jsonArray!=null) {
                                        SharedPreferences sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);
                                        SharedPreferences.Editor e=sp.edit();
                                        e.putString(Config.cemail,jsonObject.getString("email"));
                                        e.putString(Config.name,jsonObject.getString("name"));
                                        e.putBoolean(Config.STATUS,true);
                                        e.commit();
                                        Intent i=new Intent(context,Home.class);
                                        startActivity(i);
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(context, "Login Unsuccesful", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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
}}
