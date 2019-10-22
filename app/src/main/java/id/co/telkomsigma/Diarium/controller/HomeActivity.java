package id.co.telkomsigma.Diarium.controller;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

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
import java.util.Date;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.controller.friend.FriendsFragment;
import id.co.telkomsigma.Diarium.controller.home.HomeFragment;
import id.co.telkomsigma.Diarium.controller.inbox.InboxFragment;
import id.co.telkomsigma.Diarium.controller.notification.NotifikasiFragment;
import id.co.telkomsigma.Diarium.util.AutoUpdater;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;


public class HomeActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private ProgressDialog progressDialog;
    AutoUpdater atualizaApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        atualizaApp = new AutoUpdater(this);

        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(HomeActivity.this);
        progressDialog = new ProgressDialog(this);

        fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, fragment).commit();

        System.out.println(FirebaseInstanceId.getInstance().getToken());
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                fragment = new HomeFragment();
                                break;
                            case R.id.navigation_inbox:
                                fragment = new InboxFragment();
                                break;
                            case R.id.navigation_friends:
                                fragment = new FriendsFragment();
                                break;
                            case R.id.navigation_notifications:
                                fragment = new NotifikasiFragment();
                                break;
//                            case R.id.navigation_more:
//                                fragment = new MoreFragment();
//                                break;

                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content, fragment).commit();
                        return true;
                    }
                });
        getRoleLMS();
//        session.setCurrentDate("2015-01-06");
//        getStatCheckin();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "Resume Sukses dari HomeActivity", Toast.LENGTH_SHORT).show();
        ConnectivityManager mgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        if (netInfo != null) {
            if (netInfo.isConnected()) {
                System.out.println("internet gw kenceng coy");
            }else {
                Toast.makeText(this, "Connection problem", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Connection problem", Toast.LENGTH_SHORT).show();
            //No internet
        }
        checkVersion();
        getRoleLMS();
//        getStatCheckin();
        getAva();
        fragment = new HomeFragment();
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
//                                session.setRoleLMS(role_code);
                            }
                            session.setRoleLMS("MENTOR");
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

    public void apkDownloader(String url, String title){
        System.out.println(url+" "+title+" paramupdate");
        final ProgressDialog pd= ProgressDialog.show(HomeActivity.this, "Please Wait..", "Downloading data", false, false);
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        // System.out.println();
        //String fileName = url.split("/")[url.split("/").length-1];
        String fileName = "diarium.apk";

        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists()){
            file.delete();//file.delete() - test this, I think sometimes it doesnt work
        }


        //get url of app on server
        //String url = url;

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Diarium");
        request.setTitle(title);

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) HomeActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);
        System.out.println("masuk1 ");
        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                pd.dismiss();
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(uri,"application/vnd.android.package-archive");
                manager.getMimeTypeForDownloadedFile(downloadId);
                (HomeActivity.this).startActivity(install);

                HomeActivity.this.unregisterReceiver(this);
                //(Activity)context.;
            }
        };
        //register receiver for when .apk download is compete
        HomeActivity.this.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void getAva(){
        progressDialogHelper.changeMessage("Get personal data..");
        AndroidNetworking.get(session.getServerURL()+"users/"+session.getUserNIK()+"/personal/"+session.getUserNIK())
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
                        System.out.println(response+"kjwrhbk4jr");
                        try {
                            if(response.getInt("status")==200){

                                session.setAvatar(response.getJSONObject("data").getString("profile"));
                            }else{
                                progressDialogHelper.dismissProgressDialog(HomeActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(HomeActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(HomeActivity.this);
                        System.out.println(error);
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
//                                switch (jsonArray.length()) {
//                                    case 0:
//                                        session.setStat("CO");
//                                        break;
//                                    case 1:
//                                        session.setStat("CI");
//                                        break;
//                                    case 2:
//                                        session.setStat("CO");
//                                        break;
//                                    case 4:
//                                        session.setStat("CO");
//                                        break;
//                                }
                            }else{
                                popUpLogin();
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

    private void checkVersion(){
        System.out.println("versimasuk");
        System.out.println(session.getServerURL()+"j3n4rje");
        progressDialogHelper.showProgressDialog(HomeActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"appversion/ANDROID/buscd/1000")
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
                        System.out.println(response+"RESPONVERSION");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i=0; i<jsonArray.length(); i++) {
                                    JSONObject a = jsonArray.getJSONObject(i);
                                    String versionApi = a.getString("version_code");
                                    String version_link = a.getString("version_link");
                                    System.out.println(a.getString("version_code")+"hg23ku4j");

                                    PackageInfo pInfo = HomeActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                                    String versionApps = pInfo.versionName;
                                    String result = versionApps.replaceAll("[.]","");
                                    int verasli = Integer.parseInt(result);
                                    int verDB = Integer.parseInt(versionApi);
                                    System.out.println(verDB+" - "+verasli+"djk23n4rjkn");
                                    if (verDB>verasli) {
                                        popupUpdate(version_link);
                                    }
                                }
                                progressDialogHelper.dismissProgressDialog(HomeActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(HomeActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(HomeActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(HomeActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.layout_session_end);
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
                session.setLoginState(false);
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    private void popupUpdate(final String url) {
        final Dialog dialog = new Dialog(HomeActivity.this);
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
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
                atualizaApp.apkDownloader(url,"diarium.apk");
//                apkDownloader(url,"Diarium Update");
//                new DownloadFile().execute(url, "diarium.apk");
                dialog.dismiss();
            }
        });
    }


    class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            progressDialog = new ProgressDialog(HomeActivity.this);
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
                String namaFile = Url[1];
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
//            progressDialog.setCancelable(false);
//            progressDialog.setIndeterminate(false);
//            progressDialog.setMax(100);
            System.out.println("progres masuk");
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result != null) {
                Toast.makeText(HomeActivity.this, "Download error: " + result, Toast.LENGTH_LONG).show();
                finish();
            } else {
//                Toast.makeText(HomeActivity.this, "masukeko", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + "diarium.apk")),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


}
