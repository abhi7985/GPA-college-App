package com.developergpaurai.collegeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class threeicon extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ImageView img,img1,img2,img3;
        TextView tv_heading,tv;
        Bundle bundle = this.getArguments();
        String name = bundle.getString("key");
        View v1 =  inflater.inflate(R.layout.fragment_threeicon, container, false);
        img = (ImageView) v1.findViewById(R.id.image_facility);
        tv = (TextView) v1.findViewById(R.id.text_facility);
        tv_heading = (TextView) v1.findViewById(R.id.heading_facility);



        if (name.equals("about")){
            View v =  inflater.inflate(R.layout.about_developer, container, false);
            img1 = (ImageView) v.findViewById(R.id.facebook_icon);
            img2 = (ImageView) v.findViewById(R.id.instagram_icon);
            img3 = (ImageView) v.findViewById(R.id.linkedin_icon);
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "https://www.facebook.com/profile.php?id=100007989045357";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(uri));
                    startActivity(i);
                }
            });
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "https://www.instagram.com/a_j_7985/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(uri));
                    startActivity(i);

                }
            });
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "https://in.linkedin.com/in/abhishekjaiswal79";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(uri));
                    startActivity(i);

                }
            });
            return v;
        }else if (name.equals("help_support")){
            View v =  inflater.inflate(R.layout.help_support, container, false);
            return v;
        }else if (name.equals("contact_us")){
            View v =  inflater.inflate(R.layout.contact_us, container, false);
            TextView map_click = (TextView) v.findViewById(R.id.map_link_contact);
            map_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "https://maps.app.goo.gl/ZeEqnBpeCYwziXJL8";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);

                }
            });
            return v;
        }
        else if (name.equals("computer_lab")){
            tv_heading.setText("Computer Lab");
            img.setImageResource(R.drawable.computerlab);
            tv.setText(R.string.scomputer_lab);
            return v1;
        }else if (name.equals("communication_lab")){
            tv_heading.setText("Communication Lab");
            img.setImageResource(R.drawable.logo);
            tv.setText(R.string.scommunication_lab);
            return v1;
        }else if (name.equals("electrical_lab")){
            tv_heading.setText("Electrical Lab");
            img.setImageResource(R.drawable.logo);
            tv.setText(R.string.selectrical_lab);
            return v1;
        }else if (name.equals("electronic_lab")){
            tv_heading.setText("Electronic Lab");
            img.setImageResource(R.drawable.logo);
            tv.setText(R.string.selectronic_lab);
            return v1;
        }else if (name.equals("library")){
            tv_heading.setText("Library");
            img.setImageResource(R.drawable.library);
            tv.setText(R.string.slibrary);
            return v1;
        }else if (name.equals("workshop")){
            tv_heading.setText("Workshop");
            img.setImageResource(R.drawable.workshop);
            tv.setText(R.string.sworkshop);
            return v1;
        }
        else{
            View v =  inflater.inflate(R.layout.fragment_threeicon, container, false);
            return v;
        }

    }
}