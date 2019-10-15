package id.co.telkomsigma.Diarium.controller.home.main_menu.mydevelopment.myknowledge;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.KnowledgeAdapter;
import id.co.telkomsigma.Diarium.adapter.TrainingAdapter;
import id.co.telkomsigma.Diarium.controller.home.main_menu.mydevelopment.mytraining.EventSessionActivity;
import id.co.telkomsigma.Diarium.model.KnowledgeModel;
import id.co.telkomsigma.Diarium.model.TrainingModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularContentFragment extends Fragment {
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<KnowledgeModel> listModel;
    private KnowledgeModel model;
    private KnowledgeAdapter adapter;
    TextView tvNull;
    ListView listActivity;
    Typeface font,fontbold;


    public PopularContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_content, container, false);
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(getActivity());
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        listActivity = view.findViewById(R.id.listOnGoing);
        tvNull = view.findViewById(R.id.tvNull);
        getTodayActivityList();
        return view;
    }

    private void getTodayActivityList(){
        System.out.println("MASUKTRAINING");
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/materi?order[BEGDA]=asc")
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
                        System.out.println("RESPONTRAINING"+response);
                        try {
                            listModel = new ArrayList<KnowledgeModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNull.setVisibility(View.VISIBLE);
                                listActivity.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                listActivity.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String bussines_code = object.getString("bussines_code");
                                    String batch = object.getString("batch");
                                    String batch_name = object.getString("batch_name");
                                    String event_id = object.getString("event_id");
                                    String event_name = object.getString("event_name");
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String event_curr_stat = object.getString("event_curr_stat");
                                    String evnt_curr_statid = object.getString("evnt_curr_statid");
                                    String event_status = object.getString("event_status");
                                    String event_stat_id = object.getString("event_stat_id");
                                    String location_id = object.getString("location_id");
                                    String location = object.getString("location");
                                    String cur_id = object.getString("cur_id");
                                    String curriculum = object.getString("curriculum");
                                    String event_type = object.getString("event_type");
                                    String participant_id = object.getString("participant_id");
                                    String partcipant_name = object.getString("partcipant_name");
                                    String parti_nicknm = object.getString("parti_nicknm");
                                    String company_name = object.getString("company_name");
                                    model = new KnowledgeModel("","","","","","","","","","","", "");
                                    listModel.add(model);
                                }
                                adapter = new KnowledgeAdapter(getActivity(), listModel);
                                listActivity.setAdapter(adapter);
                                listActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Intent i = new Intent(getActivity(), EventSessionActivity.class);
//                                        i.putExtra("bussines_code",listModel.get(position).getBussines_code());
//                                        i.putExtra("batch",listModel.get(position).getBatch());
//                                        i.putExtra("batch_name",listModel.get(position).getBatch_name());
//                                        i.putExtra("event_id",listModel.get(position).getEvent_id());
//                                        i.putExtra("event_name",listModel.get(position).getEvent_name());
//                                        i.putExtra("begin_date",listModel.get(position).getBegin_date());
//                                        i.putExtra("end_date",listModel.get(position).getEnd_date());
//                                        i.putExtra("event_curr_stat",listModel.get(position).getEvent_curr_stat());
//                                        i.putExtra("event_status",listModel.get(position).getEvent_status());
//                                        i.putExtra("event_stat_id",listModel.get(position).getEvent_stat_id());
//                                        i.putExtra("location_id",listModel.get(position).getLocation_id());
//                                        i.putExtra("location",listModel.get(position).getLocation());
//                                        i.putExtra("cur_id",listModel.get(position).getCur_id());
//                                        i.putExtra("curriculum",listModel.get(position).getCurriculum());
//                                        i.putExtra("event_type",listModel.get(position).getEvent_type());
//                                        i.putExtra("participant_id",listModel.get(position).getParticipant_id());
//                                        i.putExtra("partcipant_name",listModel.get(position).getPartcipant_name());
//                                        i.putExtra("parti_nicknm",listModel.get(position).getParti_nicknm());
//                                        i.putExtra("company_name",listModel.get(position).getCompany_name());
                                        startActivity(i);
                                    }
                                });
                            }
                            progressDialogHelper.dismissProgressDialog(getActivity());
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(getActivity());
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(getActivity());
                        System.out.println(error);
                    }
                });
    }
}
