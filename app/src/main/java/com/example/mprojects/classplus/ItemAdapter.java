package com.example.mprojects.classplus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.mprojects.classplus.Home.*;
import static com.example.mprojects.classplus.Home.sp;

public class ItemAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Item object) {
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
        View row = convertView;
        final ItemHolder h;
        final SharedPreferences sp = getContext().getSharedPreferences(Config.SPName, Context.MODE_PRIVATE);
        final SharedPreferences.Editor e = sp.edit();
        if (row == null) {
            LayoutInflater LI = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = LI.inflate(R.layout.item_layout, parent, false);
            h = new ItemHolder(row);
            h.txtname = (TextView) row.findViewById(R.id.txtname);
            h.txtdegree = row.findViewById(R.id.txtdegree);
            h.txtstud = row.findViewById(R.id.txtstud);
            h.txtcode = row.findViewById(R.id.classcode);
            h.txtdate = row.findViewById(R.id.date);
            row.setTag(h);
        } else {
            h = (ItemHolder) row.getTag();
        }
        final Item item = (Item) this.getItem(position);
        h.txtname.setText(item.getName());
        h.txtdegree.setText(item.getDegree());
        h.txtstud.setText(item.getNoofstuds());
        h.txtcode.setText(item.getCode());
        h.txtdate.setText(item.getPost());
        h.setLongclick(new longclick() {
            @Override
            public void onItemLongClick() {
                e.putString(Config.code, h.txtcode.getText().toString());
                e.commit();
            }
        });
        return row;
    }

    public void getSelectedItem(MenuItem item) {
        if(item.getItemId()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do you want to remove .class?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Home.delete();
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
    }


    static class ItemHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener{
        TextView txtname,txtdegree,txtstud,txtcode,txtdate;
        ImageView dots;
        longclick l;

        final SharedPreferences.Editor e=sp.edit();

        public ItemHolder(View v){
            v.setOnCreateContextMenuListener(this);
	    v.setOnLongClickListener(this);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name=((TextView)v.findViewById(R.id.txtname)).getText().toString();
                    String currcode=((TextView)v.findViewById(R.id.classcode)).getText().toString();
                    e.putString(Config.code,currcode);
                    e.putString(Config.Classname,name);
                    e.commit();
                    Home.checkstatus(sp.getString(Config.cemail,"Not Found"),currcode);
                    Log.e("status",status);
                    if(status.equals("teacher")){
                        Intent i = new Intent(context, classcreated.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        //context.finish();
                    }
                    else{
                        Intent i = new Intent(context, joined.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        //finish();
                    }
                }
            });
            
        }

        public  void setLongclick(longclick l){
            this.l=l;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,0,0,"Remove from Class");
        }

        @Override
        public boolean onLongClick(View v) {
            this.l.onItemLongClick();
            return false;
        }


    }
}
