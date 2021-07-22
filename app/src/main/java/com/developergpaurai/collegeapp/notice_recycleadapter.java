package com.developergpaurai.collegeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class notice_recycleadapter extends FirebaseRecyclerAdapter<notice_dataholder,notice_recycleadapter.holder> {


    public notice_recycleadapter(@NonNull FirebaseRecyclerOptions<notice_dataholder> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull holder holder, int position, @NonNull notice_dataholder model) {
        holder.tv1.setText("@ "+model.getName());
        holder.tv2.setText(model.getDate());
        holder.tv3.setText(model.getTime());
        holder.tv4.setText(model.getNotice());
        holder.tv1.setTag(model.getId());

    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_recyclerview,parent,false);
        return new holder(view);
    }

    class holder extends RecyclerView.ViewHolder{
        TextView tv1,tv2,tv3,tv4;
        LinearLayoutCompat layoutCompat;

        public holder(@NonNull final View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.notice_name);
            tv2 = (TextView) itemView.findViewById(R.id.notice_date);
            tv3 = (TextView) itemView.findViewById(R.id.notice_time);
            tv4 = (TextView) itemView.findViewById(R.id.notice_notice);
            layoutCompat = (LinearLayoutCompat) itemView.findViewById(R.id.layout_notice_recycle);
            layoutCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = tv1.getText().toString();
                    String notice  = tv4.getText().toString();
                    String image_name = tv1.getTag().toString();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Notice_showFragment fr = new Notice_showFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("kname",name);
                    bundle.putString("knotice",notice);
                    bundle.putString("kimage_name",image_name);
                    fr.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.Framecontainer,fr).addToBackStack(null).commit();
                }
            });
        }
    }
}