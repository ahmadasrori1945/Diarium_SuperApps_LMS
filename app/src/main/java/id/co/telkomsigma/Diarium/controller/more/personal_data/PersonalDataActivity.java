package id.co.telkomsigma.Diarium.controller.more.personal_data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.DataPersonalAdapter;
import id.co.telkomsigma.Diarium.model.PersonalDataModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;


public class PersonalDataActivity extends AppCompatActivity {

    Typeface font,fontbold;
    private static final int REQUEST_READ_CONTACTS = 0;
    public static final int ENROLL_REQ_CODE = 12800;
    public static final int AUTH_REQ_CODE = 12801;

    String[] identity={
            "Nama",
            "NIK",
            "KTP",
            "KK",
            "SIM",
            "No BPJS",
    };
    String[] isi={
            "12345",
            "67891",
            "23456",
            "12345",
            "67891",
            "23456",
    };

    private List<PersonalDataModel> listModel;
    private PersonalDataModel model;
    private DataPersonalAdapter adapter;

//    Button btnFRRegis;
    TextView tvName, tvNIK, tvJob, tvKTP, tvRekening, tvNPWP, tvBPJS;
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    ListView lvDataPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(PersonalDataActivity.this);

        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");
//        btnFRChange = (Button) findViewById(R.id.btnFRChange);
//        btnFRRegis = (Button) findViewById(R.id.btnFRRegis);
        lvDataPersonal = (ListView) findViewById(R.id.listDataPersonal);

        TextView a = (TextView) findViewById(R.id.identity);
        tvName = (TextView) findViewById(R.id.tvTitle);
        tvNIK = (TextView) findViewById(R.id.tvNik);
        tvJob = (TextView) findViewById(R.id.tvJob);
        a.setTypeface(fontbold);
//        btnFRRegis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(PersonalDataActivity.this, "not available at this time", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(PersonalDataActivity.this, FMActivity.class);
//                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_ENROLL);
//                startActivityForResult(i,ENROLL_REQ_CODE);
//            }
//        });

