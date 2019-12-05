
package com.taisheng.now.view.biaoqing;


import android.util.ArrayMap;
import android.util.Log;

import com.taisheng.now.R;


/**
 * Created by SiberiaDante
 * Describe: 表情加载类,可自己添加多种表情，分别建立不同的map存放和不同的标志符即可
 * Time: 2017/6/26
 * Email: 994537867@qq.com
 * GitHub: https://github.com/SiberiaDante
 * 博客园： http://www.cnblogs.com/shen-hua/
 */
public class EmotionUtils {

    /**
     * 表情类型标志符
     */
    public static final int EMOTION_CLASSIC_TYPE = 0x0001;//经典表情
    private static final String TAG = EmotionUtils.class.getSimpleName();

    /**
     * key-表情文字;
     * value-表情图片资源
     */
    public static ArrayMap<String, Integer> EMPTY_MAP;
    public static ArrayMap<String, Integer> EMOTION_CLASSIC_MAP;


    static {
        EMPTY_MAP = new ArrayMap<>();
        EMOTION_CLASSIC_MAP = new ArrayMap<>();



//        ["[微笑]", "[嘻嘻]", "[哈哈]", "[可爱]", "[可怜]",
//                "[挖鼻]", "[吃惊]", "[害羞]", "[挤眼]",
//                "[闭嘴]", "[鄙视]", "[爱你]", "[泪]",
//                "[偷笑]", "[亲亲]", "[生病]", "[太开心]",
//                "[白眼]", "[右哼哼]", "[左哼哼]", "[嘘]",
//                "[衰]", "[委屈]", "[吐]", "[哈欠]", "[抱抱]",
//                "[怒]", "[疑问]", "[馋嘴]", "[拜拜]", "[思考]",
//                "[汗]", "[困]", "[睡]", "[钱]", "[失望]", "[酷]",
//                "[色]", "[哼]", "[鼓掌]", "[晕]", "[悲伤]", "[抓狂]",
//                "[黑线]", "[阴险]", "[怒骂]", "[互粉]", "[心]", "[伤心]",
//                "[猪头]", "[熊猫]", "[兔子]", "[ok]", "[耶]", "[good]",
//                "[NO]", "[赞]", "[来]", "[弱]", "[草泥马]", "[神马]",
//                "[囧]", "[浮云]", "[给力]", "[围观]", "[威武]",
//                "[奥特曼]", "[礼物]", "[钟]", "[话筒]",
//                "[蜡烛]", "[蛋糕]"]

        EMOTION_CLASSIC_MAP.put("[白眼]", R.drawable.d_baiyan);
        EMOTION_CLASSIC_MAP.put("[抱抱]", R.drawable.d_baobao);
        EMOTION_CLASSIC_MAP.put("[色]", R.drawable.d_se);
        EMOTION_CLASSIC_MAP.put("[互粉]", R.drawable.d_hufen);
        EMOTION_CLASSIC_MAP.put("[心]", R.drawable.d_xin);
        EMOTION_CLASSIC_MAP.put("[伤心]", R.drawable.d_shangxin);


        EMOTION_CLASSIC_MAP.put("[猪头]", R.drawable.d_zhutou);
        EMOTION_CLASSIC_MAP.put("[熊猫]", R.drawable.d_xiongmao);
        EMOTION_CLASSIC_MAP.put("[兔子]", R.drawable.d_tuzhi);
        EMOTION_CLASSIC_MAP.put("[ok]", R.drawable.d_ok);
        EMOTION_CLASSIC_MAP.put("[耶]", R.drawable.d_yeah);


        EMOTION_CLASSIC_MAP.put("[good]", R.drawable.d_good);
        EMOTION_CLASSIC_MAP.put("[NO]", R.drawable.d_no);
        EMOTION_CLASSIC_MAP.put("[赞]", R.drawable.d_zan);
        EMOTION_CLASSIC_MAP.put("[来]", R.drawable.d_lai);


        EMOTION_CLASSIC_MAP.put("[弱]", R.drawable.d_ruo);
        EMOTION_CLASSIC_MAP.put("[草泥马]", R.drawable.d_chaonima);
        EMOTION_CLASSIC_MAP.put("[神马]", R.drawable.d_shenma);
        EMOTION_CLASSIC_MAP.put("[囧]", R.drawable.d_jiong);

        EMOTION_CLASSIC_MAP.put("[浮云]", R.drawable.d_fuyun);
        EMOTION_CLASSIC_MAP.put("[给力]", R.drawable.d_geili);
        EMOTION_CLASSIC_MAP.put("[围观]", R.drawable.d_weiguan);
        EMOTION_CLASSIC_MAP.put("[威武]", R.drawable.d_weiwu);


        EMOTION_CLASSIC_MAP.put("[奥特曼]", R.drawable.d_aoteman);
        EMOTION_CLASSIC_MAP.put("[礼物]", R.drawable.d_liwu);
        EMOTION_CLASSIC_MAP.put("[钟]", R.drawable.d_zhong);
        EMOTION_CLASSIC_MAP.put("[话筒]", R.drawable.d_huatong);
        EMOTION_CLASSIC_MAP.put("[蜡烛]", R.drawable.d_lazhu);
        EMOTION_CLASSIC_MAP.put("[蛋糕]", R.drawable.d_dangao);



        EMOTION_CLASSIC_MAP.put("[微笑]", R.drawable.d_hehe);
        EMOTION_CLASSIC_MAP.put("[嘻嘻]", R.drawable.d_xixi);
        EMOTION_CLASSIC_MAP.put("[哈哈]", R.drawable.d_haha);
        EMOTION_CLASSIC_MAP.put("[爱你]", R.drawable.d_aini);
        EMOTION_CLASSIC_MAP.put("[挖鼻]", R.drawable.d_wabishi);
        EMOTION_CLASSIC_MAP.put("[吃惊]", R.drawable.d_chijing);
        EMOTION_CLASSIC_MAP.put("[晕]", R.drawable.d_yun);
        EMOTION_CLASSIC_MAP.put("[泪]", R.drawable.d_lei);
        EMOTION_CLASSIC_MAP.put("[馋嘴]", R.drawable.d_chanzui);
        EMOTION_CLASSIC_MAP.put("[抓狂]", R.drawable.d_zhuakuang);
        EMOTION_CLASSIC_MAP.put("[哼]", R.drawable.d_heng);
        EMOTION_CLASSIC_MAP.put("[可爱]", R.drawable.d_keai);
        EMOTION_CLASSIC_MAP.put("[怒]", R.drawable.d_nu);
        EMOTION_CLASSIC_MAP.put("[汗]", R.drawable.d_han);
        EMOTION_CLASSIC_MAP.put("[害羞]", R.drawable.d_haixiu);
        EMOTION_CLASSIC_MAP.put("[睡]", R.drawable.d_shuijiao);
        EMOTION_CLASSIC_MAP.put("[钱]", R.drawable.d_qian);
        EMOTION_CLASSIC_MAP.put("[偷笑]", R.drawable.d_touxiao);
        EMOTION_CLASSIC_MAP.put("[酷]", R.drawable.d_ku);
        EMOTION_CLASSIC_MAP.put("[衰]", R.drawable.d_sui);
        EMOTION_CLASSIC_MAP.put("[闭嘴]", R.drawable.d_bizui);
        EMOTION_CLASSIC_MAP.put("[鄙视]", R.drawable.d_bishi);
        EMOTION_CLASSIC_MAP.put("[鼓掌]", R.drawable.d_guzhang);
        EMOTION_CLASSIC_MAP.put("[悲伤]", R.drawable.d_beishang);
        EMOTION_CLASSIC_MAP.put("[思考]", R.drawable.d_sikao);
        EMOTION_CLASSIC_MAP.put("[生病]", R.drawable.d_shengbing);
        EMOTION_CLASSIC_MAP.put("[亲亲]", R.drawable.d_qinqin);
        EMOTION_CLASSIC_MAP.put("[怒骂]", R.drawable.d_numa);
        EMOTION_CLASSIC_MAP.put("[太开心]", R.drawable.d_taikaixin);
        EMOTION_CLASSIC_MAP.put("[右哼哼]", R.drawable.d_youhengheng);
        EMOTION_CLASSIC_MAP.put("[左哼哼]", R.drawable.d_zuohengheng);
        EMOTION_CLASSIC_MAP.put("[嘘]", R.drawable.d_xu);
        EMOTION_CLASSIC_MAP.put("[委屈]", R.drawable.d_weiqu);
        EMOTION_CLASSIC_MAP.put("[吐]", R.drawable.d_tu);
        EMOTION_CLASSIC_MAP.put("[可怜]", R.drawable.d_kelian);
        EMOTION_CLASSIC_MAP.put("[哈欠]", R.drawable.d_haqian);
        EMOTION_CLASSIC_MAP.put("[挤眼]", R.drawable.d_jiyan);
        EMOTION_CLASSIC_MAP.put("[失望]", R.drawable.d_shiwang);
        EMOTION_CLASSIC_MAP.put("[疑问]", R.drawable.d_yiwen);
        EMOTION_CLASSIC_MAP.put("[困]", R.drawable.d_kun);
        EMOTION_CLASSIC_MAP.put("[拜拜]", R.drawable.d_baibai);
        EMOTION_CLASSIC_MAP.put("[黑线]", R.drawable.d_heixian);
        EMOTION_CLASSIC_MAP.put("[阴险]", R.drawable.d_yinxian);
        EMOTION_CLASSIC_MAP.put("[猪头]", R.drawable.d_zhutou);
        EMOTION_CLASSIC_MAP.put("[熊猫]", R.drawable.d_xiongmao);



    }

    /**
     * 根据名称获取当前表情图标R值
     *
     * @param EmotionType 表情类型标志符
     * @param imgName     名称
     * @return
     */
    public static int getImgByName(int EmotionType, String imgName) {
        Integer integer = null;
        switch (EmotionType) {
            case EMOTION_CLASSIC_TYPE:
                integer = EMOTION_CLASSIC_MAP.get(imgName);
                break;
            default:
                Log.e(TAG, "getImgByName: the emotionMap is null!! Handle Yourself ");
                break;
        }
        return integer == null ? -1 : integer;
    }

    /**
     * 根据类型获取表情数据
     *
     * @param EmotionType
     * @return
     */
    public static ArrayMap<String, Integer> getEmotionMap(int EmotionType) {
        ArrayMap EmojiMap = null;
        switch (EmotionType) {
            case EMOTION_CLASSIC_TYPE:
                EmojiMap = EMOTION_CLASSIC_MAP;
                break;
            default:
                EmojiMap = EMPTY_MAP;
                break;
        }
        return EmojiMap;
    }
}
