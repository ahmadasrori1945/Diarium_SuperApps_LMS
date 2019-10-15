package id.co.telkomsigma.Diarium.controller.home.main_menu.today_activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
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
import java.util.Locale;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.util.UserSessionManager;

public class AddTodayActActivity extends AppCompatActivity {

    Typeface font, fontbold;

    ArrayList<String> listId;
    ArrayList<String> listItems;
    ArrayList<String> listItemsStartDate;
    ArrayList<String> listItemsEndDate;
    ArrayAdapter<String> adapter;
    int countdone = 0;
    int id_selanjutnya=0;
    int id_lanjutan=0;
    boolean jenis = true;
    TextView tvAdd;
    EditText etActivity, etStartDate, etEndDate;
    final Calendar myCalendar = Calendar.getInstance();
    ListView list;
    LinearLayout rootView;
    Button btnFinish;
    UserSessionManager session;
    String id_global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_today);
        session = new UserSessionManager(AddTodayActActivity.this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        font = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Nexa Bold.otf");
        rootView = findViewById(R.id.lin_login);
        tvAdd = findViewById(R.id.tvAdd);
        list = findViewById(R.id.list_activity);
        etActivity = findViewById(R.id.etActivity);

        TextView a = findViewById(R.id.add);
        btnFinish = findViewById(R.id.btnFinish);

        a.setTypeface(fontbold);
        etActivity.setTypeface(font);
        btnFinish.setTypeface(fontbold);


        tvAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String aktifitas = etActivity.getText().toString();
                if (aktifitas.equals("") || aktifitas.isEmpty()) {
                    Snackbar.make(rootView, "Activity must be input !", Snackbar.LENGTH_LONG).show();
                } else {
                    AddActivity(aktifitas);
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    session.setStat("CI");
//                Toast.makeText(AddTodayActActivity.this, "lenght : "+list.getCount(), Toast.LENGTH_SHORT).show();
//                generateActivityId();
                submitTodayActivity();
//                System.out.println(listItems.size()+"JUMLAHNYA");
//                for (int i=0; i<listItems.size();i++) {
//                    String activity_title = listItems.get(i);
//                    String activity_start = listItemsStartDate.get(i);
//                    String activity_finish = listItemsEndDate.get(i);
//
//                    String id = id_global;
//                }
            }
        });

        listItems = new ArrayList<String>();
        listId = new ArrayList<String>();
        listItemsStartDate = new ArrayList<String>();
        listItemsEndDate = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(AddTodayActActivity.this,
                android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void AddActivity(final String aktifitas) {
        final Dialog dialog = new Dialog(AddTodayActActivity.this);
        dialog.setContentView(R.layout.popup_add_activity);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");

        etStartDate = (EditText) dialog.findViewById(R.id.etStartDate);
        etEndDate = (EditText) dialog.findViewById(R.id.etEndDate);
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView tvActivity = (TextView) dialog.findViewById(R.id.tvActivity);

        tvActivity.setText(aktifitas);
        dialog.show();
        dialog.setCancelable(false);

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
                new DatePickerDialog(AddTodayActActivity.this, dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddTodayActActivity.this, dateEnd, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String S = etStartDate.getText().toString();
                String E = etEndDate.getText().toString();
                if (S.equals("")||E.equals("")) {
                    Toast.makeText(AddTodayActActivity.this, "Please input date", Toast.LENGTH_SHORT).show();
                } else {
                    listItems.add(aktifitas);
                    listItemsStartDate.add(S);
                    listItemsEndDate.add(E);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    etActivity.getText().clear();
                }
            }
        });
    }

    private void generateActivityId() {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL() + "users/generateIDActivity")
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("djhwebfheActivityId" + response);
                        // do anything with response
                        try {
                            if (response.getInt("status") == 200) {
                                String idhasil = response.getJSONObject("data").getString("id");
                                System.out.println(idhasil+"IDNYAADALAH");
//                                id_selanjutnya = Integer.parseInt(idhasil);
//                                submitTodayActivity(idhasil);
//                                if (id_lanjutan<Integer.parseInt(idhasil)) {
//                                    id_lanjutan = Integer.parseInt(idhasil);
//                                } else {
//                                    submitTodayActivity(id_selanjutnya,activity_title,activity_start,activity_finish);
//                                }
                            } else {
//                                popUpLogin();
                            }
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        } catch (Exception e) {
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

    private void submitTodayActivity() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jamParam = new SimpleDateFormat("yyyMMddHHmmss");
        String jamResParam = jamParam.format(new Date());

        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listItems.size(); i++) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat jamId = new SimpleDateFormat("yyyMMddHHmmss");
            String jamResId = jamId.format(new Date());
            obj = new JSONObject();
            try {
                obj.put("begin_date", tRes);
                obj.put("end_date", "9999-12-31");
                obj.put("business_code", session.getUserBusinessCode());
                obj.put("personal_number", session.getUserNIK());
                obj.put("activity_id", jamResId+i);
                obj.put("activity_type", "00");
                obj.put("activity_title", listItems.get(i));
                obj.put("activity_start", listItemsStartDate.get(i));
                obj.put("activity_finish", listItemsEndDate.get(i));
                obj.put("approval_status", "1");
                obj.put("change_user", session.getUserNIK());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(obj);
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
                                Toast.makeText(AddTodayActActivity.this, "Success add activity, happy work :)", Toast.LENGTH_SHORT).show();
                                finish();
//                                Intent a = new Intent(AddTodayActActivity.this, HomeActivity.class);
//                                a.putExtra("key", "asd");
//                                startActivity(a);

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
