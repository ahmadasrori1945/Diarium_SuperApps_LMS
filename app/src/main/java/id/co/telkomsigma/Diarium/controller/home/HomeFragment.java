package id.co.telkomsigma.Diarium.controller.home;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.co.telkomsigma.Diarium.adapter.InboxAdapter;
import id.co.telkomsigma.Diarium.adapter.MenuAdapter;
import id.co.telkomsigma.Diarium.adapter.MyPostingAdapter;
import id.co.telkomsigma.Diarium.controller.HomeActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.community.CommunityActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.employee_care.EmployeeCareActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.hr_wiki.HRWikiActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.MyEventActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myteam.MyTeamActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.mytime.checkin.CheckinActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.personal_data.PersonalDataActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.report.ReportActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.search_partner.SearchTempActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.survey.SurveyActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.today_activity.TodayActivity;
import id.co.telkomsigma.Diarium.controller.inbox.DetailInboxActivity;
import id.co.telkomsigma.Diarium.controller.inbox.InboxActivity;
import id.co.telkomsigma.Diarium.controller.profile.DetailpostActivity;
import id.co.telkomsigma.Diarium.controller.profile.ProfileActivity;
import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.model.InboxModel;
import id.co.telkomsigma.Diarium.model.MyPostingModel;
import id.co.telkomsigma.Diarium.util.ExpandableHeightGridView;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.qiscus.ui.activity.QiscusChatActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    TextView tvName, tvNullInbox, tvNullPost;
    UserSessionManager session;
    private String namaFile;
    ListView lvInbox, lvPost;
    private List<InboxModel> listModel;
    private InboxModel model;
    private InboxAdapter adapter;
    private List<MyPostingModel> listModelPost;
    private MyPostingModel modelPost;
    private MyPostingAdapter adapterPost;
    EditText etSearch;
    CircularProgressBar circularProgressBar;
    TextView menu_checkin;
    Typeface font,fontbold;
    ImageView ivProfile;
    private ProgressDialog progressDialog;
    ExpandableHeightGridView gridView;
    String[] menu ={
            "My Time",
            "My Performance",
            "My Task",
            "My Profile",
            "My Travel",
            "My Integrity",
            "My Development",
            "FAQ (Chatbot)"
    };


    int[] image={
            R.drawable.ic_mytime,
            R.drawable.ic_performance,
            R.drawable.ic_mytask,
            R.drawable.ic_profile,
            R.drawable.ic_mytravel,
            R.drawable.ic_integrity,
            R.drawable.ic_myprofile,
            R.drawable.ic_faqmenu
            //  R.drawable.today_activity
    };


    public HomeFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        session = new UserSessionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");


        //========================================================================================== ONSALE
