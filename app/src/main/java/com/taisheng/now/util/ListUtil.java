package com.taisheng.now.util;

import java.util.HashSet;
import java.util.List;

public class ListUtil {
    /**
     * 去除重复数据
     * @param list
     * @return
     */
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
