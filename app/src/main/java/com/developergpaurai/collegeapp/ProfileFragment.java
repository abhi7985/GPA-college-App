package com.developergpaurai.collegeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mfirebaseauth;
    TextView name,email,enrollment,branch,year,dob,mobile;
    Button mbtnlogout,retry_btn;
    LinearLayout lname,lemail,lenroll,lbranch,lyear,ldob,lmob;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        getActivity().setTitle("Profile");
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
        }
        else {
            View v2 = inflater.inflate(R.layout.network_connection,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(v2)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getFragmentManager().beginTransaction().replace(R.id.Framecontainer,new ProfileFragment()).commit();
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
        }
        mfirebaseauth = FirebaseAuth.getInstance();
        mbtnlogout = (Button) view.findViewById(R.id.btnlogout);
        name = (TextView) view.findViewById(R.id.uname);
        email = (TextView) view.findViewById(R.id.uemail);
        enrollment = (TextView) view.findViewById(R.id.uenrollment);
        branch = (TextView) view.findViewById(R.id.ubranch);
        year = (TextView) view.findViewById(R.id.uyear);
        dob = (TextView) view.findViewById(R.id.udob);
        mobile = (TextView) view.findViewById(R.id.umobile);
        lname = (LinearLayout) view.findViewById(R.id.lname);
        lemail = (LinearLayout) view.findViewById(R.id.lemail);
        lenroll = (LinearLayout) view.findViewById(R.id.lenroll);
        lbranch = (LinearLayout) view.findViewById(R.id.lbranch);
        lyear = (LinearLayout) view.findViewById(R.id.lyear);
        lmob = (LinearLayout) view.findViewById(R.id.lmobile);
        ldob = (LinearLayout) view.findViewById(R.id.ldob);


        FirebaseUser mfirebaseuser = mfirebaseauth.getCurrentUser();
        String uemail = mfirebaseuser.getEmail();
        final String uid = mfirebaseuser.getUid();
        email.setText(uemail);
        if (mfirebaseuser != null) {
//            there is some user loged in
            FirebaseFirestore.getInstance().collection("TeacherProfile").document(uid)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()){
                            // Teacher Login
                            name.setText(doc.getData().get("name").toString());
                            lbranch.setVisibility(View.GONE);
                            lenroll.setVisibility(View.GONE);
                            lyear.setVisibility(View.GONE);
                            lmob.setVisibility(View.GONE);
                            ldob.setVisibility(View.GONE);

                        }
                        else{

                            FirebaseFirestore.getInstance().collection("UserProfile").document(uid)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot doc = task.getResult();
                                        if (doc.exists()){
                                            name.setText(doc.getData().get("fname").toString());
                                            enrollment.setText(doc.getData().get("enrollment_no").toString());
                                            branch.setText(doc.getData().get("branch").toString());
                                            year.setText(doc.getData().get("year").toString());
                                            dob.setText(doc.getData().get("d_o_b").toString());
                                            mobile.setText(doc.getData().get("mobile_no").toString());
                                        }
                                    }
                                }
                            });

                        }
                    }
                }
            });

        }
        else{
//            no user found
            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.Framecontainer,new LoginFragment());
            fr.commit();
        }



        mbtnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });
        return view;
    }


    public void logout(View view){
        mfirebaseauth.signOut();
        Intent myintent = new Intent(getActivity(),MainActivity.class);
        startActivity(myintent);
    }

}