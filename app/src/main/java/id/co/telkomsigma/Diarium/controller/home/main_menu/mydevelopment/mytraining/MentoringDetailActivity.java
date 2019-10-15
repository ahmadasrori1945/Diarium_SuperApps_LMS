package id.co.telkomsigma.Diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.DetailMentoringAdapter;
import id.co.telkomsigma.Diarium.adapter.ForumCommentAdapter;
import id.co.telkomsigma.Diarium.adapter.ViewPagerAdapter;
import id.co.telkomsigma.Diarium.controller.home.main_menu.today_activity.AddTodayActActivity;
import id.co.telkomsigma.Diarium.controller.profile.DetailpostActivity;
import id.co.telkomsigma.Diarium.model.DetailMentoringModel;
import id.co.telkomsigma.Diarium.model.ForumCommentModel;
import id.co.telkomsigma.Diarium.util.TimeHelper;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;

public class MentoringDetailActivity extends AppCompatActivity {

    String  bussines_code;
    String  batch;
    String  batch_name;
    String  event_id;
    String  event_name;
    String  begin_date;
    String  end_date;
    String  event_curr_stat;
    String  evnt_curr_statid;
    String  event_status;
    String  event_stat_id;
    String  location_id;
    String  location;
    String  cur_id;
    String  curriculum;
    String  event_type;
    String  participant_id;
    String  partcipant_name;
    String  parti_nicknm;
    String  company_name;
    String  activity_name;
    String  session_name;
    String  begin_date_activity;
    String  end_date_activity;
    String  title;
    String  topic;
    String  description;
    TextView tvEvent, tvBatch, tvLocation, tvType, tvCurriculum, tvDate, tvActivitName, tvSessionName, tvDateActivity, tvCompany;
    TextView tvTitle, tvTopic, tvDescription, tvMentor;
    Button btnUpload;
    ListView lvComment;
    TimeHelper timeHelper;
    Typeface font,fontbold;
    UserSessionManager session;
    private List<DetailMentoringModel> listModel;
    private DetailMentoringModel model;
    private DetailMentoringAdapter adapter;
    private ProgressDialogHelper progressDialogHelper;
    EditText etComment;
    TextView tvPost;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentoring_detail);
        progressDialogHelper = new ProgressDialogHelper();
        timeHelper = new TimeHelper();
        font = Typeface.createFromAsset(MentoringDetailActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(MentoringDetailActivity.this.getAssets(),"fonts/Nexa Bold.otf");
        tvEvent = findViewById(R.id.tvEvent);
        tvBatch = findViewById(R.id.tvBatch);
        tvLocation = findViewById(R.id.tvLocation);
        tvType = findViewById(R.id.tvType);
        tvCurriculum = findViewById(R.id.tvCurriculum);
        tvDate = findViewById(R.id.tvDate);
        tvActivitName = findViewById(R.id.tvActivityName);
        tvSessionName = findViewById(R.id.tvSessionName);
        tvDateActivity = findViewById(R.id.tvDateActivity);
        tvCompany = findViewById(R.id.tvCompany);
        session = new UserSessionManager(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvTopic = findViewById(R.id.tvTopic);
        tvDescription = findViewById(R.id.tvDescription);
        tvMentor = findViewById(R.id.tvMentor);
        btnUpload = findViewById(R.id.btnUpload);
        lvComment = findViewById(R.id.lvComment);
        etComment = findViewById(R.id.etComment);
        tvPost = findViewById(R.id.tvPost);

        Intent intent = getIntent();
        bussines_code = intent.getStringExtra("bussines_code");
        batch = intent.getStringExtra("batch");
        batch_name = intent.getStringExtra("batch_name");
        event_id = intent.getStringExtra("event_id");
        event_name = intent.getStringExtra("event_name");
        begin_date = intent.getStringExtra("begin_date");
        end_date = intent.getStringExtra("end_date");
        event_curr_stat = intent.getStringExtra("event_curr_stat");
        evnt_curr_statid = intent.getStringExtra("evnt_curr_statid");
        event_status = intent.getStringExtra("event_status");
        event_stat_id = intent.getStringExtra("event_stat_id");
        location_id = intent.getStringExtra("location_id");
        location = intent.getStringExtra("location");
        cur_id = intent.getStringExtra("cur_id");
        curriculum = intent.getStringExtra("curriculum");
        event_type = intent.getStringExtra("event_type");
        participant_id = intent.getStringExtra("participant_id");
        partcipant_name = intent.getStringExtra("partcipant_name");
        parti_nicknm = intent.getStringExtra("parti_nicknm");
        company_name = intent.getStringExtra("company_name");

        activity_name = intent.getStringExtra("activity_name");
        session_name = intent.getStringExtra("session_name");
        begin_date_activity = intent.getStringExtra("begin_date_activity");
        end_date_activity = intent.getStringExtra("end_date_activity");

        title = intent.getStringExtra("title");
        topic = intent.getStringExtra("topic");
        description = intent.getStringExtra("description");

        session.setactivity_nameLMS(activity_name);
        session.setsession_nameLMS(session_name);
        session.setbegin_date_activityLMS(begin_date_activity);
        session.setend_date_activityLMS(end_date_activity);

        tvEvent.setText(event_name);
        tvBatch.setText(batch+ " "+ batch_name);
        tvLocation.setText(location);
        tvType.setText(event_type+" Event");
        tvDate.setText(timeHelper.getTimeFormat(begin_date)+" - "+timeHelper.getTimeFormat(end_date));
        tvEvent.setText(event_name);
        tvCompany.setText(company_name+" - "+location_id);
        tvCurriculum.setText(curriculum);

        tvActivitName.setText(activity_name);
        tvSessionName.setText(session_name);
        tvDateActivity.setText(timeHelper.getTimeFormat(begin_date_activity)+" - "+timeHelper.getTimeFormat(end_date_activity));

        tvMentor.setText("Mentor : "+ "Mentor 1");
        tvTitle.setText("Title : "+ title);
        tvTopic.setText("Topic : "+ topic);
        tvDescription.setText("Description : "+ description);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MentoringDetailActivity.this, "Upload Success!", Toast.LENGTH_SHORT).show();
            }
        });
        etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etComment.getText().toString();
                if (text.isEmpty() || text.equals("")||text.length()==0) {
                    Toast.makeText(MentoringDetailActivity.this, "Plase input comment first !", Toast.LENGTH_SHORT).show();
                } else {
                    submitTextMentoring(text);
                }
            }
        });
        getListComment();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mentoring Detail");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void submitTextMentoring(String text) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jamParam = new SimpleDateFormat("yyyMMddHHmmss");
        String jamResParam = jamParam.format(new Date());

        JSONObject obj = new JSONObject();// /sub Object

        try {
            obj.put("business_code", session.getUserBusinessCode());
            obj.put("mentoring", "5");
            obj.put("otype", "TEXT");
            obj.put("sender", "2");
            obj.put("sender_type", "PART1");
            obj.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(obj + "ARRAYMENTORING");
        AndroidNetworking.post("https://testapi.digitalevent.id/lms/api/mentoringchat")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(obj)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONSENDCHAT");
                        try {
                                Toast.makeText(MentoringDetailActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            getListComment();

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

    private void getListComment(){
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/mentoringchat?mentoring_id=5")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("COMMENTRESPONSE"+response);
                        // do anything with response
                        try {
                            listModel = new ArrayList<DetailMentoringModel>();
                            for (int a = 0; a < response.length(); a++) {
                                JSONObject object = response.getJSONObject(a);
                                String begin_date = object.getString("begin_date");
                                String chat_text = object.getString("chat_text");
                                String sender_name = object.getString("sender_name");
                                model = new DetailMentoringModel(begin_date,chat_text,sender_name);
                                listModel.add(model);
                            }
                            adapter = new DetailMentoringAdapter(MentoringDetailActivity.this, listModel);
                            lvComment.setAdapter(adapter);
                            progressDialogHelper.dismissProgressDialog(MentoringDetailActivity.this);

                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(MentoringDetailActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(MentoringDetailActivity.this);

                        System.out.println(error);
                    }
                });
    }



}
