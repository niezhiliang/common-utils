package com.niezhiliang.common.utils.str;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/11/26 下午6:48
 */
public class StringTools {
    /**
     * 判断字符串是否为空或""
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str) || "null".equals(str);
    }

    /**
     * 判断对象是或否为空
     * @param object
     * @return
     */
    public static boolean isNullOrEmpty(Object object) {
        return object == null || "".equals(object);
    }
}
