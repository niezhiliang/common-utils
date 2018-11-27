package com.niezhiliang.common.utils.random;

import java.util.UUID;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/11/26 下午7:58
 */
public class UuidUtils {

    /**
     * 获取uuid带-
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }


    /**
     * 获取uuid不带-
     * @return
     */
    public static String getUUIDNoSlash() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
