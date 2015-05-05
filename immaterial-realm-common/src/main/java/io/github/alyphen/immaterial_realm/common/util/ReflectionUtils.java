package io.github.alyphen.immaterial_realm.common.util;

public class ReflectionUtils {

    private ReflectionUtils() {}

    public static boolean isSubclassOf(Class<?> clazz, Class<?> superClass) {
        return clazz.getSuperclass() != null && (clazz == superClass || isSubclassOf(clazz.getSuperclass(), superClass));
    }

}
