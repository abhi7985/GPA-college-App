package com.developergpaurai.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bnv;
    private FirebaseAuth mfirebaseauth;
    private String uid;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.treeicon_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Fragment temp1 = null;
        Bundle bundle = new Bundle();
        switch (item.getItemId()){
            case R.id.aboutdev_nav:
                bundle.putString("key","about");
                temp1 = new threeicon();
                temp1.setArguments(bundle);
                break;
            case R.id.help_support_nav:
                bundle.putString("key","help_support");
                temp1 = new threeicon();
                temp1.setArguments(bundle);
                break;
            case R.id.contactus_nav:
                bundle.putString("key","contact_us");
                temp1 = new threeicon();
                temp1.setArguments(bundle);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.Framecontainer,temp1).commit();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.Framecontainer,new HomeFragment()).commit();
        mfirebaseauth = FirebaseAuth.getInstance();
        FirebaseUser mfirebaseuser = mfirebaseauth.getCurrentUser();
        bnv = (BottomNavigationView) findViewById(R.id.bottomNavigationView);


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
            //connected
        }
        else {
            View v2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.network_connection,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(v2)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                Fragment temp = null;
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.menu_home:
                            temp = new HomeFragment();
                            break;
                        case R.id.menu_notice:
                            temp = new NoticeFragment();
                            break;
                        case R.id.menu_class_assin:
                            temp = new AssesmentFragment();
                            break;
                        case R.id.menu_college:
                            temp = new AboutusFragment();
                            break;
                        case R.id.menu_profile:
                            temp = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.Framecontainer,temp).commit();
                    return true;
                }
            });
        }

        if (mfirebaseuser != null){
            uid = mfirebaseuser.getUid();
            FirebaseFirestore.getInstance().collection("TeacherProfile").document(uid)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            // Teacher Login --------
                            bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                                Fragment temp = null;
                                @Override
                                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                    switch(item.getItemId()){
                                        case R.id.menu_home:
                                            temp = new HomeFragment();
                                            break;
                                        case R.id.menu_notice:
                                            temp = new NoticeFragment();
                                            break;
                                        case R.id.menu_class_assin:
                                            temp = new ClassFragment();
                                            break;
                                        case R.id.menu_college:
                                            temp = new AboutusFragment();
                                            break;
                                        case R.id.menu_profile:
                                            temp = new ProfileFragment();
                                            break;
                                    }
                                    getSupportFragmentManager().beginTransaction().replace(R.id.Framecontainer,temp).commit();
                                    return true;
                                }
                            });
                        }
                        else{
                            // Student Login ------------
                            FirebaseFirestore.getInstance().collection("UserProfile").document(uid)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        if (doc.exists()) {
                                            bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                                                Fragment temp = null;
                                                @Override
                                                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                                    switch(item.getItemId()){
                                                        case R.id.menu_home:
                                                            temp = new HomeFragment();
                                                            break;
                                                        case R.id.menu_notice:
                                                            temp = new NoticeFragment();
                                                            break;
                                                        case R.id.menu_class_assin:
                                                            temp = new AssesmentFragment();
                                                            break;
                                                        case R.id.menu_college:
                                                            temp = new AboutusFragment();
                                                            break;
                                                        case R.id.menu_profile:
                                                            temp = new ProfileFragment();
                                                            break;
                                                    }
                                                    getSupportFragmentManager().beginTransaction().replace(R.id.Framecontainer,temp).commit();
                                                    return true;
                                                }
                                            });
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
            bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                Fragment temp = null;
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.menu_home:
                            temp = new HomeFragment();
                            break;
                        case R.id.menu_notice:
                            temp = new NoticeFragment();
                            break;
                        case R.id.menu_class_assin:
                            temp = new LoginFragment();
                            break;
                        case R.id.menu_college:
                            temp = new AboutusFragment();
                            break;
                        case R.id.menu_profile:
                            temp = new LoginFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.Framecontainer,temp).commit();
                    return true;
                }
            });
        }



    }
    @Override
    protected void onStart() {
        super.onStart();
        final String version = "v.1.0.2";
        FirebaseFirestore.getInstance().collection("Version").document("old_version")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()){
                        if (version.equals(doc.getData().get("normal_version").toString())){
                            // confirm same version
                        }else{
                            // new version
                            View v1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.updatealert,null);
                            if (version.equals(doc.getData().get("compulsory_version").toString())){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setView(v1)
                                        .setMessage("An Update Available")
                                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.developergpaurai.collegeapp");
                                                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("Cancel",null)
                                        .setCancelable(false);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setView(v1)
                                        .setMessage("An Update Available")
                                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.developergpaurai.collegeapp");
                                                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                                startActivity(intent);
                                            }
                                        })
                                        .setCancelable(false);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }


                        }
                    }

                }
            }
        });


    }


    @Override
    public void onBackPressed() {

        if (bnv.getSelectedItemId()== R.id.menu_home){
            super.onBackPressed();

        }else {
            bnv.setSelectedItemId(R.id.menu_home);
        }
    }
}