package com.taisheng.now.bussiness.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.chat.C2CActivity;
import com.taisheng.now.chat.CircularCoverView;
import com.taisheng.now.chat.ColorUtils;
import com.taisheng.now.chat.CoreDB;
import com.taisheng.now.chat.HistoryBean;
import com.taisheng.now.chat.MLOC;
import com.taisheng.now.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;


/**
 * Created by Administrator on 2015/6/12.
 */

@SuppressLint("WrongConstant")
public class MessageFragment extends BaseFragment {

    private String mTargetId;
    private List<HistoryBean> mHistoryList;
    private ListView vHistoryList;
    private View ll_noresult;
    private MyListAdapter listAdapter;


    public String doctorName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        initView(rootView);


        EventBus.getDefault().register(this);
        initData();

        return rootView;
    }

    void initView(View rootView) {
        mHistoryList = new ArrayList<>();
        listAdapter = new MyListAdapter();
        vHistoryList = (ListView) rootView.findViewById(R.id.history_list);

        vHistoryList.setAdapter(listAdapter);
        vHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MLOC.addHistory(mHistoryList.get(position), true);
                mTargetId = (String) mHistoryList.get(position).getConversationId();
                Intent intent = new Intent(getActivity(), C2CActivity.class);
                intent.putExtra("targetId", mTargetId);
                intent.putExtra("doctorAvator", mHistoryList.get(position).doctorAvator);
                intent.putExtra("doctorName", mHistoryList.get(position).doctorName);


                intent.putExtra("nickName",mHistoryList.get(position).doctorName);
//                intent.putExtra("title",doctorBean.title);
                intent.putExtra("avatar",mHistoryList.get(position).doctorAvator);
                intent.putExtra("doctorId",mTargetId);


                startActivity(intent);
            }
        });
        ll_noresult = rootView.findViewById(R.id.ll_noresult);
    }

    void initData() {


    }


    @Override
    public void onResume() {
        super.onResume();
        MLOC.hasNewC2CMsg = false;
        mHistoryList.clear();
        List<HistoryBean> list = MLOC.getHistoryList(CoreDB.HISTORY_TYPE_C2C);
        if (list != null && list.size() > 0) {
            mHistoryList.addAll(list);
            vHistoryList.setVisibility(View.VISIBLE);
            ll_noresult.setVisibility(View.GONE);
        } else {
            vHistoryList.setVisibility(View.GONE);
            ll_noresult.setVisibility(View.VISIBLE);
        }
        listAdapter.notifyDataSetChanged();


    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void recevieMessage(EventManage.AEVENT_C2C_REV_MSG eventObj) {
        onResume();
    }

    public class MyListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyListAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (mHistoryList != null)
                return mHistoryList.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mHistoryList == null)
                return null;
            return mHistoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (mHistoryList == null)
                return 0;
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder itemSelfHolder;
            if (convertView == null) {
                itemSelfHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_c2c_history, null);
                itemSelfHolder.vUserId = (TextView) convertView.findViewById(R.id.item_id);
                itemSelfHolder.vTime = (TextView) convertView.findViewById(R.id.item_time);
                itemSelfHolder.vMessage = (TextView) convertView.findViewById(R.id.item_msg);
                itemSelfHolder.vCount = (TextView) convertView.findViewById(R.id.item_count);
//                itemSelfHolder.vHeadBg =  convertView.findViewById(R.id.head_bg);
                itemSelfHolder.sdv_header = convertView.findViewById(R.id.sdv_header);
//                itemSelfHolder.vHeadImage = (ImageView) convertView.findViewById(R.id.head_img);
//                itemSelfHolder.vHeadCover = (CircularCoverView) convertView.findViewById(R.id.head_cover);
                convertView.setTag(itemSelfHolder);
            } else {
                itemSelfHolder = (ViewHolder) convertView.getTag();
            }

            HistoryBean historyBean = mHistoryList.get(position);
            String userId = historyBean.getConversationId();
            itemSelfHolder.vUserId.setText(historyBean.doctorName);
//            itemSelfHolder.vHeadBg.setBackgroundColor(ColorUtils.getColor(getActivity(),userId));
//            itemSelfHolder.vHeadCover.setCoverColor(Color.parseColor("#FFFFFF"));
//            int cint = DensityUtil.dip2px(getActivity(),28);
//            itemSelfHolder.vHeadCover.setRadians(cint, cint, cint, cint,0);
//            itemSelfHolder.vHeadImage.setImageResource(MLOC.getHeadImage(getActivity(),userId));

            if (historyBean.doctorAvator != null && !"".equals(historyBean.doctorAvator)) {
                Uri uri = Uri.parse(historyBean.doctorAvator);
                itemSelfHolder.sdv_header.setImageURI(uri);
            }
            itemSelfHolder.vTime.setText(historyBean.getLastTime());
            String rawmessage=historyBean.getLastMsg();
            if(rawmessage.startsWith("img[")&&rawmessage.endsWith("]")){
                itemSelfHolder.vMessage.setText("[图片]");

            }else{
                itemSelfHolder.vMessage.setText(rawmessage);
            }
            if (historyBean.getNewMsgCount() == 0) {
                itemSelfHolder.vCount.setVisibility(View.INVISIBLE);
            } else {
                itemSelfHolder.vCount.setText("" + historyBean.getNewMsgCount());
                itemSelfHolder.vCount.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    public class ViewHolder {
        public TextView vUserId;
        public TextView vTime;
        public TextView vMessage;
        public TextView vCount;
        //        public View vHeadBg;
//        public CircularCoverView vHeadCover;
//        public ImageView vHeadImage;
        public SimpleDraweeView sdv_header;
    }


    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
