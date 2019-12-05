package com.taisheng.now.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
//import android.support.v4.app.Fragment;


/**
 * Created by dragon on 2019/6/27.
 */
public class BaseFragment extends Fragment {

    protected Activity mActivity;


    public void onAttach(Activity activity) {

        mActivity= activity;
        super.onAttach(mActivity);
    }



    private static final String TAG = BaseFragment.class.getSimpleName();
    //传递过来的参数Bundle，供子类使用
    protected Bundle args;

    /**
     * 创建fragment的静态方法，方便传递参数
     *
     * @param args 传递的参数
     * @return
     */
    public static <T extends Fragment> T newInstance(Class clazz, Bundle args) {
        T mFragment = null;
        try {
            mFragment = (T) clazz.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
