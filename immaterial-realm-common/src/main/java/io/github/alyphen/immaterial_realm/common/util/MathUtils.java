package io.github.alyphen.immaterial_realm.common.util;

public class MathUtils {

    private MathUtils() {}

    public static int max(int... nums) {
        if (nums.length == 0) return 0;
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            if (num > max) max = num;
        }
        return max;
    }
}
