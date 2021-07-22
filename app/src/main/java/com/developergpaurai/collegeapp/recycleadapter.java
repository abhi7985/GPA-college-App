package com.developergpaurai.collegeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recycleadapter extends RecyclerView.Adapter<recycleadapter.holder> {
    ImageView img;
    Object data [][];

    public recycleadapter(Object[][] data) {
        this.data = data;
    }

//    String data[];
//
//    public recycleadapter(String[] data) {
//        this.data = data;
//    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycledemo,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.tv1.setText(data[position][0].toString());
        holder.tv2.setText(data[position][1].toString());
        holder.img.setImageResource(Integer.valueOf((Integer) data[position][2]));
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class holder extends RecyclerView.ViewHolder{
        TextView tv1,tv2;
        ImageView img;

        public holder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.teacher_img);

            tv1 = (TextView) itemView.findViewById(R.id.teacher_name);
            tv2 = (TextView) itemView.findViewById(R.id.teacher_email);


        }
    }
}
