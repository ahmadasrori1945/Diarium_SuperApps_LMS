package id.co.telkomsigma.Diarium.controller.friend;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.FriendsAdapter;
import id.co.telkomsigma.Diarium.adapter.ListFriendAdapter;
import id.co.telkomsigma.Diarium.controller.profile.ListFriendActivity;
import id.co.telkomsigma.Diarium.controller.profile.ProfileActivity;
import id.co.telkomsigma.Diarium.model.FriendsModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;
import id.co.telkomsigma.Diarium.util.element.ProgressDialogHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    UserSessionManager session;
    private List<FriendsModel> listModel;
    private FriendsModel model;
    private FriendsAdapter adapter;
    private ListView listFriendsRequest;

    private List<FriendsModel> listModelFriend;
    private FriendsModel modelFriend;
    private ListFriendAdapter adapterFriend;
    private ListView listFriends;

    private TextView tvFriendRequest,tvFriend;
    Typeface font,fontbold;
    private ProgressDialogHelper progressDialogHelper;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();
        listFriendsRequest = view.findViewById(R.id.list_friends_request);
        listFriends = view.findViewById(R.id.list_friends);
        tvFriendRequest = view.findViewById(R.id.tvFriendRequest);
        tvFriend = view.findViewById(R.id.tvFriend);
//        tvNull = (TextView) view.findViewById(R.id.tvNull);
        getFriendRequest();
        getListFriend(session.getUserNIK());
        return view;
    }

    private void getFriendRequest(){
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/friendrequest/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<FriendsModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listFriendsRequest.setVisibility(View.GONE);
                                    tvFriendRequest.setVisibility(View.GONE);
                                } else {
                                    listFriendsRequest.setVisibility(View.VISIBLE);
                                    tvFriendRequest.setVisibility(View.VISIBLE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String status = object.getString("status");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String profile = object.getString("profile");
                                        JSONArray arrayFriend = object.getJSONArray("friend");
                                        for (int b=0; b<arrayFriend.length(); b++) {
                                            JSONObject objectFriend = arrayFriend.getJSONObject(b);
                                            String personal_number_teman = objectFriend.getString("personal_number");
                                            String full_name = objectFriend.getString("full_name");

                                            model = new FriendsModel(begin_date, end_date, business_code, personal_number, status, change_date, change_user, full_name, personal_number_teman, profile);
                                            listModel.add(model);
                                        }
                                    }
                                    adapter = new FriendsAdapter(getActivity(), listModel);
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

    private void getListFriend(String personal_numbe_paramr){
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/friendapprove/"+personal_numbe_paramr+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"LISTFRIENDNYA");
                        try {
                            if(response.getInt("status")==200){
                                listModelFriend = new ArrayList<FriendsModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listFriends.setVisibility(View.GONE);
                                    tvFriend.setVisibility(View.GONE);
                                } else {
                                    listFriends.setVisibility(View.VISIBLE);
                                    tvFriend.setVisibility(View.VISIBLE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String status = object.getString("status");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String profile = object.getString("profile");
                                        JSONArray arrayFriend = object.getJSONArray("friend");
                                        for (int b=0; b<arrayFriend.length(); b++) {
                                            JSONObject objectFriend = arrayFriend.getJSONObject(b);
                                            String personal_number_teman = objectFriend.getString("personal_number");
                                            String full_name = objectFriend.getString("full_name");
//                                            String nickname = objectFriend.getString("nick_name");
                                            modelFriend = new FriendsModel(begin_date, end_date, business_code, personal_number, status, change_date, change_user, full_name, personal_number_teman, profile);
                                            listModelFriend.add(modelFriend);
                                        }
                                    }
                                    adapterFriend = new ListFriendAdapter(getActivity(), listModelFriend);
                                    listFriends.setAdapter(adapterFriend);
//                                    listFriendsRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                            Intent i = new Intent (getActivity(), ProfileActivity.class);
//                                            i.putExtra("personal_number",listModel.get(position).getPersonal_number_teman());
//                                            i.putExtra("avatar",listModel.get(position).getPersonal_number_teman());
//                                            startActivity(i);
//                                        }
//                                    });
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