//
//        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
//        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
//        lvInbox = view.findViewById(R.id.lvInbox);
//        lvPost = view.findViewById(R.id.lvPost);
//        ivProfile= view.findViewById(R.id.ivProfile);
//        TextView a = view.findViewById(R.id.tvGreeting);
//        tvName = view.findViewById(R.id.tvTitle);
//        tvNullInbox = view.findViewById(R.id.tvNullInbox);
//        tvNullPost = view.findViewById(R.id.tvNullPost);
//        etSearch = view.findViewById(R.id.inputSearch);
//        TextView a1 = view.findViewById(R.id.judulsub);
//        TextView b1 = view.findViewById(R.id.judulsub2);
//        menu_checkin = view.findViewById(R.id.menu1);
//        TextView menu_b = view.findViewById(R.id.menu2);
//        TextView menu_c = view.findViewById(R.id.menu3);
//        TextView menu_d = view.findViewById(R.id.menu4);
//        TextView menu_e = view.findViewById(R.id.menu5);
//        TextView more = view.findViewById(R.id.moreinbox);
//        Picasso.get().load(session.getAvatar()).error(R.drawable.profile).into(ivProfile);
//        a.setTypeface(fontbold);
//        Calendar c = Calendar.getInstance();
//        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
//        if(timeOfDay >= 0 && timeOfDay < 12){
//            a.setText("Good Morning");
//        }else if(timeOfDay >= 12 && timeOfDay < 16){
//            a.setText("Good Afternoon");
//        }else if(timeOfDay >= 16 && timeOfDay < 21){
//            a.setText("Good Evening");
//        }else if(timeOfDay >= 21 && timeOfDay < 24){
//            a.setText("Good Night");
//        }
//        tvName.setTypeface(fontbold);
//        tvName.setText(session.getUserFullName());
//        a1.setTypeface(fontbold);
//        b1.setTypeface(fontbold);
//        menu_checkin.setTypeface(fontbold);
//        getStatCheckin();
//        System.out.println(session.getStat()+"Status ABSENYA");
//        if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
//            menu_checkin.setText("Check In");
//        } else {
//            menu_checkin.setText("Check Out");
//        }
//        SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM-dd");
//        Date myDate = new Date();
//        System.out.println(session.getBornDate()+"BORNDATENYA");
//        String currentDate = timeStampFormat.format(myDate);
//        System.out.println(currentDate+"CURRENTDATENYA");
//        if (currentDate.equals(session.getBornDate()) && session.getStatusClickBornDate().equals("0")) {
//            popupBirthday();
//        }
//        menu_b.setTypeface(fontbold);
//        menu_c.setTypeface(fontbold);
//        menu_d.setTypeface(fontbold);
//        menu_e.setTypeface(fontbold);
//        more.setTypeface(fontbold);
//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), InboxActivity.class);
//                startActivity(i);
//            }
//        });
//        TextView more1= view.findViewById(R.id.morepost);
//        more1.setTypeface(fontbold);
//        more1.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(getActivity(), EmployeePostingActivity.class);
//                startActivity(hariian);
//            }
//        });
//
//        LinearLayout set = view.findViewById(R.id.profile);
//        set.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(getActivity(), ProfileActivity.class);
//                hariian.putExtra("personal_number", session.getUserNIK());
//                hariian.putExtra("avatar", session.getAvatar());
//                startActivity(hariian);
//            }
//        });
//
//        LinearLayout menu1 = view.findViewById(R.id.menu_1);
//        menu1.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(getActivity(), CheckinActivity.class);
//                startActivity(hariian);
//            }
//        });
//
//        LinearLayout menu2 = view.findViewById(R.id.menu_2);
//        menu2.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(getActivity(), TodayActivity.class);
//                hariian.putExtra("personal_number",session.getUserNIK());
//                hariian.putExtra("name",session.getUserFullName());
//                hariian.putExtra("status","none");
//                hariian.putExtra("position",session.getJob());
//                hariian.putExtra("avatar",session.getAvatar());
//                startActivity(hariian);
//            }
//        });
//        LinearLayout menu3 = view.findViewById(R.id.menu_3);
//        menu3.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(getActivity(), ReportActivity.class);
//                startActivity(hariian);
//            }
//        });
//        LinearLayout menu4 = view.findViewById(R.id.menu_4);
//        menu4.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(getActivity(), PersonalDataActivity.class);
//                startActivity(hariian);
//            }
//        });
//        LinearLayout menu5 = view.findViewById(R.id.menu_5);
//        menu5.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(getActivity(), MyTeamActivity.class);
//                startActivity(hariian);
//            }
//        });
//        LinearLayout menu6 = view.findViewById(R.id.menu_6);
//        menu6.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(getActivity(), MyEventActivity.class);
//                startActivity(hariian);
//            }
//        });
//        etSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), SearchTempActivity.class);
//                startActivity(i);
//            }
//        });
//
//        getInbox();
//        getMyPost();
        //========================================================================================== DEVTELKOM

        lvInbox = view.findViewById(R.id.lvInbox);
        lvPost = view.findViewById(R.id.lvPost);
        TextView more = view.findViewById(R.id.moreinbox);
        gridView = view.findViewById(R.id.gridview);
        ivProfile= view.findViewById(R.id.ivProfile);
        TextView a = view.findViewById(R.id.tvGreeting);
        tvName = view.findViewById(R.id.tvTitle);
        tvNullInbox = view.findViewById(R.id.tvNullInbox);
        tvNullPost = view.findViewById(R.id.tvNullPost);
        etSearch = view.findViewById(R.id.inputSearch);
        TextView a1 = view.findViewById(R.id.judulsub);
        TextView b1 = view.findViewById(R.id.judulsub2);
        TextView more1= view.findViewById(R.id.morepost);
        LinearLayout set = view.findViewById(R.id.profile);

        MenuAdapter gridAdapter = new MenuAdapter(getActivity(),image,menu);
        gridView.setAdapter(gridAdapter);
        gridView.setExpanded(true);
        Picasso.get().load(session.getAvatar()).error(R.drawable.profile).into(ivProfile);
        a.setTypeface(fontbold);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            a.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            a.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            a.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            a.setText("Good Night");
        }
        tvName.setTypeface(fontbold);
        tvName.setText(session.getUserFullName());
        a1.setTypeface(fontbold);
        b1.setTypeface(fontbold);
        more1.setTypeface(fontbold);
        more.setTypeface(fontbold);

        getStatCheckin();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM-dd");
        Date myDate = new Date();
        String currentDate = timeStampFormat.format(myDate);
        if (currentDate.equals(session.getBornDate()) && session.getStatusClickBornDate().equals("0")) {
            popupBirthday();
        }

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HomeActivity.class);
                i.putExtra("key", "moreinbox");
                startActivity(i);
            }
        });

        more1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent hariian = new Intent(getActivity(), EmployeePostingActivity.class);
                startActivity(hariian);
            }
        });

        set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent hariian = new Intent(getActivity(), ProfileActivity.class);
                hariian.putExtra("personal_number", session.getUserNIK());
                hariian.putExtra("avatar", session.getAvatar());
                startActivity(hariian);
            }
        });

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SearchTempActivity.class);
                startActivity(i);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                String clickedText = menu[position];
