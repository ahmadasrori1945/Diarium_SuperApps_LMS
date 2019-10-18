package id.co.telkomsigma.Diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import id.co.telkomsigma.Diarium.controller.LoginActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.survey.QuestionSurveyActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.survey.SurveyActivity;
import id.co.telkomsigma.Diarium.controller.profile.ProfileActivity;
import id.co.telkomsigma.Diarium.model.AnswerMultipleChoiceModel;
import id.co.telkomsigma.Diarium.model.AnswerQuesionerLMSModel;
import id.co.telkomsigma.Diarium.model.JawabanSurveyModel;
import id.co.telkomsigma.Diarium.model.KuisionerLMSModel;
import id.co.telkomsigma.Diarium.model.KuisionerModel;
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
    private List<KuisionerLMSModel> listModel;
    private KuisionerLMSModel model;

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
    TextView tvPertanyaanKe, tvText, tvTitle;
    String survey_id, survey_name, survey_content;
//    JSONArray dat;
    SparseBooleanArray sparseBooleanArray ;
    RadioButton radioTrue, radioFalse, radioButton;
    RadioGroup rgTF;
    String jawaban = "aaa";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuisioner_lms);
        listModel = new ArrayList<KuisionerLMSModel>();

        answer = new JSONArray();
        listJawabanEsay = new ArrayList<String>();
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        font = Typeface.createFromAsset(KuisionerLMSActivity.this.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(KuisionerLMSActivity.this.getAssets(), "fonts/Nexa Bold.otf");
        tvPertanyaanKe = findViewById(R.id.tvpertanyaanKe);
        tvText = findViewById(R.id.tvText);
        tvTitle = findViewById(R.id.tvTitle);
        listMultipleChoice = findViewById(R.id.listMultipleChoice);
        etEsay = findViewById(R.id.etEsay);
        lay_esay = findViewById(R.id.lay_esay);
        lay_multiple_choice = findViewById(R.id.lay_multiple_choice);
        lay_true_false = findViewById(R.id.lay_true_false);
        buttonAction = findViewById(R.id.btnAction);
        radioTrue = findViewById(R.id.radioTrue);
        radioFalse = findViewById(R.id.radioFalse);
        rgTF = findViewById(R.id.radioGroupTF);
        listModelAnswer = new ArrayList<AnswerQuesionerLMSModel>();
        getQuestion();
//        getAnswer();
        lay_multiple_choice.setVisibility(View.GONE);
        lay_true_false.setVisibility(View.GONE);
        lay_esay.setVisibility(View.GONE);
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(posisi+"POSISISAATINISETELAHCLICK"+session.getCountLMS());
                if (posisi+1==session.getCountLMS()) {
                    if (listModel.get(posisi).getQuesioner_type_value().equals("01")) {
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
//                        Toast.makeText(KuisionerLMSActivity.this, "Selesai Multiple Choice", Toast.LENGTH_SHORT).show();
//                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"","","kelar");
                    } else if (listModel.get(posisi).getQuesioner_type_value().equals("02")) {
                        int selectedId = rgTF.getCheckedRadioButtonId();
                        RadioButton radioSexButton = findViewById(selectedId);
                        String txt = String.valueOf(radioSexButton.getText());
//                        Toast.makeText(KuisionerLMSActivity.this, txt, Toast.LENGTH_SHORT).show();
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",txt,"kelar");
                    } else {
                        String text_answer = etEsay.getText().toString();
//                        Toast.makeText(KuisionerLMSActivity.this, "Selesai"+text_answer, Toast.LENGTH_SHORT).show();
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",text_answer,"kelar");
                    }
                } else {
                    if (listModel.get(posisi).getQuesioner_type_value().equals("01")) {
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
//                        Toast.makeText(KuisionerLMSActivity.this, "Multiple Choice", Toast.LENGTH_SHORT).show();
//                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"","","b");
                    } else if (listModel.get(posisi).getQuesioner_type_value().equals("02")) {
                        int selectedId = rgTF.getCheckedRadioButtonId();
                        RadioButton radioSexButton = findViewById(selectedId);
                        String txt = String.valueOf(radioSexButton.getText());
//                        Toast.makeText(KuisionerLMSActivity.this, txt, Toast.LENGTH_SHORT).show();
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",txt,"b");
                    } else {
                        String text_answer = etEsay.getText().toString();
//                        Toast.makeText(KuisionerLMSActivity.this, text_answer, Toast.LENGTH_SHORT).show();
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",text_answer,"b");
                    }
                    posisi++;
                    tvPertanyaanKe.setText("Test "+(posisi+1)+" of "+session.getCountLMS());
                    tvText.setText(listModel.get(posisi).getQuesioner_text());
                    tvTitle.setText(listModel.get(posisi).getQuesioner_title());
                    getAnswer(listModel.get(posisi).getQuesioner_type_value());
                }
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Survey");
    }

    private void getQuestion(){
        progressDialogHelper.showProgressDialog(KuisionerLMSActivity.this, "Getting data...");
        System.out.println("MASUKPERTANYAANLMS®");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/lmsrelationobject?order[oid]=desc&object[]=1&table_code[]=QUESN&relation[]=Q001&otype[]=TPLCD&per_page=999&begin_date_lte=2019-10-17&end_date_gte=2019-10-17")
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
//                            dat =  response.getJSONArray("data");
                            JSONArray jsonArray = response.getJSONArray("data");
                            String quesioner_id = null,quesioner_text = null, quesioner_title = null, tipe = null;
                            System.out.println("PANJANGDATA"+jsonArray.length());
                            session.setCountLMS(jsonArray.length());
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                quesioner_id = object.getJSONObject("id").getString("quesioner_id");
                                quesioner_text = object.getJSONObject("id").getString("quesioner_text");
                                quesioner_title = object.getJSONObject("id").getString("quesioner_title");
                                tipe = object.getJSONObject("id").getJSONObject("quesioner_type").getString("id");
                                model = new KuisionerLMSModel(quesioner_id,quesioner_text,quesioner_title,tipe);
                                listModel.add(model);
                            }
                            tvPertanyaanKe.setText("Test "+(posisi+1)+" of "+session.getCountLMS());
                            tvText.setText(listModel.get(posisi).getQuesioner_text());
                            tvTitle.setText(listModel.get(posisi).getQuesioner_title());
                            getAnswer(listModel.get(posisi).getQuesioner_type_value());
