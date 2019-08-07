package com.example.demo.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.*;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 *  BeanCopyUtil
 */
public class BeanUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    public static <T extends CharSequence> T notEmpty(T str) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("assert source can not be empty");
        }
        return str;
    }

    public static <T> T notNull(T ref) {
        if (ref == null) {
            throw new IllegalArgumentException("assert source can not be null");
        }
        return ref;
    }

    /**
     * 将一个对象的值copy到新的对象中去
     * copy 过程中根据Mapping 映射到对应字段
     *
     * @param src
     * @param targetType
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copyProperties(S src, Class<T> targetType) {
        T target = BeanUtils.instantiateClass(targetType);
        //备注：这儿判断如果为null返回空对象，
        if (src == null) {
            return target;
        }
        copyProperties(src, target, false);
        return target;
    }

    public static <S, T> List<T> copyList(List<S> src, Class<T> targetType) {
        return copyList(src, targetType, true);
    }

    /**
     * 将一个集合的拷贝到另一个集合(集合中实体类型可不同)
     *
     * @param src
     * @param targetType
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyList(List<S> src, Class<T> targetType, boolean copyNull) {
        List<T> target = new ArrayList<>();

        if (!CollectionUtils.isEmpty(src)) {
            for (S s : src) {
                T t = BeanUtils.instantiateClass(targetType);
                copyProperties(s, t, copyNull);
                target.add(t);
            }
        }
        return target;
    }

    public static void copyProperties(Object source, Object target) throws BeansException {
        copyProperties(source, target, false);
    }

    public static void copyProperties(Object source, Object target, Boolean copyNull) throws BeansException {
        copyProperties(source, target, copyNull, null, (String[]) null);
    }

    private static void copyProperties(Object source, Object target, boolean copyNull, Class<?> editable, String... ignoreProperties)
            throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }


        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {

                String name = targetPd.getName();

                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), name);
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);

                            if (value != null && value instanceof List && !CollectionUtils.isEmpty((List) value)) {
                                Type[] genericParameterTypes = writeMethod.getGenericParameterTypes();
                                if (genericParameterTypes != null && genericParameterTypes.length > 0) {
                                    Type genericParameterType = genericParameterTypes[0];
                                    if (genericParameterType != null && genericParameterType instanceof ParameterizedTypeImpl) {
                                        Type[] actualTypeArguments = ((ParameterizedTypeImpl) genericParameterType).getActualTypeArguments();
                                        if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                                            if (actualTypeArguments[0] instanceof Class) {
                                                Class<?> actualTypeArgument = (Class<?>) actualTypeArguments[0];
                                                if (!actualTypeArgument.equals(((List) value).get(0).getClass())) {
                                                    value = copyList((List<?>) value, actualTypeArgument);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                ReflectionUtils.makeAccessible(writeMethod);
                            }

                            if (value != null && !ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                                Class<?> parameterType = writeMethod.getParameterTypes()[0];
                                if (parameterType != null) {
                                    if (parameterType.equals(Double.class)) {
                                        value = Double.parseDouble(String.valueOf(value));
                                    } else if (parameterType.equals(Integer.class)) {
                                        value = Integer.parseInt(String.valueOf(value));
                                    } else if (parameterType.equals(Long.class)) {
                                        value = Long.parseLong(String.valueOf(value));
                                    } else if (parameterType.equals(Short.class)) {
                                        value = Short.parseShort(String.valueOf(value));
                                    } else if (parameterType.equals(String.class)) {
                                        value = String.valueOf(value);
                                    } else if (parameterType.equals(BigDecimal.class)) {
                                        value = new BigDecimal(String.valueOf(value));
                                    } else if (parameterType.equals(Byte.class)) {
                                        value = Byte.valueOf(String.valueOf(value));
                                    } else {
                                        continue;
                                    }
                                }
                            }
                            if (value != null || copyNull) {
                                writeMethod.invoke(target, value);
                            }
                        } catch (Throwable ex) {
                            LOGGER.error("BeanUtil Throwable", ex);
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }
}