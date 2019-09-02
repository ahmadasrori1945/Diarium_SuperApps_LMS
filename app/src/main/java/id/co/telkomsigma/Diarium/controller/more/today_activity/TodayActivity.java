package id.co.telkomsigma.Diarium.controller.more.today_activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.EventAdapter;
import id.co.telkomsigma.Diarium.adapter.ViewPagerAdapter;
import id.co.telkomsigma.Diarium.controller.HomeActivity;
import id.co.telkomsigma.Diarium.model.EventModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;


public class TodayActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Typeface font,fontbold;
    ImageView leftNav, rightNav;
    int counter = 0;
    String monthResult = null, weekResult = null, paramMonth = null;
    Calendar calendar = Calendar.getInstance();
    private List<EventModel> listModel;
    private EventModel model;
    private EventAdapter adapter;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    ImageView ivEmotCheckin, ivEmotCheckout, ivMapsCheckin, ivMapsCheckout, ivProfile;
    TextView tvTimeCheckin, tvTimeCheckout, tvStatCheckin, tvStatCheckout, tvNameNik, tvJob, tvDay, tvDate, tvWeek, tv_checkin, tv_checkout;
    String avatar,pers, nama, status, position;
    LinearLayout lay_checkin, lay_checkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        Intent intent = getIntent();
        pers = intent.getStringExtra("personal_number");
        avatar = intent.getStringExtra("avatar");
        nama = intent.getStringExtra("name");
        status = intent.getStringExtra("status");
        position = intent.getStringExtra("position");

        //========================================================================================== INITIATION
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        lay_checkin = findViewById(R.id.lay_checkin);
        lay_checkout = findViewById(R.id.lay_checkout);
        tv_checkin = findViewById(R.id.tv_checkin);
        tv_checkout = findViewById(R.id.tv_checkout);
        tvTimeCheckin = findViewById(R.id.tvTimeCheckin);
        tvTimeCheckout = findViewById(R.id.tvTimeCheckout);
        tvStatCheckin = findViewById(R.id.tvStatCheckin);
        tvStatCheckout = findViewById(R.id.tvStatCheckout);
        ivProfile = findViewById(R.id.ivProfile);
        tvDay = findViewById(R.id.tvDay);
        tvDate = findViewById(R.id.tvDate);
        tvWeek = findViewById(R.id.tvWeek);
        tvNameNik = findViewById(R.id.tvNameNik);
        tvJob = findViewById(R.id.tvJob);
        leftNav = findViewById(R.id.ivLeft);
        rightNav = findViewById(R.id.ivRight);
        ivEmotCheckin = findViewById(R.id.ivEmotCheckin);
        ivEmotCheckout = findViewById(R.id.ivEmotCheckout);
        ivMapsCheckin = findViewById(R.id.ivMapsCheckin);
        ivMapsCheckout = findViewById(R.id.ivMapsCheckout);
        session = new UserSessionManager(TodayActivity.this);
        progressDialogHelper = new ProgressDialogHelper();

        session.setTempPers(pers);
        session.setTodayActivityPersonalNumber(pers);
        session.setTodayActivityName(nama);
        tv_checkin.setVisibility(View.GONE);
        tv_checkout.setVisibility(View.GONE);
        lay_checkin.setVisibility(View.GONE);
        lay_checkout.setVisibility(View.GONE);
        //========================================================================================== TYPEFACE
        font = Typeface.createFromAsset(TodayActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(TodayActivity.this.getAssets(),"fonts/Nexa Bold.otf");
        tvDay.setTypeface(fontbold);
        tvDate.setTypeface(font);
        tvWeek.setTypeface(fontbold);
        tvNameNik.setTypeface(fontbold);
        tvJob.setTypeface(fontbold);
        tvTimeCheckin.setTypeface(fontbold);
        tvTimeCheckout.setTypeface(fontbold);
        tvStatCheckin.setTypeface(font);
        tvStatCheckout.setTypeface(font);

        Picasso.get().load(avatar).error(R.drawable.profile).into(ivProfile);
        //========================================================================================== DAY NAME AND WEEK NAME
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayResult = null;
        switch (day) {
            case Calendar.SUNDAY:
                dayResult = "Sunday";
                weekResult = "Weekend";
                break;
            case Calendar.MONDAY:
                dayResult = "Monday";
                weekResult = "Weekday";
                break;
            case Calendar.TUESDAY:
                dayResult = "Tuesday";
                weekResult = "Weekday";
                break;
            case Calendar.WEDNESDAY:
                dayResult = "Wednesday";
                weekResult = "Weekday";
                break;
            case Calendar.THURSDAY:
                dayResult = "Thursday";
                weekResult = "Weekday";
                break;
            case Calendar.FRIDAY:
                dayResult = "Friday";
                weekResult = "Weekday";
                break;
            case Calendar.SATURDAY:
                dayResult = "Saturday";
                weekResult = "Weekend";
                break;
        }
        //========================================================================================== END DAY NAME AND WEEK NAME

        //========================================================================================== DAY< MONTH, AND YEAR FORMAT
        int a = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
                monthResult = "JANUARY";
                paramMonth = "01";
                break;
            case Calendar.FEBRUARY:
                monthResult = "FEBRUARY";
                paramMonth = "02";
                break;
            case Calendar.MARCH:
                monthResult = "MARCH";
                paramMonth = "03";
                break;
            case Calendar.APRIL:
                monthResult = "APRIL";
                paramMonth = "04";
                break;
            case Calendar.MAY:
                monthResult = "MAY";
                paramMonth = "05";
                break;
            case Calendar.JUNE:
                monthResult = "JUNE";
                paramMonth = "06";
                break;
            case Calendar.JULY:
                monthResult = "JULY";
                paramMonth = "07";
                break;
            case Calendar.AUGUST:
                monthResult = "AUGUST";
                paramMonth = "08";
                break;
            case Calendar.SEPTEMBER:
                monthResult = "SEPTEMBER";
                paramMonth = "09";
                break;
            case Calendar.OCTOBER:
                monthResult = "OCTOBER";
                paramMonth = "10";
                break;
            case Calendar.NOVEMBER:
                monthResult = "NOVEMBER";
                paramMonth = "11";
                break;
            case Calendar.DECEMBER:
                monthResult = "DECEMBER";
                paramMonth = "12";
                break;
        }

