package com.homepainter.util;

import java.util.Random;

public class RandomUtils {
    /**
     * 随机生成指定范围指定个数的正整数，取值范围0~2147483647
     *
     * @param number 个数（>=0）
     * @param min    最小（>=0）
     * @param max    最大（>=0）
     * @param repeat 允许重复
     * @return
     */
    public static int[] getRandomNumbers(int number, int min, int max, boolean repeat) {
        Random random = new Random();
        int[] array = new int[number];
        boolean[] flags = new boolean[max + 1];
        if (repeat) {
            for (int i = 0; i < array.length; i++) {
                array[i] = random.nextInt(max + 1 - min) + min;
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                do {
                    array[i] = random.nextInt(max + 1 - min) + min;
                } while (flags[array[i] - min]);
                flags[array[i] - min] = true;
            }
        }
        return array;
    }

    public static double generateRandomNumber() {
        return Math.random();
    }
}
