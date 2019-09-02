package id.co.telkomsigma.Diarium.controller.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.MenuAdapter;
import id.co.telkomsigma.Diarium.controller.more.checkin.CheckinActivity;
import id.co.telkomsigma.Diarium.controller.more.community.CommunityActivity;
import id.co.telkomsigma.Diarium.controller.more.employee_care.EmployeeCareActivity;
import id.co.telkomsigma.Diarium.controller.more.employee_corner.EmployeeCornerActivity;
import id.co.telkomsigma.Diarium.controller.more.hr_wiki.HRWikiActivity;
import id.co.telkomsigma.Diarium.controller.more.myevent.MyEventActivity;
import id.co.telkomsigma.Diarium.controller.more.myteam.MyTeamActivity;
import id.co.telkomsigma.Diarium.controller.more.onehseet.OneSheetActivity;
import id.co.telkomsigma.Diarium.controller.more.personal_data.PersonalDataActivity;
import id.co.telkomsigma.Diarium.controller.more.report.ReportActivity;
import id.co.telkomsigma.Diarium.controller.more.survey.SurveyActivity;
import id.co.telkomsigma.Diarium.controller.more.today_activity.TodayActivity;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.qiscus.ui.activity.QiscusChatActivity;

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
            "Ingenium",
            "Cognitium",
            "Assistium"
    };


    int[] imageMyDevelopment={
            R.drawable.myevent,
            R.drawable.ic_congnitium,
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
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
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
                    case "Ingenium":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "Cognitium":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                    case "Assistium":
                        Toast.makeText(SubMyTimeActivity.this, "This menu not available", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(code);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
