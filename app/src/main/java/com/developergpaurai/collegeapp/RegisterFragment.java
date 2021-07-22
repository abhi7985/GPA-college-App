package com.developergpaurai.collegeapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    Button btnregister;
    private EditText fullname,email,enrollment,password,mobileno;
    private TextView dob;
    private Spinner spinner_branch,spinner_year;
    private FirebaseAuth mfirebaseauth;
    private FirebaseFirestore fstore;
    String userid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        getActivity().setTitle("Register");
        mfirebaseauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        btnregister = (Button) view.findViewById(R.id.btnregister);
        fullname = (EditText) view.findViewById(R.id.full_name);
        email = (EditText) view.findViewById(R.id.email_address);
        enrollment = (EditText) view.findViewById(R.id.enrollment_no);
        password = (EditText) view.findViewById(R.id.password);
        mobileno = (EditText) view.findViewById(R.id.phone_no);
        dob = (TextView) view.findViewById(R.id.d_o_b);
        spinner_branch = (Spinner) view.findViewById(R.id.branch);
        spinner_year = (Spinner) view.findViewById(R.id.branch_year);



        ArrayAdapter<String> myadapter_branch = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.branch_name));
        myadapter_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_branch.setAdapter(myadapter_branch);

        ArrayAdapter<String> myadapter_year = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.branch_year));
        myadapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(myadapter_year);
        String p = spinner_branch.getSelectedItem().toString();

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register(v);
            }
        });

        // for date import
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        dob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date  = dayOfMonth+"/"+month+"/"+year;
                        dob.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        //

        return view;
    }
    private void register(View view){
        final String mfull_name = fullname.getText().toString();
        final String memail = email.getText().toString();
        final String menrollment = enrollment.getText().toString();
        final String mpassword = password.getText().toString();
        final String mmobileno = mobileno.getText().toString();
        final String mdob = dob.getText().toString();
        final String mselect_branch = spinner_branch.getSelectedItem().toString();
        final String mselect_year = spinner_year.getSelectedItem().toString();

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Uploading...");
        dialog.setCancelable(false);
        dialog.show();

        if (!memail.isEmpty()&&!mpassword.isEmpty()){
            //got the values
            if (mpassword.length()>=6){
                //
                if (menrollment.length()>=10 && menrollment.startsWith("E")){
                    mfirebaseauth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                userid = mfirebaseauth.getCurrentUser().getUid();
                                DocumentReference documentrefrence = fstore.collection("UserProfile").document(userid);

                                Map<String,Object> user = new HashMap<>();
                                user.put("fname",mfull_name);
                                user.put("email",memail);
                                user.put("enrollment_no",menrollment);
                                user.put("mobile_no",mmobileno);
                                user.put("d_o_b",mdob);
                                user.put("branch",mselect_branch);
                                user.put("year",mselect_year);
                                user.put("approval","NO");
                                documentrefrence.set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(), "Succesfully Register", Toast.LENGTH_SHORT).show();
                                                Intent myintent = new Intent(getActivity(),MainActivity.class);
                                                startActivity(myintent);
                                            }
                                        });

                            }
                            else{
                                //something went wrong
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Failed
                            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            System.out.println("Canceled");
                            dialog.dismiss();
                        }
                    });
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Enrollment not match", Toast.LENGTH_SHORT).show();
                }

            }
            else{
                // password is short
                dialog.dismiss();
                Toast.makeText(getActivity(), "Password is short", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            //no field can be left
            dialog.dismiss();
            Toast.makeText(getActivity(), "No Field Can be Blanck", Toast.LENGTH_SHORT).show();
        }
    }
}