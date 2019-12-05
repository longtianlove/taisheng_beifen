package com.taisheng.now.bussiness.me;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.UserInfoPostBean;
import com.taisheng.now.bussiness.bean.result.ModifyUserInfoResultBean;
import com.taisheng.now.bussiness.bean.result.UserInfo;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class UpdateZhanghaoActivity extends BaseActivity {
    View iv_back;
    EditText et_nickname;
    ImageView iv_nickname_guanbi;
    View btn_update;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatezhanghao);
        initView();
    }
    void initView(){
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_update=findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                bean.sysUser.userName=et_nickname.getText().toString();
                bean.sysUser.nickName=UserInstance.getInstance().userInfo.nickName;


                ApiUtils.getApiService().modifyuser(bean).enqueue(new TaiShengCallback<BaseBean<ModifyUserInfoResultBean>>() {
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
                                UserInstance.getInstance().userInfo.nickName=bean.sysUser.nickName;
                                SPUtil.putNickname(bean.sysUser.nickName);
                                UserInstance.getInstance().userInfo.userName=bean.sysUser.userName;
                                SPUtil.putZhanghao(bean.sysUser.userName);
                                finish();
                                break;

                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean<ModifyUserInfoResultBean>> call, Throwable t) {

                    }
                });
            }
        });
        iv_nickname_guanbi= (ImageView) findViewById(R.id.iv_nickname_guanbi);
        iv_nickname_guanbi.setVisibility(View.INVISIBLE);
        iv_nickname_guanbi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                et_nickname.setText("");
            }
        });

        et_nickname= (EditText) findViewById(R.id.et_nickname);
        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    iv_nickname_guanbi.setVisibility(View.VISIBLE);
                }else{
                    iv_nickname_guanbi.setVisibility(View.GONE);
                }

                if(s != null && s.length() > 0&&!s.equals(UserInstance.getInstance().getZhanghao())){
                    btn_update.setEnabled(true);
                }else{
                    btn_update.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_nickname.setText(UserInstance.getInstance().getZhanghao());
        CharSequence text = et_nickname.getText();
        //Debug.asserts(text instanceof Spannable);
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable)text;
            Selection.setSelection(spanText, text.length());
        }


    }
}
