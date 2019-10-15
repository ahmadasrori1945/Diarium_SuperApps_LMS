package id.co.telkomsigma.Diarium.controller.inbox;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.InboxAdapter;
import id.co.telkomsigma.Diarium.model.InboxModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;


public class InboxActivity extends AppCompatActivity {

    Typeface font,fontbold;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<InboxModel> listModel;
    private InboxModel model;
    private InboxAdapter adapter;
    ListView listInbox;
    private TextView tvNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        session = new UserSessionManager(InboxActivity.this);
        progressDialogHelper = new ProgressDialogHelper();

        font = Typeface.createFromAsset(InboxActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(InboxActivity.this.getAssets(),"fonts/Nexa Bold.otf");
        tvNull = findViewById(R.id.tvNull);
        listInbox = findViewById(R.id.list_inbox);

        getInbox();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void getInbox(){
        progressDialogHelper.showProgressDialog(InboxActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/inbox/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"ktjbkgrjbhy");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<InboxModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listInbox.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listInbox.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String inbox_id = object.getString("inbox_id");
                                        String title = object.getString("title");
                                        String description = object.getString("description");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        model = new InboxModel(begin_date, end_date, business_code, personal_number, inbox_id, title, description, change_date, change_user);
                                        listModel.add(model);
                                    }
                                    adapter = new InboxAdapter(InboxActivity.this, listModel);
                                    listInbox.setAdapter(adapter);
                                    listInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                            String item = (String) listInbox.getItemAtPosition(position);
                                            Toast.makeText(InboxActivity.this,"You selected : " + item, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    listInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(InboxActivity.this, DetailInboxActivity.class);
                                            i.putExtra("title",listModel.get(position).getTitle());
                                            i.putExtra("date",listModel.get(position).getChange_date());
                                            i.putExtra("desc",listModel.get(position).getDescription());
                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(InboxActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(InboxActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(InboxActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(InboxActivity.this);

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

