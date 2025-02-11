package id.co.telkomsigma.Diarium.controller.home.main_menu.today_activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import id.co.telkomsigma.Diarium.adapter.TodayActivityListAdapter;
import id.co.telkomsigma.Diarium.model.TodayActivityListModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_listFragment extends Fragment {

    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<TodayActivityListModel> listModel;
    private TodayActivityListModel model;
    private TodayActivityListAdapter adapter;
    TextView tvNull;
    ListView listActivity;
//    TextView tvNull;

    Typeface font,fontbold;
    public Activity_listFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activitylist, container, false);
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(getActivity());
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        listActivity = view.findViewById(R.id.listActivity);
        tvNull = view.findViewById(R.id.tvNull);
        getTodayActivityList(session.getTodayActivity());
        System.out.println(session.getTempPers()+"nekr");
//        Toast.makeText(getActivity(), "Param aktifitas : "+session.getTodayActivity(), Toast.LENGTH_SHORT).show();
        System.out.println(session.getStatus()+"knewjlrn");
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getTodayActivityList(session.getTodayActivity());
    }

    private void getTodayActivityList(final String date){
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/todayActivity/"+date+"/nik/"+session.getTempPers()+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"kjwrhbk4jrEvent"+date);
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                JSONArray jsonArray = response.getJSONArray("data");

                                    listActivity.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    System.out.println(jsonArray.length()+"wdefrefw");
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String full_name = object.getString("full_name");
                                        String personal_number_aktivitas = object.getString("personal_number");
                                        JSONArray jsonArrayTodayActivity = object.getJSONArray("today_activity");
                                        System.out.println(jsonArrayTodayActivity.length()+"dlj3nr432");
                                        if (jsonArrayTodayActivity.length()==0) {
                                            listActivity.setVisibility(View.GONE);
                                            tvNull.setVisibility(View.VISIBLE);
                                        } else {
                                        listModel = new ArrayList<TodayActivityListModel>();
                                        for (int b = 0; b < jsonArrayTodayActivity.length(); b++) {
                                            JSONObject objectTodayActivity = jsonArrayTodayActivity.getJSONObject(b);
                                            String activity_id = objectTodayActivity.getString("activity_id");
                                            String activity_type = objectTodayActivity.getString("activity_type");
                                            String activity_title = objectTodayActivity.getString("activity_title");
                                            String activity_start = objectTodayActivity.getString("activity_start");
                                            String activity_finish = objectTodayActivity.getString("activity_finish");
                                            String approval_status = objectTodayActivity.getString("approval_status");
                                            model = new TodayActivityListModel(full_name, personal_number_aktivitas, activity_id, activity_type, activity_title, activity_start, activity_finish, approval_status);
                                            listModel.add(model);
                                            System.out.println(model.getActivity_title()+"dl2j3nr3");
                                        }
                                            System.out.println(listModel.get(0).getActivity_title()+"6666666");
                                            adapter = new TodayActivityListAdapter(getActivity(), listModel);
                                            listActivity.setAdapter(adapter);
                                            listActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                                                    if (session.getStatus().equals("chief") && listModel.get(position).getApproval_status().equals("0")) {
//                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                        builder.setTitle("Confirm");
//                                                        builder.setMessage("Are you sure to approve this activity ?");
//                                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                // Do nothing but close the dialog
//                                                                String full_name = listModel.get(position).getFull_name();
//                                                                String personal_number_aktivitas = listModel.get(position).getPersonal_number();
//                                                                String activity_id = listModel.get(position).getActivity_id();
//                                                                String activity_type = listModel.get(position).getActivity_type();
//                                                                String activity_title = listModel.get(position).getActivity_title();
//                                                                String activity_start = listModel.get(position).getActivity_start();
//                                                                String activity_finish = listModel.get(position).getActivity_finish();
//                                                                String approval_status = listModel.get(position).getApproval_status();
//                                                                submitTodayActivity(full_name, personal_number_aktivitas, activity_id, activity_type, activity_title, activity_start, activity_finish, approval_status);
//                                                            }
//                                                        });
//
//                                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int which) {
//
//                                                                // Do nothing
//                                                                dialog.dismiss();
//                                                            }
//                                                        });
//
//                                                        AlertDialog alert = builder.create();
//                                                        alert.show();
//                                                    } else {
                                                        String full_name = listModel.get(position).getFull_name();
                                                        String personal_number_aktivitas = listModel.get(position).getPersonal_number();
                                                        String activity_id = listModel.get(position).getActivity_id();
                                                        String activity_type = listModel.get(position).getActivity_type();
                                                        String activity_title = listModel.get(position).getActivity_title();
                                                        String activity_start = listModel.get(position).getActivity_start();
                                                        String activity_finish = listModel.get(position).getActivity_finish();
                                                        String approval_status = listModel.get(position).getApproval_status();
                                                        popupMenuActivity(full_name, personal_number_aktivitas, activity_id, activity_type, activity_title, activity_start, activity_finish, approval_status);
//                                                }
                                                }
                                            });
                                        }
                                    progressDialogHelper.dismissProgressDialog(getActivity());
                                }
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }
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

    private void popupMenuActivity(final String full_name, final String personal_number_aktivitas, final String activity_id, final String activity_type, final String activity_title, final String activity_start, final String activity_finish, final String approval_status) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_activity);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnChange =(Button) dialog.findViewById(R.id.btnChange);
        Button btnDelete =(Button) dialog.findViewById(R.id.btnDelete);
        TextView tvTitle =(TextView) dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("'"+activity_title+"'");
        dialog.show();
        dialog.setCancelable(true);
        if (activity_type.equals("03")) {
            btnDelete.setVisibility(View.GONE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure to delete this activity ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            submitDelete(activity_id);
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
                    dialog.dismiss();
                }
            });
        }
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CommentActivity.class);
                i.putExtra("full_name", full_name);
                i.putExtra("personal_number", personal_number_aktivitas);
                i.putExtra("activity_id", activity_id);
                i.putExtra("activity_type", activity_type);
                i.putExtra("activity_title", activity_title);
                i.putExtra("activity_start", activity_start);
                i.putExtra("activity_finish", activity_finish);
                i.putExtra("approval_status", approval_status);
                startActivity(i);
                dialog.dismiss();
            }
        });
    }

    private void submitDelete(String id_get) {
        System.out.println("ketikamasukdelete"+id_get);
        AndroidNetworking.post(session.getServerURL()+"users/deleteActivity/actid/"+id_get+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"HASILDELETENYA");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(getActivity(), "Success delete activity", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                getTodayActivityList(session.getTodayActivity());
                            } else {
                                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
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

    private void submitTodayActivity(String full_name, String personal_number_aktivitas, String activity_id, String activity_type, String activity_title, String activity_start, String activity_finish, String status_approve) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jResult = new JSONObject();// main object
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
        JSONObject jGroup = new JSONObject();// /sub Object
        try {
            jGroup.put("begin_date", tRes);
            jGroup.put("end_date", "9999-12-31");
            jGroup.put("business_code", session.getUserBusinessCode());
            jGroup.put("personal_number", session.getTempPers());
            jGroup.put("activity_id", activity_id);
            jGroup.put("activity_type", activity_type);
            jGroup.put("activity_title", activity_title);
            jGroup.put("activity_start", activity_start);
            jGroup.put("activity_finish", activity_finish);
            jGroup.put("approval_status", "1");
            jGroup.put("change_user", session.getUserNIK());
            jArray.put(jGroup);
            // /itemDetail Name is JsonArray Name
            jResult.put("activity", jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jArray + "3nkej3n3eAktivitas");
        AndroidNetworking.post(session.getServerURL()+"users/activity/"+activity_id+"/nik/"+session.getTempPers()+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"edknwkjrAktivitas");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(getActivity(), "Success approve activity", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                getTodayActivityList(session.getTodayActivity());
                            }else {
                                Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
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
