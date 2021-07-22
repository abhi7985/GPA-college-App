package com.developergpaurai.collegeapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AboutusFragment extends Fragment {
    Button buttoncs,buttonit,buttonelx,buttonnonstaff,buttonapplied;
    ConstraintLayout cons_layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);
        getActivity().setTitle("About Teachers");
        buttoncs = (Button) view.findViewById(R.id.csbtn);
        buttonit = (Button) view.findViewById(R.id.itbtn);
        buttonelx = (Button) view.findViewById(R.id.elxbtn);
        buttonapplied = (Button) view.findViewById(R.id.applied_btn);
        buttonnonstaff = (Button) view.findViewById(R.id.nonteaching_btn);
        cons_layout = (ConstraintLayout) view.findViewById(R.id.hod_layout);
        buttoncs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherFragment fragment = new teacherFragment();
                Bundle bundle = new Bundle();
                bundle.putString("keybranch","CS");
                fragment.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.Framecontainer, fragment).addToBackStack(null);
                fr.commit();
            }
        });
        buttonit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherFragment fragment = new teacherFragment();
                Bundle bundle = new Bundle();
                bundle.putString("keybranch","IT");
                fragment.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.Framecontainer, fragment).addToBackStack(null);
                fr.commit();
            }
        });
        buttonelx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherFragment fragment = new teacherFragment();
                Bundle bundle = new Bundle();
                bundle.putString("keybranch","ELX");
                fragment.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction().addToBackStack(null);
                fr.replace(R.id.Framecontainer, fragment);
                fr.commit();
            }
        });
        buttonapplied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherFragment fragment = new teacherFragment();
                Bundle bundle = new Bundle();
                bundle.putString("keybranch","applied");
                fragment.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction().addToBackStack(null);
                fr.replace(R.id.Framecontainer, fragment);
                fr.commit();
            }
        });
        buttonnonstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherFragment fragment = new teacherFragment();
                Bundle bundle = new Bundle();
                bundle.putString("keybranch","nonteaching");
                fragment.setArguments(bundle);
                FragmentTransaction fr = getFragmentManager().beginTransaction().addToBackStack(null);
                fr.replace(R.id.Framecontainer, fragment);
                fr.commit();
            }
        });
        return view;
    }

}