//                Toast.makeText(getActivity(), "posisi"+clickedText, Toast.LENGTH_SHORT).show();
                switch (clickedText) {
                    case "My Time":
                        Intent a = new Intent(getActivity(), SubMyTimeActivity.class);
                        a.putExtra("code", "MyTime");
                        startActivity(a);
                        break;
                    case "My Performance":
                        Intent b = new Intent(getActivity(), SubMyTimeActivity.class);
                        b.putExtra("code", "MyPerformance");
                        startActivity(b);
                        break;
                    case "My Task":
                        Intent c = new Intent(getActivity(), SubMyTimeActivity.class);
                        c.putExtra("code", "MyTask");
                        startActivity(c);
                        break;
                    case "My Profile":
                        Intent intent = new Intent(getActivity(), SubMyTimeActivity.class);
                        intent.putExtra("code", "MyProfile");
                        startActivity(intent);
                        break;
                    case "My Travel":
                        Intent d = new Intent(getActivity(), SubMyTimeActivity.class);
                        d.putExtra("code", "MyTravel");
                        startActivity(d);
                        break;
                    case "My Integrity":
                        Intent e = new Intent(getActivity(), SubMyTimeActivity.class);
                        e.putExtra("code", "MyIntegrity");
                        startActivity(e);
                        break;
                    case "My Development":
                        Intent f = new Intent(getActivity(), SubMyTimeActivity.class);
                        f.putExtra("code", "MyDevelopment");
                        startActivity(f);
                        break;
                    case "FAQ (Chatbot)":
//                        Toast.makeText(getActivity(), "not available", Toast.LENGTH_SHORT).show();
                        Intent h = new Intent(getActivity(), QiscusChatActivity.class);
                        System.out.println(session.getUserNIK()+"@diarium.co.id"+ "NIKNYA");
                        h.putExtra("name","Dexy-Kiwari");
                        h.putExtra("email",session.getUserNIK()+"@diarium.co.id");
                        startActivity(h);
                        break;
                    case "Community":
//                        Toast.makeText(getActivity(), "This menu not available in trial mode", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), CommunityActivity.class);
                        startActivity(i);
                        break;
                    case "Employee Survey":
//                        Toast.makeText(getActivity(), "This menu not available in trial mode", Toast.LENGTH_SHORT).show();
                        Intent j = new Intent(getActivity(), SurveyActivity.class);
                        startActivity(j);
                        break;
                    case "Employee Care":
//                        Toast.makeText(getActivity(), "This menu not available in trial mode", Toast.LENGTH_SHORT).show();
                        Intent k = new Intent(getActivity(), EmployeeCareActivity.class);
                        startActivity(k);
                        break;
                    case "HR Wiki":
//                        Toast.makeText(getActivity(), "This menu not available in trial mode", Toast.LENGTH_SHORT).show();
                        Intent l = new Intent(getActivity(), HRWikiActivity.class);
                        startActivity(l);
                        break;
                }
            }
        });

        getInbox();
        getMyPost();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getStatCheckin();
        System.out.println(session.getStat()+"Status ABSENYA");
        getInbox();
        getMyPost();
    }

    private void getStatCheckin(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/"+session.getUserNIK()+"/statuspresensi/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode()+"/date/"+tRes)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("ebk2j3nj32ePresensi"+response);
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    session.setStat("CO");
                                }
                                for (int i=0; i<jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String stat = obj.getString("presence_type");
                                    session.setStat(stat);
                                }
