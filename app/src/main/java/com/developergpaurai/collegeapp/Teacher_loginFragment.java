package com.developergpaurai.collegeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Teacher_loginFragment extends Fragment {
    Switch switch_btn;
    Button btn_login;
    EditText email,password;
    private FirebaseAuth mfirebaseauth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_login, container, false);
        mfirebaseauth = FirebaseAuth.getInstance();
        switch_btn = (Switch) view.findViewById(R.id.switch_teacher);
        email      = (EditText) view.findViewById(R.id.email_teacher_login);
        password   = (EditText) view.findViewById(R.id.pass_teacher_login);
        btn_login  = (Button) view.findViewById(R.id.btn_teacher_login);


        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.Framecontainer, new LoginFragment());
                    fr.commit();
                    Toast.makeText(getActivity(), "Student Login", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Nothing To Change", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        return view;
    }
    public void login(View view){
        final String memail = email.getText().toString();
        final String mpassword = password.getText().toString();
        if (!memail.isEmpty()&&!mpassword.isEmpty()){
            mfirebaseauth.signInWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent myintent = new Intent(getActivity(),MainActivity.class);
                        startActivity(myintent);
                    }
                    else{
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(getActivity(), "Cancled", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.Framecontainer,new LoginFragment());
            fr.commit();
            Toast.makeText(getActivity(),"Fill the both column", Toast.LENGTH_SHORT).show();
        }
    }

}