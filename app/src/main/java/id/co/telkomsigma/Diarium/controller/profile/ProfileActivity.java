package id.co.telkomsigma.Diarium.controller.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.MyPostingAdapter;
import id.co.telkomsigma.Diarium.controller.HomeActivity;
import id.co.telkomsigma.Diarium.controller.LoginActivity;
import id.co.telkomsigma.Diarium.model.MyPostingModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;


public class ProfileActivity extends AppCompatActivity {

    ListView listMyPosting;
    Typeface font,fontbold;
    private ProgressDialogHelper progressDialogHelper;
    private List<MyPostingModel> listModel;
    private MyPostingModel model;
    private MyPostingAdapter adapter;
    UserSessionManager session;
    TextView tvNullPost, tvCountFriend, tvCountCommunity, tvName, tvNIK, tvJob, tvLokasi;
    String personal_numbe_paramr, avatar, personal_number, full_name, born_city, profile;
    Dialog dialog;
    LinearLayout lay_listfriend;
    ImageView ivProfile;
    boolean apakahtemen = false;
    int panjang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        personal_numbe_paramr = intent.getStringExtra("personal_number");
        avatar = intent.getStringExtra("avatar");
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(ProfileActivity.this);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");
        ivProfile = findViewById(R.id.ivProfile);
        lay_listfriend = findViewById(R.id.lay_listfriend);
        tvName = findViewById(R.id.tvName);
        tvNIK = findViewById(R.id.tvNIK);
        tvJob = findViewById(R.id.tvJob);
        tvLokasi = findViewById(R.id.tvLokasi);
        tvNullPost = findViewById(R.id.tvNullPost);
        TextView e = findViewById(R.id.nama6);
        tvCountFriend = findViewById(R.id.tvCountFriend);
        tvCountCommunity = findViewById(R.id.tvCountCommunity);
        TextView h2 = findViewById(R.id.nama8);
        TextView h3 = findViewById(R.id.nama12);
        listMyPosting = findViewById(R.id.listMyPosting);
        checkMyFriend(personal_numbe_paramr);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personal_numbe_paramr.equals(session.getUserNIK())) {
                    Intent i = new Intent (ProfileActivity.this, FotoProfileActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent (ProfileActivity.this, FullFotoActivity.class);
                    i.putExtra("avatar", avatar);
                    startActivity(i);
                }
            }
        });

        tvName.setTypeface(font);
        tvNIK.setTypeface(font);
        tvJob.setTypeface(fontbold);
