package id.co.telkomsigma.Diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.model.ReportActivityModel;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class ActivityReportAdapter extends BaseAdapter {
    private Context mContext;
    private ReportActivityModel model;
    private List<ReportActivityModel> listModel;
    private TextView tvTitle, tvDate, tvType;
    private ImageView ivType;

    public ActivityReportAdapter(Context mContext, List<ReportActivityModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
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
            view = inflater.inflate(R.layout.item_list_activityreport, null);
        }
        String type = null;
        int imageType = 0;
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvType = view.findViewById(R.id.tvType);
        ivType = view.findViewById(R.id.ivType);
        switch (model.getActivity_type()) {
            case "00":
                type = "To Do";
                imageType = R.drawable.list;
                break;
            case "01":
                type = "In Progress";
                imageType = R.drawable.load;
                break;
            case "02":
                type = "Pending";
                imageType = R.drawable.pending;
                break;
            case "03":
                type = "Done";
                imageType = R.drawable.done;
                break;
            case "04":
                type = "Cancel";
                imageType = R.drawable.canceled;
                break;
        }
        tvType.setText(type);
        tvTitle.setText(model.getActivity_title());
        tvDate.setText(model.getActivity_start()+" - "+model.getActivity_finish());
        ivType.setImageResource(imageType);

        return view;
    }
}
