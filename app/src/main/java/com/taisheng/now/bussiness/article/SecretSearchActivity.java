package com.taisheng.now.bussiness.article;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.HotPostBean;
import com.taisheng.now.bussiness.bean.result.HotResultBean;
import com.taisheng.now.bussiness.bean.result.HotSearchBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.WrapLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/5.
 */

public class SecretSearchActivity extends BaseActivity {
    View iv_back;
    EditText et_search;
    View iv_search_guanbi;
    View tv_search;
    View iv_deletehistory;

    View ll_history_label;
    WrapLayout wl_histroy_search;

    TaishengListView lv_hotsearch;

    ArrayList<String> historysearchlist;

    MessageAdapter madapter;//适配器

    String searchKey;//搜索关键字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serert_search);
        initView();
    }


    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_search_guanbi = findViewById(R.id.iv_search_guanbi);
        iv_search_guanbi.setVisibility(View.GONE);
        iv_search_guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    iv_search_guanbi.setVisibility(View.VISIBLE);
                } else {
                    iv_search_guanbi.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_search = findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = et_search.getText().toString();
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
        iv_deletehistory = findViewById(R.id.iv_deletehistory);
        iv_deletehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_history_label.setVisibility(View.GONE);
                wl_histroy_search.setVisibility(View.GONE);
                historysearchlist.clear();
                SPUtil.putHistorySearch(historysearchlist);
            }
        });
        ll_history_label = findViewById(R.id.ll_history_label);
        wl_histroy_search = (WrapLayout) findViewById(R.id.wl_histroy_search);


        madapter = new MessageAdapter(this);
        lv_hotsearch = (TaishengListView) findViewById(R.id.lv_hotsearch);
        lv_hotsearch.setAdapter(madapter);
        lv_hotsearch.setHasLoadMore(false);
        getHot();
    }


    void getHot() {
        HotPostBean bean = new HotPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        ApiUtils.getApiService().hotSearchArticle(bean).enqueue(new TaiShengCallback<BaseBean<HotResultBean>>() {
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
    protected void onStart() {
        super.onStart();
        historysearchlist = SPUtil.getHistorySearch();
        if (historysearchlist == null || historysearchlist.isEmpty()) {
            ll_history_label.setVisibility(View.GONE);
            wl_histroy_search.setVisibility(View.GONE);
        } else {
            ll_history_label.setVisibility(View.VISIBLE);
            wl_histroy_search.setVisibility(View.VISIBLE);
            wl_histroy_search.setData(historysearchlist, this, 14, 15, 4, 14, 4, 24, 12, 24, 12);
            wl_histroy_search.setMarkClickListener(new WrapLayout.MarkClickListener() {
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
