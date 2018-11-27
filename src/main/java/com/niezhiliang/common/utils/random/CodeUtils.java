package com.niezhiliang.common.utils.random;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/11/26 下午8:10
 */
public class CodeUtils {

    static String[] blend = new String[] { "0","1","2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
    };


    /**
     * 获取四位数字字母混合验证码
     * @return
     */
    public static String getBlendCode4() {
        StringBuffer code = null;
        while (true) {
            code = new StringBuffer();
            for (int i = 0;i < 4;i++) {
                code.append(blend[new Random().nextInt(62)]);
            }
            if (chkCode(code.toString())) {
                break;
            }
        }
        return code.toString();
    }

    /**
     * 获取六位数字字母混合验证码
     * @return
     */
    public static String getBlendCode6() {
        StringBuffer code = null;
        while (true) {
            code = new StringBuffer();
            for (int i = 0;i < 6;i++) {
                code.append(blend[new Random().nextInt(62)]);
            }
            if (chkCode(code.toString())) {
                break;
            }
        }
        return code.toString();
    }

    /**
     * 获取指定位数字字母混合验证码
     * @return
     */
    public static String getBlendCode(int length) {
        StringBuffer code = null;
        while (true) {
            code = new StringBuffer();
            for (int i = 0;i < length;i++) {
                code.append(blend[new Random().nextInt(62)]);
            }
            if (chkCode(code.toString())) {
                break;
            }
        }
        return code.toString();
    }

    /**
     * 获取四位纯数字的验证码
     * @return
     */
    public static String getNumCode4() {
        return new Random().nextInt(8999)+1000+"";
    }

    /**
     * 获取六位纯数字的验证码
     * @return
     */
    public static String getNumCode6() {
        return new Random().nextInt(899999)+100000+"";
    }

    /**
     * 正则表达式 验证字符串必须带数字大写小写 最少4位
     * @param mobiles
     * @return
     */
    public static boolean chkCode(String mobiles) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9]{4,}$");
        Matcher matcher = pattern.matcher(mobiles);
        boolean b = matcher.matches();
        return b;

    }
}
