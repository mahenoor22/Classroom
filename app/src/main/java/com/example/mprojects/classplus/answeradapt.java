package com.example.mprojects.classplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class answeradapt extends RecyclerView.Adapter<answeradapt.ViewHolder> {

    private List<Pdf> list;
    private Context mCtx;
    Pdf pdf;

    public answeradapt(List<Pdf> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    @Override
    public answeradapt.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer, parent, false);
        return new answeradapt.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final answeradapt.ViewHolder holder, final int position) {
        SharedPreferences sp=mCtx.getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);
        final SharedPreferences.Editor e=sp.edit();

        final Pdf pdf=list.get(position);
        holder.name.setText(pdf.getName());
        holder.textviewPosted.setText(pdf.getPostdate());
        holder.textviewPoints.setText(pdf.getPoints()+" Points");
        holder.qid.setText(pdf.getQid());
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(mCtx, view, Gravity.RIGHT);
                //inflating menu from xml resource
                popup.inflate(R.menu.option);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.download:
                                Pdf pdf=list.get(position);
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                intent.setData(Uri.parse(pdf.getUrl()));
                                mCtx.startActivity(intent);
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show(); }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,buttonViewOption,textviewPoints,textviewPosted,qid;

        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.ansname);
            textviewPosted=itemView.findViewById(R.id.ansposted);
            textviewPoints=itemView.findViewById(R.id.anspoints);
            qid=itemView.findViewById(R.id.qid);
        }
    }
}
