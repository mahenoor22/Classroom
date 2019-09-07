package com.example.mprojects.classplus;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class noticeadapt extends RecyclerView.Adapter<noticeadapt.ViewHolder> {

    private List<Pdf> list;
    private Context mCtx;
    //ArrayList<Pdf> data=new ArrayList<Pdf>();
    Pdf pdf;

    public noticeadapt(List<Pdf> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noticecard, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final noticeadapt.ViewHolder holder, final int position) {
        Pdf pdf=list.get(position);

        holder.textViewTitle.setText(pdf.getTopic());
        holder.textViewDesc.setText(pdf.getDesc());
        holder.textviewPosted.setText(pdf.getPostdate());
        
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTitle;
        public TextView textViewDesc,textviewPosted;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardview);
            textViewTitle = (TextView) itemView.findViewById(R.id.mtopic);
            textViewDesc = (TextView) itemView.findViewById(R.id.mdesc);
            textviewPosted=itemView.findViewById(R.id.mposted);
        }
    }
}
