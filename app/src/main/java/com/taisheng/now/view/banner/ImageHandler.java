package com.taisheng.now.view.banner;

import android.os.Handler;
import android.os.Message;

/**
 * Created by long on 16/3/17.
 */
public class ImageHandler extends Handler {
    /**
     * 第一次加载轮播
     */
    protected static final int  MSG_FIRST=-1;
    /**
     * 请求更新显示的View。
     */
    protected static final int MSG_UPDATE_IMAGE  = 1;
    /**
     * 请求暂停轮播。
     */
    public static final int MSG_KEEP_SILENT   = 2;
    /**
     * 请求恢复轮播。
     */
    protected static final int MSG_BREAK_SILENT  = 3;
    /**
     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
     */
    protected static final int MSG_PAGE_CHANGED  = 4;

    //轮播间隔时间
    protected static final long MSG_DELAY = 2000;

    //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
//    private WeakReference<RecommendFragment> weakReference;

    private BannerViewPager mbannerViewPager;
    private int currentItem = 0;

    protected ImageHandler(BannerViewPager bannerViewPager,int currentItem){
       this. currentItem=currentItem;
//        weakReference = wk;
        mbannerViewPager=bannerViewPager;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
//            Log.d(LOG_TAG, receive message + msg.what);
//        RecommendFragment fragment = weakReference.get();
//        if (mhandler==null){
//            //Fragment已经回收，无需再处理UI了
//            return ;
//        }
        //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
        if (hasMessages(MSG_UPDATE_IMAGE)){
            removeMessages(MSG_UPDATE_IMAGE);
        }
//        Log.e("longlonglong++current", currentItem + "*****");
        switch (msg.what) {
            case MSG_UPDATE_IMAGE://自然更新
                currentItem++;
                mbannerViewPager.mviewPager.setCurrentItem(currentItem);
                //准备下次播放
                sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;
            case MSG_FIRST://第一次加载
                currentItem++;
                mbannerViewPager.mviewPager.setCurrentItem(currentItem);
            case MSG_KEEP_SILENT://请求暂停轮播
                //只要不发送消息就暂停了
                break;
            case MSG_BREAK_SILENT://请求恢复轮播
               sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;
            case MSG_PAGE_CHANGED:
                //记录当前的页号，避免播放的时候页面显示不正确。
                currentItem = msg.arg1;
                break;
            default:
                break;
        }
    }
}