//                            posisi++;
                            System.out.println(posisi+"POSISISAATINISEBELUMCLICK"+session.getCountLMS());
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
        progressDialogHelper.showProgressDialog(KuisionerLMSActivity.this, "Getting data...");
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
                            // tipe "01" = Multiple Choice
                            // tipe "02" = True False
                            // tipe "03" = Esay`
                            if (tipe.equals("01")) {
                                lay_multiple_choice.setVisibility(View.VISIBLE);
                                lay_esay.setVisibility(View.GONE);
                                lay_true_false.setVisibility(View.GONE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String text = object.getString("text_choice");
                                    modelAnswer= new AnswerQuesionerLMSModel("", text);
                                    listModelAnswer.add(modelAnswer);
                                }
                                adapterPg = new AnswerKuisionerAdapter(KuisionerLMSActivity.this, listModelAnswer, mSelectedItem);
                                listMultipleChoice.setAdapter(adapterPg);
//                                listMultipleChoice.setOnItemClickListener(new AdapterView.OnItemClickListener()
//                                {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        // TODO Auto-generated method stub
//
//                                    }
//                                });

                            } else if (tipe.equals("02")) {
                                lay_multiple_choice.setVisibility(View.GONE);
                                lay_esay.setVisibility(View.GONE);
                                lay_true_false.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String text = object.getString("text_choice");
                                    modelAnswer= new AnswerQuesionerLMSModel("", text);
                                    listModelAnswer.add(modelAnswer);
                                }

                                rgTF.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                                        RadioButton rb= findViewById(checkedId);
                                        jawaban = rb.getText().toString();
                                    }
                                });

//                                rgTF.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                    @Override
//                                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                                        switch(i){
//                                            case R.id.radioTrue:
//                                                jawaban = "True";
//                                                Toast.makeText(KuisionerLMSActivity.this, jawaban, Toast.LENGTH_SHORT).show();
//                                                break;
//                                            case R.id.radioFalse:
//                                                jawaban = "False";
//                                                Toast.makeText(KuisionerLMSActivity.this, jawaban, Toast.LENGTH_SHORT).show();
//                                                break;
//                                        }
//                                    }
//                                });
                            } else {
                                lay_multiple_choice.setVisibility(View.GONE);
                                lay_true_false.setVisibility(View.GONE);
                                lay_esay.setVisibility(View.VISIBLE);
                                jawaban = etEsay.getText().toString();
//                                session.setJawaban(jawaban);
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

    private void submitKuisioner(String quesioner, String relation_quesioner, String text_choice, String kondisi) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        JSONObject jGroup = new JSONObject();// /sub Object
        try {
            jGroup.put("begin_date", tRes);
            jGroup.put("end_date", "9999-12-31");
            jGroup.put("business_code", session.getUserBusinessCode());
            jGroup.put("participant", "2");
            jGroup.put("quesioner", quesioner);
            jGroup.put("relation_quesioner", relation_quesioner);
            jGroup.put("text_choice", text_choice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jGroup + "ARRAYPOST");
        AndroidNetworking.post("https://testapi.digitalevent.id/lms/api/quesionerparticipantchoice")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                .addJSONObjectBody(jGroup)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONPOST : "+text_choice);
                        if (kondisi.equals("kelar")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(KuisionerLMSActivity.this);

//                            builder.setTitle("Confirm");
                            builder.setMessage("Terima kasih telah mengisi kuesioner ini !");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

//                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    // Do nothing
//                                    dialog.dismiss();
//                                }
//                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

}
