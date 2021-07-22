package com.developergpaurai.collegeapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class student_recycleadpter extends FirebaseRecyclerAdapter<message_dataholder,student_recycleadpter.holder> {
    private StorageReference mstorageReference;
    private String class_branch;
    public student_recycleadpter(@NonNull FirebaseRecyclerOptions<message_dataholder> options ,String class_name) {
        super(options);
        this.class_branch = class_name;
    }

    @Override
    protected void onBindViewHolder(@NonNull final holder holder, int position, @NonNull final message_dataholder model) {

        holder.tv1.setText(model.getName());
        holder.tv2.setText(model.getMessage());
        holder.tv3.setText(model.getTime());
        holder.layout.setVisibility(View.GONE);
        if (model.getImage_name() == null && model.getPdf_name() == null){
            holder.layout.setVisibility(View.GONE);
        }else if (model.getPdf_name() == null){
            holder.layout.setVisibility(View.VISIBLE);
            holder.pdf.setVisibility(View.GONE);
            mstorageReference = FirebaseStorage.getInstance().getReference("Message_image").child(class_branch).child(model.getImage_name());
            try {
                final File localfile = File.createTempFile(model.getImage_name(),".jpg");
                mstorageReference.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                holder.img.setImageBitmap(bitmap);
                                holder.img.setTag(class_branch);
                                holder.tv_download.setTag(model.getImage_name());
                            }
                        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                        holder.img.setImageResource(R.drawable.reload);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        holder.img.setVisibility(View.GONE);
                    }
                });
            }catch (Exception e){

            }
        }else if (model.getImage_name() == null){
            holder.layout.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.GONE);
            mstorageReference = FirebaseStorage.getInstance().getReference("Message_image").child(class_branch).child(model.getPdf_name());
            try {
                final File localfile = File.createTempFile(model.getPdf_name(),".pdf");
                mstorageReference.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                holder.pdf.setTag(class_branch);
                                holder.tv_download.setTag(model.getPdf_name());
                                holder.pdf.setBackgroundResource(R.drawable.pdf_icon);
                            }
                        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                        holder.pdf.setBackgroundResource(R.drawable.reload);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        holder.pdf.setVisibility(View.GONE);
                    }
                });
            }catch (Exception e){

            }
        }
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_recycle,parent,false);
        return new holder(view);
    }


    class holder extends RecyclerView.ViewHolder{
        TextView tv1,tv2,tv3,tv_download,pdf;
        ImageView img;
        LinearLayout layout;
        FirebaseStorage mfirebasestorage;
        public holder(@NonNull View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_image_download);
            img = (ImageView) itemView.findViewById(R.id.image_message);
            pdf = (TextView) itemView.findViewById(R.id.pdf_message);
            tv1 = (TextView) itemView.findViewById(R.id.message_name);
            tv2 = (TextView) itemView.findViewById(R.id.message_item);
            tv3 = (TextView) itemView.findViewById(R.id.message_time);
            tv_download = (TextView) itemView.findViewById(R.id.image_download_icon);
            tv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (img.getTag()!=null){
                        StorageReference databaseReference = FirebaseStorage.getInstance().getReference("Message_image").child(img.getTag().toString()).child(tv_download.getTag().toString());
                        databaseReference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {
//                                    Toast.makeText(v.getContext(), ""+uri.toString(), Toast.LENGTH_SHORT).show();
                                        download(uri,"image");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else if (pdf.getTag()!=null){
                        StorageReference databaseReference = FirebaseStorage.getInstance().getReference("Message_image").child(pdf.getTag().toString()).child(tv_download.getTag().toString());
                        databaseReference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {
//                                    Toast.makeText(v.getContext(), ""+uri.toString(), Toast.LENGTH_SHORT).show();
                                        download(uri,"pdf");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

            });
        }

        private void download(final Uri uri,final String filetype) {
            File rootpath = new File(Environment.getExternalStorageDirectory(),"GPAurai");
            if (!rootpath.exists()){
                rootpath.mkdirs();
            }
            final AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
            Dexter.withActivity(activity)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            String directory = "GPAurai";
                            if (filetype == "image"){
                                try {
                                    DownloadManager downloadManager = (DownloadManager) itemView.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                    DownloadManager.Request request = new DownloadManager.Request(uri)
                                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                            .setDestinationInExternalPublicDir(directory, "image.jpg");
                                    downloadManager.enqueue(request);
                                    Toast.makeText(activity, "Downloading....", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                                }
                            }else if (filetype == "pdf"){
                                try {
                                    DownloadManager downloadManager = (DownloadManager) itemView.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                    DownloadManager.Request request = new DownloadManager.Request(uri)
                                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                            .setDestinationInExternalPublicDir(directory, "document.pdf");
                                    downloadManager.enqueue(request);
                                    Toast.makeText(activity, "Downloading....", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
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
}
