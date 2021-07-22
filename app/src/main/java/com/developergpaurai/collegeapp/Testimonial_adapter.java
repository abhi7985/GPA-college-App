package com.developergpaurai.collegeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.smarteist.autoimageslider.SliderViewAdapter;

public class Testimonial_adapter extends SliderViewAdapter<Testimonial_adapter.holder> {
    Object [][]data;

    public Testimonial_adapter(Object[][] data) {
        this.data = data;
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_testimonial,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(holder viewHolder, int position) {
        viewHolder.img.setImageResource(Integer.valueOf((Integer) data[position][0]));
        viewHolder.tv1.setText(Integer.valueOf((Integer) data[position][1]));
        viewHolder.tv2.setText(data[position][2].toString());
        viewHolder.tv3.setText(data[position][3].toString());
        viewHolder.cons_layout_testimonial.setBackgroundResource(Integer.valueOf((Integer) data[position][4]));
    }

    @Override
    public int getCount() {
        return data.length;
    }

    public class holder extends SliderViewAdapter.ViewHolder {
        ImageView img;
        TextView tv1,tv2,tv3;
        ConstraintLayout cons_layout_testimonial;
        public holder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.student_image);
            cons_layout_testimonial = (ConstraintLayout) itemView.findViewById(R.id.cons_layout_testimonial);
            tv1 = (TextView) itemView.findViewById(R.id.student_message);
            tv2 = (TextView) itemView.findViewById(R.id.student_name);
            tv3 = (TextView) itemView.findViewById(R.id.student_qualification);
        }
    }
}
