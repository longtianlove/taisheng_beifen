package com.taisheng.now.bussiness.bean.result;

import com.taisheng.now.bussiness.market.gouwuche.NewShoppingCartBean;

import java.util.ArrayList;
import java.util.List;

public class GouwucheResultBean {

   public List<NewShoppingCartBean> records=new ArrayList<>();
   public int total;
   public int size;
   public int current;
   public boolean searchCount;
   public int pages;

}
