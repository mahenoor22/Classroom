package com.example.mprojects.classplus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.mprojects.classplus.Home.*;
import static com.example.mprojects.classplus.Home.sp;

public class gradeadapter extends ArrayAdapter {

    List list=new ArrayList();
    String name;
    int pos;
    public gradeadapter( Context context, int resource) {
        super(context, resource);
    }

    public void add(gradesitem object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row=convertView;
        final gradesHolder h;
        final SharedPreferences sp=getContext().getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);

        final SharedPreferences.Editor e=sp.edit();
        if(row==null){
            LayoutInflater LI=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=LI.inflate(R.layout.answer,parent,false);
            h=new gradesHolder(row);
            h.txtname=row.findViewById(R.id.ansname);
            h.txtdate=row.findViewById(R.id.ansposted);
            h.txtpoints=row.findViewById(R.id.anspoints);
            h.aid=row.findViewById(R.id.aid);
            row.setTag(h);
        }
        else{
            h=(gradesHolder) row.getTag();
        }
        gradesitem item=(gradesitem) this.getItem(position);
        h.txtname.setText(item.getName());
        h.txtdate.setText(item.getPost());
        h.aid.setText(item.getAid());
        h.txtpoints.setText(sp.getString(Config.grade,"Points"));
        h.setLongclick(new longclick() {
            @Override
            public void onItemLongClick() {
                pos=position;
                e.putString(Config.aid,h.aid.getText().toString());
                e.commit();
            }
        });

        return row;
    }

    public void getSelectedItem(MenuItem item){
        gradesitem pdf= (gradesitem) list.get(pos);
        switch(item.getItemId()){
            case 0:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(pdf.getUrl()));
                getContext().startActivity(intent);
                break;
            case 1:
                alert();

        }

    }

    public void alert(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.cust_alert, null);
        dialogBuilder.setView(dialogView);
        final SharedPreferences.Editor e=sp.edit();
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        final String s=edt.getText().toString();
        dialogBuilder.setTitle("Grade");
        dialogBuilder.setMessage("Enter marks");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                e.putString(Config.points,s);
                e.commit();
                submitGrade(edt.getText().toString());
                //grades.grading();
                Log.e("grade",edt.getText().toString());
                //Toast.makeText(getContext(), "Grades send Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void submitGrade(String points) {

        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST + "point.php").newBuilder();

            //Toast.makeText(context, points, Toast.LENGTH_SHORT).show();
            urlBuilder.addQueryParameter("points",points);
            urlBuilder.addQueryParameter("aid", Home.sp.getString(Config.aid, "Not Found"));

            String url = urlBuilder.build().toString();
            //Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            SharedPreferences.Editor e = Home.sp.edit();
            String data = client.newCall(request).execute().body().string().trim();
            //Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
            if (data.trim().equals("success")) {
                Toast.makeText(context, "Grades Updated", Toast.LENGTH_SHORT).show();
                e.putString(Config.grade, Home.sp.getString(Config.points, "Not Found"));
                e.commit();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    static class gradesHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener{
        TextView txtname,txtpoints,txtdate,aid;
        longclick l;

        final SharedPreferences.Editor e=sp.edit();

        public gradesHolder(View v){
            v.setOnCreateContextMenuListener(this);
            v.setOnLongClickListener(this);
        }

        public  void setLongclick(longclick l){
            this.l=l;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,0,0,"Download Answer");
            menu.add(0,1,1,"Grade");
        }

        @Override
        public boolean onLongClick(View v) {
            this.l.onItemLongClick();
            return false;
        }


    }
}