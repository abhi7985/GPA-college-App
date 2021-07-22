package com.developergpaurai.collegeapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;


public class Notice_showFragment extends Fragment {
    TextView tv_name,tv_notice,download_icon,tv_pdf;
    ImageView imageView;
    private StorageReference mstorageReference;
    CardView cdview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notice_show, container, false);
        tv_name = (TextView) view.findViewById(R.id.notice_name1);
        tv_notice = (TextView) view.findViewById(R.id.notice_notice1);
        tv_pdf = (TextView) view.findViewById(R.id.pdf_notice_show);
        imageView = (ImageView) view.findViewById(R.id.img_notice_show);
        download_icon = (TextView) view.findViewById(R.id.notice_dwn_icon);
        cdview = (CardView) view.findViewById(R.id.card_notice_show);
        Bundle bundle = this.getArguments();
        String name = bundle.getString("kname");
        String notice = bundle.getString("knotice");
        final String file_name = bundle.getString("kimage_name");

        tv_name.setText(name);
        tv_notice.setText(notice);
        mstorageReference = FirebaseStorage.getInstance().getReference().child("Notice_img_and_pdf").child(file_name);
        try {
            if (file_name.startsWith("img")){
                final File localfile = File.createTempFile(file_name,".jpg");
                mstorageReference.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                imageView.setImageBitmap(bitmap);
                                tv_pdf.setVisibility(view.GONE);
                            }
                        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                        imageView.setImageResource(R.drawable.reload);
                        tv_pdf.setVisibility(view.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        imageView.setVisibility(view.GONE);
                    }
                });
            }else if (file_name.startsWith("pdf")){
                final File localfile = File.createTempFile(file_name,".pdf");
                mstorageReference.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                tv_pdf.setBackgroundResource(R.drawable.pdf_icon);
                                imageView.setVisibility(view.GONE);
                            }
                        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                        tv_pdf.setBackgroundResource(R.drawable.reload);
                        imageView.setVisibility(view.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tv_pdf.setVisibility(view.GONE);
                    }
                });
            }else{
                cdview.setVisibility(View.GONE);
                download_icon.setVisibility(View.GONE);
            }

        }catch (Exception e){
            cdview.setVisibility(View.GONE);
        }

        download_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file_name.startsWith("img")){
                    StorageReference databaseReference = FirebaseStorage.getInstance().getReference().child("Notice_img_and_pdf").child(file_name);
                    databaseReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    download(uri, "image");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                                }
                            });

                }else if (file_name.startsWith("pdf")){
                    StorageReference databaseReference = FirebaseStorage.getInstance().getReference().child("Notice_img_and_pdf").child(file_name);
                    databaseReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    download(uri, "pdf");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(getActivity(), "Something missing...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


    private void download(final Uri uri,final String filetype) {
        File rootpath = new File(Environment.getExternalStorageDirectory(),"GPAurai");
        if (!rootpath.exists()){
            rootpath.mkdirs();
        }
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        String directory = "GPAurai";
                        if (filetype == "image"){
                            try {
                                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                DownloadManager.Request request = new DownloadManager.Request(uri)
                                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                        .setDestinationInExternalPublicDir(directory, "image.jpg");
                                downloadManager.enqueue(request);
                                Toast.makeText(getActivity(), "Downloading....", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                            }
                        }else if (filetype == "pdf"){
                            try {
                                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                DownloadManager.Request request = new DownloadManager.Request(uri)
                                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                        .setDestinationInExternalPublicDir(directory, "document.pdf");
                                downloadManager.enqueue(request);
                                Toast.makeText(getActivity(), "Downloading....", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                            }
                        }

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
}