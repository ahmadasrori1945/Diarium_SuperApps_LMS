package id.co.telkomsigma.Diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.AnswerKuisionerAdapter;
import id.co.telkomsigma.Diarium.adapter.SurveyAnswerAdapter;
import id.co.telkomsigma.Diarium.controller.home.main_menu.survey.QuestionSurveyActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.survey.SurveyActivity;
import id.co.telkomsigma.Diarium.model.AnswerMultipleChoiceModel;
import id.co.telkomsigma.Diarium.model.AnswerQuesionerLMSModel;
import id.co.telkomsigma.Diarium.model.JawabanSurveyModel;
import id.co.telkomsigma.Diarium.model.QuestionSurveyModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;

public class KuisionerLMSActivity extends AppCompatActivity {
    int mSelectedItem = 0;
    int count=0;
    Typeface font,fontbold;
    //NUMBER FOR THE COUNTER
    public int mCount = 100;
    LinearLayout lay_esay, lay_multiple_choice, lay_true_false;
    Button buttonAction;
    int posisi = 0;
    UserSessionManager session;
    JSONArray answer ;
    private List<QuestionSurveyModel> listModel;
    private QuestionSurveyModel model;
    private List<JawabanSurveyModel> listModelJawaban;
    private JawabanSurveyModel modelJawaban;
    private List<AnswerQuesionerLMSModel> listModelAnswer;
    private AnswerQuesionerLMSModel modelAnswer;
    private AnswerKuisionerAdapter adapterPg;
    boolean click = false;
    ArrayList<String> listJawabanEsay;
    private ProgressDialogHelper progressDialogHelper;
    ListView listMultipleChoice;
    EditText etEsay;
    TextView tvPertanyaanKe, tvQuestion;
    String survey_id, survey_name, survey_content;
    JSONArray dat;
    SparseBooleanArray sparseBooleanArray ;
    RadioButton radioTrue, radioFalse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuisioner_lms);
        listModel = new ArrayList<QuestionSurveyModel>();

        answer = new JSONArray();
        listJawabanEsay = new ArrayList<String>();
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        font = Typeface.createFromAsset(KuisionerLMSActivity.this.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(KuisionerLMSActivity.this.getAssets(), "fonts/Nexa Bold.otf");
        tvPertanyaanKe = findViewById(R.id.tvpertanyaanKe);
        tvQuestion = findViewById(R.id.question);
        listMultipleChoice = findViewById(R.id.listMultipleChoice);
        etEsay = findViewById(R.id.etEsay);
        lay_esay = findViewById(R.id.lay_esay);
        lay_multiple_choice = findViewById(R.id.lay_multiple_choice);
        lay_true_false = findViewById(R.id.lay_true_false);
        buttonAction = findViewById(R.id.btnAction);
        radioTrue = findViewById(R.id.radioTrue);
        radioFalse = findViewById(R.id.radioFalse);

        getQuestion();
//        getAnswer();
        lay_multiple_choice.setVisibility(View.GONE);
        lay_true_false.setVisibility(View.GONE);
        lay_esay.setVisibility(View.GONE);

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(posisi+"POSISISAATINI");
                if (session.getCountLMS()<=posisi) {
                    Toast.makeText(KuisionerLMSActivity.this, "Selesai", Toast.LENGTH_SHORT).show();
                } else {
                    tvPertanyaanKe.setText("Test "+(posisi)+" of "+session.getCountLMS());
                    tvQuestion.setText(listModel.get(posisi).getList_question());
                }
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Survey for "+survey_name);
    }

    private void getQuestion(){
        progressDialogHelper.showProgressDialog(KuisionerLMSActivity.this, "Getting data...");
        System.out.println("MASUKPERTANYAANLMS®");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/lmsrelationobject?order[oid]=desc&object[]=1&table_code[]=QUESN&relation[]=Q001&otype[]=TPLCD&per_page=999&begin_date_lte=2019-10-07&end_date_gte=2019-10-07")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONPERTANYAANLMS");
                        try {
                            dat =  response.getJSONArray("data");
                            JSONArray jsonArray = response.getJSONArray("data");
                            String question_text = null, tipe = null;
                            session.setCountLMS(jsonArray.length());
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                question_text = object.getJSONObject("id").getString("quesioner_title");
                                tipe = object.getJSONObject("id").getJSONObject("quesioner_type").getString("id");
                                model = new QuestionSurveyModel("","","","","","","","","","","","",tipe,question_text,String.valueOf(a++));
                                listModel.add(model);
                            }
                            tvPertanyaanKe.setText("Test "+(posisi+1)+" of "+session.getCountLMS());
                            tvQuestion.setText(listModel.get(posisi).getList_question());
                            posisi++;
                            getAnswer(tipe);
                        }catch (Exception e){
                            System.out.println(e);
                        }
                        progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                        System.out.println(error);
                    }
                });
    }


    private void getAnswer(String tipe){
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/quesionerchoice?quesioner[]=1&per_page=999&begin_date_lte=2019-10-07&end_date_gte=2019-10-07")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("RESPONANSWER"+response);
                        try {
                            // tipe "1" = Multiple Choice
                            // tipe "2" = True False
                            // tipe "3" = Esay`
                            if (listModel.get(posisi).getSurvey_type().equals("01")) {
                                listModelAnswer = new ArrayList<AnswerQuesionerLMSModel>();
                                lay_multiple_choice.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String text = object.getString("text_choice");
                                    modelAnswer= new AnswerQuesionerLMSModel("", text);
                                    listModelAnswer.add(modelAnswer);
                                }
                                adapterPg = new AnswerKuisionerAdapter(KuisionerLMSActivity.this, listModelAnswer, mSelectedItem);
                                listMultipleChoice.setAdapter(adapterPg);
                                listMultipleChoice.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        // TODO Auto-generated method stub

                                        SparseBooleanArray checked = listMultipleChoice.getCheckedItemPositions();
                                        ArrayList<String> selectedItems = new ArrayList<String>();

                                        for (int i = 0; i < listModelAnswer.size(); i++) {
                                            if (checked.get(i))
                                                selectedItems.add((String) adapterPg.getItem(i));
                                        }
                                        String[] outputStrArr = new String[selectedItems.size()];

                                        for (int i = 0; i < selectedItems.size(); i++) {
                                            outputStrArr[i] = selectedItems.get(i);
                                            Toast.makeText(KuisionerLMSActivity.this, outputStrArr[i], Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else if (listModel.get(posisi).getSurvey_type().equals("02")) {
                                lay_true_false.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String text = object.getString("text_choice");
                                    modelAnswer= new AnswerQuesionerLMSModel("", text);
                                    listModelAnswer.add(modelAnswer);
                                }
                                radioTrue.setText(listModelAnswer.get(0).getAnswer_text());
                                radioFalse.setText(listModelAnswer.get(1).getAnswer_text());
                            } else {
                                lay_esay.setVisibility(View.VISIBLE);
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


    private void submitSurvey() {
        progressDialogHelper.showProgressDialog(KuisionerLMSActivity.this, "Submit data...");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

//        JSONObject jResult = new JSONObject();// main object
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
        System.out.println(listModelJawaban.size()+"neje4nk4jrn");
        for (int i = 0; i < listModelJawaban.size(); i++) {
            JSONObject jGroup = new JSONObject();// /sub Object
            try {
                jGroup.put("begin_date", tRes);
                jGroup.put("end_date", "9999-12-31");
                jGroup.put("business_code", session.getUserBusinessCode());
                jGroup.put("participant", session.getUserNIK());
                jGroup.put("quesioner", survey_id);
                jGroup.put("relation_quesioner", listModelJawaban.get(i).getQuestion_id());
                jGroup.put("text_choice", listModelJawaban.get(i).getAnswer_id());
                jArray.put(jGroup);
                // /itemDetail Name is JsonArray Name
//                jResult.put("activity", jArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        System.out.println(jArray + "ARRAYSURVEYNYA");
        AndroidNetworking.post("https://testapi.digitalevent.id/lms/api/quesionerparticipantchoice")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONArrayBody(jArray)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONSUBMITSURVEY");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(KuisionerLMSActivity.this, "Success send this survey !", Toast.LENGTH_SHORT).show();
                                Intent a = new Intent(KuisionerLMSActivity.this, SurveyActivity.class);
                                startActivity(a);
                                finish();
                            } else {
                                Toast.makeText(KuisionerLMSActivity.this, "Error send survey", Toast.LENGTH_SHORT).show();
                            }
                            progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                        }catch (Exception e){
                            System.out.println(e);
                            progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                        progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                    }
                });
    }

}
