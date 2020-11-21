package cn.tripman.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hero
 */
public class ClassUtil {

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

}
