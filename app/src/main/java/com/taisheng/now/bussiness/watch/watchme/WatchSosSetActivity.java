package com.taisheng.now.bussiness.watch.watchme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.view.WithScrolleViewListView;
import com.th.j.commonlibrary.global.Global;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WatchSosSetActivity extends BaseIvActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.lv_sos)
    WithScrolleViewListView lvSos;
    @BindView(R.id.tv_addsos)
    TextView tvAddsos;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watch_sos_set);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        lvSos.setOnItemClickListener(this);
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.watch_msg36));
    }

    @OnClick(R.id.tv_addsos)
    public void onViewClicked() {
        Intent intent=new Intent(this,WatchSosUpdataActivity.class);
        intent.putExtra(Global.INTENT_TYPE,Global.SOS_ADD);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,WatchSosUpdataActivity.class);
        intent.putExtra(Global.INTENT_TYPE,Global.SOS_UPDATA);
        startActivity(intent);
    }
}
