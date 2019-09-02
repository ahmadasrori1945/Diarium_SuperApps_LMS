package id.co.telkomsigma.Diarium.controller.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.CommentAdapter;
import id.co.telkomsigma.Diarium.model.CommentModel;
import id.co.telkomsigma.Diarium.util.TimeHelper;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;


public class DetailpostActivity extends AppCompatActivity {

    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<CommentModel> listModel;
    private CommentModel model;
    private CommentAdapter adapter;
    ListView lvComment;
    TextView tvCountComment, tvCountLike, tvPost;
    EditText etComment;
    String generatedCommendId, generatedLikeId;
    LinearLayout lvLike;
    ImageView ivLike, ivProfile;
    //    ImageView ivPost;
    private ZoomageView demoView;
    boolean status_like;
    TimeHelper timeHelper;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        timeHelper = new TimeHelper();
        setContentView(R.layout.activity_detailpost);
        Intent intent = getIntent();
        final String posting_id = intent.getStringExtra("posting_id");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");
        String avatar = intent.getStringExtra("avatar");
        status_like = intent.getBooleanExtra("status_like", false);
        demoView = findViewById(R.id.myZoomageView);
        ivLike = findViewById(R.id.ivLike);
        ivProfile = findViewById(R.id.ivProfile);
        Picasso.get().load(avatar).error(R.drawable.profile).into(ivProfile);


//        ivPost = findViewById(R.id.ivPost);
        lvLike = findViewById(R.id.lvLike);
        etComment = findViewById(R.id.etComment);
        tvPost = findViewById(R.id.tvPost);
        lvComment = findViewById(R.id.lvComment);
        tvCountComment = findViewById(R.id.tvCountComment);
        tvCountLike = findViewById(R.id.tvCountLike);
        lvComment.setDivider(null);

        TextView txtTitle = findViewById(R.id.tvName);
        txtTitle.setText(name);
        TextView txtTitle_dua =  findViewById(R.id.tvTitle);
        txtTitle_dua.setText(title);
        TextView txtTitle_tiga = findViewById(R.id.tvDate);
        txtTitle_tiga.setText(timeHelper.getElapsedTime(date));
        TextView txtTitle_empat = findViewById(R.id.tvDesc);
        txtTitle_empat.setText(description);
//        Picasso.get().load(image).error(R.drawable.placeholder_news).fit().into(ivPost);

        downloadImage(this, image, demoView);


        etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        generatedCommentID(posting_id);
//        generatedLikeID(posting_id);
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if (comment.isEmpty() || comment.equals("")||comment.length()==0) {
                    Toast.makeText(DetailpostActivity.this, "Plase input comment first !", Toast.LENGTH_SHORT).show();
                } else {
                    submitComment(posting_id, comment);
                }
            }
        });


        lvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status_like) {
                    submitDisLike(posting_id, session.getIdentifier());
                } else {
                    submitLike(posting_id);
                }

            }
        });

        getListComment(posting_id);
        countLike(posting_id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void downloadImage(Context context, String url, ImageView image) {
        Picasso.get()
                .load(url)
                .error(R.drawable.placeholder_gallery)
                .into(image);
    }

    private void generatedCommentID(String posting_id) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL() + "users/posting/"+posting_id+"/generateIDComment")
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
                                generatedCommendId = response.getJSONObject("data").getString("id");
