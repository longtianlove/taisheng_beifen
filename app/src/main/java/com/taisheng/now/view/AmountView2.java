package com.taisheng.now.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.taisheng.now.R;
import com.th.j.commonlibrary.utils.TextsUtils;


/**
 *
 */
public class AmountView2 extends FrameLayout {
    private Context mContext;

    private View rootView;
    private EditText amountEt;
    private TextView reduceBtn;
    private TextView increaseBtn;
    private AttributeSet mAttrs;
    private int defaultNum = 1;

    //------------------------attrs-----------------------------
    private float textSize; // default 8sp
    private int currentValue; // default use minValue
    private int minValue = 1; // must no less than 1,default is 1
    private int maxValue = 9999; // default is 99

    private OnChangeListener mOnChangeListener;
    private OnChangeListener2 mOnChangeListener2;
    private OnChangeClick onChangeClick;
    //------------------------attrs-----------------------------

    public AmountView2(Context context) {
        this(context, null);
    }

    public AmountView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmountView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        this.mAttrs = attrs;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_amount_view, null);
        amountEt = (EditText) rootView.findViewById(R.id.amount_et);
        amountEt.setFocusable(false);
        amountEt.setFocusableInTouchMode(false);
//        amountEt.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        amountEt.setImeOptions(EditorInfo.IME_ACTION_SEND);
        amountEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // after changed, check minValue and maxValue. change button enable. call listener.
                if (TextUtils.isEmpty((amountEt.getText().toString()))) {
                    minValue = minValue;
                    amountEt.setHint(String.valueOf(minValue));
                } else {
                    int amount = Integer.parseInt(amountEt.getText().toString());

                    if (amount < minValue) {
                        minValue = minValue;
                        amountEt.setHint(String.valueOf(minValue));
                    } else if (amount > maxValue) {
                        minValue = minValue;
                        amountEt.setText(String.valueOf(maxValue));
                    } else {
                        if (amount > minValue) {
                            reduceBtn.setEnabled(true);
                        } else {
                            reduceBtn.setEnabled(false);
                        }
                        if (amount < maxValue) {
                            increaseBtn.setEnabled(true);
                        } else {
                            increaseBtn.setEnabled(false);
                        }
                        try {
                            if (defaultNum > 1) {
                                int k = Integer.parseInt(amountEt.getText().toString());
                                int i = inputeSet(defaultNum, k);
                                if (i != -1) {
                                    amountEt.setText(String.valueOf(i));
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (null != mOnChangeListener) {
                    mOnChangeListener.onChanged(amountEt, Integer.parseInt(amountEt.getText().toString()));
                }
                if (mOnChangeListener2 != null) {
//                                mOnChangeListener2.afterTextChanged(getCurrentValue()+"");
                }
                amountEt.setSelection(amountEt.getText().toString().length());
                return true;
            }
        });
        amountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mOnChangeListener2 != null) {
                    mOnChangeListener2.onTextChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // after changed, check minValue and maxValue. change button enable. call listener.
                if (TextUtils.isEmpty(s.toString())) {
                    minValue = minValue;
                    amountEt.setHint(String.valueOf(minValue));
                } else {
                    int amount = Integer.parseInt(amountEt.getText().toString());

                    if (amount < minValue) {
                        minValue = minValue;
                        amountEt.setHint(String.valueOf(minValue));
                    } else if (amount > maxValue) {
                        minValue = minValue;
                        amountEt.setText(String.valueOf(maxValue));
                    } else {
                        if (amount > minValue) {
                            reduceBtn.setEnabled(true);
                        } else {
                            reduceBtn.setEnabled(false);
                        }
                        if (amount < maxValue) {
                            increaseBtn.setEnabled(true);
                        } else {
                            increaseBtn.setEnabled(false);
                        }
                        try {
                            if (defaultNum > 1) {
                                int k = Integer.parseInt(amountEt.getText().toString());
                                int i = inputeSet(defaultNum, k);
                                if (i != -1) {
                                    amountEt.setText(String.valueOf(i));
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                            /*if (null != mOnChangeListener) {
                                mOnChangeListener.onChanged(amountEt,getCurrentValue());
                            }
                            if (mOnChangeListener2 != null) {
                                mOnChangeListener2.afterTextChanged(s);
                            }*/
                    }
                    amountEt.setSelection(amountEt.getText().toString().length());
                }

            }
        });

        reduceBtn = (TextView) rootView.findViewById(R.id.reduce_btn);
        reduceBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(amountEt.getText().toString()) > minValue) {
                    amountEt.setText(String.valueOf(Integer.parseInt(TextsUtils.isEmptys(amountEt.getText().toString(), "1")) - defaultNum));
                }

                if (onChangeClick != null) {
                    int amount = Integer.parseInt(amountEt.getText().toString());
                    onChangeClick.onChangeds(2, amount);
                }
            }
        });
        increaseBtn = (TextView) rootView.findViewById(R.id.increase_btn);
        increaseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Integer.parseInt(amountEt.getText().toString()) < maxValue) {
                amountEt.setText(String.valueOf(Integer.parseInt(TextsUtils.isEmptys(amountEt.getText().toString(), "0")) + defaultNum));
                if (onChangeClick != null) {
                    int amount = Integer.parseInt(amountEt.getText().toString());
                    onChangeClick.onChangeds(1, amount);
                }
//                }
            }
        });

        if (mAttrs != null) {
            TypedArray typedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.AmountView);

