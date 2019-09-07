package com.example.mprojects.classplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.Context.MODE_PRIVATE;

public class jclasswork extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String pdfName, pdfUrl,pdfDesc,pdfPoints,pdfDue,pdfPost,pdfTopic,pdfQid;

    public static final String PDF_FETCH_URL = Config.HOST+"getpdf.php";
    private TextView button;
    ArrayList<Pdf> pdfList;
    PdfAdapter pdfAdapter;
    SharedPreferences sp;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jclasswork, container, false);

        recyclerView = rootView.findViewById(R.id.jrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pdfList = new ArrayList<>();
        sp=getContext().getSharedPreferences(Config.SPName,MODE_PRIVATE);
        SharedPreferences.Editor e=sp.edit();
        loadRecyclerViewItem();
       return rootView;
    }

    private void loadRecyclerViewItem() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(PDF_FETCH_URL).newBuilder();
            urlBuilder.addQueryParameter("email",sp.getString(Config.cemail,"Not Found"));
            urlBuilder.addQueryParameter("code",sp.getString(Config.code,"Not Found"));
            Log.e("ccode",sp.getString(Config.code,"Not Found"));
            Log.e("ccode",sp.getString(Config.cemail,"Not Found"));
            String url = urlBuilder.build().toString();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(getContext(), "Please Check ur internet", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(Call call, final okhttp3.Response response) throws IOException {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String data=response.body().string();
                                JSONObject obj = new JSONObject(data);
                                Log.e("try block", data.trim());
                                JSONArray jsonArray = obj.getJSONArray("pdfs");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //Declaring a json object corresponding to every pdf object in our json Array
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    //Declaring a Pdf object to add it to the ArrayList  pdfList
                                    Pdf pdf = new Pdf();
                                    pdfTopic=jsonObject.getString("title");
                                    pdfDesc = jsonObject.getString("details");
                                    pdfPoints= jsonObject.getString("points");
                                    pdfPost=jsonObject.getString("postdate");
                                    pdfDue = jsonObject.getString("duedate");
                                    pdfName = jsonObject.getString("filename");
                                    pdfUrl = jsonObject.getString("fileurl");
                                    pdfQid=jsonObject.getString("qid");
                                    pdf.setTopic(pdfTopic);
                                    pdf.setDesc(pdfDesc);
                                    pdf.setPoints(pdfPoints);
                                    pdf.setPostdate(pdfPost);
                                    pdf.setDuedate(pdfDue);
                                    pdf.setName(pdfName);
                                    pdf.setUrl(pdfUrl);
                                    pdf.setQid(pdfQid);
                                    pdfList.add(pdf);

                                }
                                pdfAdapter=new PdfAdapter(getActivity(),R.id.cardview,pdfList);
                                adapter = new JCustomAdapter(pdfList,getActivity());
                                recyclerView.setAdapter(adapter);
                                pdfAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
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
    }
}

