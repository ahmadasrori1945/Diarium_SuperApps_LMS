package id.co.telkomsigma.Diarium.controller.more.search_partner;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import id.co.telkomsigma.Diarium.adapter.SearchTempAdapter;
import id.co.telkomsigma.Diarium.controller.profile.ProfileActivity;
import id.co.telkomsigma.Diarium.model.SearchTempModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;

public class SearchTempActivity extends AppCompatActivity {

    private EditText mEditText;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<SearchTempModel> listModel;
    private SearchTempModel model;
    private SearchTempAdapter adapter;
    ListView listPartner;
//    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_temp);
        session = new UserSessionManager(this);
        listPartner = (ListView) findViewById(R.id.listView1);
        mEditText = (EditText) findViewById(R.id.editText1);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String param = mEditText.getText().toString();
                getPartner(param);
                    return true;
                }
                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Partner");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getPartner(String param){
        AndroidNetworking.get(session.getServerURL()+"users/search/key/"+param+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"seradkewjekj");
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<SearchTempModel>();
//                                SearchTempModel contacts = new SearchTempModel();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String full_name = object.getString("full_name");
                                    String nickname = object.getString("nickname");
                                    String born_city = object.getString("born_city");
                                    String born_date = object.getString("born_date");
                                    String gender = object.getString("gender");
                                    String religion = object.getString("religion");
                                    String language = object.getString("language");
                                    String national = object.getString("national");
                                    String tribe = object.getString("tribe");
                                    String blood_type = object.getString("blood_type");
                                    String rhesus = object.getString("rhesus");
                                    String marital_status = object.getString("marital_status");
                                    String marital_date = object.getString("marital_date");
                                    String personal_number_reference = object.getString("personal_number_reference");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    String begin_date = object.getString("begin_date");
                                    String job = object.getString("job");
                                    String unit = object.getString("unit");
                                    String posisi = object.getString("posisi");
                                    String profile = object.getString("profile");
                                    if (!personal_number.equals(session.getUserNIK())) {
                                        model = new SearchTempModel(personal_number, full_name, job, unit, posisi, profile);
                                        listModel.add(model);
                                    }
                                    listPartner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(SearchTempActivity.this, ProfileActivity.class);
                                            i.putExtra("personal_number",listModel.get(position).getPersonal_number());
                                            i.putExtra("avatar",listModel.get(position).getProfile());
                                            i.putExtra("job",listModel.get(position).getUnit());
                                            startActivity(i);
                                        }
                                    });
                                }

                                adapter = new SearchTempAdapter(SearchTempActivity.this, listModel);
                                listPartner.setAdapter(adapter);

                            }else{
                                Toast.makeText(SearchTempActivity.this, "no result for "+param, Toast.LENGTH_SHORT).show();
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

}