        tvName.setText(session.getUserFullName());
        tvNIK.setText(session.getUserNIK());
        tvJob.setText(session.getJob());
        getMyIdentity();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_REQ_CODE) {
            if (session.getFMResult() == Activity.RESULT_OK) {
//                Snackbar.make(rootView,"Your face has successfuly registered",Snackbar.LENGTH_LONG).show();
                // User enrolled successfully
                try {
                    System.out.println(session.getFMResponse());
                    System.out.println(session.getFMMessage());
                    JSONObject dat = new JSONObject(session.getFMMessage());
                    if(dat.getString("result").equals("VERIFIED")){
                        session.setFMResponse("-");
                        session.setFMMessage("-");
                        session.setFMResult(Activity.RESULT_CANCELED);
                        Toast.makeText(this, "Success on face register", Toast.LENGTH_SHORT).show();
//                        session.setLoginState(true);
//                        Intent i = new Intent(PersonalDataActivity.this, HomeActivity.class);
//                        i.putExtra("key", "none");
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
                        finish();
                    }else{
                        new AlertDialog.Builder(PersonalDataActivity.this)
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
                    Toast.makeText(PersonalDataActivity.this,"Seems like you have connection problem ",Toast.LENGTH_SHORT).show();
                }


            } else if(resultCode == Activity.RESULT_CANCELED){
                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
                Toast.makeText(this, "Face registration failed, Try Again!", Toast.LENGTH_SHORT).show();
//                Snackbar.make(rootView,"Face registration failed, Try Again!",Snackbar.LENGTH_LONG).show();
                // Enrollment cancelled
            }else{
                Toast.makeText(this, "No activity found", Toast.LENGTH_SHORT).show();
//                Snackbar.make(rootView,"No activity found",Snackbar.LENGTH_LONG).show();
            }
        }

        if (requestCode == ENROLL_REQ_CODE) {
            if (session.getFMResult() == Activity.RESULT_OK) {
                try {
                    System.out.println(session.getFMResponse());
                    System.out.println(session.getFMMessage());
                    JSONObject dat = new JSONObject(session.getFMMessage());
                    if(dat.getBoolean("isModelReady")){
                        session.setFMResponse("-");
                        session.setFMMessage("-");
                        session.setFMResult(Activity.RESULT_CANCELED);
                        Toast.makeText(this, "sukses", Toast.LENGTH_SHORT).show();
//                        session.setLoginState(true);
//                        Intent i = new Intent(PersonalDataActivity.this, HomeActivity.class);
//                        i.putExtra("key", "none");
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
                        finish();
                    }else{
                        new AlertDialog.Builder(PersonalDataActivity.this)
                                .setMessage("Your face is not valid, please check your camera and try again")
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
                    Toast.makeText(PersonalDataActivity.this,"Seems like you have connection problem ",Toast.LENGTH_SHORT).show();

                }


            } else if(resultCode == Activity.RESULT_CANCELED){

                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
                Toast.makeText(this, "Face registration failed, Try Again!", Toast.LENGTH_SHORT).show();
//                Snackbar.make(rootView,"Face registration failed, Try Again!",Snackbar.LENGTH_LONG).show();
                // Enrollment cancelled
            }else{
                Toast.makeText(this, "No activity found", Toast.LENGTH_SHORT).show();
//                Snackbar.make(rootView,"No activity found",Snackbar.LENGTH_LONG).show();
            }
        }

    }


    private void getMyIdentity(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        progressDialogHelper.showProgressDialog(PersonalDataActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/mydetailidentity/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONAVAILABLE"+response);
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<PersonalDataModel>();
                                JSONArray jsonArraycommunication = response.getJSONObject("data").getJSONArray("communication");
                                for (int a=0;a<jsonArraycommunication.length();a++) {
                                    JSONObject objcommunication = jsonArraycommunication.getJSONObject(a);
                                    String value = objcommunication.getString("communication_number");
                                    JSONObject objname = objcommunication.getJSONObject("communication_name");
                                    String name = objname.getString("name");
                                    model = new PersonalDataModel(name, value);
                                    listModel.add(model);
                                }
                                JSONArray jsonArrayidentification = response.getJSONObject("data").getJSONArray("identification");
                                for (int b=0;b<jsonArrayidentification.length();b++) {
                                    JSONObject objidentification = jsonArrayidentification.getJSONObject(b);
                                    String value = objidentification.getString("identification_number");
                                    JSONArray namearray = objidentification.getJSONArray("identification_name");
                                    for (int o=0;o<namearray.length();o++) {
                                        JSONObject objname = namearray.getJSONObject(o);
                                        String name = objname.getString("name");
                                        model = new PersonalDataModel(name, value);
                                        listModel.add(model);
                                    }
                                }

                                String name_bpjs_ketenagakerjaan = "BPJS Ketenagakerjaan";
                                String value_bpjs_ketenagakerjaan = response.getJSONObject("data").getJSONObject("bpjs_ketenagakerjaan").getString("bpjs_number");

                                model = new PersonalDataModel(name_bpjs_ketenagakerjaan, value_bpjs_ketenagakerjaan);
                                listModel.add(model);

                                String name_bpjs_kesehatan = "BPJS Kesehatan";
                                String value_bpjs_kesehatan = response.getJSONObject("data").getJSONObject("bpjs_kesehatan").getString("insurance_number");

                                model = new PersonalDataModel(name_bpjs_kesehatan, value_bpjs_kesehatan);
                                listModel.add(model);

                                JSONArray jsonarraybank_employee = response.getJSONObject("data").getJSONArray("bank_employee");
                                for (int a=0;a<jsonarraybank_employee.length();a++) {
                                    JSONObject objcommunication = jsonarraybank_employee.getJSONObject(a);
                                    String value = objcommunication.getString("account_number");
                                    String name = objcommunication.getString("bank_type");
                                    model = new PersonalDataModel(name, value);
                                    listModel.add(model);
                                }


                                adapter = new DataPersonalAdapter(PersonalDataActivity.this, listModel);
                                lvDataPersonal.setAdapter(adapter);

                                progressDialogHelper.dismissProgressDialog(PersonalDataActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(PersonalDataActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(PersonalDataActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(PersonalDataActivity.this);
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
