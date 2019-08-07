package com.example.demo.access.utils;

import com.example.demo.access.domain.RPPublicArea;
import com.example.demo.access.utils.annotation.ConvertKey;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Locale.ENGLISH;

public class AccessDomainConvertUtils {

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";
    private static final String DOT = ".";

    /**
     * 转化List对象为HashMap对象.
     * key的值(xxx.index):
     * 1.若字段上设置了ConvertKey, 则取其value
     * 2.否则取其字段名做为key
     * value的值就是字段值.
     */
    public static <T> Map<String, String> convert(List<T> objList) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        if (CollectionUtils.isEmpty(objList)) {
            return null;
        }
        Map<String, String> map = new HashMap<>(objList.size() * 5);
        for (T o : objList) {
            Class<?> clazz = o.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Method method = getReadMethod(fields[i], clazz);
                if (method != null) {
                    Object value = method.invoke(o);
                    map.put(getKey(fields[i]) + DOT + i, value == null ? null : value.toString());
                }
            }
        }
        return map;
    }

    /**
     * 转化为单个对象
     */
    public static <T> Map<String, String> convert(T t) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        if (t == null) {
            return null;
        }
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> map = new HashMap<>(fields.length);
        for (Field field : fields) {
            Method method = getReadMethod(field, clazz);
            if (method != null) {
                Object value = method.invoke(t);
                map.put(getKey(field), value == null ? null : value.toString());
            }
        }
        return map;
    }

    /**
     * Gets the method that should be used to read the property value.
     *
     * @return The method that should be used to read the property value.
     * May return null if the field or clazz can't be read.
     */
    private static Method getReadMethod(Field field, Class<?> clazz) throws NoSuchMethodException {
        if (field == null || clazz == null) {
            return null;
        }
        String baseName = capitalize(field.getName());
        String readMethodName;
        Class<?> type = field.getType();
        if (type == boolean.class || type == null) {
            readMethodName = IS_PREFIX + baseName;
        } else {
            readMethodName = GET_PREFIX + baseName;
        }
        return clazz.getMethod(readMethodName);
    }

    /**
     * Returns a String which capitalizes the first letter of the string.
     */
    private static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

    /**
     * return a String which value is {@code ConvertKey annotation value} if is exist or field's name.
     */
    private static String getKey(Field field) {
        String fieldName;
        ConvertKey convertKey = field.getDeclaredAnnotation(ConvertKey.class);
        if (convertKey != null && !StringUtils.isEmpty(fieldName = convertKey.value())) {
            return fieldName;
        }
        return field.getName();
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<RPPublicArea> list = new ArrayList();
        RPPublicArea area = new RPPublicArea();
        list.add(area);
        System.out.println(AccessDomainConvertUtils.convert(list));
        System.out.println(AccessDomainConvertUtils.convert(area));
    }
}
