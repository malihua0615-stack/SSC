package com.example.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具类
 */
public class RandomUtils {

    /**
     * 在闭区间 [min, max] 中随机取一个整数（含两端）。
     *
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机整数
     * @throws IllegalArgumentException 当 min > max 时抛出
     */
    public static int randomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min should be <= max");
        }
        // ThreadLocalRandom 的 nextInt(origin, bound) 是左闭右开，所以传 max + 1
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * 在闭区间 [min, max] 中随机取不重复的整数列表。
     *
     * 实现说明：当区间长度不是非常大时（默认阈值 1_000_000），会先生成所有候选值并打乱，
     * 然后截取前 {@code count} 个值；当区间非常大时采用 HashSet 循环去重的方式以节省内存。
     *
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @param count 要取的数量，必须在 [0, max-min+1]
     * @return 长度为 count 的随机不重复整数列表（顺序为随机顺序）
     * @throws IllegalArgumentException 参数不合法时抛出
     */
    public static List<Integer> randomDistinctInts(int min, int max, int count) {
        if (min > max) {
            throw new IllegalArgumentException("min should be <= max");
        }
        long range = (long) max - (long) min + 1L;
        if (count < 0 || count > range) {
            throw new IllegalArgumentException("count must be between 0 and the size of the range");
        }

        List<Integer> result = new ArrayList<>(count);
        if (count == 0) {
            return result;
        }

        final long THRESHOLD = 1_000_000L;
        if (range <= THRESHOLD) {
            // 生成所有候选值，打乱，取前 count 个。适用于中小区间，速度快且简单。
            List<Integer> all = new ArrayList<>((int) range);
            for (int i = min; i <= max; i++) {
                all.add(i);
            }
            Collections.shuffle(all);
            result.addAll(all.subList(0, count));
            return result;
        }

        // 对于非常大的区间，使用 HashSet 循环生成不重复随机数以节省内存。
        Set<Integer> set = new HashSet<>(count);
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        while (set.size() < count) {
            int v = rnd.nextInt(min, max + 1);
            set.add(v);
        }
        result.addAll(set);
        // 顺序也是随机的，但为了更随机地打乱集合顺序，再 shuffle 一下
        Collections.shuffle(result);
        return result;
    }
}

