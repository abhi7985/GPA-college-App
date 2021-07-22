
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class AssesmentFragment extends Fragment {

    private FirebaseAuth mfirebaseauth;
    ScrollView scv;
    ImageView imgv;
    private String uid,uemail,uname,uyear,uenrollment,teacher_name,Class;
    private EditText message;
    private TextView sendmessage,attach_icon,add_photo,add_pdf;
    LinearLayout attach_layout,type_message_lay,message_lay;
    RecyclerView recyclerView;
    student_recycleadpter myadapter;
    ArrayList<ArrayList<String>> arr;
    ArrayList<String> arr1;
    Uri filepath;
    Bitmap bitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_assesment, container, false);

        // Inflate the layout for this fragment
        final Boolean[] boolen = {false};

        mfirebaseauth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_student);
        message = (EditText) view.findViewById(R.id.type_message);
        sendmessage = (TextView) view.findViewById(R.id.send_message);
        attach_icon = (TextView) view.findViewById(R.id.attach_file_message);
        add_photo = (TextView) view.findViewById(R.id.add_photo_message);
        add_pdf = (TextView) view.findViewById(R.id.add_pdf_message);
        attach_layout = (LinearLayout) view.findViewById(R.id.layout_attach_message);
        type_message_lay = (LinearLayout) view.findViewById(R.id.type_message_layout);
        message_lay = (LinearLayout) view.findViewById(R.id.send_message_allow_layout);
        scv = (ScrollView) view.findViewById(R.id.scroll_view_message);
        imgv = (ImageView) view.findViewById(R.id.image_show_message);

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
                            getFragmentManager().beginTransaction().replace(R.id.Framecontainer,new AssesmentFragment()).commit();
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
        }




        // message part
        FirebaseRecyclerOptions<message_dataholder> options1 =
                new FirebaseRecyclerOptions.Builder<message_dataholder>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Class_Messages").child("E184457355"),message_dataholder.class)
                        .build();
        myadapter = new student_recycleadpter(options1,"E18444");

        arr = new ArrayList<>();
        // Check User Id to get Branch
        FirebaseUser firebaseUser = mfirebaseauth.getCurrentUser();
        if(firebaseUser != null){
            uid = firebaseUser.getUid();
            uemail = firebaseUser.getEmail();
            final Bundle bundle = this.getArguments();
            FirebaseFirestore.getInstance().collection("TeacherProfile").document(uid)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        final DocumentSnapshot doc = task.getResult();
                        if (doc.exists()){
                            type_message_lay.setVisibility(View.VISIBLE);
                            message_lay.setVisibility(View.GONE);
                            Class = bundle.getString("enrollment_no");
                            final String b_and_y = bundle.getString("branch_year");
                            try {
                                getActivity().setTitle(b_and_y);
                            }catch (Exception e){
                                System.out.println("Error to set Title");
                            }

                            teacher_name = doc.getData().get("name").toString();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            // show message part
                            FirebaseRecyclerOptions<message_dataholder> options =
                                    new FirebaseRecyclerOptions.Builder<message_dataholder>()
                                            .setQuery(FirebaseDatabase.getInstance().getReference("Class_Messages").child(Class).orderByChild("date"),message_dataholder.class)
                                            .build();
                            myadapter = new student_recycleadpter(options,Class);
                            recyclerView.setAdapter(myadapter);
                            myadapter.startListening();
                            //-------

                            sendmessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //only for image and message upload
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                                    String time = timeFormat.format(new Date());
                                    SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyyMMdd_hh:mm:ss_a");
                                    String date = timeFormat1.format(new Date());
                                    String message_id = teacher_name+date;

                                    if (message.getText().toString().equals("")){
                                        if (imgv.getDrawable() == null){
                                            // no message and  no imaage
                                            Toast.makeText(getActivity(),"null",Toast.LENGTH_SHORT).show();
                                        }else{
                                            // not message write on uploading image
                                            upload(Class,teacher_name,message.getText().toString());
                                            AssesmentFragment fr = new AssesmentFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("enrollment_no",Class);
                                            bundle.putString("branch_year",b_and_y);
                                            fr.setArguments(bundle);
                                            getFragmentManager().beginTransaction().replace(R.id.Framecontainer,fr).addToBackStack(null).commit();
                                        }
                                    }
                                    else{
                                        if (imgv.getDrawable() != null){
                                            // some message and images
                                            String image_name = message_id;
                                            String pdf_name = null;
                                            upload(Class,teacher_name,message.getText().toString());
                                            message.setText("");
                                            AssesmentFragment fr = new AssesmentFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("enrollment_no",Class);
                                            bundle.putString("branch_year",b_and_y);
                                            fr.setArguments(bundle);
                                            getFragmentManager().beginTransaction().replace(R.id.Framecontainer,fr).addToBackStack(null).commit();
                                        }
                                        else{
                                            // some meesaage and  no image
                                            String image_name = null;
                                            String pdf_name = null;
                                            message_dataholder obj = new message_dataholder(teacher_name, message.getText().toString(), date, time, image_name, pdf_name);
                                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                                            DatabaseReference node = db.getReference("Class_Messages");
                                            node.child(Class).child(message_id).setValue(obj);
                                            message.setText("");
                                        }
                                    }
                                }
                            });
                        }
                        else{
                            // Student login check
                            try {
                                FirebaseFirestore.getInstance().collection("UserProfile").document(uid)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            final DocumentSnapshot doc = task.getResult();
                                            if (doc.exists()) {
                                                uenrollment = doc.getData().get("enrollment_no").toString();
                                                uname = doc.getData().get("fname").toString();
                                                String branch = doc.getData().get("branch").toString();
                                                String year = doc.getData().get("year").toString();

                                                final String Class = uenrollment.substring(0, 10);
                                                final String b_and_y = branch + "_" + year;

                                                FirebaseFirestore.getInstance().collection("Branch_Year").document(b_and_y)
                                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot doc1 = task.getResult();
                                                            if (doc1.exists()) {
                                                                if (doc1.getData().get("branch").toString().equals(Class)) {
                                                                    try {
                                                                        getActivity().setTitle(b_and_y);
                                                                    }catch (Exception e){
                                                                        System.out.println("Error"+e);
                                                                    }

                                                                    type_message_lay.setVisibility(View.GONE);
                                                                    attach_icon.setVisibility(View.GONE);
                                                                    message_lay.setVisibility(View.VISIBLE);
                                                                    recyclerView = (RecyclerView) view.findViewById(R.id.rcv_student);
                                                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                                                    // show message part
                                                                    FirebaseRecyclerOptions<message_dataholder> options =
                                                                            new FirebaseRecyclerOptions.Builder<message_dataholder>()
                                                                                    .setQuery(FirebaseDatabase.getInstance().getReference("Class_Messages").child(Class).orderByChild("date"), message_dataholder.class)
                                                                                    .build();
                                                                    myadapter = new student_recycleadpter(options, Class);
                                                                    recyclerView.setAdapter(myadapter);
                                                                    myadapter.startListening();

                                                                    // send message part
                                                                    sendmessage.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {

                                                                            if (message.getText().toString().equals("")) {
                                                                                // no msg

                                                                            } else {
                                                                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                                                                                String time = timeFormat.format(new Date());
                                                                                SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyyMMdd_hh:mm:ss_a");
                                                                                String date = timeFormat1.format(new Date());
                                                                                String message_id = uname + date;
                                                                                String image_name = null;
                                                                                String pdf_name = null;
                                                                                message_dataholder obj = new message_dataholder(uname, message.getText().toString(), date, time, image_name, pdf_name);
                                                                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                                                                DatabaseReference node = db.getReference("Class_Messages");
                                                                                node.child(Class).child(message_id).setValue(obj);
                                                                                message.setText("");
                                                                            }
                                                                        }
                                                                    });
                                                                } else {
                                                                    type_message_lay.setVisibility(View.GONE);
                                                                    attach_icon.setVisibility(View.GONE);
                                                                    message_lay.setVisibility(View.VISIBLE);
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                                    builder.setTitle("Locked Message")
                                                                            .setMessage("Check Your Enrollment No or Approval Pending")
                                                                            .setPositiveButton("OK", null);
                                                                    AlertDialog alertDialog = builder.create();
                                                                    alertDialog.show();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        System.out.println("Fail it is");
                                                    }
                                                });

                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Not show ", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }catch (Exception e){
                                System.out.println(e);
                            }
                        }
                        // ----------------------------
                    }
                }
            });

        }else{
            //not Login
            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.Framecontainer,new LoginFragment()).addToBackStack(null);
            fr.commit();
        }


        attach_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boolen[0]){
                    attach_layout.setVisibility(View.GONE);
                    boolen[0] = false;
                }else {
                    attach_layout.setVisibility(View.VISIBLE);
                    boolen[0] = true;
                }

            }
        });
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scv.setVisibility(View.GONE);
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
                Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT).show();
            }
        });

        add_pdf.setOnClickListener(new View.OnClickListener() {
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
                                AssesmentFragment fr = new AssesmentFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("enrollment_no",Class);
                                bundle.putString("branch_year",getActivity().getTitle().toString());
                                fr.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.Framecontainer,fr).addToBackStack(null).commit();
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

        return view;

    }


    private void upload(final String class_name,final String sender_name,final String messages) {
        final String image_name,pdf_name,time,date,message_id;
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Sending...");
        dialog.setCancelable(false);
        dialog.show();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        time = timeFormat.format(new Date());
        SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyyMMdd_hh:mm:ss_a");
        date = timeFormat1.format(new Date());
        message_id = sender_name+date;

        if (messages != null){
            // ------------------Image Section - ---------------------------------------------------
            image_name = message_id;
            pdf_name = null;
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference reference = firebaseStorage.getReference("Message_image").child(class_name).child(message_id);
            reference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            message_dataholder obj = new message_dataholder(sender_name,messages,date,time,image_name,pdf_name);
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference node = db.getReference("Class_Messages");
                            node.child(class_name).child(message_id).setValue(obj);
                            message.setText("");
                            imgv.setVisibility(View.GONE);
                            scv.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Send", Toast.LENGTH_SHORT).show();



                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            dialog.setMessage("Sending "+(int)percent+"%");
                        }
                    });

        }else{
            //------------------------------Pdf Section --------------------------------
            image_name = null;
            pdf_name = message_id;
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference reference = firebaseStorage.getReference("Message_image").child(class_name).child(message_id);
            reference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            message_dataholder obj = new message_dataholder(sender_name,messages,date,time,image_name,pdf_name);
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference node = db.getReference("Class_Messages");
                            node.child(class_name).child(message_id).setValue(obj);
                            message.setText("");
                            imgv.setVisibility(View.GONE);
                            scv.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "File Upload", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            dialog.setMessage("Sending "+(int)percent+"%");
                        }
                    });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK){
            filepath = data.getData();
            String filename = filepath.getScheme();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imgv.setVisibility(View.VISIBLE);
                imgv.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
                System.out.println("the sout is"+e);

            }
        }
        else if (requestCode == 2 && resultCode == RESULT_OK){
            filepath = data.getData();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String time = timeFormat.format(new Date());
            SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyy/MM/dd_hh:mm:ss_a");
            String date = timeFormat1.format(new Date());
            String message_id = teacher_name+date;
            String pdf_name = null;

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Are You Sure ")
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            upload(Class,teacher_name,null);
                        }
                    })
                    .setNegativeButton("Cancel",null);
            AlertDialog alert = builder.create();
            alert.show();
        }
        else{
            scv.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseUser mfirebaseuser = mfirebaseauth.getCurrentUser();
//        if (mfirebaseuser != null) {
////            there is some user loged in
//
//        }
//        else{
////            no user found
//            FragmentTransaction fr = getFragmentManager().beginTransaction();
//            fr.replace(R.id.Framecontainer,new LoginFragment()).addToBackStack(null);
//            fr.commit();
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        myadapter.stopListening();
    }
}
