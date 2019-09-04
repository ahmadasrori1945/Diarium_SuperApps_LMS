package id.co.telkomsigma.Diarium.controller.more.checkin;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.controller.HomeActivity;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;

import id.co.telkomsigma.Diarium.util.element.FMActivity;

public class CheckinActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener,OnMapReadyCallback {

    Typeface font,fontbold;
    Button showMenu;
    private GoogleMap mMap;
    TextView tvTime, tvDay, tvDate, tvFeel;
    UserSessionManager session;
    LinearLayout rootView;
    public static final int ENROLL_REQ_CODE = 12800;
    public static final int AUTH_REQ_CODE = 12801;
    String emot = null;
    Location location;
    String checkinType;
    Spinner spinner;
    LinearLayout underPressure, comfort, happy;
    String monthResult = null;
    String getTime;
    private ProgressDialogHelper progressDialogHelper;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        session = new UserSessionManager(CheckinActivity.this);
        getListOfFakeLocationApps(this);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        progressDialogHelper = new ProgressDialogHelper();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDay = (TextView) findViewById(R.id.tvDay);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvFeel = (TextView) findViewById(R.id.feel);
        underPressure = findViewById(R.id.underPressure);
        comfort = findViewById(R.id.comfort);
        happy = findViewById(R.id.happy);

        tvTime.setTypeface(font);
        tvDay.setTypeface(font);
        tvDate.setTypeface(font);
        tvFeel.setTypeface(font);


//        Toast.makeText(this, "Status :"+session.getStat(), Toast.LENGTH_SHORT).show();

        //========================================================================================== TIME
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        getTime = time.format(new Date());
        tvTime.setText(getTime);
        //========================================================================================== END TIME

        //========================================================================================== DAY
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayResult = null;
        switch (day) {
            case Calendar.SUNDAY:
                dayResult = "Sunday";
                break;
            case Calendar.MONDAY:
                dayResult = "Monday";
                break;
            case Calendar.TUESDAY:
                dayResult = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayResult = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayResult = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayResult = "Friday";
                break;
            case Calendar.SATURDAY:
                dayResult = "Saturday";
                break;
        }
        tvDay.setText(dayResult);
        //========================================================================================== END DAY

        //========================================================================================== DATE
        SimpleDateFormat tgl = new SimpleDateFormat("dd");
        final String tglResult = tgl.format(new Date());

        SimpleDateFormat tahun = new SimpleDateFormat("yyyy");
        final String tahunResult = tahun.format(new Date());

        int month = calendar.get(Calendar.MONTH);
        String bulan = null;
        switch (month) {
            case Calendar.JANUARY:
                monthResult = "JANUARY";
                bulan = "01";
                break;
            case Calendar.FEBRUARY:
                monthResult = "FEBRUARY";
                bulan = "02";
                break;
            case Calendar.MARCH:
                monthResult = "MARCH";
                bulan = "03";

                break;
            case Calendar.APRIL:
                monthResult = "APRIL";
                bulan = "04";

                break;
            case Calendar.MAY:
                monthResult = "MAY";
                bulan = "05";

                break;
            case Calendar.JUNE:
                monthResult = "JUNE";
                bulan = "06";

                break;
            case Calendar.JULY:
                monthResult = "JULY";
                bulan = "07";

                break;
            case Calendar.AUGUST:
                monthResult = "AUGUST";
                bulan = "08";

                break;
            case Calendar.SEPTEMBER:
                monthResult = "SEPTEMBER";
                bulan = "09";

                break;
            case Calendar.OCTOBER:
                monthResult = "OCTOBER";
                bulan = "10";

                break;
            case Calendar.NOVEMBER:
                monthResult = "NOVEMBER";
                bulan = "11";

                break;
            case Calendar.DECEMBER:
                monthResult = "DECEMBER";
                bulan = "12";

                break;
        }
        tvDate.setText(tglResult+" "+bulan+" "+tahunResult);

        //========================================================================================== END DATE
        // Get reference of widgets from XML layout
        spinner = (Spinner) findViewById(R.id.spinner);

        // Initializing a String Array
        String[] plants = new String[]{
                "REGULER",
                "LEMBUR",
        };

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item,plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.getSolidColor();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                checkinType = parent.getItemAtPosition(pos).toString();
//                Toast.makeText(CheckinActivity.this, "TIPE : "+checkinType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
            spinner.setEnabled(true);
        } else if (session.getStat().equals("CI")){
            spinner.setSelection(0);
            spinner.setEnabled(false);
        } else if (session.getStat().equals("OI")) {
            spinner.setSelection(1);
            spinner.setEnabled(false);
        }


        underPressure.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

