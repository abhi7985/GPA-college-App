package com.developergpaurai.collegeapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    TextView computerlab,communicationlab,library,electricallab,electroniclab,workshop;
    ImageSlider imageSliderhome,imageSliderfaculty,imageSliderstudent;
    SliderView ourfaculty_sliderView,testimonial_sliderView;
    Object [][]student_testimonial = {
            {R.drawable.shaktiumar, R.string.shakti_umar,"Shakti Umar ","Worked at Augurs Technology",R.color.fitcolor},
            {R.drawable.parvezansari, R.string.parvez_ansari,"Parvez Ansari","Worked at nextolive.com as web developer",R.color.fcscolor},
            {R.drawable.udaybhan, R.string.uday_bhan,"Uday Bhan ","Worked at Oppo Mobile India ",R.color.fitcolor},

    };
    Object [][]our_faculty = {
            {R.drawable.csback, "Computer Science and Eng.",R.color.fcscolor},
            {R.drawable.itback, "Information Technology",R.color.fitcolor},
            {R.drawable.elxback, "Electronic Eng.",R.color.felxcolor}
    };
    Testimonial_adapter testimonial_adapter;
    our_faculty_adapter Our_faculty_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");

        imageSliderhome = (ImageSlider) view.findViewById(R.id.image_slider_home);
        testimonial_sliderView = (SliderView) view.findViewById(R.id.student_testimonial_slider);
        ourfaculty_sliderView = (SliderView) view.findViewById(R.id.our_faculty_slider);
        computerlab = (TextView) view.findViewById(R.id.computer_lab);
        communicationlab = (TextView) view.findViewById(R.id.communication_lab);
        library = (TextView) view.findViewById(R.id.library);
        electricallab = (TextView) view.findViewById(R.id.electrical_lab);
        electroniclab = (TextView) view.findViewById(R.id.electronic_lab);
        workshop = (TextView) view.findViewById(R.id.workshop);


        final List<SlideModel> slideModels = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("slider_image");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    slideModels.add(new SlideModel(dataSnapshot.getValue().toString()));
                }
                imageSliderhome.setImageList(slideModels,true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Our_faculty_adapter = new our_faculty_adapter(our_faculty);
        ourfaculty_sliderView.setSliderAdapter(Our_faculty_adapter);
        ourfaculty_sliderView.setIndicatorAnimation(IndicatorAnimationType.SCALE_DOWN);
        ourfaculty_sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        ourfaculty_sliderView.startAutoCycle();

        testimonial_adapter = new Testimonial_adapter(student_testimonial);
        testimonial_sliderView.setSliderAdapter(testimonial_adapter);
        testimonial_sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        testimonial_sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        testimonial_sliderView.startAutoCycle();

        computerlab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","computer_lab");
                threeicon frag = new threeicon();
                frag.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.Framecontainer,frag).addToBackStack(null);
                fr.commit();
            }
        });
        communicationlab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","communication_lab");
                threeicon frag = new threeicon();
                frag.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.Framecontainer,frag).addToBackStack(null);
                fr.commit();

            }
        });
        electricallab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","electrical_lab");
                threeicon frag = new threeicon();
                frag.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.Framecontainer,frag).addToBackStack(null);
                fr.commit();

            }
        });
        electroniclab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","electronic_lab");
                threeicon frag = new threeicon();
                frag.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.Framecontainer,frag).addToBackStack(null);
                fr.commit();

            }
        });
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","library");
                threeicon frag = new threeicon();
                frag.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.Framecontainer,frag).addToBackStack(null);
                fr.commit();

            }
        });
        workshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","workshop");
                threeicon frag = new threeicon();
                frag.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.Framecontainer,frag).addToBackStack(null);
                fr.commit();

            }
        });
        return  view;
    }

}