//        roundColor = typedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.GRAY);
//
//        progress = typedArray.getFloat(R.styleable.RoundProgressBar_progress, 0);

            textSize = typedArray.getDimension(R.styleable.AmountView_textSizes, 14);
            amountEt.setTextSize(textSize);
            reduceBtn.setTextSize(textSize);
            increaseBtn.setTextSize(textSize);


            minValue = typedArray.getInteger(R.styleable.AmountView_minValue, 0);
//            if (minValue < 1) {
//                minValue = 1;
//            }
            maxValue = typedArray.getInteger(R.styleable.AmountView_maxValue, 99);
            if (maxValue < minValue) {
                maxValue = minValue;
            }
            currentValue = typedArray.getInteger(R.styleable.AmountView_currentValue, minValue);
//
//        isShowProgress = typedArray.getBoolean(R.styleable.RoundProgressBar_isShowProgress, true);
//
//        titleStr = typedArray.getString(R.styleable.RoundProgressBar_titleStr);

            typedArray.recycle();
        }

        amountEt.setText(String.valueOf(currentValue));
        addView(rootView);
    }

    //----------------------------- get/set ----------------------------------------
    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        amountEt.setTextSize(textSize);
        reduceBtn.setTextSize(textSize);
        increaseBtn.setTextSize(textSize);
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        if (minValue > maxValue) {
            minValue = maxValue;
        }
        this.minValue = minValue;
        setCurrentValue(getCurrentValue());
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        if (maxValue < minValue) {
            maxValue = minValue;
        }
        this.maxValue = maxValue;
        setCurrentValue(getCurrentValue());
    }

    public void setOnChangeListener(OnChangeListener mOnChangeListener) {
        this.mOnChangeListener = mOnChangeListener;
    }

    public void setOnChangeListener2(OnChangeListener2 mOnChangeListener2) {
        this.mOnChangeListener2 = mOnChangeListener2;
    }

    public int getCurrentValue() {
        if (TextsUtils.isEmpty(amountEt.getText().toString())) {
            return minValue;
        }
        return Integer.parseInt(amountEt.getText().toString());
    }


    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
        amountEt.setText(String.valueOf(currentValue));
    }

    public void removeTextChanged(OnChangeListener2 mOnChangeListener2) {
        mOnChangeListener2 = null;
        this.mOnChangeListener2 = mOnChangeListener2;
    }
    //----------------------------- get/set ----------------------------------------

    private int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public void setOnChangeClick(OnChangeClick onChangeClick) {
        this.onChangeClick = onChangeClick;
    }

    public void setDefaultNum(int defaultNum) {
        this.defaultNum = defaultNum;
    }

    public interface OnChangeListener {
        void onChanged(EditText v, int value);
    }

    public interface OnChangeClick {
        void onChangeds(int type, int value);
    }


    public interface OnChangeListener2 {
        void onTextChanged();

        void afterTextChanged(Editable s);
    }

    /**
     * 设置 成倍数增加时 在输入的时候对输入数值的限制
     *
     * @param i 默认数值
     * @param k 输入的数值
     * @return
     */
    private int inputeSet(int i, int k) {
        if (k % i != 0) {
            int j = k / i;
            if (j != 0) {
                int n = j * i;
                int m = (j + 1) * i;
                if (Math.abs(n - k) >= Math.abs(m - k)) {
                    k = m;
                } else {
                    k = n;
                }
            } else {
                k = i;
            }
        } else {
            return -1;
        }
        return k;
    }
}
