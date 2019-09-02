package id.co.telkomsigma.Diarium.controller.notification;


import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.NotificationAdapter;
import id.co.telkomsigma.Diarium.model.NotifModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;

//import id.co.telkomsigma.Diarium.adapter.NotifikasiAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotifikasiFragment extends Fragment {


    String[] nama={
            "Dian Fitriana Dewi",
            "Nurannisa Nasution",
            "Muhammad Hasbi",
    };
    String[] notifikasi={
            "Started Following You",
            "Started Following You",
            "Started Following You",
    };
    String[] date= {
        "20 days ago",
            "15 days ago",
            "10 days ago"
    };

    UserSessionManager session;
    private List<NotifModel> listModel;
    private NotifModel model;
    private NotificationAdapter adapter;
    private ListView listFriendsRequest;
    private TextView tvNull;
    Typeface font,fontbold;
    private ProgressDialogHelper progressDialogHelper;

    public NotifikasiFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();
        listFriendsRequest = view.findViewById(R.id.list_notif);
        tvNull = (TextView) view.findViewById(R.id.tvNull);
        getNotif();
        return view;
    }

    private void getNotif(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/showAllNotif/nik/"+session.getUserNIK()+"/"+tRes+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"3k2bk4notif");
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<NotifModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listFriendsRequest.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listFriendsRequest.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String title = object.getString("title");
                                        String desc = object.getString("description");
                                        String change_date = object.getString("change_date");
                                        model = new NotifModel(title, desc, change_date);
                                        listModel.add(model);
                                    }
                                    adapter = new NotificationAdapter(getActivity(), listModel);
                                    listFriendsRequest.setAdapter(adapter);
                                }
                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }else{
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
}