//                SimpleDateFormat tglPlus = new SimpleDateFormat("yyyy-MM-dd");
//                String tResplus = tglPlus.format(new Date());
//                Calendar c = Calendar.getInstance();
//                try {
//                    c.setTime(tglPlus.parse(tResplus));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                c.add(Calendar.DATE, -1);
//                SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
//                String output = sdf1.format(c.getTime());
//
//                SimpleDateFormat jam = new SimpleDateFormat("HHmmss");
//                String jamRes = jam.format(new Date());
//                System.out.println(jamRes+"HASILJAM"+output);

                new AlertDialog.Builder(CheckinActivity.this)
                        .setMessage("Type : "+checkinType+" \nDate : "+tglResult+" "+monthResult+" "+tahunResult+"\nTime : "+getTime+"\nExpression : Under Pressure")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.setEmot(":|");
                                if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                                    checkIn();
                                } else {
                                    checkOut();
                                }

//                                Intent i = new Intent(CheckinActivity.this, FMActivity.class);
//                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                                startActivityForResult(i,AUTH_REQ_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                //Snackbar.make(rootView,"Can't get your personal data",Snackbar.LENGTH_LONG).show();
            }
        });

        comfort.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                new AlertDialog.Builder(CheckinActivity.this)
                        .setMessage("Type : "+checkinType+" \nDate : "+tglResult+" "+monthResult+" "+tahunResult+"\nTime : "+getTime+"\nExpression : Comfort")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.setEmot(":)");
                                if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                                    checkIn();
                                } else {
                                    checkOut();
                                }
////
////
//                                Intent i = new Intent(CheckinActivity.this, FMActivity.class);
//                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                                startActivityForResult(i,AUTH_REQ_CODE);
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                //Snackbar.make(rootView,"Can't get your personal data",Snackbar.LENGTH_LONG).show();
            }
        });

        happy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                session.setEmot(":D");
                new AlertDialog.Builder(CheckinActivity.this)
                        .setMessage("Type : "+checkinType+" \nDate : "+tglResult+" "+monthResult+" "+tahunResult+"\nTime : "+getTime+"\nExpression : Happy")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.setEmot(":D");
                                if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                                    checkIn();
                                } else {
                                    checkOut();
                                }
