package com.example.mprojects.classplus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class forgotp extends AppCompatActivity {

    Button b;
    ProgressDialog pdialog=null;
    String receiver,subject,textMessage,sender;
    javax.mail.Session session=null;
    String emailsender ,s1;
    TextInputEditText t1;
    int num;
    SharedPreferences sp;
    TextInputLayout emaill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotp);

        b=findViewById(R.id.send);
        t1=findViewById(R.id.email);
        emaill=findViewById(R.id.emailL);


        sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);
        t1.setText(sp.getString(Config.cemail,"Enter Ur Email"));


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                sendotp(view);
            }
        });
    }
    public void demo(View v)
    {
        validate();
    }
    public boolean validate()
    {
        if(t1.getText().length() ==0)
        {
            t1.setError("Enter Email-Id");
            t1.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(t1.getText().toString()).matches())
        {
            t1.setError("Invalid Email-Id");
            t1.requestFocus();
            return false;
        }
        return false;
    }


    public void randomgen()
    {
        Random random=new Random();
        num=123+random.nextInt(234);
        num=num+num;
        num=num*4;

    }
    public void sendotp(View v)
    {

        validate();

        ConnectivityManager manager=(ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();

        if(info!=null && info.isConnected())
        {
            BackgroundWorker bg=new BackgroundWorker(this);
            bg.execute();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();

        }
    }
    private void sendEmail()
    {
        randomgen();
        emailsender= emaill.getEditText().getText().toString();
        receiver=emailsender.trim();
        subject="Password Authentication Key".trim();
        sender=String.valueOf(num);
        textMessage="Pass key:"+num;


        Properties props=new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        final  String pass="projext123";
        session= javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {

                new javax.mail.PasswordAuthentication("projext.classplus@gmail.com", pass.trim());
                return super.getPasswordAuthentication();
            }
        });

        pdialog=ProgressDialog.show(this,"","Processing...", true);
        RetreiveFeedTask task=new RetreiveFeedTask();


        task.execute();

    }
    public class BackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;

        BackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            s1="not found";
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();

                HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"forgot.php").newBuilder();
                urlBuilder.addQueryParameter("emailid", emailsender);

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
                                    String result = response.body().string();
                                    pdialog.dismiss();
                                    Log.e("Result",result);
                                    //Toast.makeText(ForgetPassword.this,result, Toast.LENGTH_SHORT).show();
                                    if(result.trim().equals("User Exists!!"))
                                    {
                                        Toast.makeText(forgotp.this, "Wait while we are sending you otp..!!!!", Toast.LENGTH_SHORT).show();
                                        sendEmail();
                                    }
                                    else
                                        {
                                            Toast.makeText(forgotp.this, "Invalid Email-Id... Please Register..!!!", Toast.LENGTH_SHORT).show();
                                        }


                                } catch (IOException e) {
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
            return s1;
        }
        @Override
        protected void onPreExecute() {
            pdialog= ProgressDialog.show(forgotp.this,"","Processing...", true);
        }

        @Override
        protected void onPostExecute(String result) {


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    class RetreiveFeedTask extends AsyncTask<String, String, Void>
    {

        @Override
        protected Void doInBackground(String... params)
        {
            try
            {
                Message message=new MimeMessage(session);
                message.setFrom(new InternetAddress("projext.classplus@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
                message.setSubject(subject);
                message.setContent(textMessage,"text/html; charset=utf-8");
                Transport.send(message);

            }
            catch(MessagingException e)
            {


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Successful..... Please check your email....",Toast.LENGTH_SHORT).show();
            Intent i =new Intent(getApplicationContext(),match_key.class);
            i.putExtra("OTP",num);
            num=0;
            startActivity(i);
        }
    }
}
