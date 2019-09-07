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

import java.util.List;

public class JCustomAdapter extends RecyclerView.Adapter<JCustomAdapter.ViewHolder> {

    private List<Pdf> list;
    private Context mCtx;
    public static final String UPLOAD_URL =Config.HOST+"assign_quest.php";
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Declaring views
    //Pdf request code
    private int PICK_PDF_REQUEST = 1;
    //Uri to store the image uri
    private Uri filePath;
    Pdf pdf;

    public JCustomAdapter(List<Pdf> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    @Override
    public JCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_items, parent, false);
        return new JCustomAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final JCustomAdapter.ViewHolder holder, final int position) {
        SharedPreferences sp=mCtx.getSharedPreferences(Config.SPName,Context.MODE_PRIVATE);
        final SharedPreferences.Editor e=sp.edit();

        final Pdf pdf=list.get(position);
        holder.textViewTitle.setText(pdf.getTopic());
        holder.textViewDesc.setText(pdf.getName());
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
                Intent i=new Intent(mCtx,assignanswer.class);
                mCtx.startActivity(i);
            }
        });
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(mCtx, view, Gravity.RIGHT);
                //inflatiZZZng menu from xml resource
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

        public TextView textViewTitle;
        public TextView textViewDesc,textviewPoints;
        public TextView buttonViewOption,textviewPosted,textViewDue,qid;
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
            qid=itemView.findViewById(R.id.qid);
        }
    }
}
