package id.co.telkomsigma.Diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.model.AnswerMultipleChoiceModel;
import id.co.telkomsigma.Diarium.model.AnswerQuesionerLMSModel;

public class AnswerKuisionerAdapter extends BaseAdapter {
    private Context mContext;
    private AnswerQuesionerLMSModel model;
    private List<AnswerQuesionerLMSModel> listModel;
    private TextView tvAnswer, tvDate, tvDesc;
    private ImageView ivObat;
    private LinearLayout pesan;
    private RadioButton radioAnswer;
    private int posisinya;
    private CheckBox checkBox;
    ArrayList<String> selectedStrings = new ArrayList<String>();

    public AnswerKuisionerAdapter(Context mContext, List<AnswerQuesionerLMSModel> listModel, int pos) {
        this.mContext = mContext;
        this.listModel = listModel;
        this.posisinya = pos;
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
            view = inflater.inflate(R.layout.list_item_answer, null);
        }
        pesan = view.findViewById(R.id.pesan);
        tvAnswer = view.findViewById(R.id.tvAnswer);
        checkBox = view.findViewById(R.id.checkBoxAnswer);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedStrings.add(checkBox.getText().toString());
                }else{
                    selectedStrings.remove(checkBox.getText().toString());
                }

            }
        });
        tvAnswer.setText(model.getAnswer_text());
        return view;
    }

    ArrayList<String> getSelectedString(){
        return selectedStrings;
    }
}