//        Toast.makeText(this, "Param : "+year+"-"+paramMonth+"-"+a, Toast.LENGTH_SHORT).show();
        tvDate.setText(a+" "+monthResult+" "+year);
        tvDay.setText(dayResult);
        tvWeek.setText(weekResult);
        getActivityCheckin(year+"-"+paramMonth+"-"+a);
        getActivityCheckOut(year+"-"+paramMonth+"-"+a);
        session.setTodayActivity(year+"-"+paramMonth+"-"+a);
        //========================================================================================== END DAY< MONTH, AND YEAR FORMAT

        //========================================================================================== PREV
        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                Calendar calendarPrev = Calendar.getInstance();
                calendarPrev.add(Calendar.DAY_OF_MONTH, counter);

                int day = calendarPrev.get(Calendar.DAY_OF_WEEK);
                String dayResult = null;
                switch (day) {
                    case Calendar.SUNDAY:
                        dayResult = "Sunday";
                        weekResult = "Weekend";
                        break;
                    case Calendar.MONDAY:
                        dayResult = "Monday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.TUESDAY:
                        dayResult = "Tuesday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.WEDNESDAY:
                        dayResult = "Wednesday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.THURSDAY:
                        dayResult = "Thursday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.FRIDAY:
                        dayResult = "Friday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.SATURDAY:
                        dayResult = "Saturday";
                        weekResult = "Weekend";
                        break;
                }

                int month = calendarPrev.get(Calendar.MONTH);
                switch (month) {
                    case Calendar.JANUARY:
                        monthResult = "JANUARY";
                        paramMonth = "01";
                        break;
                    case Calendar.FEBRUARY:
                        monthResult = "FEBRUARY";
                        paramMonth = "02";
                        break;
                    case Calendar.MARCH:
                        monthResult = "MARCH";
                        paramMonth = "03";
                        break;
                    case Calendar.APRIL:
                        monthResult = "APRIL";
                        paramMonth = "04";
                        break;
                    case Calendar.MAY:
                        monthResult = "MAY";
                        paramMonth = "05";
                        break;
                    case Calendar.JUNE:
                        monthResult = "JUNE";
                        paramMonth = "06";
                        break;
                    case Calendar.JULY:
                        monthResult = "JULY";
                        paramMonth = "07";
                        break;
                    case Calendar.AUGUST:
                        monthResult = "AUGUST";
                        paramMonth = "08";
                        break;
                    case Calendar.SEPTEMBER:
                        monthResult = "SEPTEMBER";
                        paramMonth = "09";
                        break;
                    case Calendar.OCTOBER:
                        monthResult = "OCTOBER";
                        paramMonth = "10";
                        break;
                    case Calendar.NOVEMBER:
                        monthResult = "NOVEMBER";
                        paramMonth = "11";
                        break;
                    case Calendar.DECEMBER:
                        monthResult = "DECEMBER";
                        paramMonth = "12";
                        break;
                }
                int year = calendarPrev.get(Calendar.YEAR);
                int a = calendarPrev.get(Calendar.DAY_OF_MONTH);
                tvDay.setText(dayResult);
                tvWeek.setText(weekResult);
                tvDate.setText(a+" "+monthResult+" "+year);
