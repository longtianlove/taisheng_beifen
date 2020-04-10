package com.taisheng.now.bussiness.article;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.HotPostBean;
import com.taisheng.now.bussiness.bean.result.HotResultBean;
import com.taisheng.now.bussiness.bean.result.HotSearchBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.WrapLayout;
import com.th.j.commonlibrary.utils.TextsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/5.
 */

public class SecretSearchActivity extends BaseIvActivity implements TextWatcher {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search_guanbi)
    ImageView ivSearchGuanbi;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_deletehistory)
    ImageView ivDeletehistory;
    @BindView(R.id.ll_history_label)
    LinearLayout llHistoryLabel;
    @BindView(R.id.wl_histroy_search)
    WrapLayout wlHistroySearch;
    @BindView(R.id.ll_hot)
    LinearLayout llHot;
    @BindView(R.id.lv_hotsearch)
    TaishengListView lvHotsearch;

    private ArrayList<String> historysearchlist;
    private MessageAdapter madapter;//适配器
    private String searchKey;//搜索关键字

    @Override
    public void initView() {
        setContentView(R.layout.activity_serert_search);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        llTop.setVisibility(View.GONE);
        ivSearchGuanbi.setVisibility(View.GONE);
        madapter = new MessageAdapter(this);
        lvHotsearch.setAdapter(madapter);
        lvHotsearch.setHasLoadMore(false);
        etSearch.addTextChangedListener(this);
    }

    @Override
    public void addData() {
        getHot();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {

    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.iv_deletehistory, R.id.iv_search_guanbi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                String searchString = TextsUtils.getTexts(etSearch);
                if (TextUtils.isEmpty(searchString)) {
                    return;
                }
                searchKey = searchString;
                if (historysearchlist == null) {
                    historysearchlist = new ArrayList<String>();
                }

                if (historysearchlist.contains(searchString)) {
                    historysearchlist.remove(searchString);
                }
                historysearchlist.add(0, searchString);
                SPUtil.putHistorySearch(historysearchlist);
//                ll_history_label.setVisibility(View.VISIBLE);
//                wl_histroy_search.setVisibility(View.VISIBLE);
//                wl_histroy_search.setData(historysearchlist, SecretSearchActivity.this, 14, 15, 4, 14, 4, 12, 12, 15, 12);
//                wl_histroy_search.setMarkClickListener(new WrapLayout.MarkClickListener() {
//                    @Override
//                    public void clickMark(int position) {
//                        ToastUtil.showTost(historysearchlist.get(position));
//                    }
//                });

                Intent intent = new Intent(SecretSearchActivity.this, SearchResultActivity.class);
                intent.putExtra("searchkey", searchKey);
                startActivity(intent);
                break;
            case R.id.iv_deletehistory:
                llHistoryLabel.setVisibility(View.GONE);
                wlHistroySearch.setVisibility(View.GONE);
                historysearchlist.clear();
                SPUtil.putHistorySearch(historysearchlist);
                break;
            case R.id.iv_search_guanbi:
                etSearch.setText("");
                break;
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null && s.length() > 0) {
            ivSearchGuanbi.setVisibility(View.VISIBLE);
        } else {
            ivSearchGuanbi.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

  private  void getHot() {
        HotPostBean bean = new HotPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        ApiUtils.getApiService_hasdialog().hotSearchArticle(bean).enqueue(new TaiShengCallback<BaseBean<HotResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<HotResultBean>> response, BaseBean<HotResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result == null) {
                            return;
                        }
                        Collections.reverse(message.result.list);
                        madapter.mData = message.result.list;
                        madapter.notifyDataSetChanged();
                        break;

                }
            }

            @Override
            public void onFail(Call<BaseBean<HotResultBean>> call, Throwable t) {

            }
        });
    }



    class MessageAdapter extends BaseAdapter {

        public Context mcontext;

        List<HotSearchBean> mData = new ArrayList<HotSearchBean>();

        public MessageAdapter(Context context) {
            this.mcontext = context;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 声明内部类
            Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_hotsearch, null);

                util.iv_hot = (ImageView) convertView.findViewById(R.id.iv_hot);
                util.iv_top = (ImageView) convertView.findViewById(R.id.iv_top);
                util.tv_hotsearchtitle = (TextView) convertView.findViewById(R.id.tv_hotsearchtitle);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            HotSearchBean bean = mData.get(position);
            if ("top".equals(bean.tag)) {
                util.iv_hot.setVisibility(View.VISIBLE);
                util.iv_top.setVisibility(View.GONE);
            } else {
                util.iv_hot.setVisibility(View.GONE);
                util.iv_top.setVisibility(View.VISIBLE);
            }
            util.tv_hotsearchtitle.setText(bean.title);
            util.tv_hotsearchtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SecretSearchActivity.this, ArticleContentActivity.class);
                    intent.putExtra("articleId", bean.id);

                    startActivity(intent);
                }
            });

            return convertView;
        }


        class Util {
            ImageView iv_hot;
            ImageView iv_top;
            TextView tv_hotsearchtitle;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        historysearchlist = SPUtil.getHistorySearch();
        if (historysearchlist == null || historysearchlist.isEmpty()) {
            llHistoryLabel.setVisibility(View.GONE);
            wlHistroySearch.setVisibility(View.GONE);
        } else {
            llHistoryLabel.setVisibility(View.VISIBLE);
            wlHistroySearch.setVisibility(View.VISIBLE);
            wlHistroySearch.setData(historysearchlist, this, 14, 15, 4, 14, 4, 24, 12, 24, 12);
            wlHistroySearch.setMarkClickListener(new WrapLayout.MarkClickListener() {
                @Override
                public void clickMark(int position) {
                    String searchString = historysearchlist.get(position);
                    if (TextUtils.isEmpty(searchString)) {
                        return;
                    }
                    searchKey = searchString;
                    if (historysearchlist == null) {
                        historysearchlist = new ArrayList<String>();
                    }

                    if (historysearchlist.contains(searchString)) {
                        historysearchlist.remove(searchString);
                    }
                    historysearchlist.add(0, searchString);
                    SPUtil.putHistorySearch(historysearchlist);
//                ll_history_label.setVisibility(View.VISIBLE);
//                wl_histroy_search.setVisibility(View.VISIBLE);
//                wl_histroy_search.setData(historysearchlist, SecretSearchActivity.this, 14, 15, 4, 14, 4, 12, 12, 15, 12);
//                wl_histroy_search.setMarkClickListener(new WrapLayout.MarkClickListener() {
//                    @Override
//                    public void clickMark(int position) {
//                        ToastUtil.showTost(historysearchlist.get(position));
//                    }
//                });

                    Intent intent = new Intent(SecretSearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("searchkey", searchKey);
                    startActivity(intent);
                }
            });
        }
    }
}
