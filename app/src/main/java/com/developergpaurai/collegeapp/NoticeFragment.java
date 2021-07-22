package com.developergpaurai.collegeapp;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class NoticeFragment extends Fragment {
    private FirebaseAuth mfirebaseauth;
    RecyclerView rcv;
    EditText subjectofnotice;
    Button notice_btn,cancel_btn,create_btn,browse_btn,retry_btn;
    ConstraintLayout layout;
    ImageView image_notice;
    ArrayList<ArrayList<String>> array;
    ArrayList<String> arrayList;
    TextView pdf_add;
    notice_recycleadapter myadapter;

    private String uid,teacher_name,time,date,notice,notice_id;

    Uri filepath;
    Bitmap bitmap,bitmap1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        final View view1 = inflater.inflate(R.layout.fragment_notice, container, false);
        getActivity().setTitle("Notice");

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
            //connected
        }
        else {
            View v2 = inflater.inflate(R.layout.network_connection,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(v2)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getFragmentManager().beginTransaction().replace(R.id.Framecontainer,new NoticeFragment()).commit();
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
        }


        mfirebaseauth = FirebaseAuth.getInstance();
        array = new ArrayList<>();
        FirebaseUser mfirebaseuser = mfirebaseauth.getCurrentUser();

        rcv = (RecyclerView) view.findViewById(R.id.notice_rcv);
        subjectofnotice = (EditText) view.findViewById(R.id.subject_notice);

        notice_btn = (Button) view.findViewById(R.id.btn_createnotice);
        browse_btn = (Button) view.findViewById(R.id.btn_browse);
        cancel_btn = (Button) view.findViewById(R.id.btn_cancel);
        create_btn = (Button) view.findViewById(R.id.btn_create);
        pdf_add = (TextView) view.findViewById(R.id.add_pdf_notice);

        image_notice = (ImageView) view.findViewById(R.id.img_notice);


        layout = (ConstraintLayout) view.findViewById(R.id.createnotice);

        FirebaseRecyclerOptions<notice_dataholder> options =
                new FirebaseRecyclerOptions.Builder<notice_dataholder>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Notice").orderByChild("date"), notice_dataholder.class)
                        .build();
        myadapter = new notice_recycleadapter(options);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcv.setAdapter(myadapter);
        myadapter.startListening();

        if (mfirebaseuser != null){
            uid = mfirebaseuser.getUid();
            FirebaseFirestore.getInstance().collection("TeacherProfile").document(uid)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            // Teacher Login
                            layout.setVisibility(view1.GONE);
                            notice_btn.setVisibility(view1.VISIBLE);

                        }
                        else{
                            FirebaseFirestore.getInstance().collection("UserProfile").document(uid)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        if (doc.exists()) {
                                            // Student Login
                                            layout.setVisibility(view1.GONE);
                                            notice_btn.setVisibility(view1.GONE);

                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });

        }else{
            //no one Login
            notice_btn.setVisibility(view.GONE);
        }



        notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
            }
        });
        browse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Please select image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        pdf_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("application/pdf");
                                startActivityForResult(Intent.createChooser(intent,"Please select pdf"),2);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
            }
        });
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                date = DateFormat.getDateInstance().format(calendar.getTime());
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                time = timeFormat.format(new Date());

                SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyyMMdd_hh:mm:ss_a");
                final String date_id = timeFormat1.format(new Date());

                notice = subjectofnotice.getText().toString();
                FirebaseFirestore.getInstance().collection("TeacherProfile").document(uid)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()){
                                teacher_name = doc.getData().get("name").toString();
                                notice_id = "msg"+teacher_name+date_id;
                                notice_dataholder obj = new notice_dataholder(teacher_name,date,time,notice,notice_id);
                                if (image_notice.getDrawable() != null){
                                    notice_id = "img"+teacher_name+date_id;
                                    obj = new notice_dataholder(teacher_name,date,time,notice,notice_id);
                                    upload(notice_id,obj);
                                }else if (pdf_add.getText().equals("Loaded..")){
                                    notice_id = "pdf"+teacher_name+date_id;
                                    obj = new notice_dataholder(teacher_name,date,time,notice,notice_id);
                                    upload(notice_id,obj);
                                }else{
                                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                                    DatabaseReference node = db.getReference("Notice");
                                    node.child(notice_id).setValue(obj);
                                }

                            }
                        }
                    }
                });
            }
        });

        return view;
    }

    private void upload(String image_name,final notice_dataholder obj) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Uploading...");
        dialog.setCancelable(false);
        dialog.show();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference reference = firebaseStorage.getReference().child("Notice_img_and_pdf").child(image_name);
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference node = db.getReference("Notice");
                        node.child(notice_id).setValue(obj);
                        Toast.makeText(getActivity(), "File Upload", Toast.LENGTH_SHORT).show();
                        FragmentTransaction fr = getFragmentManager().beginTransaction();
                        fr.replace(R.id.Framecontainer,new NoticeFragment()).addToBackStack(null);
                        fr.commit();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        dialog.setMessage("Uploaded "+(int)percent+"%");
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK){
            filepath = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                image_notice.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
                System.out.println("the sout is"+e);

            }
        }else if(requestCode == 2 && resultCode == RESULT_OK){
            filepath = data.getData();
            pdf_add.setBackgroundResource(0);
            pdf_add.setText("Loaded..");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        myadapter.stopListening();
    }
}