//                Toast.makeText(TodayActivity.this, "Param : "+year+"-"+paramMonth+"-"+a, Toast.LENGTH_SHORT).show();
                getActivityCheckin(year+"-"+paramMonth+"-"+a);
                getActivityCheckOut(year+"-"+paramMonth+"-"+a);
                session.setTodayActivity(year+"-"+paramMonth+"-"+a);

                viewPagerAdapter = new ViewPagerAdapter(TodayActivity.this.getSupportFragmentManager());
                viewPagerAdapter.addFragments(new Activity_listFragment(),"Activity List");
//                viewPagerAdapter.addFragments(new Event_ListFragment(),"Event List");
//        viewPagerAdapter.addFragments(new PaidFragment(),"Paid");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        //========================================================================================== END PREV

        //========================================================================================== NEXT
        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                Calendar calendarCurrent = Calendar.getInstance();

                Calendar calendarNext = Calendar.getInstance();
                calendarNext.add(Calendar.DAY_OF_MONTH, counter);

                int nextDay = calendarNext.get(Calendar.DAY_OF_WEEK);
                String dayResult = null;
                switch (nextDay) {
                    case Calendar.SUNDAY:
                        dayResult = "Sunday";
                        weekResult = "Weekend";
                        break;
                    case Calendar.MONDAY:
                        dayResult = "Monday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.TUESDAY:
                        dayResult = "Tuesday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.WEDNESDAY:
                        dayResult = "Wednesday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.THURSDAY:
                        dayResult = "Thursday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.FRIDAY:
                        dayResult = "Friday";
                        weekResult = "Weekday";
                        break;
                    case Calendar.SATURDAY:
                        dayResult = "Saturday";
                        weekResult = "Weekend";
                        break;
                }


                int month = calendarNext.get(Calendar.MONTH);
                switch (month) {
                    case Calendar.JANUARY:
                        monthResult = "JANUARY";
                        paramMonth = "01";
                        break;
                    case Calendar.FEBRUARY:
                        monthResult = "FEBRUARY";
                        paramMonth = "02";
                        break;
                    case Calendar.MARCH:
                        monthResult = "MARCH";
                        paramMonth = "03";
                        break;
                    case Calendar.APRIL:
                        monthResult = "APRIL";
                        paramMonth = "04";
                        break;
                    case Calendar.MAY:
                        monthResult = "MAY";
                        paramMonth = "05";
                        break;
                    case Calendar.JUNE:
                        monthResult = "JUNE";
                        paramMonth = "06";
                        break;
                    case Calendar.JULY:
                        monthResult = "JULY";
                        paramMonth = "07";
                        break;
                    case Calendar.AUGUST:
                        monthResult = "AUGUST";
                        paramMonth = "08";
                        break;
                    case Calendar.SEPTEMBER:
                        monthResult = "SEPTEMBER";
                        paramMonth = "09";
                        break;
                    case Calendar.OCTOBER:
                        monthResult = "OCTOBER";
                        paramMonth = "10";
                        break;
                    case Calendar.NOVEMBER:
                        monthResult = "NOVEMBER";
                        paramMonth = "11";
                        break;
                    case Calendar.DECEMBER:
                        monthResult = "DECEMBER";
                        paramMonth = "12";
                        break;
                }
                int yN = calendarNext.get(Calendar.YEAR);
                int mN = calendarNext.get(Calendar.MONTH);
                int dN = calendarNext.get(Calendar.DAY_OF_MONTH);
                String rN = String.valueOf(yN)+"0"+String.valueOf(mN)+"0"+String.valueOf(dN);
