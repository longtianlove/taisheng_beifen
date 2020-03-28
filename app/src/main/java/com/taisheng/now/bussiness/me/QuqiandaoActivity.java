package com.taisheng.now.bussiness.me;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.view.sign.OnSignedSuccess;
import com.taisheng.now.view.sign.SignDate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dragon on 2019/6/28.
 */

public class QuqiandaoActivity extends BaseIvActivity {

    @BindView(R.id.sdv_header)
    SimpleDraweeView sdvHeader;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.tv_yiqiandao)
    TextView tvYiqiandao;
    @BindView(R.id.signDate)
    SignDate signDate;

    @Override
    public void initView() {
        setContentView(R.layout.activity_quqiandao);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        initViews();
        EventBus.getDefault().register(this);
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.sigin1));
    }

    private void initViews() {
        signDate.setOnSignedSuccess(new OnSignedSuccess() {
            @Override
            public void OnSignedSuccess() {
                Log.e("wqf", "Success");
            }
        });
        signDate.qiandao();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void qiandaoChenggong(EventManage.qiaodaoSuccess event) {
        tvYiqiandao.setText(getString(R.string.sigin3) + event.tomorrowPoints + getString(R.string.sigin4));
        tvJifen.setText(event.points);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (UserInstance.getInstance().userInfo.avatar != null) {
            Uri uri = Uri.parse(Constants.Url.File_Host + UserInstance.getInstance().userInfo.avatar);
            sdvHeader.setImageURI(uri);
        }
        if (!TextUtils.isEmpty(UserInstance.getInstance().userInfo.nickName)) {
            tvNickname.setText(UserInstance.getInstance().userInfo.nickName);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
