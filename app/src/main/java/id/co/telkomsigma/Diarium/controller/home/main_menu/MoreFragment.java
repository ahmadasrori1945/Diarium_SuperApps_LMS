package id.co.telkomsigma.Diarium.controller.home.main_menu;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import id.co.telkomsigma.Diarium.adapter.MenuAdapter;
import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myteam.MyTeamActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.mytime.checkin.CheckinActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.report.ReportActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.survey.SurveyActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.community.CommunityActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.employee_care.EmployeeCareActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.personal_data.PersonalDataActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.employee_corner.EmployeeCornerActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.MyEventActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.today_activity.TodayActivity;
import id.co.telkomsigma.Diarium.util.UserSessionManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    GridView gridview;
    String[] menu ={
            "Check In",
            "Today Activity",
            "Report Activity",
            "Personal Data",
            "My Team",
            "My Event",
//            "One Sheet",
            "Employee Corner",
//            "FAQ",
            "Community",
            "Employee Survey",
            "Employee Care",
//            "HR Wiki"
            // "Add Today Activity"
    };


    int[] image={
            R.drawable.checkin,
            R.drawable.today_activity,
            R.drawable.report,
            R.drawable.user,
            R.drawable.team,
            R.drawable.myevent,
//            R.drawable.cv,
            R.drawable.employee_corner,
//            R.drawable.ic_faq,
            R.drawable.community,
            R.drawable.employeesurvey,
            R.drawable.horn,
//            R.drawable.horn
            //  R.drawable.today_activity
    };
    UserSessionManager session;

    public MoreFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        gridview = (GridView) view.findViewById(R.id.gridview);
        session = new UserSessionManager(getActivity());

        MenuAdapter gridAdapter = new MenuAdapter(getActivity(),image,menu);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                String clickedText = menu[position];
//                Toast.makeText(getActivity(), "posisi"+clickedText, Toast.LENGTH_SHORT).show();
                switch (clickedText) {
                    case "Check In":
                        Intent a = new Intent(getActivity(), CheckinActivity.class);
                        startActivity(a);
                        break;
                    case "Today Activity":
                        Intent b = new Intent(getActivity(), TodayActivity.class);
                        b.putExtra("personal_number", session.getUserNIK());
                        b.putExtra("name", session.getUserFullName());
                        b.putExtra("status", "none");
                        b.putExtra("position", session.getJob());
                        b.putExtra("avatar", session.getAvatar());
                        startActivity(b);
                        break;
                    case "Report Activity":
                        Intent c = new Intent(getActivity(), ReportActivity.class);
                        startActivity(c);
                        break;
                    case "Personal Data":
                        Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                        startActivity(intent);
                        break;
                    case "My Team":
                        Intent d = new Intent(getActivity(), MyTeamActivity.class);
                        startActivity(d);
                        break;
                    case "My Event":
//                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
                        Intent e = new Intent(getActivity(), MyEventActivity.class);
                        startActivity(e);
                        break;
                    case "One Sheet":
                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
//
//                        Intent f = new Intent(getActivity(), OneSheetActivity.class);
//                        startActivity(f);
                        break;
                    case "Employee Corner":
//                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
                        Intent g = new Intent(getActivity(), EmployeeCornerActivity.class);
                        startActivity(g);
                        break;
                    case "FAQ":
                        Toast.makeText(getActivity(), "not available", Toast.LENGTH_SHORT).show();
//                        Intent h = new Intent(getActivity(), QiscusChatActivity.class);
//                        System.out.println(session.getUserNIK()+"@diarium.co.id"+ "NIKNYA");
//                        h.putExtra("name","Dexy-Kiwari");
//                        h.putExtra("email",session.getUserNIK()+"@diarium.co.id");
//                        startActivity(h);
                        break;
                    case "Community":
//                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), CommunityActivity.class);
                        startActivity(i);
                        break;
                    case "Employee Survey":
//                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
                        Intent j = new Intent(getActivity(), SurveyActivity.class);
                        startActivity(j);
                        break;
                    case "Employee Care":
//                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
                        Intent k = new Intent(getActivity(), EmployeeCareActivity.class);
                        startActivity(k);
                        break;
                    case "HR Wiki":
                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
//                        Intent l = new Intent(getActivity(), HRWikiActivity.class);
//                        startActivity(l);
                        break;
                }


//                if(position==0) {
//                    Intent intent = new Intent(getActivity(), CheckinActivity.class);
//                    startActivity(intent);
//                }else if(position==1)
//                {
//                    Intent intent = new Intent(getActivity(), TodayActivity.class);
//                    intent.putExtra("personal_number", session.getUserNIK());
//                    intent.putExtra("name", session.getUserFullName());
//                    startActivity(intent);
//
//                }
//                else if(position==2)
//                {
//                    Intent intent = new Intent(getActivity(), ReportActivity.class);
//                    startActivity(intent);
//
//                }
//                else if(position==3)
//                {
//                    Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
//                    startActivity(intent);
//
//                }
//                else if(position==4)
//                {
//                    Intent intent = new Intent(getActivity(), MyTeamActivity.class);
//                    startActivity(intent);
//
//                }
//                else if(position==5)
//                {
//                    Intent intent = new Intent(getActivity(), MyEventActivity.class);
//                    startActivity(intent);
//
//                }
//                else if(position==6)
//                {
//                    Intent intent = new Intent(getActivity(), OneSheetActivity.class);
//                    startActivity(intent);
//
//                }
//                else if(position==7)
//                {
//                    Intent intent = new Intent(getActivity(), EmployeeCornerActivity.class);
//                    startActivity(intent);
//                    //Toast.makeText(getActivity(),"Comming soon",Toast.LENGTH_SHORT).show();
//                }
//                else if(position==8)
//                {
//                    Intent intent = new Intent(getActivity(), QiscusChatActivity.class);
//                    intent.putExtra("name","Diva");
//                    intent.putExtra("email",session.getUserNIK()+"@diarium.co.id");
//                    startActivity(intent);
//                    //Toast.makeText(getActivity(),"Comming soon",Toast.LENGTH_SHORT).show();
//                }
//                else if(position==9)
//                {
//                    Intent intent = new Intent(getActivity(), CommunityActivity.class);
//                    startActivity(intent);
//                    //Toast.makeText(getActivity(),"Comming soon",Toast.LENGTH_SHORT).show();
//                }
//                else if(position==10)
//                {
//                    Intent intent = new Intent(getActivity(), SurveyActivity.class);
//                    startActivity(intent);
//                    //Toast.makeText(getActivity(),"Comming soon",Toast.LENGTH_SHORT).show();
//                }
//                else if(position==9)
//                {
//                    Intent intent = new Intent(getActivity(), AddTodayActActivity.class);
////                    intent.putExtra("name",session.getUserFullName());
////                    intent.putExtra("email","diarium@telkom.co.id");
//                    startActivity(intent);
//                    //Toast.makeText(getActivity(),"Comming soon",Toast.LENGTH_SHORT).show();
//                }
                //Toast.makeText(getActivity(), mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }



}
