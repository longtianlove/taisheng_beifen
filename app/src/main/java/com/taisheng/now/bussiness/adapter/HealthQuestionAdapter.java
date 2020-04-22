package com.taisheng.now.bussiness.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.bussiness.bean.result.AssessmentOptionsList;
import com.th.j.commonlibrary.interfaces.ILvItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthQuestionAdapter extends BasAdapter<AssessmentOptionsList> {
    public Context context = null;
    ViewHolder holder = null;
    private ILvItemClick click;

    public HealthQuestionAdapter(Context context) {
        this.context = context;
    }

    public void setClick(ILvItemClick click) {
        this.click = click;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_health_question, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        List<AssessmentOptionsList> data = getData();
        AssessmentOptionsList assessmentOptionsList = data.get(position);
        holder.tvALabel.setText(assessmentOptionsList.options+"");
        holder.tvA.setText(assessmentOptionsList.optionsValue);
        holder.tvALabel.setSelected(assessmentOptionsList.isCheck);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_a_label)
        TextView tvALabel;
        @BindView(R.id.tv_a)
        TextView tvA;
        @BindView(R.id.ll_a)
        LinearLayout llA;
        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
