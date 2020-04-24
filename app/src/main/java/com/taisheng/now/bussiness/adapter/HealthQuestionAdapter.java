package com.taisheng.now.bussiness.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.bussiness.bean.result.AssessmentOptionsList;
import com.th.j.commonlibrary.interfaces.ILvItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthQuestionAdapter extends BasAdapter<AssessmentOptionsList> {
    public Context context = null;
    ViewHolder holder = null;
    private ILvItemClick click;
    private List<AssessmentOptionsList> data2=new ArrayList<>();
    private int mSelect=-1;
    public HealthQuestionAdapter(Context context) {
        this.context = context;
    }
    public void setIndext(int indext) {
        if (indext!=mSelect){
            mSelect = indext;
            this.notifyDataSetChanged();
        }
    }

    public void setData2(List<AssessmentOptionsList> data3) {
        this.data2.clear();
        this.data2.addAll(data3);
        this.notifyDataSetChanged();
    }

    public void setClick(ILvItemClick click) {
        this.click = click;
    }
    @Override
    public int getCount() {
        return data2 != null ? data2.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        AssessmentOptionsList assessmentOptionsList = data2.get(position);
        holder.tvALabel.setText(assessmentOptionsList.options+"");
        holder.tvA.setText(assessmentOptionsList.optionsValue);
        if (mSelect==position){
            holder.tvALabel.setSelected(true);
        }else {
            holder.tvALabel.setSelected(false);
        }
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