//                System.out.println("next : "+rN);
                int yC = calendarCurrent.get(Calendar.YEAR);
                int mC = calendarCurrent.get(Calendar.MONTH);
                int dC = calendarCurrent.get(Calendar.DAY_OF_MONTH);
                String rC = String.valueOf(yC)+"0"+String.valueOf(mC)+"0"+String.valueOf(dC);
//                System.out.println("curent : "+rC);
                tvDay.setText(dayResult);
                tvWeek.setText(weekResult);
                int year = calendarNext.get(Calendar.YEAR);
                tvDate.setText(dN+" "+monthResult+" "+year);
//                Toast.makeText(TodayActivity.this, "Next : "+rN+" Current : "+rC, Toast.LENGTH_SHORT).show();
                getActivityCheckin(year+"-"+paramMonth+"-"+dN);
                getActivityCheckOut(year+"-"+paramMonth+"-"+dN);
                session.setTodayActivity(year+"-"+paramMonth+"-"+dN);
                viewPagerAdapter = new ViewPagerAdapter(TodayActivity.this.getSupportFragmentManager());
                viewPagerAdapter.addFragments(new Activity_listFragment(),"Activity List");
//                viewPagerAdapter.addFragments(new Event_ListFragment(),"Event List");
//        viewPagerAdapter.addFragments(new PaidFragment(),"Paid");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);

//                if (Integer.parseInt(rN)<=Integer.parseInt(rC)) {
//
//                } else {
//                    counter--;
//                    Toast.makeText(TodayActivity.this, "Activity submission in the future is not allowed!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        //========================================================================================== END NEXT

        viewPagerAdapter = new ViewPagerAdapter(TodayActivity.this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new Activity_listFragment(),"Activity List");
//        viewPagerAdapter.addFragments(new Event_ListFragment(),"Event List");
//        viewPagerAdapter.addFragments(new PaidFragment(),"Paid");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Today Activity");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (session.getUserNIK().equals(session.getTempPers())) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_add_post, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_post:
                Intent intent = new Intent(TodayActivity.this, AddTodayActActivity.class);
