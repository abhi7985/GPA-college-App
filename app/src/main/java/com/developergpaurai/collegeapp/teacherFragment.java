package com.developergpaurai.collegeapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class teacherFragment extends Fragment {
    Button btndatabase,btnshowdata;
    RecyclerView recyclerView;
    ImageView img_hod;
    TextView tv_name_hod,tv_email_hod;
    ConstraintLayout cons_layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        Bundle bundle = this.getArguments();
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv);
        img_hod = (ImageView) view.findViewById(R.id.hod_image);
        tv_name_hod = (TextView) view.findViewById(R.id.hod_name);
        tv_email_hod = (TextView) view.findViewById(R.id.hod_email);
        cons_layout = (ConstraintLayout) view.findViewById(R.id.hod_layout);

        String branch = bundle.getString("keybranch");
        int[][] name_teacher ={

                {R.array.Anup_Yadav,R.drawable.anup_yadav},
                {R.array.Rishikant_singh,R.drawable.rishikant_singh},
        };
        if ("CS" == branch){
            img_hod.setImageResource(R.drawable.satyaprakashsri);
            tv_name_hod.setText("Satya Prakash Srivastava");
            tv_email_hod.setText("sps_srivastava@rediffmail.com");
            name_teacher = new int[][]{
                    {R.array.Anup_Yadav,R.drawable.anup_yadav},
                    {R.array.Rishikant_singh,R.drawable.rishikant_singh},
            };
        }
        else if("IT" == branch){
            img_hod.setImageResource(R.drawable.kamleshmani);
            tv_name_hod.setText("Kamlesh Mani Tripathi");
            tv_email_hod.setText("tripathikamleshmani@gmail.com");
            name_teacher = new int[][]{
                    {R.array.Rajendra_prasad,R.drawable.rajendra_prasad},
                    {R.array.Vinod_yadav,R.drawable.vinod_yadav},
                    {R.array.Vijay_gupta,R.drawable.vijay_gupta}
            };

        }else if ("ELX" == branch){
            img_hod.setImageResource(R.drawable.prateek_kumar);
            tv_name_hod.setText("Prateek Kumar");
            tv_email_hod.setText("prat_ambastha@gmail.com");
            name_teacher = new int[][]{
                    {R.array.Sanjay_singh,R.drawable.sanjay_singh},
                    {R.array.Anirudh_yadav,R.drawable.anirudh_yadav},
                    {R.array.Vivekanand,R.drawable.vivekanand_yadav},
            };
        }else if ("applied" == branch){
            cons_layout.setVisibility(View.GONE);
            name_teacher = new int[][]{
                    {R.array.Umesh_chaurasiya,R.drawable.umesh_chaurasiya},
                    {R.array.Dhirendra_tripathi,R.drawable.dhirendratri},
                    {R.array.Deepak_sonkar,R.drawable.deepak_sonkar},
            };
        }else if ("nonteaching" == branch){
            cons_layout.setVisibility(View.GONE);
            name_teacher = new int[][]{
                    {R.array.Awadesh_kumar,R.drawable.avadesh_kumar},
                    {R.array.Rc_jaiswal,R.drawable.rc_jaiswal},
                    {R.array.Kanchan,R.drawable.kanchan},
            };
        }


        Object arr[][] = new Object[name_teacher.length][3];

        for (int i=0 ; i<name_teacher.length ; i++){

            String []a = getResources().getStringArray(name_teacher[i][0]);
            for (int ip=i;ip<=i;ip++){
                arr[ip][0] = a[0];
                arr[ip][1] = a[1];
                arr[ip][2] = Integer.valueOf(name_teacher[i][1]);

            }
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new recycleadapter(arr));
        return view;

    }


}