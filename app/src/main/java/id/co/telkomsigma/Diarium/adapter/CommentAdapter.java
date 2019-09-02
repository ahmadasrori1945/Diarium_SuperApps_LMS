package id.co.telkomsigma.Diarium.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.model.CommentModel;
import id.co.telkomsigma.Diarium.util.TimeHelper;

public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private CommentModel model;
    private List<CommentModel> listModel;
    private TextView tvDate, tvName, tvComment;
    private ImageView ivObat;
    TimeHelper timeHelper;

    public CommentAdapter(Context mContext, List<CommentModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        timeHelper = new TimeHelper();
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
            view = inflater.inflate(R.layout.list_comment, null);
        }
        tvDate = view.findViewById(R.id.tvDate);
        tvName = view.findViewById(R.id.tvTitle);
        tvComment = view.findViewById(R.id.tvComment);
        ivObat = view.findViewById(R.id.ivProfile);

        tvDate.setText(timeHelper.getElapsedTime(model.getChange_date()));
        tvName.setText(model.getFull_name());
        tvComment.setText(model.getText_comment());
        Picasso.get().load(listModel.get(position).getProfile()).error(R.drawable.profile).into(ivObat);
        return view;
    }
}
