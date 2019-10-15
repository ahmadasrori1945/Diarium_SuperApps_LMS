package id.co.telkomsigma.Diarium.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.element.common.PermissionUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import id.co.telkomsigma.Diarium.BuildConfig;
import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.FMActivity;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;

import id.co.telkomsigma.Diarium.util.element.FMActivity;

/**
 * A login screen that offers login via nik/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    public static final int ENROLL_REQ_CODE = 12800;
    public static final int AUTH_REQ_CODE = 12801;
    // private UserLoginTask mAuthTask = null;
    UserSessionManager session;
    Dialog myDialog;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private ProgressDialogHelper progressDialogHelper;
    String[] permissionsRequired = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    // UI references.
    private EditText nikField;
    private EditText passwordField;
    private View mProgressView;
    private View mLoginFormView;
    LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        AndroidNetworking.initialize(getApplicationContext());
        nikField =  findViewById(R.id.nik);
        myDialog = new Dialog(LoginActivity.this);
       // populateAutoComplete();
        session = new UserSessionManager(LoginActivity.this);
        if(ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[2])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(LoginActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

//            txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
        }
        progressDialogHelper = new ProgressDialogHelper();
        rootView = findViewById(R.id.lin_login);

//        PermissionUtils.verifyPermissions(
//                LoginActivity.this,
//                Manifest.permission.CAMERA,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.READ_PHONE_STATE);

        if(session.isLogin()){
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            i.putExtra("key", "none");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }


        passwordField =  findViewById(R.id.password);
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton =  findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                 attemptLogin();
//                 Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//
//                Intent i = new Intent(LoginActivity.this, FMActivity.class);
//                i.putExtra(FMActivity.USER_ID,nikField.getText().toString());
//                if(passwordField.getText().toString().equals("passEnroll")){
//                    i.putExtra(FMActivity.METHOD,FMActivity.METHOD_ENROLL);
//                }else{
//                    i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                }
//
//                startActivityForResult(i,AUTH_REQ_CODE);

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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
                        session.setLoginState(true);
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        i.putExtra("key", "none");
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }else{
                        new AlertDialog.Builder(LoginActivity.this)
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
                    Toast.makeText(LoginActivity.this,"Seems like you have connection problem ",Toast.LENGTH_SHORT).show();
                }


            } else if(resultCode == Activity.RESULT_CANCELED){
                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
                Snackbar.make(rootView,"Face registration failed, Try Again!", Snackbar.LENGTH_LONG).show();
                // Enrollment cancelled
            }else{
                Snackbar.make(rootView,"No activity found", Snackbar.LENGTH_LONG).show();
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
                        session.setLoginState(true);
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        i.putExtra("key", "none");
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }else{
                        new AlertDialog.Builder(LoginActivity.this)
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
                    Toast.makeText(LoginActivity.this,"Seems like you have connection problem ",Toast.LENGTH_SHORT).show();

                }


            } else if(resultCode == Activity.RESULT_CANCELED){

                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
                Snackbar.make(rootView,"Face registration failed, Try Again!", Snackbar.LENGTH_LONG).show();
                // Enrollment cancelled
            }else{
                Snackbar.make(rootView,"No activity found", Snackbar.LENGTH_LONG).show();
            }
        }
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
            //Got Permission
        }
    }





    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // populateAutoComplete();
            }
        }
    }

    private void postDevice(String buscd, String nik){
        System.out.println("MASUKDEVICE");
//        final String nik = nikField.getText().toString();
//        progressDialogHelper.showProgressDialog(LoginActivity.this, "Logging in..");
//
//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String dev_id = "";
//        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            dev_id =  telephonyManager.getDeviceId();
//        }else{
//            PermissionUtils.verifyPermissions(
//                    LoginActivity.this,
//                    Manifest.permission.READ_PHONE_STATE);
//        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        String reg_id = FirebaseInstanceId.getInstance().getToken();

        JSONObject body = new JSONObject();
        try {
            body.put("begin_date",tRes);
            body.put("end_date","9999-12-31");
            body.put("business_code",buscd);
            body.put("personal_number",nik);
            body.put("device_id",nik);
            body.put("device_model","ANDROID");
            body.put("register_id",reg_id);
            body.put("platform","ANDROID");
            body.put("version_code","1");
            body.put("change_user",nik);
        }catch (JSONException e){
            System.out.println(e);

        }
        System.out.println("PARAMDEVICE"+body);

        AndroidNetworking.post(session.getServerURL()+"users/"+nik+"/personaldevice/"+nik+"/deviceid/"+nik)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONAUTH");
                        try {
                            if (response.getInt("status")==200) {
                                System.out.println("suksespostdevice");
                            } else {
                                System.out.println("gagalpost");
                            }
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                            System.out.println(e);
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                        Snackbar.make(rootView,"error to login", Snackbar.LENGTH_LONG).show();
                        System.out.println(error);
                    }
                });
    }

    private void attemptLogin(){
        System.out.println("MASUKLOGIN");
        nikField.setError(null);
        passwordField.setError(null);

        // Store values at the time of the login attempt.
        final String nik = nikField.getText().toString();
        String password = passwordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordField.setError(getString(R.string.error_invalid_password));
            focusView = passwordField;
            cancel = true;
        }

        // Check for a valid nik address.
        if (TextUtils.isEmpty(nik)) {
            nikField.setError(getString(R.string.error_field_required));
            focusView = nikField;
            cancel = true;
        } else if (!isNikValid(nik)) {
            nikField.setError(getString(R.string.error_invalid_email));
            focusView = nikField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            progressDialogHelper.showProgressDialog(LoginActivity.this, "Logging in..");

//            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//            String dev_id = "";
//            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                dev_id =  telephonyManager.getDeviceId();
//            }else{
//                PermissionUtils.verifyPermissions(
//                        LoginActivity.this,
//                        Manifest.permission.READ_PHONE_STATE);
//            }

//            String reg_id = FirebaseInstanceId.getInstance().getToken();

            JSONObject body = new JSONObject();
            try {
                body.put("application_id","6");
                body.put("username",nik);
                body.put("password",password);
            }catch (JSONException e){
                System.out.println(e);

            }
            System.out.println("PARAMLOGIN"+body);

            AndroidNetworking.post("https://main.hc.digitalevent.id/ldap/api/auth")
                    .addHeaders("Accept","application/json")
                    .addHeaders("Content-Type","application/json")
                    .addJSONObjectBody(body)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            System.out.println(response+"RESPONAUTH");
                            try {
                                if(response.getInt("status")==200){
                                    session.setToken(response.getString("access_token"));
                                    session.setUserNik(nik);
//                                    session.setUserNik("632406");
                                    getPersonalData(session.getToken(),session.getUserNIK());
//                                    getPersonalData(session.getToken(),"10101010");
                                }else{
                                    progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                                    Snackbar.make(rootView,"Wrong username and password", Snackbar.LENGTH_LONG).show();
                                }
                            }catch (Exception e){
                                progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                                System.out.println(e);
                            }


                        }
                        @Override
                        public void onError(ANError error) {
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                            Snackbar.make(rootView,"error to login", Snackbar.LENGTH_LONG).show();
                            System.out.println(error);
                        }
                    });
        }
    }

    private boolean isNikValid(String nik) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    private void getPersonalData(String token, String pernr){
        progressDialogHelper.changeMessage("Get personal data..");

        AndroidNetworking.get(session.getServerURL()+"users/"+pernr+"/personal/"+pernr)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",token)
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"kjwrhbk4jr");
                        try {
                            if(response.getInt("status")==200){
                                session.setUserNik(response.getJSONObject("data").getString("personal_number"));
                                session.setUserBusinessCode(response.getJSONObject("data").getString("business_code"));
                                session.setUserFullName(response.getJSONObject("data").getString("full_name"));
                                session.setUserNickName(response.getJSONObject("data").getString("nickname"));
                                session.setAvatar(response.getJSONObject("data").getString("profile"));
                                session.setBornDate(response.getJSONObject("data").getString("born_date").substring(5));
                                session.setStatusClickBornDate("0");
                                session.setUserFaceCode(session.getUserBusinessCode()+"-"+session.getUserNIK());
                                postDevice(response.getJSONObject("data").getString("business_code"), pernr);
                                JSONArray arrayPosisi = response.getJSONObject("data").getJSONArray("Posisi");
                                for (int i=0;i<arrayPosisi.length();i++) {
                                    JSONObject objPosisi = arrayPosisi.getJSONObject(i);
                                    JSONArray arrayDalemanPosisi = objPosisi.getJSONArray("posisi");
                                    for (int a=0;a<arrayDalemanPosisi.length();a++) {
                                        JSONObject objDaleman = arrayDalemanPosisi.getJSONObject(a);
                                        String organizational_type = objDaleman.getString("organizational_type");
                                        if (organizational_type.equals("S")) {
                                            String organizational_name = objDaleman.getString("organizational_name");
                                            session.setJob(organizational_name);
                                        }
                                    }
                                }
//                                getMyTeam(response.getJSONObject("data").getString("business_code"), response.getJSONObject("data").getString("personal_number"));

//                                checkFaceEnroll(session.getUserFaceCode());

                                session.setLoginState(true);
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                i.putExtra("key", "none");
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }else{
                                progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                                Snackbar.make(rootView,"Can't get your personal data", Snackbar.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                            Snackbar.make(rootView,"error to get personal data", Snackbar.LENGTH_LONG).show();
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void getMyTeam(String buscd, final String personal_number_param){
        AndroidNetworking.get(session.getServerURL()+"users/"+buscd+"/myteam?")
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
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String personal_number_get = object.getString("personal_number");
                                    String name = object.getString("name");
                                    String start_date = object.getString("start_date");
                                    String end_date = object.getString("end_date");
                                    String unit = object.getString("unit");
                                    String position = object.getString("position");
                                    String job = object.getString("job");
                                    String relation = object.getString("relation");
                                    String status = object.getString("status");
                                    if (personal_number_get.equals(personal_number_param)) {
                                        session.setStatus(status);
//                                        session.setJob(position);
                                    }
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

    private void checkFaceEnroll(String faceCode){
        progressDialogHelper.changeMessage("Checking face data..");

        JSONObject body = new JSONObject();
        try {
            body.put("userId",faceCode);
        }catch (JSONException e){
            System.out.println(e);
            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
        }

        System.out.println(body);

        AndroidNetworking.post(FMActivity.METHOD_ENROLL_CHECK_URL)
                .addHeaders("apiKey", FMActivity.API_KEY)
                .addHeaders("appVersion", BuildConfig.VERSION_NAME)
                .addHeaders("os","ANDROID")
                .addHeaders("appId",getPackageName())
                .addHeaders("deviceModel",Build.MODEL)
                .addHeaders("sdkVersion","1.0")
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                        System.out.println(response+"kjewbrjkw4");
                        try {
                            System.out.println(response);
                            if(response.getBoolean("isEnrolled")){
                                Intent i = new Intent(LoginActivity.this, FMActivity.class);
                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);

                                startActivityForResult(i,AUTH_REQ_CODE);
                            }else{
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setMessage("Your face is not registered yet, please register your face first")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(LoginActivity.this, FMActivity.class);
                                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
                                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_ENROLL);
                                                startActivityForResult(i,ENROLL_REQ_CODE);

                                            }

                                        })
                                        .show();

                                //Snackbar.make(rootView,"Can't get your personal data",Snackbar.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                            Snackbar.make(rootView,"error to get face recognition", Snackbar.LENGTH_LONG).show();
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                        System.out.println(error);
                    }
                });
    }


}

