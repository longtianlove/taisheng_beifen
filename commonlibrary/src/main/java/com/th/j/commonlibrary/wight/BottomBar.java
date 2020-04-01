package com.th.j.commonlibrary.wight;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.th.j.commonlibrary.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.graphics.drawable.GradientDrawable.OVAL;

/**
 * @author 韩晓康
 * @date :2019/3/8  8:38
 * @description: 底部控件
 */
public class BottomBar extends LinearLayout implements View.OnClickListener {
    private List<String> mTitle = new ArrayList<>();//文字集合
    private List<Integer> mImageTrue = new ArrayList<>();//选中图片
    private List<Integer> mImageFalse = new ArrayList<>();//未选中图片
    private List<Fragment> mFragment = new ArrayList<>();//fragment
    private List<Boolean> mRed = new ArrayList<>();//小红点
    private Context mContext;
    private int fragmentId;//fragment显示父控件id
    private FragmentManager manager;
    private LinearLayout mLinearLayout;
    private int screenWidth;//屏幕宽度
    private boolean isVersion = false;//判断版本是不是16以上
    private OnItemListener onItemListener;//点击回调监听
    private int text_select;//选中字体颜色
    private int text_no_select;//未选中字体颜色
//    private float text_size;//文字大小
    private int circle_color;//小红点颜色
    private int circle_size;//小红点大小
    private Activity activity;
    TextView tvIcon;
    ImageView ivIcon;
    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_bar_layout, this, true);
        mLinearLayout = view.findViewById(R.id.container);
        screenWidth = getScreenWidth();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            isVersion = true;
        } else {
            isVersion = false;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.bottom_bars);
        if (array != null) {
            text_select = array.getColor(R.styleable.bottom_bars_text_select, ContextCompat.getColor(context, R.color.color28b28b));
            text_no_select = array.getColor(R.styleable.bottom_bars_text_no_select, ContextCompat.getColor(context, R.color.color999999));
            circle_color = array.getColor(R.styleable.bottom_bars_circle_color, ContextCompat.getColor(context, R.color.colorff2c58));
//            float font_size = array.getDimension(R.styleable.bottom_bars_text_size, 20);
            float red_size = array.getInteger(R.styleable.bottom_bars_circle_size, 25);
//            text_size = px2sp(context, font_size);
            circle_size = px2dp(context, red_size);
        }
        array.recycle();
    }

    /**
     * 初始fragemnt参数
     *
     * @param manager
     * @param fragmentId
     * @return
     */
    public BottomBar init(FragmentManager manager, @Nullable int fragmentId,Activity activity) {
        this.manager = manager;
        this.fragmentId = fragmentId;
        this.activity = activity;
        return this;
    }


    /**
     * 添加条目
     *
     * @param title      文字
     * @param imageTrue  选中图片
     * @param imageFalse 未选中图片
     * @param fragment
     * @param isRed      是否显示小红点
     * @return
     */
    public BottomBar addItem(String title, @DrawableRes int imageTrue, @DrawableRes int imageFalse, @Nullable Fragment fragment, boolean isRed) {
        mTitle.add(title);
        mImageTrue.add(imageTrue);
        mImageFalse.add(imageFalse);
        if (fragment != null) {
            mFragment.add(fragment);
        }
        mRed.add(isRed);
        addViews();
        return this;
    }

    /**
     * 添加显示布局
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void addViews() {
        String l = System.currentTimeMillis() + "";//设置id 防止通一个页面id重复
        String sId = l.substring(l.length() - 3);
        mLinearLayout.removeAllViews();
        int mFragmentSize = mImageTrue.size();
        if (mImageTrue != null && mFragmentSize > 0) {
            for (int i = 0; i < mFragmentSize; i++) {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_bar_layout, null);
                ImageView ivIcon = (ImageView) inflate.findViewById(R.id.iv_icon);
                TextView tvIcon = (TextView) inflate.findViewById(R.id.tv_icon);
                TextView tvRed = (TextView) inflate.findViewById(R.id.tv_red);
                if (isVersion) {
                    ivIcon.setBackground(initStateListDrawable(i));
                    tvRed.setBackground(getDrawable(circle_color, circle_size));
                } else {
                    ivIcon.setBackgroundDrawable(initStateListDrawable(i));
                    tvRed.setBackgroundDrawable(getDrawable(circle_color, circle_size));
                }
                tvRed.setWidth(circle_size);
                tvRed.setHeight(circle_size);
//                tvIcon.setTextSize(text_size);
                tvIcon.setTextColor(colorStateList(text_select, text_select, text_no_select));
                if (!TextUtils.isEmpty(mTitle.get(i))) {
                    tvIcon.setVisibility(VISIBLE);
                    tvIcon.setText(mTitle.get(i));
                } else {
                    tvIcon.setVisibility(GONE);
                }
                if (mRed.get(i)) {
                    tvRed.setVisibility(VISIBLE);
                } else {
                    tvRed.setVisibility(GONE);
                }
                inflate.setLayoutParams(new ViewGroup.LayoutParams(screenWidth / mFragmentSize, ViewGroup.LayoutParams.MATCH_PARENT));
                inflate.setId((i + Integer.valueOf(sId)));
                inflate.setOnClickListener(this);
                mLinearLayout.addView(inflate);
            }
        }
    }

    public void defaultIndext(int index) {
        FragmentTransaction fragmentTransaction = null;
        if (manager != null) {
            fragmentTransaction = manager.beginTransaction();
        }
        hideAllFragment(fragmentTransaction);
        for (int i = 0; i < mImageTrue.size(); i++) {
            if (fragmentTransaction != null && mFragment != null && mFragment.size() > 0) {
                if (!mFragment.get(i).isAdded()) {
                    fragmentTransaction.add(fragmentId, mFragment.get(i));
                }
            }
            tvIcon = mLinearLayout.getChildAt(i).findViewById(R.id.tv_icon);
            ivIcon = mLinearLayout.getChildAt(i).findViewById(R.id.iv_icon);
            tvIcon.setSelected(false);
            ivIcon.setSelected(false);

            if (i == index) {
                if (fragmentTransaction != null && mFragment != null && mFragment.size() > 0) {
                    fragmentTransaction.show(mFragment.get(i));
                }
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        tvIcon.setSelected(true);
                        ivIcon.setSelected(true);
                    }
                });
            }
        }
        if (fragmentTransaction != null) {
//            fragmentTransaction.commit();
            fragmentTransaction.commitAllowingStateLoss();
        }

    }
    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = null;
        if (manager != null) {
            fragmentTransaction = manager.beginTransaction();
        }
        hideAllFragment(fragmentTransaction);
        setAllSelect();
        if (mImageTrue != null) {
            for (int i = 0; i < mImageTrue.size(); i++) {
                if (mFragment != null && mFragment.size() > 0) {
                    if (fragmentTransaction != null && !mFragment.get(i).isAdded()) {
                        fragmentTransaction.add(fragmentId, mFragment.get(i));
                    }
                }
                if (v.getId() == mLinearLayout.getChildAt(i).getId()) {
                    if (fragmentTransaction != null && mFragment != null && mFragment.size() > 0) {
                        fragmentTransaction.show(mFragment.get(i));
                    }
                    tvIcon = mLinearLayout.getChildAt(i).findViewById(R.id.tv_icon);
                     ivIcon = mLinearLayout.getChildAt(i).findViewById(R.id.iv_icon);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            tvIcon.setSelected(true);
                            ivIcon.setSelected(true);
                        }
                    });
                    if (onItemListener != null) {
                        onItemListener.onItem(i);
                    }
                }
            }
        }
        if (fragmentTransaction != null) {
            fragmentTransaction.commit();
        }
    }

    /**
     * 否显示小红点
     *
     * @param index
     */
    public void setRedPoint(int index) {
        TextView tvRed = (TextView) mLinearLayout.getChildAt(index).findViewById(R.id.tv_red);
        tvRed.setVisibility(VISIBLE);
    }

    /**
     * 取消显示小红点
     *
     * @param index
     */
    public void setCancelRedPoint(int index) {
        TextView tvRed = (TextView) mLinearLayout.getChildAt(index).findViewById(R.id.tv_red);
        tvRed.setVisibility(GONE);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }


    public interface OnItemListener {
        void onItem(int i);
    }

    private void setAllSelect() {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
              tvIcon = mLinearLayout.getChildAt(i).findViewById(R.id.tv_icon);
              ivIcon = mLinearLayout.getChildAt(i).findViewById(R.id.iv_icon);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    tvIcon.setSelected(false);
                    ivIcon.setSelected(false);
                }
            });
        }
    }

    /**
     * 选择器
     * @return
     */
    private StateListDrawable initStateListDrawable(int orientation) {
        //初始化一个空对象
        StateListDrawable btState = new StateListDrawable();
        btState.addState(new int[]{android.R.attr.state_selected},
                getResources().getDrawable(mImageTrue.get(orientation)));
        btState.addState(new int[]{android.R.attr.state_pressed},
                getResources().getDrawable(mImageTrue.get(orientation)));
        btState.addState(new int[]{},
                getResources().getDrawable(mImageFalse.get(orientation)));
        return btState;
    }

    /**
     * 对TextView设置不同状态时其文字颜色。
     *
     * @return
     */
    private ColorStateList colorStateList(int selected, int pressed, int normal) {
        int[] colors = new int[]{selected, pressed, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 产生shape类型的drawable
     *
     * @param solidColor
     * @return
     */
    public static GradientDrawable getDrawable(int solidColor, int circle_size) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(OVAL);
        drawable.setSize(circle_size, circle_size);
        drawable.setColor(solidColor);
        return drawable;
    }

    /**
     * 隐藏所有Fragment
     *
     * @param fragmentTransaction
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fragmentTransaction != null) {
            for (int i = 0; i < mFragment.size(); i++) {
                fragmentTransaction.hide(mFragment.get(i));
            }
        }
    }

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private float px2sp(Context context, float size) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final float fontScale = displayMetrics.scaledDensity;
        return (int) (size / 2 + 0.5f);
    }

    private int px2dp(Context context, float red_size) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final float fontScale = displayMetrics.density;
        return (int) (red_size / fontScale + 0.5f);
    }
}
