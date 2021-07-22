package com.developergpaurai.collegeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class LoginFragment extends Fragment {
    Button btn_newacc,btn_login;
    Switch switch_btn;
    EditText email,password;
    private FirebaseAuth mfirebaseauth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        getActivity().setTitle("Login");
        mfirebaseauth = FirebaseAuth.getInstance();
        switch_btn = (Switch) view.findViewById(R.id.switch_student);
        btn_newacc = (Button) view.findViewById(R.id.btnnewacc);
        btn_login = (Button) view.findViewById(R.id.btnlogin);
        email = (EditText) view.findViewById(R.id.etenrollment);
        password = (EditText) view.findViewById(R.id.etpassword);

        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.Framecontainer,new Teacher_loginFragment()).addToBackStack(null);
                    fr.commit();
                    Toast.makeText(getActivity(), "Teacher Login", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Teacher Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_newacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.Framecontainer,new RegisterFragment()).addToBackStack(null);
                fr.commit();

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
    
    public void login(final View view){
        final String memail = email.getText().toString();
        final String mpassword = password.getText().toString();

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("login...");
        dialog.setCancelable(false);
        dialog.show();

        if (!memail.isEmpty()&&!mpassword.isEmpty()){
            mfirebaseauth.signInWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        dialog.dismiss();
                        Intent myintent = new Intent(getActivity(),MainActivity.class);
                        startActivity(myintent);
                    }
                    else{
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Cancled", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            dialog.dismiss();
            Toast.makeText(getActivity(),"Fill the both column", Toast.LENGTH_SHORT).show();
        }
    }
}