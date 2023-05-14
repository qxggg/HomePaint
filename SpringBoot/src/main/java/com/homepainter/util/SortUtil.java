package com.homepainter.util;



import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: SPF
 * @Date: 2020/8/17 14:27
 */
public class SortUtil {

    public static void main(String[] args) {
        List<String> sortList = new ArrayList<String>();
        sortList.add("1");
        sortList.add("002");
        sortList.add("002c");
        sortList.add("003a");
        sortList.add("2");
        sortList.add("2a");
        sortList.add("1a");
        sortList.add("2c");
        sortList.add("3a");
        sortList.add("11a");
        sortList.add("11");
        sortList.add("a");
        sortList.add("a11");
        sortList.add("a05");
        sortList.add("aa");
        sortList.add("ac");
        sortList.add("a1");
        sortList.add("a2");
        sortList.add("b");
        sortList.add("bd");
        sortList.add("10");
        sortList.add("100");
        sortList.add("101");
        sortList.add("a000001");
        sortList.add("a001");
        sortList.add("a1001");
        sortList.add("a10001");
        sortByFileName(sortList);
        for (String s : sortList) {
            System.out.println(s);
        }
    }

    public static void sortByFileName(List<String> sortList) {
        Collections.sort(sortList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // 去掉特殊字符
                o1 = removeSpecialStr(o1);
                o2 = removeSpecialStr(o2);
                // 去掉非数字开始的、共同的首字符串
                int prefixIndex = prefixIndex(o1, o2);
                o1 = removeSuffix(o1.substring(prefixIndex));
                o2 = removeSuffix(o2.substring(prefixIndex));
                // 保护代码，防止处理后的字符串出现空字符的情况
                if (StringUtils.isEmpty(o1) && StringUtils.isNotEmpty(o2)) {
                    return -1;
                }
                if (StringUtils.isNotEmpty(o1) && StringUtils.isEmpty(o2)) {
                    return 1;
                }
                if (StringUtils.isEmpty(o1) && StringUtils.isEmpty(o2)) {
                    return 0;
                }
                // 按照数字类型比较
                if (isNumeric(o1) && isNumeric(o2)) {
                    long result = Long.parseLong(o1) - Long.parseLong(o2);
                    if (result > 0) {
                        return 1;
                    }
                    if (result < 0) {
                        return -1;
                    }
                    if (o1.length() != o2.length()) {
                        return o1.length() - o2.length() < 0 ? 1 : -1;
                    }
                    return 0;
                }
                // 获取字符串首的数字字符串
                String numStr1 = getQuantity(o1);
                String numStr2 = getQuantity(o2);
                if (StringUtils.isNotEmpty(numStr1) && StringUtils.isNotEmpty(numStr2)) {
                    // System.out.print("按照首位数字比较");
                    long result = Long.parseLong(numStr1) - Long.parseLong(numStr2);
                    if (result > 0) {
                        return 1;
                    }
                    if (result < 0) {
                        return -1;
                    }
                }
                // 按照JAVA字符串排序
                return o1.compareTo(o2);
            }
        });
    }

    /**
     * 判断是否是数字
     *
     * @param str 需要判断的字符串
     * @return 结果
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 两个字符串共同的前缀(非数字)截止下标
     *
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 共同前缀截止下标
     */
    private static int prefixIndex(String s1, String s2) {
        int index = 0;
        // StringBuilder str = new StringBuilder();
        int min = Math.min(s1.length(), s2.length());
        char[] ch1 = s1.toCharArray();
        char[] ch2 = s2.toCharArray();
        for (int i = 0; i < min; i++) {
            if (ch1[i] == ch2[i] && !Character.isDigit(s1.charAt(i)) && !Character.isDigit(s2.charAt(i))) {
                // str.append(ch1[i]);
                continue;
            }
            index = i;
            break;
        }
        return index;
    }

    /**
     * 去掉文件名的后缀
     *
     * @param str 文件名
     * @return 无后缀的文件名
     */
    private static String removeSuffix(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        int lastIndex = str.lastIndexOf(".");
        if (lastIndex == -1) {
            return str;
        }
        return str.substring(0, lastIndex);
    }

    /**
     * 获取字符串前边的数字
     *
     * @param regular 待处理字符串
     * @return 结果
     */
    private static String getQuantity(String regular) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < regular.length(); i++) {
            char c = regular.charAt(i);
            if (Character.isDigit(c)) {
                result.append(c);
            } else {
                break;
            }
        }
        return result.toString();
    }

    /**
     * 去掉字符串中的所有符号
     * @param str 字符串
     * @return 去掉所有符号后的字符串
     */
    public static String removeSpecialStr(String str) {
        str = str.replaceAll("[\\s*|\t|\r|\n]", "");
        return str.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");
    }
}
