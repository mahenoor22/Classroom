package com.example.mprojects.classplus;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.SharedPreferences.*;

public class CCustomAdapter extends RecyclerView.Adapter<CCustomAdapter.ViewHolder> {

    private List<Pdf> list;
    private Context mCtx;
    //ArrayList<Pdf> data=new ArrayList<Pdf>();
    Pdf pdf;

    public CCustomAdapter(List<Pdf> list, Context mCtx) {
            this.list = list;
            this.mCtx = mCtx;
            }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.cardview_items, parent, false);

            return new ViewHolder(v);
            }

    @Override
    public void onBindViewHolder(final CCustomAdapter.ViewHolder holder, final int position) {
            Pdf pdf=list.get(position);
            final SharedPreferences sp=mCtx.getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);
            final SharedPreferences.Editor e=sp.edit();
            holder.textViewTitle.setText(pdf.getTopic());
            holder.textViewDesc.setText(pdf.getDesc());
            holder.textviewPosted.setText(pdf.getPostdate());
            holder.textviewPoints.setText(pdf.getPoints()+" Points");
            holder.textViewDue.setText(pdf.getDuedate());
            holder.qid.setText(pdf.getQid());
            holder.cardView.setClickable(true);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    e.putString(Config.qid,holder.qid.getText().toString());
                    e.commit();
                    Log.e("QuestionId",sp.getString(Config.qid,"Not Found"));
                    Intent i=new Intent(mCtx,grades.class);
                    mCtx.startActivity(i);
                }
            });
            holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
                 @Override
                public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(mCtx, view, Gravity.RIGHT);
                //inflating menu from xml resource
                popup.inflate(R.menu.createoptions);
                //adding click listener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
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

        public TextView textViewTitle;
        public TextView textViewDesc,textviewPoints,qid;
        public TextView buttonViewOption,textviewPosted,textViewDue;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardview);
            textViewTitle = (TextView) itemView.findViewById(R.id.ctopic);
            textViewDesc = (TextView) itemView.findViewById(R.id.cdesc);
            textviewPosted=itemView.findViewById(R.id.cposted);
            textviewPoints=itemView.findViewById(R.id.cpoints);
            buttonViewOption = (TextView) itemView.findViewById(R.id.cbutton);
            textViewDue=itemView.findViewById(R.id.duedate);
            qid = (TextView) itemView.findViewById(R.id.qid);
        }
    }
}
