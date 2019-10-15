package id.co.telkomsigma.Diarium.controller.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.MenuAdapter;
import id.co.telkomsigma.Diarium.controller.home.main_menu.mydevelopment.myknowledge.MyKnowledgeActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.mytime.checkin.CheckinActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.community.CommunityActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.employee_care.EmployeeCareActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.employee_corner.EmployeeCornerActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.hr_wiki.HRWikiActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.MyEventActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myteam.MyTeamActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.mytime.izin.IzinActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.mydevelopment.mytraining.MyTrainingActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.onehseet.OneSheetActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.personal_data.PersonalDataActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.report.ReportActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.survey.SurveyActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.today_activity.TodayActivity;
import id.co.telkomsigma.Diarium.util.UserSessionManager;

public class SubMyTimeActivity extends AppCompatActivity {
    GridView gridView;
    TextView tvNull;
    UserSessionManager session;
    //============================================================================================== TIME 1
    String[] menuMyTime ={
            "Check In",
            "Cuti Online"

    };

    int[] imageMyTime={
            R.drawable.checkin,
            R.drawable.ic_cuti_online
    };

    //============================================================================================== PERFORMANCE 0
    String[] menuMyPerformance ={
            "Flexible Point Reward",
            "Kontrak Management",
            "SKI / NKI",
            "CBHRM 360",

    };
    int[] imageMyPerformance={
            R.drawable.ic_reward,
            R.drawable.today_activity,
            R.drawable.today_activity,
            R.drawable.ic_cbhrm
    };
    //============================================================================================== TASK 1
    String[] menuMyTask ={
            "Today Activity",
            "Report Activity"

    };

    int[] imageMyTask={
            R.drawable.today_activity,
            R.drawable.report
    };
    //============================================================================================== PROFILE 8
    String[] menuMyProfile ={
            "Personal Data",
            "One Sheets",
            "My Team",
            "My Event",
            "Community",
            "Employee Care",
            "Survey",
            "Employee Corner",
            "HR Wiki"

    };

    int[] imageMyProfile={
            R.drawable.user,
            R.drawable.cv,
            R.drawable.team,
            R.drawable.myevent,
            R.drawable.team,
            R.drawable.team,
            R.drawable.cv,
            R.drawable.employee_corner,
            R.drawable.myevent
    };
    //============================================================================================== TRAVEL 0
    String[] menuMyTravel ={
            "SPPD Online"
    };
    int[] imageMyTravel={
            R.drawable.ic_sppd
    };
    //============================================================================================== INTEGRITY 0
    String[] menuMyintegrity ={
            "LHKPN",
            "SPT Online",
            "Pakta Integritas",
            "Etika Bisnis"
    };


    int[] imageMyIntegrity={
            R.drawable.ic_lhkpn,
            R.drawable.ic_spt_online,
            R.drawable.ic_pakta_integritas,
            R.drawable.ic_etika_bisnis
    };
    //============================================================================================== DEVELOPMENT 0
    String[] menuMyDevelopment ={
            "My Career",
            "My Knowledge",
            "Assistium",
            "My Learning"
    };

