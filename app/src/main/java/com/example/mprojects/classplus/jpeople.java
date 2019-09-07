package com.example.mprojects.classplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import okhttp3.Response;

public class jpeople extends Fragment {
    SharedPreferences sp;
    ListView list,list1;
    ArrayList<Object> alist;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jpeople, container, false);
        sp=getActivity().getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);
        list=rootView.findViewById(R.id.tlist);
        list1=rootView.findViewById(R.id.tlist1);
        alist=new ArrayList<>();
        listvalues();
        return rootView;
    }
    public void listvalues(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "people.php").newBuilder();
            urlBuilder.addQueryParameter("code", sp.getString(Config.code, "Not Found"));

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(getContext(), "Please Check ur internet", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(Call call, final Response response) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                            try {
                                ArrayList<Object> alist=new ArrayList<>();
                                String data = response.body().string();
                                Log.e("people", data);
                                JSONObject jsonobject = new JSONObject(data);
                                JSONArray jsonArray = jsonobject.getJSONArray("teacher");
                                int count = 0;
                                alist.add(new String("Teachers"));
                                while(count<jsonArray.length()){
                                    JSONObject JO=jsonArray.getJSONObject(count);
                                    alist.add(new Item(JO.getString("name"),"","","",""));
                                    count++;
                                }
                                list.setAdapter(new peopleadapt(getContext(),alist));
                                alist=new ArrayList<>();
                                jsonArray = jsonobject.getJSONArray("student");
                                Log.e("stud",data);
                                count=0;
                                alist.add(new String("Students"));
                                while(count<jsonArray.length()){
                                    JSONObject JO=jsonArray.getJSONObject(count);
                                    alist.add(new Item(JO.getString("name"),"","","",""));
                                    count++;

                                }
                                list1.setAdapter(new peopleadapt(getContext(),alist));
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
}