//        tvName.setText(session.getUserNickName());
//        tvNIK.setText(session.getUserNIK());
//        tvJob.setText(session.getJob());
        e.setTypeface(font);
        tvCountFriend.setTypeface(fontbold);
        tvCountCommunity.setTypeface(fontbold);
        h2.setTypeface(font);
        h3.setTypeface(fontbold);

        lay_listfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, ListFriendActivity.class);
                i.putExtra("personal_numbe_paramr",personal_numbe_paramr);
                startActivity(i);
            }
        });

        LinearLayout sent = findViewById(R.id.sendpost);
        if (session.getUserNIK().equals(personal_numbe_paramr)) {
            sent.setVisibility(View.VISIBLE);
            sent.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent hariian = new Intent(getApplication() , PostActivity.class);
                    startActivity(hariian);
//                Toast.makeText(ProfileActivity.this,"Comming soon",Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            sent.setVisibility(View.GONE);
        }

        getPersonalData(personal_numbe_paramr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        getPersonalData(personal_numbe_paramr);
    }

    private void getPersonalData(String nik){
        progressDialogHelper.showProgressDialog(ProfileActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/"+session.getUserNIK()+"/personal/"+nik)
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
                        System.out.println(response+"kjwrhbk4jrpersonaldata");
                        try {
                            if(response.getInt("status")==200){
                                personal_number = response.getJSONObject("data").getString("personal_number");
                                full_name = response.getJSONObject("data").getString("full_name");
                                born_city = response.getJSONObject("data").getString("born_city");
                                profile = response.getJSONObject("data").getString("profile");
                                JSONArray arrayPosisi = response.getJSONObject("data").getJSONArray("Posisi");
                                for (int i=0;i<arrayPosisi.length();i++) {
                                    JSONObject object = arrayPosisi.getJSONObject(i);
                                    JSONArray arrayPos = object.getJSONArray("posisi");
                                    for (int j=0; j<arrayPos.length(); j++) {
                                        JSONObject objectPos = arrayPos.getJSONObject(j);
                                        String organizational_name = objectPos.getString("organizational_name");
                                        tvJob.setText(organizational_name);
                                    }
                                }
                                tvLokasi.setText(born_city);
                                tvName.setText(full_name);
                                tvNIK.setText(personal_number);
                                Picasso.get().load(profile).error(R.drawable.profile).into(ivProfile);
                                getMyPost(personal_number, full_name);
                                getCountFriend(personal_number);
                                getCountCommunity(personal_number);
                                progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                        System.out.println(error);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!personal_numbe_paramr.equals(session.getUserNIK())) {
            if (panjang==0) {
                inflater.inflate(R.menu.menu_profile, menu);
            }
        } else {
            inflater.inflate(R.menu.logout, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.addfriend:
                popUpAddFriend(personal_number, full_name);
                return true;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to logout ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        session.setLoginState(false);
                        session.logoutUser();
                        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void popUpAddFriend(String personal_number, String full_name) {
        dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.layout_add_friend);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        Button btnNo =(Button) dialog.findViewById(R.id.btnNo);
        dialog.show();
        dialog.setCancelable(false);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (panjang>0) {
                    Toast.makeText(ProfileActivity.this, "Already Friend", Toast.LENGTH_SHORT).show();
                } else {
                    addFriend(personal_number, full_name);
                }
                dialog.dismiss();
            }
        });
    }


    private void addFriend(String friend, String name) {
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
            jsonObject.put("friend", friend);
            jsonObject.put("change_user", session.getUserNIK());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject+"PARAMADDFRIEND");
        AndroidNetworking.post(session.getServerURL()+"users/"+session.getUserNIK()+"/addFriend/"+friend)
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
                        System.out.println(response+"ADDFRIENDRESPONSE");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(ProfileActivity.this,"Succes add "+name+" as a friend !",Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(ProfileActivity.this,"error!",Toast.LENGTH_SHORT).show();
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

        private void checkMyFriend(String temen){
        AndroidNetworking.get(session.getServerURL()+"users/verifyFriends/nik/"+temen+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONCEKFRIEND");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                System.out.println(jsonArray.length()+"arraynyatemen");
                                panjang = jsonArray.length();
//                                if (jsonArray.length()==0) {
//                                    apakahtemen = true;
//                                } else {
//                                    apakahtemen = false;
//                                }
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

    private void getCountFriend(String nik){
        AndroidNetworking.get(session.getServerURL()+"users/friendapprove/"+nik+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"JUMLAHTEMEN");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                tvCountFriend.setText(String.valueOf(jsonArray.length()));
                                System.out.println(jsonArray.length()+"jhertbjhrbken");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String status = object.getString("status");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    JSONArray arrayFriend = object.getJSONArray("friend");
//                                    tvCountFriend.setText(String.valueOf(arrayFriend.length()));
                                    for (int b=0; b<arrayFriend.length(); b++) {
                                        JSONObject objectFriend = arrayFriend.getJSONObject(b);
                                        String personal_number_teman = objectFriend.getString("personal_number");
                                        String full_name = objectFriend.getString("full_name");
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

    private void getCountCommunity(String nik){
        AndroidNetworking.get(session.getServerURL()+"users/myCommunity/nik/"+nik+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"JUMLAHCOMMUNITY");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                System.out.println(jsonArray.length()+"kdwnr4jansbdkh");
                                tvCountCommunity.setText(String.valueOf(jsonArray.length()));
                            }else{
                                popUpLogin();
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

    private void getMyPost(String nik, String name){
        progressDialogHelper.showProgressDialog(ProfileActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/myDetailPosting/limit/1000/nik/"+nik+"/buscd/"+session.getUserBusinessCode())
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
                                listModel = new ArrayList<MyPostingModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listMyPosting.setVisibility(View.GONE);
                                    tvNullPost.setVisibility(View.VISIBLE);
                                } else {
                                    listMyPosting.setVisibility(View.VISIBLE);
                                    tvNullPost.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
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
                                        JSONArray jsonArrayComment = object.getJSONArray("comments");
                                        JSONArray jsonArrayLike = object.getJSONArray("people_likes");
                                        boolean status_like = false;
                                        for (int c=0; c<jsonArrayLike.length(); c++) {
                                            JSONObject objectLike = jsonArray.getJSONObject(c);
                                            String personal_number_like = object.getString("personal_number");
                                            if (personal_number_like.equals(session.getUserNIK())) {
                                                status_like = true;
                                            } else {
                                                status_like = false;
                                            }
                                        }
                                        model = new MyPostingModel(begin_date, end_date, business_code, personal_number, posting_id, title, description, image, date, time, change_date, change_user, String.valueOf(jsonArrayComment.length()), String.valueOf(jsonArrayLike.length()), name, status_like, avatar);
                                        listModel.add(model);
                                    }
                                    adapter = new MyPostingAdapter(ProfileActivity.this, listModel);
                                    listMyPosting.setAdapter(adapter);
                                    listMyPosting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        generateActivityId();
                                            Intent i = new Intent(ProfileActivity.this, DetailpostActivity.class);
                                            i.putExtra("posting_id",listModel.get(position).getPosting_id());
                                            i.putExtra("title",listModel.get(position).getTitle());
                                            i.putExtra("description",listModel.get(position).getDescription());
                                            i.putExtra("date",listModel.get(position).getChange_date());
                                            i.putExtra("image",listModel.get(position).getImage());
                                            i.putExtra("status_like",listModel.get(position).isStatus_like());
                                            i.putExtra("name",session.getUserFullName());
                                            i.putExtra("avatar",session.getAvatar());
                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                            }else{
                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(ProfileActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(ProfileActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ProfileActivity.this);

                        System.out.println(error);
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
                                String id = response.getJSONObject("data").getString("id");
                                session.setGeneratedCommentId(id);
                            } else {
//                                popUpLogin();
                            }
                            System.out.println("status ya : " + session.getStat());
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

    private void popUpLogin() {
        final Dialog dialog = new Dialog(ProfileActivity.this);
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
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(i);
        return true;
    }
}
