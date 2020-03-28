package com.th.j.commonlibrary.wight;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.th.j.commonlibrary.R;

public class RoundImgView extends ImageView {

    private Paint paint;
    private int roundWidth = 10;
    private int roundHeight = 10;
    private boolean rightUpCorner = true;
    private boolean leftUpCorner = true;
    private boolean rightDownCorner = true;
    private boolean leftDownCorner = true;
    private Paint paint2;
    private static Context context;

    public RoundImgView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        if(attrs != null) {
            //Point1
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImgView);
            roundWidth= a.getDimensionPixelSize(R.styleable.RoundImgView_roundWidth2, roundWidth);
            roundHeight= a.getDimensionPixelSize(R.styleable.RoundImgView_roundHeight2, roundHeight);
            rightUpCorner = a.getBoolean(R.styleable.RoundImgView_rightUpCorner,true);
            rightDownCorner = a.getBoolean(R.styleable.RoundImgView_rightDownCorner,true);
            leftUpCorner = a.getBoolean(R.styleable.RoundImgView_leftUpCorner,true);
            leftDownCorner = a.getBoolean(R.styleable.RoundImgView_leftDownCorner,true);
        }else {
            //Point2
            float density = context.getResources().getDisplayMetrics().density;
            roundWidth = (int) (roundWidth*density);
            roundHeight = (int) (roundHeight*density);
        }

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        //Point3
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void draw(Canvas canvas) {
        //Point4
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        try {
            Canvas canvas2 = new Canvas(bitmap);
            //Point5
            super.draw(canvas2);
            if(leftUpCorner){
                //Point6
                drawLiftUp(canvas2);
            }
            if(leftDownCorner){
                drawLiftDown(canvas2);
            }
            if(rightUpCorner){
                drawRightUp(canvas2);
            }
            if(rightDownCorner){
                drawRightDown(canvas2);
            }
            //Point7
            canvas.drawBitmap(bitmap, 0, 0, paint2);
            bitmap.recycle();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        path.arcTo(new RectF(
                        0,
                        0,
                        roundWidth * 2,
                        roundHeight * 2),
                -90,
                -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLiftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight()-roundHeight);
        path.lineTo(0, getHeight());
        path.lineTo(roundWidth, getHeight());
        path.arcTo(new RectF(
                        0,
                        getHeight()-roundHeight*2,
                        0+roundWidth*2,
                        getHeight()),
                90,
                90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth()-roundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight()-roundHeight);
        path.arcTo(new RectF(
                getWidth()-roundWidth*2,
                getHeight()-roundHeight*2,
                getWidth(),
                getHeight()), 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), roundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth()-roundWidth, 0);
        path.arcTo(new RectF(
                        getWidth()-roundWidth*2,
                        0,
                        getWidth(),
                        0+roundHeight*2),
                -90,
                90);
        path.close();
        canvas.drawPath(path, paint);
    }

}