    int[] imageMyDevelopment={
            R.drawable.myevent,
            R.drawable.ic_congnitium,
            R.drawable.myevent,
            R.drawable.myevent
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_my_time);
        session = new UserSessionManager(this);
        gridView = findViewById(R.id.gridview);
        tvNull = findViewById(R.id.tvNull);
        tvNull.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String code = intent.getStringExtra("code");
        MenuAdapter gridAdapter = null;
        switch(code) {
            case "MyTime":
                tvNull.setVisibility(View.GONE);
                gridAdapter = new MenuAdapter(SubMyTimeActivity.this,imageMyTime,menuMyTime);
                break;
            case "MyPerformance":
                tvNull.setVisibility(View.GONE);
                gridAdapter = new MenuAdapter(SubMyTimeActivity.this,imageMyPerformance,menuMyPerformance);
                break;
            case "MyTask":
                tvNull.setVisibility(View.GONE);
                gridAdapter = new MenuAdapter(SubMyTimeActivity.this,imageMyTask,menuMyTask);
                break;
            case "MyProfile":
                tvNull.setVisibility(View.GONE);
                gridAdapter = new MenuAdapter(SubMyTimeActivity.this,imageMyProfile,menuMyProfile);
                break;
            case "MyTravel":
                tvNull.setVisibility(View.GONE);
                gridAdapter = new MenuAdapter(SubMyTimeActivity.this,imageMyTravel,menuMyTravel);
                break;
            case "MyIntegrity":
                tvNull.setVisibility(View.GONE);
                gridAdapter = new MenuAdapter(SubMyTimeActivity.this,imageMyIntegrity,menuMyintegrity);
                break;
            case "MyDevelopment":
                tvNull.setVisibility(View.GONE);
                gridAdapter = new MenuAdapter(SubMyTimeActivity.this,imageMyDevelopment,menuMyDevelopment);
                break;
        }


        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                String clickedText = null;
                switch(code) {
                    case "MyTime":
                        clickedText = menuMyTime[position];
                        break;
                    case "MyPerformance":
                        clickedText = menuMyPerformance[position];
                        break;
                    case "MyTask":
                        clickedText = menuMyTask[position];
                        break;
                    case "MyProfile":
                        clickedText = menuMyProfile[position];
                        break;
                    case "MyTravel":
                        clickedText = menuMyTravel[position];
                        break;
                    case "MyIntegrity":
                        clickedText = menuMyintegrity[position];
                        break;
                    case "MyDevelopment":
                        clickedText = menuMyDevelopment[position];
                        break;
                }
//                Toast.makeText(getActivity(), "posisi"+clickedText, Toast.LENGTH_SHORT).show();
                Intent i;
                switch (clickedText) {

                    //============================================================================================== TIME
                    case "Check In":
                        i = new Intent(SubMyTimeActivity.this, CheckinActivity.class);
                        startActivity(i);
                        break;
                    case "Cuti Online":
                        i = new Intent(SubMyTimeActivity.this, IzinActivity.class);
                        startActivity(i);
                        break;

                    //============================================================================================== PERFORMANCE
                    case "Flexible Point Reward":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "Kontrak Management":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                    case "CBHRM 360":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "SKI / NKI":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;

                    //============================================================================================== TASK
                    case "Today Activity":
                        i = new Intent(SubMyTimeActivity.this, TodayActivity.class);
                        i.putExtra("personal_number",session.getUserNIK());
                        i.putExtra("name",session.getUserFullName());
                        i.putExtra("status","none");
                        i.putExtra("position",session.getJob());
                        i.putExtra("avatar",session.getAvatar());
                        startActivity(i);
                        break;
                    case "Report Activity":
                        i = new Intent(SubMyTimeActivity.this, ReportActivity.class);
                        startActivity(i);
                        break;

                    //============================================================================================== PROFILE
                    case "Personal Data":
                        i = new Intent(SubMyTimeActivity.this, PersonalDataActivity.class);
                        startActivity(i);
                        break;
                    case "One Sheets":
                        i = new Intent(SubMyTimeActivity.this, OneSheetActivity.class);
                        startActivity(i);
                        break;
                    case "My Team":
                        i = new Intent(SubMyTimeActivity.this, MyTeamActivity.class);
                        startActivity(i);
                        break;
                    case "My Event":
                        i = new Intent(SubMyTimeActivity.this, MyEventActivity.class);
                        startActivity(i);
                        break;
                    case "Community":
                        i = new Intent(SubMyTimeActivity.this, CommunityActivity.class);
                        startActivity(i);
                        break;
                    case "Employee Care":
                        i = new Intent(SubMyTimeActivity.this, EmployeeCareActivity.class);
                        startActivity(i);
                        break;
                    case "Survey":
                        i = new Intent(SubMyTimeActivity.this, SurveyActivity.class);
                        startActivity(i);
                        break;
                    case "Employee Corner":
                        i = new Intent(SubMyTimeActivity.this, EmployeeCornerActivity.class);
                        startActivity(i);
                        break;
                    case "HR Wiki":
                        i = new Intent(SubMyTimeActivity.this, HRWikiActivity.class);
                        startActivity(i);
                        break;
                    //============================================================================================== TRAVEL 0
                    case "SPPD Online":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;

                    //============================================================================================== INTEGRITY
                    case "LHKPN":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "SPT Online":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "Pakta Integritas":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "Etika Bisnis":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;

                    //============================================================================================== DEVELOPMENT
                    case "My Career":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "My Knowledge":
                        i = new Intent(SubMyTimeActivity.this, MyKnowledgeActivity.class);
                        startActivity(i);
                        break;
                    case "Assistium":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "My Learning":
                        i = new Intent(SubMyTimeActivity.this, MyTrainingActivity.class);
                        startActivity(i);
                        break;
                }
            }
        });
        getRoleLMS();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(code);
    }


    private void getRoleLMS(){
        System.out.println("MASUKROLELMS");
        AndroidNetworking.get(session.getServerURL()+"get_v_lms_userbuscd/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("RESPONROLELMS"+response);
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i=0;i<data.length();i++) {
                                JSONObject object = data.getJSONObject(i);
                                String role_code = object.getString("role_code");
                                session.setRoleLMS(role_code);
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
