package com.developergpaurai.collegeapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ClassFragment extends Fragment {
    TextView tvcs1,tvcs2,tvcs3,tvit1,tvit2,tvit3,tvelx1,tvelx2,tvelx3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_class, container, false);
        getActivity().setTitle("Branch and Year");

        view.findViewById(R.id.cs1st).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Computer Science and Eng._1st Year";
                branch(branchandyear);

            }
        });
        view.findViewById(R.id.cs2nd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Computer Science and Eng._2nd Year";
                branch(branchandyear);

            }
        });
        view.findViewById(R.id.cs3rd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Computer Science and Eng._3rd Year";
                branch(branchandyear);

            }
        });


        view.findViewById(R.id.it1st).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Information Technology_1st Year";
                branch(branchandyear);

            }
        });
        view.findViewById(R.id.it2nd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Information Technology_2nd Year";
                branch(branchandyear);

            }
        });
        view.findViewById(R.id.it3rd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Information Technology_3rd Year";
                branch(branchandyear);

            }
        });


        view.findViewById(R.id.elx1st).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Electronics Eng._1st Year";
                branch(branchandyear);
            }
        });
        view.findViewById(R.id.elx2nd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Electronics Eng._2nd Year";
                branch(branchandyear);

            }
        });
        view.findViewById(R.id.elx3rd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Electronics Eng._3rd Year";
                branch(branchandyear);

            }
        });







        view.findViewById(R.id.listcs1st).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Computer Science and Eng._1st Year";
                onclicklist(branchandyear);

            }
        });
        view.findViewById(R.id.listcs2nd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Computer Science and Eng._2nd Year";
                onclicklist(branchandyear);

            }
        });
        view.findViewById(R.id.listcs3rd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Computer Science and Eng._3rd Year";
                onclicklist(branchandyear);

            }
        });


        view.findViewById(R.id.listit1st).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Information Technology_1st Year";
                onclicklist(branchandyear);

            }
        });
        view.findViewById(R.id.listit2nd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Information Technology_2nd Year";
                onclicklist(branchandyear);

            }
        });
        view.findViewById(R.id.listit3rd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Information Technology_3rd Year";
                onclicklist(branchandyear);

            }
        });


        view.findViewById(R.id.listelx1st).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Electronics Eng._1st Year";
                onclicklist(branchandyear);
            }
        });
        view.findViewById(R.id.listelx2nd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Electronics Eng._2nd Year";
                onclicklist(branchandyear);

            }
        });
        view.findViewById(R.id.listelx3rd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branchandyear = "Electronics Eng._3rd Year";
                onclicklist(branchandyear);

            }
        });

        return view;
    }

    private void branch(final String b_and_y) {
        FirebaseFirestore.getInstance().collection("Branch_Year").document(b_and_y)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()){
                        AssesmentFragment fr = new AssesmentFragment();
                        Bundle bundle = new Bundle();
                        String enrollment_no = doc.getData().get("branch").toString();
                        bundle.putString("enrollment_no",enrollment_no);
                        bundle.putString("branch_year",b_and_y);
                        fr.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.Framecontainer,fr).addToBackStack(null).commit();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void onclicklist(String branch){
        studentlistFragment fr = new studentlistFragment();
        getFragmentManager().beginTransaction().replace(R.id.Framecontainer,fr).addToBackStack(null).commit();
    }



}