//                                if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
////            Toast.makeText(getActivity(), "Saatnya Checkin", Toast.LENGTH_SHORT).show();
//                                    menu_checkin.setText("Check In");
//                                } else {
////            Toast.makeText(getActivity(), "Saatnya Checkout", Toast.LENGTH_SHORT).show();
//                                    menu_checkin.setText("Check Out");
//                                }
                                System.out.println(jsonArray.length()+" ql3jen2kelr");
                            }else{
                            }
                            System.out.println("status ya : "+session.getStat());
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        }catch (Exception e){
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void popupBirthday() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_happy_birthday);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        TextView tvGreeting =(TextView) dialog.findViewById(R.id.tvGreeting);
        dialog.show();
        session.setStatusClickBornDate("1");
        dialog.setCancelable(true);
        tvGreeting.setText("Happy Birthday "+session.getUserFullName());
    }

    private void getMyPost(){
        AndroidNetworking.get(session.getServerURL()+"users/timelineDetailPosting/limit/1000/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"ktjbkgrjbhymyposting");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModelPost = new ArrayList<MyPostingModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    lvPost.setVisibility(View.GONE);
                                    tvNullPost.setVisibility(View.VISIBLE);
                                } else {
                                    lvPost.setVisibility(View.VISIBLE);
                                    tvNullPost.setVisibility(View.GONE);
                                    for (int a = 0; a < 1; a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String posting_id = object.getString("posting_id");
                                        String title = object.getString("title");
                                        String description = object.getString("description");
                                        String image = object.getString("image");
                                        String date = object.getString("date");
                                        String time = object.getString("time");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String avatar = object.getString("profile");
//                                        String lovelikes = object.getString("lovelikes");
//                                        JSONArray jsonArrayLike = object.getJSONArray("lovelikes");
                                        String full_name = null;
                                        JSONArray jsonArrayName = object.getJSONArray("name");
                                        for (int d=0; d<jsonArrayName.length(); d++) {
                                            JSONObject objectName = jsonArrayName.getJSONObject(d);
                                            full_name = objectName.getString("full_name");
                                        }
                                        JSONArray jsonArrayComment = object.getJSONArray("comments");
                                        JSONArray jsonArrayLike = object.getJSONArray("people_likes");
                                        boolean status_like = false;
                                        for (int c=0; c<jsonArrayLike.length(); c++) {
                                            JSONObject objectLike = jsonArrayLike.getJSONObject(c);
                                            String personal_number_like = objectLike.getString("personal_number");
                                            if (personal_number_like.equals(session.getUserNIK())) {
                                                status_like = true;
                                            } else {
                                                status_like = false;
                                            }
                                        }
                                        modelPost = new MyPostingModel(begin_date, end_date, business_code, personal_number, posting_id, title, description, image, date, time, change_date, change_user, String.valueOf(jsonArrayComment.length()), String.valueOf(jsonArrayLike.length()), full_name, status_like, avatar);
                                        listModelPost.add(modelPost);
                                    }
                                    adapterPost = new MyPostingAdapter(getActivity(), listModelPost);
                                    lvPost.setAdapter(adapterPost);
                                    lvPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        generateActivityId();
                                            Intent i = new Intent(getActivity(), DetailpostActivity.class);
                                            i.putExtra("posting_id",listModelPost.get(position).getPosting_id());
                                            i.putExtra("title",listModelPost.get(position).getTitle());
                                            i.putExtra("description",listModelPost.get(position).getDescription());
                                            i.putExtra("date",listModelPost.get(position).getChange_date());
                                            i.putExtra("image",listModelPost.get(position).getImage());
                                            i.putExtra("name",listModelPost.get(position).getName());
                                            i.putExtra("status_like",listModelPost.get(position).isStatus_like());
                                            i.putExtra("avatar",listModelPost.get(position).getAvatar());
                                            startActivity(i);
                                        }
                                    });
                                }
                            }else{

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




    private void getInbox(){
        AndroidNetworking.get(session.getServerURL()+"users/inbox/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"ktjbkgrjbhy");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<InboxModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    lvInbox.setVisibility(View.GONE);
                                    tvNullInbox.setVisibility(View.VISIBLE);
                                } else {
                                    lvInbox.setVisibility(View.VISIBLE);
                                    tvNullInbox.setVisibility(View.GONE);
                                    for (int a = 0; a < 1; a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String inbox_id = object.getString("inbox_id");
                                        String title = object.getString("title");
                                        String description = object.getString("description");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        model = new InboxModel(begin_date, end_date, business_code, personal_number, inbox_id, title, description, change_date, change_user);
                                        listModel.add(model);
                                    }
                                    adapter = new InboxAdapter(getActivity(), listModel);
                                    lvInbox.setAdapter(adapter);
                                    lvInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getActivity(), DetailInboxActivity.class);
                                            i.putExtra("title",listModel.get(position).getTitle());
                                            i.putExtra("date",listModel.get(position).getChange_date());
                                            i.putExtra("desc",listModel.get(position).getDescription());
                                            startActivity(i);
                                        }
                                    });
                                }
                            }else{
//                                popUpLogin();
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

    private void getVersion(){
        AndroidNetworking.get(session.getServerURL()+"appversion/ANDROID/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"ktjbkgrjbhy");
                        try {
                            if(response.getInt("status")==200){
                                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                                int version_aplikasi = pInfo.versionCode;

                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String version_db = object.getString("version_code");
//                                    String type = object.getString("type");
                                    String url = object.getString("version_link");
                                    System.out.println(version_aplikasi+"VERSIAPLIKASI");
                                    System.out.println(version_db+"VERSIDB");
                                    if (Integer.parseInt(version_db)>version_aplikasi) {
                                        popupUpdate("file", url);
                                    }
                                }
                            }else{
                                Toast.makeText(getActivity(), "error to get update", Toast.LENGTH_SHORT).show();
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

    private void popupUpdate(String type, String url) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_popup_update);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("playstore")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    new DownloadFile().execute("http://bumncc.digitalevent.id/download/bumncc.apk", "diarium.apk");
                }
            }
        });
    }

    class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            progressDialog = new ProgressDialog(getActivity());
            // Set your progress dialog Title
            progressDialog.setTitle("Updating Application");
            // Set your progress dialog Message
            progressDialog.setMessage("Please Wait!");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Show progress dialog
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                namaFile = Url[1];
                URLConnection connection = url.openConnection();
                connection.connect();

                // Detect the file lenghth
                int fileLength = connection.getContentLength();

                // Locate storage location
                String filepath = Environment.getExternalStorageDirectory().toString();
                Log.d("isi file path nya", filepath);

                // Download the file
                InputStream input = new BufferedInputStream(url.openStream());

                // Save the downloaded file
                OutputStream output = new FileOutputStream(filepath + "/Download/" + namaFile);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
                // Close connection
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                // Error Log
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Update the progress dialog
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            if (result != null) {
                Toast.makeText(getActivity(), "Download error: " + result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "File downloaded", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + "sigmabukber.apk")),
                        "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }


}
