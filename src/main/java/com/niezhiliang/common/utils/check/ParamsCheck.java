package com.niezhiliang.common.utils.check;

import com.niezhiliang.common.utils.str.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/11/26 下午6:45
 */
public class ParamsCheck {
    private final static Logger logger = LoggerFactory.getLogger(ParamsCheck.class);
    /**
     * 验证是否缺少参数
     * 多个参数之间以英文逗号隔开 eg: hasAllRequired(new Student(),"id,name,age")
     * @param obj
     * @param requiredColumns
     * @return
     */
    public static boolean hasAllRequired(Object obj,String requiredColumns) {
        if (!StringTools.isNullOrEmpty(requiredColumns)) {
            //验证字段非空
            String[] columns = requiredColumns.split(",");
            String missCol = "";
            for (String column : columns) {
                try {
                    Field field = obj.getClass().getDeclaredField(column.trim());
                    field.setAccessible(true);
                    // 获取属性的对应的值
                    Object value = field.get(obj);
                    if (StringTools.isNullOrEmpty(value)) {
                        missCol += column+",";
                    }
                } catch (Exception e) {
                    missCol += column+",";
                }
            }
            if (!StringTools.isNullOrEmpty(missCol)) {
                logger.warn("请求缺少参数:"+missCol.substring(0,missCol.length()-1));
                return false;
            }

        }
        return true;
    }
}
