package com.example.mprojects.classplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class peopleadapt extends BaseAdapter {
    ArrayList<Object> list;
    private static final int people=0;
    private static final int header=1;
    private LayoutInflater inflater;

    public peopleadapt(Context context,ArrayList<Object> list){
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position)instanceof Item){
            return people;
        }
        else{
            return header;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            switch (getItemViewType(position)){
                case people:
                    convertView=inflater.inflate(R.layout.peopleitem,null);
                    break;
                case header:
                    convertView=inflater.inflate(R.layout.peopleheader,null);
                    break;
            }
        }
        else{
            switch (getItemViewType(position)){
                case people:
                    TextView p=convertView.findViewById(R.id.people);
                    p.setText(((Item)list.get(position)).getName());
                    break;
                case header:
                    TextView t=convertView.findViewById(R.id.htext);
                    t.setText((String)list.get(position));
                    break;
            }
        }
        return convertView;
    }
}
