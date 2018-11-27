package com.niezhiliang.common.utils.str;

import org.junit.Test;
import java.util.Date;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/11/26 下午6:52
 */
public class StringToolsTest {

    @Test
    public void testStr() {
        System.out.println(StringTools.isNullOrEmpty(""));
        System.out.println(StringTools.isNullOrEmpty(null));
        System.out.println(StringTools.isNullOrEmpty("null"));
    }


    @Test
    public void testObj() {
        Date date = null;
        System.out.println(StringTools.isNullOrEmpty(date));
        System.out.println(StringTools.isNullOrEmpty(new Date()));
    }

}