//                                Toast.makeText(DetailpostActivity.this, "ID : "+generatedCommendId, Toast.LENGTH_SHORT).show();
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

    private void submitComment(final String posting_id, String comment) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        SimpleDateFormat commentId = new SimpleDateFormat("yyyyMMddHHmmss");
        String commentIdRes = commentId.format(new Date());

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", session.getUserNIK());
            jsonObject.put("posting_id", posting_id);
            jsonObject.put("comment_id", generatedCommendId);
            jsonObject.put("text_comment", comment);
            jsonObject.put("date", tRes);
            jsonObject.put("time", jamRes);
            jsonObject.put("change_user", session.getUserNIK());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonArray+"PARAMKOMEN");
        AndroidNetworking.post(session.getServerURL()+"users/posting/"+posting_id+"/comment/"+generatedCommendId+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONArrayBody(jsonArray)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONKOMEN");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
                                getListComment(posting_id);
                                adapter.notifyDataSetChanged();
                                etComment.setText("");
                            }
                        }catch (Exception e){
                            System.out.println("aaa"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("bbb"+error);
                    }
                });
    }

    private void submitDisLike(final String posting_id ,final String identifier) {
        AndroidNetworking.delete(session.getServerURL()+"users/posting/lovelike?object_identifier="+identifier)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONDISLIKE");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
                                adapter.notifyDataSetChanged();
                                getListComment(posting_id);
                                countLike(posting_id);
                                status_like = false;
                            }
                        }catch (Exception e){
                            System.out.println("aaa"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("bbb"+error);
                    }
                });
    }

    private void submitLike(final String posting_id) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        SimpleDateFormat commentId = new SimpleDateFormat("yyyyMMddHHmmss");
        String commentIdRes = commentId.format(new Date());

        JSONObject jsonObject = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", session.getUserNIK());
            jsonObject.put("posting_id", posting_id);
            jsonObject.put("lovelike_id", "1");
            jsonObject.put("change_user", session.getUserNIK());
//            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject+"PARAMLIKE");
        AndroidNetworking.post(session.getServerURL()+"users/posting/lovelike")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONLIKE");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
                                adapter.notifyDataSetChanged();
                                getListComment(posting_id);
                                countLike(posting_id);
                                status_like = true;
                            }
                        }catch (Exception e){
                            System.out.println("aaa"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("bbb"+error);
                    }
                });
    }


    private void getListComment(String posting_id){
//        progressDialogHelper.showProgressDialog(DetailpostActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/posting/"+posting_id+"/comment/buscd/"+session.getUserBusinessCode())
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
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<CommentModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                tvCountComment.setText(jsonArray.length()+" Comments");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String posting_id = object.getString("posting_id");
                                    String comment_id = object.getString("comment_id");
                                    String text_comment = object.getString("text_comment");
                                    String date = object.getString("date");
                                    String time = object.getString("time");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    String profile = object.getString("profile");
                                    String full_name = null;
                                    JSONArray nameArray = object.getJSONArray("name");
                                    for (int b=0; b<nameArray.length(); b++) {
                                        JSONObject objName = nameArray.getJSONObject(b);
                                        full_name = objName.getString("full_name");
                                    }
                                    model = new CommentModel(begin_date, end_date, business_code, personal_number, posting_id, comment_id, text_comment, date, time, change_date, change_user, profile,full_name);
                                    listModel.add(model);

                                }
                                adapter = new CommentAdapter(DetailpostActivity.this, listModel);
                                lvComment.setAdapter(adapter);
                                progressDialogHelper.dismissProgressDialog(DetailpostActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(DetailpostActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(DetailpostActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(DetailpostActivity.this);

                        System.out.println(error);
                    }
                });
    }

    private void countLike(String posting_id){
//        progressDialogHelper.showProgressDialog(DetailpostActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/posting/lovelike?business_code="+session.getUserBusinessCode()+"&posting_id="+posting_id)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" RESPONLIKE");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()<1) {
                                    ivLike.setImageResource(R.drawable.like_false);
                                }
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String ide = object.getString("object_identifier");
                                    String pers = object.getString("personal_number");
                                    if (pers.equals(session.getUserNIK())) {
                                        session.setIdentifier(ide);
                                        ivLike.setImageResource(R.drawable.like_true);
                                    } else {
                                        ivLike.setImageResource(R.drawable.like_false);
                                    }
                                }
                                tvCountLike.setText(jsonArray.length()+" Likes");
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(DetailpostActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(DetailpostActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(DetailpostActivity.this);
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
