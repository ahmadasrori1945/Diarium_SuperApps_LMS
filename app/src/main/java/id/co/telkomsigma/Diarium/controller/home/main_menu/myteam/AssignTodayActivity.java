package id.co.telkomsigma.Diarium.controller.home.main_menu.myteam;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.AssignmentMyTeamAdapter;
import id.co.telkomsigma.Diarium.model.TeamAssignmenModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;

public class AssignTodayActivity extends AppCompatActivity {
    Typeface font, fontbold;

    ArrayList<String> listItems;
    ArrayList<String> listItemsStartDate;
    ArrayList<String> listItemsEndDate;
    ArrayAdapter<String> adapter;
    private ProgressDialogHelper progressDialogHelper;
    int countdone = 0;
    int bates = 0;
    EditText etActivity, etStartDate, etEndDate;
    final Calendar myCalendar = Calendar.getInstance();
    ListView list_member;
    LinearLayout rootView;
    Button btnFinish;
    UserSessionManager session;
    private TextView tvNull;
    int id_selanjutnya=0;

    private List<TeamAssignmenModel> listModelMyTeam;
    private TeamAssignmenModel modelMyTeam;
    private AssignmentMyTeamAdapter adapterMyTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_today);
        session = new UserSessionManager(AssignTodayActivity.this);
        progressDialogHelper = new ProgressDialogHelper();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        font = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Nexa Bold.otf");
        rootView = findViewById(R.id.lin_login);
        list_member = findViewById(R.id.list_member);
        etActivity = findViewById(R.id.etActivity);
        tvNull = (TextView) findViewById(R.id.tvNull);
        etStartDate = (EditText) findViewById(R.id.etStartDate);
        etEndDate = (EditText) findViewById(R.id.etEndDate);
        TextView a = findViewById(R.id.add);
        btnFinish = findViewById(R.id.btnFinish);

        final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStartDate();
            }
        };

        final DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEndDate();
            }
        };

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AssignTodayActivity.this, dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AssignTodayActivity.this, dateEnd, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        getMyTeam();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aktivity = etActivity.getText().toString();
                String start = etStartDate.getText().toString();
                String finish = etEndDate.getText().toString();
                if (aktivity.matches("")||start.matches("")||finish.matches("")) {
                    Toast.makeText(AssignTodayActivity.this, "Please input all field", Toast.LENGTH_SHORT).show();
                } else {
                    submitTodayActivity(aktivity, start, finish);
                }

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Assignment");
    }

    private void getMyTeam(){
        progressDialogHelper.showProgressDialog(AssignTodayActivity.this, "Getting data team...");
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        AndroidNetworking.get(session.getServerURL()+"users/"+session.getUserBusinessCode()+"/myteam?")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"getOurTeamKOKOKWOY");
                        try {
                            if(response.getInt("status")==200){
                                listModelMyTeam = new ArrayList<TeamAssignmenModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    list_member.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    list_member.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String personal_number = object.getString("personal_number");
                                        String name = object.getString("name");
                                        String start_date = object.getString("start_date");
                                        String end_date = object.getString("end_date");
                                        String unit = object.getString("unit");
                                        String position = object.getString("position");
                                        String relation = object.getString("relation");
                                        String status = object.getString("status");
                                        String profile = object.getString("profile");
                                        if (!personal_number.equals(session.getUserNIK())) {
                                            modelMyTeam = new TeamAssignmenModel(personal_number, name, start_date, end_date, unit, position, relation, status, false, profile);
                                            listModelMyTeam.add(modelMyTeam);
                                        }
                                    }
                                    adapterMyTeam = new AssignmentMyTeamAdapter(AssignTodayActivity.this, listModelMyTeam);
                                    list_member.setAdapter(adapterMyTeam);
                                }
                            }else{
                            }
                            progressDialogHelper.dismissProgressDialog(AssignTodayActivity.this);
                        }catch (Exception e){
                            System.out.println(e);
                            progressDialogHelper.dismissProgressDialog(AssignTodayActivity.this);

                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                        progressDialogHelper.dismissProgressDialog(AssignTodayActivity.this);

                    }
                });
    }

    private void submitTodayActivity(String aktivity, String start, String end) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jamParam = new SimpleDateFormat("yyyMMddHHmmss");
        String jamResParam = jamParam.format(new Date());

        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listModelMyTeam.size(); i++) {
            if (listModelMyTeam.get(i).isChecked()) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat jamId = new SimpleDateFormat("yyyMMddHHmmss");
                String jamResId = jamId.format(new Date());
                obj = new JSONObject();
                try {
                    obj.put("begin_date", tRes);
                    obj.put("end_date", "9999-12-31");
                    obj.put("business_code", session.getUserBusinessCode());
                    obj.put("personal_number", listModelMyTeam.get(i).getPersonal_number());
                    obj.put("activity_id", jamResId+i);
                    obj.put("activity_type", "00");
                    obj.put("activity_title", aktivity);
                    obj.put("activity_start", start);
                    obj.put("activity_finish", end);
                    obj.put("approval_status", "1");
                    obj.put("change_user", session.getUserNIK());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(obj);
            }
        }

        System.out.println(jsonArray + "3nkej3n3eAktivitas");
        AndroidNetworking.post(session.getServerURL()+"users/activity/"+jamResParam+"/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONArrayBody(jsonArray)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"edknwkjrAktivitas");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(AssignTodayActivity.this, "Success add assignment!", Toast.LENGTH_SHORT).show();
                                etActivity.setText("");
                                etStartDate.setText("");
                                etEndDate.setText("");
                                finish();

//                                countdone++;
//                                id_selanjutnya++;
//                                if (countdone==listItems.size()) {
//
//                                }
                            }else {
                                Snackbar.make(rootView,"Something wrong", Snackbar.LENGTH_LONG).show();
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

    private void updateLabelStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelEndDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etEndDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
