package com.developergpaurai.collegeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.smarteist.autoimageslider.SliderViewAdapter;

public class our_faculty_adapter extends SliderViewAdapter<our_faculty_adapter.holder> {
    Object [][]data;

    public our_faculty_adapter(Object[][] data) {
        this.data = data;
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.our_faculty_adapter,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(holder viewHolder, int position) {
        try{
            viewHolder.img.setImageResource(Integer.valueOf((Integer) data[position][0]));
        }catch (Exception e){
            System.out.println("why");
        }

        viewHolder.tv1.setText(data[position][1].toString());
        viewHolder.cons_layout.setBackgroundResource(Integer.valueOf((Integer) data[position][2]));
    }

    @Override
    public int getCount() {
        return data.length;
    }


    public class holder extends SliderViewAdapter.ViewHolder {
        ImageView img;
        TextView tv1,tv2,tv3;
        ConstraintLayout cons_layout;
        CardView cardView;
        public holder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.our_faculty_image_slider);
            tv1 = (TextView) itemView.findViewById(R.id.branch_name_slider);
            cons_layout = (ConstraintLayout) itemView.findViewById(R.id.constrain_slider);

        }
    }
}
