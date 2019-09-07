package com.example.mprojects.classplus;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PdfAdapter extends ArrayAdapter<Pdf>
{
        Activity activity;
        int layoutResourceId;
        ArrayList<Pdf> data;
        Pdf pdf;
        public PdfAdapter(Activity activity, int layoutResourceId, ArrayList<Pdf> data) {
        super(activity, layoutResourceId, data);
        this.activity=activity;
        this.layoutResourceId=layoutResourceId;
        this.data=data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        PdfHolder holder=null;
        if(row==null)
        {
        LayoutInflater inflater=LayoutInflater.from(activity);
        row=inflater.inflate(layoutResourceId,parent,false);
        holder=new PdfHolder();
        holder.topic= row.findViewById(R.id.ctopic);
        holder.desc=row.findViewById(R.id.cdesc);
        holder.Url= row.findViewById(R.id.cbutton);
        holder.postdate= row.findViewById(R.id.cposted);
        holder.points= row.findViewById(R.id.points);
          row.setTag(holder);
        }
        else
        {
        holder= (PdfHolder) row.getTag();
        }

        pdf = data.get(position);
        holder.desc.setText(pdf.getName());
        holder.Url.setText(pdf.getUrl());
        return row;
        }


class PdfHolder
{
    TextView desc,Url,topic,postdate,duedate,points;
}

}