package id.co.telkomsigma.Diarium.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.model.FriendsModel;
import id.co.telkomsigma.Diarium.util.UserSessionManager;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class FriendsAdapter extends BaseAdapter {
    private Context mContext;
    private FriendsModel model;
    private List<FriendsModel> listModel;
    private TextView tvTitle, tvDate, tvDesc;
    private ImageView ivProfile;
    private LinearLayout pesan;
    private Button btnConfirm, btnDescline;
    Dialog myDialog;
    Typeface font,fontbold;
    UserSessionManager session;

    public FriendsAdapter(Context mContext, List<FriendsModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        myDialog = new Dialog(mContext);
        session = new UserSessionManager(mContext);
        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nexa Bold.otf");
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public Object getItem(int position) {
        return listModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_friends, null);
        }
        tvTitle = view.findViewById(R.id.name);
        tvDate = view.findViewById(R.id.nik);
        btnConfirm = view.findViewById(R.id.confirm);
        btnDescline = view.findViewById(R.id.reject);
        ivProfile = view.findViewById(R.id.ivProfile);
        if (model.getProfile().isEmpty()) {
            ivProfile.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(model.getProfile()).error(R.drawable.profile).into(ivProfile);
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShowPopupApprove(model.getPersonal_number());
                approveFriends(model.getFull_name(), model.getPersonal_number_teman());
            }
        });

        btnDescline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShowPopupDecline(model.getPersonal_number());
                declineFriends(model.getFull_name(), model.getPersonal_number_teman());
            }
        });

        tvTitle.setText(model.getFull_name());
        tvDate.setText(model.getBegin_date());

        return view;
    }

    private void approveFriends(final String nama_friend, final String friends) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", session.getUserNIK());
            jsonObject.put("friend", friends);
            jsonObject.put("change_user", session.getUserNIK());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(session.getServerURL()+"users/"+session.getUserNIK()+"/approveFriend/"+friends)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"KONFIRMYA");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
                                ShowPopupApprove(nama_friend, friends);
                            }else {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
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

    private void declineFriends(final String nama_friend, final String friends) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", session.getUserNIK());
            jsonObject.put("friend", friends);
            jsonObject.put("change_user", session.getUserNIK());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(session.getServerURL()+"users/"+session.getUserNIK()+"/declineFriend/"+friends)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"REJECTYA");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
                                ShowPopupDecline(nama_friend, friends);
                                listModel.notify();
                            }else {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
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

    public void ShowPopupApprove(String nama_friend, String friends) {
        TextView txtapprove;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_popup);
        txtapprove =(TextView) myDialog.findViewById(R.id.text);
        txtapprove.setTypeface(fontbold);
        txtapprove.setText(nama_friend+" is now your friend");
        txtapprove.setTextColor(Color.GRAY);
//        btnFollow = (Button) myDialog.findViewById(R.id.confirm);
        txtapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowPopupDecline(String nama_friend, String friends) {
        TextView txtapprove;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_popup_decline);
        txtapprove =(TextView) myDialog.findViewById(R.id.text);
        txtapprove.setTypeface(fontbold);
        txtapprove.setText("Success reject "+nama_friend);
        txtapprove.setTextColor(Color.GRAY);
//        btnFollow = (Button) myDialog.findViewById(R.id.confirm);
        txtapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}