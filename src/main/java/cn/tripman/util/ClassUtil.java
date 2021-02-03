package cn.tripman.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author hero
 */
public class ClassUtil {
    /**
     * 一个不容易出现的字符
     */
    private static final String replace = "§";

    /**
     * 获取字段的类型 ，List里面的类型
     */
    public static Class fieldClass(Field field) {
        if (field.getType() == java.util.List.class) {
            Type genericType = field.getGenericType();
            if (null == genericType) {
                return null;
            }
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                return (Class) pt.getActualTypeArguments()[0];
            }
            return null;
        } else {
            return field.getType();
        }
    }

    /**
     * 获取类、父类的字段的列表
     */
    public static <T> Set<Field> fieldList(Class<T> clazz) {
        Set<Field> fieldSet = new LinkedHashSet<>(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fieldSet.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        }
        return fieldSet;
    }

    /**
     * 获取method
     */
    public static Method getMethod(ProceedingJoinPoint point) {
        return ((MethodSignature) point.getSignature()).getMethod();
    }

    /**
     * 取出第i个list
     */
    public static int getListIndex(Object[] args) {
        if (args == null || args.length == 0) {
            return -1;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof List) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 空字符串和null会有冲突，这个默认忽略
     */
    public static String join(Object[] args) {
        StringBuilder builder = new StringBuilder();
        if (args == null || args.length == 0) {
            return builder.toString();
        }
        for (Object object : args) {
            if (object == null) {
                builder.append("_");
            } else {
                builder.append(object.toString()).append("_");
            }
        }
        return builder.substring(0, builder.length() - 1);
    }

    /**
     * 参数替换
     */
    public static <T> void replaceFirstList(Object[] args, List<T> list) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof List) {
                args[i] = list;
            }
        }
    }

    /**
     * 获取method返回的数据类型
     */
    public static Class getReturnClass(Method method) {
        if (method.getReturnType() == List.class) {
            return (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
        }
        return method.getReturnType();
    }

    /**
     * 参数转化
     */
    public static Map<Object, String> joinKeys(String pre, Object[] args) {
        Map<Object, String> keyMap = new LinkedHashMap<>();
        int index = ClassUtil.getListIndex(args);
        if (index == -1) {
            keyMap.put(pre, ClassUtil.join(args));
            return keyMap;
        }
        List<Object> list = (List<Object>) args[index];
        args[index] = replace;
        //list转 string 在替换 list节点的 string
        list.forEach(e -> keyMap.put(e, pre + "_" + ClassUtil.join(args).replace(replace, e == null ? "" : e.toString())));
        args[index] = list;
        return keyMap;
    }
}
