package com.i.lubov.util;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class GlobalUtils {

    /**
     * 数据类型转换
     *
     * @param value
     * @param field
     * @return
     */
    public static Object convertDataType(String value, Field field) {
        Object convertValue;
        Class<?> fieldTypeClass = field.getType();
        if (fieldTypeClass.isAssignableFrom(value.getClass())) {
            convertValue = value;
        } else {
            convertValue = GeneralHelper.str2Object(fieldTypeClass, value);
        }
        return convertValue;
    }

    /**
     * 获取参数下所有字段，静态的除外
     *
     * @param clazz
     * @param stopClass
     * @return
     */
    public static List<Field> getDeclaredField(Class<?> clazz, Class<?> stopClass) {
        List<Field> list = Lists.newArrayList();
        for (; clazz != stopClass; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    list.add(field);
                }
            }
        }
        return list;
    }

    /**
     * 将字符串第一个字符转换为小写
     *
     * @param str
     * @return
     */
    public static String toLowerCaseFirst(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return Character.toLowerCase(str.charAt(0)) + str.substring(1);
        }
    }
}
