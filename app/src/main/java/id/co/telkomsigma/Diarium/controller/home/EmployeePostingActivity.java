package id.co.telkomsigma.Diarium.controller.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.MyPostingAdapter;
import id.co.telkomsigma.Diarium.controller.profile.DetailpostActivity;
import id.co.telkomsigma.Diarium.model.MyPostingModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;

public class EmployeePostingActivity extends AppCompatActivity {
    ListView listMyPosting;
    private ProgressDialogHelper progressDialogHelper;
    private List<MyPostingModel> listModel;
    private MyPostingModel model;
    private MyPostingAdapter adapter;
    UserSessionManager session;
    String personal_numbe_paramr;
    TextView tvNullPost, tvCountFriend, tvCountCommunity, tvName, tvNIK, tvJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_posting);
        Intent intent = getIntent();
        personal_numbe_paramr = intent.getStringExtra("personal_number");
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(this);
        listMyPosting = findViewById(R.id.listMyPosting);
        tvNullPost = (TextView) findViewById(R.id.tvNullPost);
        getMyPost(session.getUserNIK(), session.getUserFullName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Employee Posting");
    }

    @Override
    public void onResume(){
        super.onResume();
        getMyPost(session.getUserNIK(), session.getUserFullName());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getMyPost(String nik, String name){
        progressDialogHelper.showProgressDialog(EmployeePostingActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/timelineDetailPosting/limit/1000/nik/"+nik+"/buscd/"+session.getUserBusinessCode())
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
                                        String full_name = null;
                                        JSONArray jsonArrayName = object.getJSONArray("name");
                                        for (int d=0; d<jsonArrayName.length(); d++) {
                                            JSONObject objectName = jsonArrayName.getJSONObject(d);
                                            full_name = objectName.getString("full_name");
                                        }
                                        JSONArray jsonArrayComment = object.getJSONArray("comments");
                                        JSONArray jsonArrayLike = object.getJSONArray("people_likes");
                                        boolean status_like = false;
                                        for (int c=0; c<jsonArrayLike.length(); c++) {
                                            JSONObject objectLike = jsonArrayLike.getJSONObject(c);
                                            String personal_number_like = objectLike.getString("personal_number");
                                            if (personal_number_like.equals(session.getUserNIK())) {
                                                status_like = true;
                                            } else {
                                                status_like = false;
                                            }
                                        }
                                        model = new MyPostingModel(begin_date, end_date, business_code, personal_number, posting_id, title, description, image, date, time, change_date, change_user, String.valueOf(jsonArrayComment.length()), String.valueOf(jsonArrayLike.length()), full_name, status_like, avatar);
                                        listModel.add(model);
                                    }
                                    adapter = new MyPostingAdapter(EmployeePostingActivity.this, listModel);
                                    listMyPosting.setAdapter(adapter);
                                    listMyPosting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        generateActivityId();
                                            Intent i = new Intent(EmployeePostingActivity.this, DetailpostActivity.class);
                                            i.putExtra("posting_id",listModel.get(position).getPosting_id());
                                            i.putExtra("title",listModel.get(position).getTitle());
                                            i.putExtra("description",listModel.get(position).getDescription());
                                            i.putExtra("date",listModel.get(position).getChange_date());
                                            i.putExtra("image",listModel.get(position).getImage());
                                            i.putExtra("name",listModel.get(position).getName());
                                            i.putExtra("status_like",listModel.get(position).isStatus_like());
                                            i.putExtra("avatar",listModel.get(position).getAvatar());
                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(EmployeePostingActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(EmployeePostingActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(EmployeePostingActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(EmployeePostingActivity.this);

                        System.out.println(error);
                    }
                });
    }
}