//
//                                Intent i = new Intent(CheckinActivity.this, FMActivity.class);
//                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                                startActivityForResult(i,AUTH_REQ_CODE);
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                //Snackbar.make(rootView,"Can't get your personal data",Snackbar.LENGTH_LONG).show();
            }
        });


        //========================================================================================== CHECK GPS IS ENABLE
        if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
            tvFeel.setText("How do you feel to start the day ?");
            getSupportActionBar().setTitle("Check In");
        } else {
            tvFeel.setText("How do you feel to end the day ?");
            getSupportActionBar().setTitle("Check Out");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            popupGPS();
        }
    }

    private void popupGPS() {
        final Dialog dialog = new Dialog(CheckinActivity.this);
        dialog.setContentView(R.layout.layout_enable_gps);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
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
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AUTH_REQ_CODE) {
            if (session.getFMResult() == Activity.RESULT_OK) {
                //Snackbar.make(rootView,"Your face has successfuly registered",Snackbar.LENGTH_LONG).show();
                // User enrolled successfully
                try {
                    System.out.println("d2j4r4jlrn"+session.getFMResponse());
                    System.out.println("4jnrk34jn34r"+session.getFMMessage());
                    JSONObject dat = new JSONObject(session.getFMMessage());
                    System.out.println(dat+"dljwnk3j4nr");
                    if(dat.getString("result").equals("VERIFIED")){
                        if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                            checkIn();
                        } else {
                            checkOut();
                        }
                    }else{
                        new AlertDialog.Builder(CheckinActivity.this)
                                .setMessage("Your face is not valid")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session.setFMResponse("-");
                                        session.setFMMessage("-");
                                        session.setFMResult(Activity.RESULT_CANCELED);
                                    }
                                })
                                .show();
                    }
                }catch (Exception e){
                    System.out.println(e);
                    Toast.makeText(CheckinActivity.this,"Your face is not valid",Toast.LENGTH_SHORT).show();
                }


            } else if(resultCode == Activity.RESULT_CANCELED){
                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
//                Toast.makeText(CheckinActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                // Enrollment cancelled
            }else{
//                Toast.makeText(CheckinActivity.this,"Else",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static List<String> getListOfFakeLocationApps(Context context) {
        List<String> runningApps = getRunningApps(context, false);
        for (int i = runningApps.size() - 1; i >= 0; i--) {
            String app = runningApps.get(i);
            if(!hasAppPermission(context, app, "android.permission.ACCESS_MOCK_LOCATION")){
                runningApps.remove(i);
            }
        }
        return runningApps;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static List<String> getRunningApps(Context context, boolean includeSystem) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<String> runningApps = new ArrayList<>();

        List<ActivityManager.RunningAppProcessInfo> runAppsList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runAppsList) {
            for (String pkg : processInfo.pkgList) {
                runningApps.add(pkg);
            }
        }

        try {
            //can throw securityException at api<18 (maybe need "android.permission.GET_TASKS")
            List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1000);
            for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
                runningApps.add(taskInfo.topActivity.getPackageName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            runningApps.add(serviceInfo.service.getPackageName());
        }

        runningApps = new ArrayList<>(new HashSet<>(runningApps));

        if(!includeSystem){
            for (int i = runningApps.size() - 1; i >= 0; i--) {
                String app = runningApps.get(i);
                if(isSystemPackage(context, app)){
                    runningApps.remove(i);
                }
            }
        }
        return runningApps;
    }

    public static boolean isSystemPackage(Context context, String app){
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo pkgInfo = packageManager.getPackageInfo(app, 0);
            return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasAppPermission(Context context, String app, String permission){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(app, PackageManager.GET_PERMISSIONS);
            if(packageInfo.requestedPermissions!= null){
                for (String requestedPermission : packageInfo.requestedPermissions) {
                    if (requestedPermission.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                mMap.clear();
                MarkerOptions mp = new MarkerOptions();
                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                System.out.println(lat+", "+lon+" dkn3b4jr44");

                session.setLatLon(lat+", "+lon);
                mp.title("Your Location");
                mMap.addMarker(mp);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }

    });
    }




    private void checkIn() {
        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Check In on progress...");
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", session.getUserNIK());
            if (checkinType.equals("REGULER")) {
                jsonObject.put("presence_type", "CI");
            } else {
                jsonObject.put("presence_type", "OI");
            }
            jsonObject.put("date", tRes);
            jsonObject.put("time", jamRes);
            jsonObject.put("emoticon", session.getEmot());
            jsonObject.put("location",session.getLatLon());
            jsonObject.put("change_user", session.getUserNIK());
            jsonObject.put("presensi_status", "00");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject+"3nkej3n3eCHeckin");
        AndroidNetworking.post(session.getServerURL()+"users/"+session.getUserNIK()+"/presensi/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"edknwkjrn4Checkin");
                        try {
                            if(response.getInt("status")==200){
                                getStatCheckin();
                                session.setStat("CO");
                                Intent i = new Intent(CheckinActivity.this, HomeActivity.class);
                                startActivity(i);
                                Toast.makeText(CheckinActivity.this,"Thanks for check in today!",Toast.LENGTH_SHORT).show();
//                                finish();
                            }else if(response.getInt("status")==500){
                                Toast.makeText(CheckinActivity.this,"You Already checkin today!",Toast.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(rootView,"Something wrong", Snackbar.LENGTH_LONG).show();
                            }
                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        }catch (Exception e){
                            System.out.println(e);
                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                    }
                });
    }

    private void checkOut() {
        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Check Out on progress...");

        SimpleDateFormat tglPlus = new SimpleDateFormat("yyyy-MM-dd");
        String tResplus = tglPlus.format(new Date());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(tglPlus.parse(tResplus));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        String output = sdf1.format(c.getTime());

        SimpleDateFormat jam = new SimpleDateFormat("HHmmss");
        String jamRes = jam.format(new Date());
        System.out.println(jamRes+"HASILJAM"+output);

        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        JSONObject jsonObject = new JSONObject();
        try {
            if (Integer.valueOf(jamRes)<060000) {
                jsonObject.put("begin_date", output);
            } else {
                jsonObject.put("begin_date", tRes);
            }
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", session.getUserNIK());
            if (checkinType.equals("REGULER")) {
                jsonObject.put("presence_type", "CO");
            } else {
                jsonObject.put("presence_type", "OO");
            }
            jsonObject.put("date", tRes);
            jsonObject.put("time", jamRes);
            jsonObject.put("emoticon", session.getEmot());
            jsonObject.put("location",session.getLatLon());
            jsonObject.put("change_user", session.getUserNIK());
            jsonObject.put("presensi_status", "00");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject+"3nkej3n3e");

        AndroidNetworking.post(session.getServerURL()+"users/"+session.getUserNIK()+"/presensi/+"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"edknwkjrn4Checkout");
                        try {
                            if(response.getInt("status")==200){
//                                getStatCheckin();
                                session.setStat("CI");
                                Toast.makeText(CheckinActivity.this, "Success chekout, thaks for today :)", Toast.LENGTH_SHORT).show();
//                                Intent a = new Intent(CheckinActivity.this, HomeActivity.class);
//                                startActivity(a);
                                finish();
                            }else if(response.getInt("status")==500){
                                Toast.makeText(CheckinActivity.this,"You Already checkout today!",Toast.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(rootView,"Something wrong", Snackbar.LENGTH_LONG).show();
                            }
                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        }catch (Exception e){
                            System.out.println(e);
                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                    }
                });
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

        @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
