package com.developergpaurai.collegeapp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class firebase_collection {
    String uid,usercheck;
    public static String checkuser1;




    public String checkuser(){
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        FirebaseUser muser = mauth.getCurrentUser();
        uid = muser.getUid();

        FirebaseFirestore.getInstance().collection("TeacherProfile").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()){
                        //Teacher Login
                        checkuser1 = "Teacher Login";
                    }
                }
                else {
                    FirebaseFirestore.getInstance().collection("UserProfile").document(uid)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists()){
                                    // Student Login
                                    checkuser1 = "Student Login";
                                }
                            }
                        }
                    });
                }

            }

        });
        return checkuser1;
    }


}
