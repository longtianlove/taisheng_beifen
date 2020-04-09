package com.taisheng.now.bussiness.me;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.UserInfoPostBean;
import com.taisheng.now.bussiness.bean.result.ModifyUserInfoResultBean;
import com.taisheng.now.bussiness.bean.result.UserInfo;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;
import com.th.j.commonlibrary.utils.TextsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class UpdateNickActivity extends BaseHActivity implements TextWatcher {
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.iv_nickname_guanbi)
    ImageView ivNicknameGuanbi;
    @BindView(R.id.btn_update)
    TextView btnUpdate;

    @Override
    public void initView() {
        setContentView(R.layout.activity_updatenickname);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        ivNicknameGuanbi.setVisibility(View.INVISIBLE);
        etNickname.addTextChangedListener(this);
    }

    @Override
    public void addData() {
        etNickname.setText(UserInstance.getInstance().getNickname());
        CharSequence text = TextsUtils.getTexts(etNickname);
        //Debug.asserts(text instanceof Spannable);
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.updata_nickname));
    }

    @OnClick({R.id.iv_nickname_guanbi, R.id.btn_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_nickname_guanbi:
                etNickname.setText("");
                break;
            case R.id.btn_update:
                UserInfoPostBean bean = new UserInfoPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.sysUser = new UserInfo();
                bean.sysUser.id = UserInstance.getInstance().getUid();
                bean.sysUser.token = UserInstance.getInstance().getToken();

                bean.sysUser.age = UserInstance.getInstance().userInfo.age;
                bean.sysUser.phone = UserInstance.getInstance().userInfo.phone;
                bean.sysUser.realName = UserInstance.getInstance().userInfo.realName;
                bean.sysUser.sex = UserInstance.getInstance().userInfo.sex;
                bean.sysUser.avatar = UserInstance.getInstance().userInfo.avatar;
                bean.sysUser.userName = UserInstance.getInstance().userInfo.userName;
                bean.sysUser.nickName = TextsUtils.getTexts(etNickname);

                ApiUtils.getApiService_hasdialog().modifyuser(bean).enqueue(new TaiShengCallback<BaseBean<ModifyUserInfoResultBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<ModifyUserInfoResultBean>> response, BaseBean<ModifyUserInfoResultBean> message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                UserInstance.getInstance().userInfo.age = bean.sysUser.age;
                                SPUtil.putAge(UserInstance.getInstance().userInfo.age);
                                UserInstance.getInstance().userInfo.phone = bean.sysUser.phone;
                                SPUtil.putPhone(UserInstance.getInstance().userInfo.phone);
                                UserInstance.getInstance().userInfo.realName = bean.sysUser.realName;
                                SPUtil.putRealname(UserInstance.getInstance().userInfo.realName);
                                UserInstance.getInstance().userInfo.sex = bean.sysUser.sex;
                                SPUtil.putSex(UserInstance.getInstance().userInfo.sex);
                                UserInstance.getInstance().userInfo.avatar = bean.sysUser.avatar;
                                SPUtil.putAVATAR(bean.sysUser.avatar);
                                UserInstance.getInstance().userInfo.nickName = bean.sysUser.nickName;
                                SPUtil.putNickname(bean.sysUser.nickName);
                                finish();
                                break;

                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean<ModifyUserInfoResultBean>> call, Throwable t) {

                    }
                });
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null && s.length() > 0) {
            ivNicknameGuanbi.setVisibility(View.VISIBLE);
        } else {
            ivNicknameGuanbi.setVisibility(View.GONE);
        }

        if (s != null && s.length() > 0 && !s.equals(UserInstance.getInstance().getNickname())) {
            btnUpdate.setEnabled(true);
        } else {
            btnUpdate.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
