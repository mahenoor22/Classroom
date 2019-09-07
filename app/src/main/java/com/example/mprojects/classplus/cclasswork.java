package com.example.mprojects.classplus;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class cclasswork extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String pdfName, pdfUrl,pdfDesc,pdfPoints,pdfDue,pdfPost,pdfTopic,pdfqid;
    public static final String PDF_FETCH_URL = Config.HOST+"getpdf.php";
    private int PICK_PDF_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    ProgressDialog progressDialog;
    //an array to hold the different pdf objects
    ArrayList<Pdf> pdfList;
    PdfAdapter pdfAdapter;
    SharedPreferences sp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cclasswork, container, false);

        recyclerView = rootView.findViewById(R.id.crecyclerView);
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
                                //Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
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
                                    pdfqid = jsonObject.getString("qid");
                                    pdf.setTopic(pdfTopic);
                                    pdf.setDesc(pdfDesc);
                                    pdf.setPoints(pdfPoints);
                                    pdf.setPostdate(pdfPost);
                                    pdf.setDuedate(pdfDue);
                                    pdf.setName(pdfName);
                                    pdf.setUrl(pdfUrl);
                                    pdf.setQid(pdfqid);
                                    pdfList.add(pdf);

                            }
                                pdfAdapter=new PdfAdapter(getActivity(),R.id.cardview,pdfList);
                                adapter = new CCustomAdapter(pdfList,getActivity());
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