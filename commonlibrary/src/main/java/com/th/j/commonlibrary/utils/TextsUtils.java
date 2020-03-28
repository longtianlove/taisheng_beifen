package com.th.j.commonlibrary.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 韩晓康
 * @date :2019/3/7  10:33
 * @description: 字符串工具类
 */
public class TextsUtils {
    /**
     * TextView
     *
     * @param view
     * @return
     */
    public static String getTexts(TextView view) {
//        CharSequence hint = view.getText();
//        String s = defaultString(hint);
        return view.getText().toString().trim();
    }

    /**
     * EditText
     *
     * @param view
     * @return
     */
    public static String getTexts(EditText view) {
//        CharSequence hint = view.getText();
//        String s = defaultString(hint);
        return view.getText().toString().trim();
    }

    public static String getHints(EditText view) {
        CharSequence hint = view.getHint();
        String s = defaultString(hint);
        return s.toString().trim();
    }

    private static String defaultString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (s == null || s.equals("null") || android.text.TextUtils.isEmpty(s)) {
            return true;
        } else {
            return false;
        }
    }


    public static String isEmptys(String s, String replace) {
        if (s == null || s.equals("null") || android.text.TextUtils.isEmpty(s)) {
            if (!android.text.TextUtils.isEmpty(replace)) {
                return replace;
            }
            return "";
        } else {
            return s;
        }
    }

    public static String isEmptys(String s) {
        if (s == null || s.equals("null") || android.text.TextUtils.isEmpty(s)) {
            return "";
        } else {
            return s;
        }
    }

    /**
     * 判断提交数组是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String... s) {
        for (String s1 : s) {
            if (s1 == null || s1.equals("null") || android.text.TextUtils.isEmpty(s1)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 改变按钮背景颜色
     */
//    public static void btBg(final View view, final boolean isTrue) {
//        if (isTrue) {
//            view.setEnabled(false);
//            view.setBackgroundResource(R.drawable.gray_button_background);
//        } else {
//            view.setBackgroundResource(R.drawable.selected_button_background);
//            view.setEnabled(true);
//        }
//    }

    /**
     * 改变按钮背景颜色
     */
//    public static void btLoginBg(final View view, final boolean isTrue) {
//        if (isTrue) {
//            view.setEnabled(false);
//            view.setBackgroundResource(R.drawable.login_normal_btn_bg);
//        } else {
//            view.setBackgroundResource(R.drawable.login_selectable_btn_bg);
//            view.setEnabled(true);
//        }
//    }

//    public static void btBg2(final View view, final boolean isTrue) {
//        if (isTrue) {
//            view.setEnabled(false);
//            view.setBackgroundResource(R.drawable.gray_button_background);
//        } else {
//            view.setBackgroundResource(R.drawable.yellow_button_background);
//            view.setEnabled(true);
//        }
//    }


    /**
     * 改变文字颜色
     */
//    public static void tvColor(Context context, final TextView view, final boolean isTrue) {
//        if (isTrue) {
//            view.setEnabled(false);
//            view.setTextColor(context.getResources().getColor(R.color.color_font_999999));
//        } else {
//            view.setTextColor(context.getResources().getColor(R.color.colorAccent));
//            view.setEnabled(true);
//        }
//    }

    /**
     * 改变按钮背景颜色
     */
//    public static void tvColorWhite(Context context, final TextView view, final boolean isTrue) {
//        if (isTrue) {
//            view.setEnabled(false);
//            view.setTextColor(context.getResources().getColor(R.color.white_tran_66));
//        } else {
//            view.setTextColor(context.getResources().getColor(R.color.color_font_ffffff));
//            view.setEnabled(true);
//        }
//    }

    //判断是否是数字
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String instertChart(int i, String str) {
        StringBuilder sb = new StringBuilder(str);// 构造一个StringBuilder对象
        sb.insert(i, "\n");// 在指定的位置10，插入指定的字符串
        str = sb.toString();
        return str;
    }

    public static String instertChart(int j, int i, String str, String sr2) {
        StringBuilder sb = new StringBuilder(str);// 构造一个StringBuilder对象
        sb.replace(j, i, sr2);// 在指定的位置10，插入指定的字符串
        str = sb.toString();
        return str;
    }

    public static String isImg(String str) {
        if (!TextsUtils.isEmpty(str)) {
            String[] split = str.split("\\,");
            str = split[0];
        } else {
            str = "";
        }
        return str;
    }

    public static int sortChange(int str) {
        if (str == 0) {
            str = 1;
        } else {
            str = 0;
        }
        return str;
    }

    /**
     * 半角转换为全角
     *
     * @param str
     * @return
     */

    public static String ToDBC(String str) {

        char[] c = str.toCharArray();

        for (int i = 0; i < c.length; i++) {

            if (c[i] == 12288) {

                c[i] = (char) 32;

                continue;

            }

            if (c[i] > 65280 && c[i] < 65375)

                c[i] = (char) (c[i] - 65248);

        }

        return new String(c);

    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */

    public static String stringFilter(String str) {

        str = str.replaceAll("【", "[").replaceAll("】", "]")

                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号

        String regEx = "[『』]"; // 清除掉特殊字符

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        return m.replaceAll("").trim();

    }


    private void setEmojiToTextView() {
        int unicodeJoy = 0x1F602;
//        String unicodeJoy = "F09F9881";
        String emojiString = getEmojiStringByUnicode(unicodeJoy);
//        etContent.setText(emojiString);
    }

    private String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    /*方法二：推荐，速度最快
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static SpannableString span(String s) {
        SpannableString span = new SpannableString(s);
//        view.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint. STRIKE_THRU_TEXT_FLAG);
        span.setSpan(new StrikethroughSpan(), 0, span.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return span;
    }
}