//                    intent.putExtra("name",session.getUserFullName());
//                    intent.putExtra("email","diarium@telkom.co.id");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getActivityCheckin(final String date){
        progressDialogHelper.showProgressDialog(TodayActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/todayActivity/"+date+"/nik/"+pers+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"kjwrhbk4jrActivityCheckin");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<EventModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String full_name = object.getString("full_name");
                                        String personal_number = object.getString("personal_number");
                                        String nickname = object.getString("nickname");
                                        tvNameNik.setText(full_name+" | "+personal_number);
                                        tvJob.setText(position);

                                        JSONArray jsonArrayPresensi = object.getJSONArray("presensi");
                                        if (jsonArrayPresensi.length()==0) {
                                            lay_checkin.setVisibility(View.GONE);
                                            tv_checkin.setVisibility(View.VISIBLE);
                                        } else {
                                            lay_checkin.setVisibility(View.VISIBLE);
                                            tv_checkin.setVisibility(View.GONE);
                                            for (int b = 0 ; b<jsonArrayPresensi.length(); b++) {
                                                JSONObject objectPresensi = jsonArrayPresensi.getJSONObject(b);
                                                String presence_type = objectPresensi.getString("presence_type");
                                                if (presence_type.equals("CI")||presence_type.equals("OI")) {
                                                    String date = objectPresensi.getString("date");
                                                    String time = objectPresensi.getString("time");
                                                    String emoticon = objectPresensi.getString("emoticon");
                                                    final String location = objectPresensi.getString("location");
                                                    tvTimeCheckin.setText(time);
                                                    tvStatCheckin.setText("Check In");
                                                    switch (emoticon) {
                                                        case ":|":
                                                            ivEmotCheckin.setImageResource(R.drawable.suffer);
                                                            break;
                                                        case ":)":
                                                            ivEmotCheckin.setImageResource(R.drawable.smile);
                                                            break;
                                                        case ":D":
                                                            ivEmotCheckin.setImageResource(R.drawable.happy);
                                                            break;
                                                    }
                                                    ivMapsCheckin.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent i = new Intent(TodayActivity.this, PresenceLocationActivity.class);
                                                            i.putExtra("location",location);
                                                            i.putExtra("tipe","Check In");
                                                            startActivity(i);
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                }
                                progressDialogHelper.dismissProgressDialog(TodayActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(TodayActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(TodayActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(TodayActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void getActivityCheckOut(final String date){
        progressDialogHelper.showProgressDialog(TodayActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/todayActivity/"+date+"/nik/"+pers+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"kjwrhbk4jrActivityCheckout");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<EventModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String full_name = object.getString("full_name");
                                    String personal_number = object.getString("personal_number");
                                    String nickname = object.getString("nickname");
                                    tvNameNik.setText(full_name+" | "+personal_number);
                                    JSONArray jsonArrayPresensi = object.getJSONArray("presensi");
                                    if (jsonArrayPresensi.length()<=1) {
                                        lay_checkout.setVisibility(View.GONE);
                                        tv_checkout.setVisibility(View.VISIBLE);
                                    } else {
                                        lay_checkout.setVisibility(View.VISIBLE);
                                        tv_checkout.setVisibility(View.GONE);
                                        for (int b = 0 ; b<jsonArrayPresensi.length(); b++) {
                                            JSONObject objectPresensi = jsonArrayPresensi.getJSONObject(b);
                                            String presence_type = objectPresensi.getString("presence_type");
                                            if (presence_type.equals("CO")||presence_type.equals("OO")) {
                                                String date = objectPresensi.getString("date");
                                                String time = objectPresensi.getString("time");
                                                String emoticon = objectPresensi.getString("emoticon");
                                                final String location = objectPresensi.getString("location");
                                                tvTimeCheckout.setText(time);
                                                tvStatCheckout.setText("Check Out");
                                                switch (emoticon) {
                                                    case ":|":
                                                        ivEmotCheckout.setImageResource(R.drawable.suffer);
                                                        break;
                                                    case ":)":
                                                        ivEmotCheckout.setImageResource(R.drawable.smile);
                                                        break;
                                                    case ":D":
                                                        ivEmotCheckout.setImageResource(R.drawable.happy);
                                                        break;
                                                }
                                                ivMapsCheckout.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent i = new Intent(TodayActivity.this, PresenceLocationActivity.class);
                                                        i.putExtra("location",location);
                                                        i.putExtra("tipe","Check Out");
                                                        startActivity(i);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                                progressDialogHelper.dismissProgressDialog(TodayActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(TodayActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(TodayActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(TodayActivity.this);
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
