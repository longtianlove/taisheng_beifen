package com.taisheng.now.view;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by dragon on 2019/7/11.
 */

public class DividerItemDecoration  extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

//        //如果不是第一个，则设置top的值。
        if (parent.getChildAdapterPosition(view) != 0){
            //这里直接硬编码为1px
            outRect.top = 1;
        }
    }

}
