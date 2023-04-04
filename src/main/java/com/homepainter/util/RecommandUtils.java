package com.homepainter.util;

import java.util.ArrayList;
import java.util.List;

public interface RecommandUtils {

    public static int[] stringToInt(String source){
        String[] strArr = source.split(", ");
        strArr[0] = strArr[0].substring(1);
        strArr[1999] = strArr[1999].substring(0, strArr[1999].length() - 1);
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++)
            intArr[i] = Integer.parseInt(strArr[i]);
        return intArr;
    }

    public static String intToString(int[] source){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < source.length; ++i)
            list.add(source[i]);
        return list.toString();
    }
}
