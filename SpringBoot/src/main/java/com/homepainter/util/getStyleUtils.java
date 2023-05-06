package com.homepainter.util;

public class getStyleUtils {

    public static String getStyle(int styleId){
        String res = "其他";
        switch (styleId){
            case 1 : res = "现代"; break;
            case 2 : res = "日式"; break;
            case 3 : res = "复古"; break;
            case 4 : res = "地中海"; break;
            case 5 : res = "韩式"; break;
            case 6 : res = "轻奢华"; break;
            case 7 : res = "极简主义"; break;
            case 8 : res = "工业"; break;
            case 9 : res = "北欧"; break;
            case 10 : res = "美式"; break;
            case 11 : res = "新中式"; break;
            case 12 : res = "欧洲"; break;
            case 13 : res = "东南亚"; break;
            case 14 : res = "新古典主义"; break;
            case 15 : res = "中国风"; break;
            case 16 : res = "明清"; break;
            case 17 : res = "其他"; break;
            case 18 : res = "儿童"; break;
            case 0 : res = "古典欧洲"; break;
            default : res = "其他";
        }
        return res;
    }

    public static int getId(String style){
        int id = 17;
        switch (style){
            case "现代" : id = 1; break;
            case "日式" : id = 2; break;
            case "复古" : id = 3; break;
            case "地中海" : id = 4; break;
            case "韩式" : id = 5; break;
            case "轻奢华" : id = 6; break;
            case "极简主义" : id = 7; break;
            case "工业" : id = 8; break;
            case "北欧" : id = 9; break;
            case "美式" : id = 10; break;
            case "新中式" : id = 11; break;
            case "欧洲" : id = 12; break;
            case "东南亚" : id = 13; break;
            case "新古典主义" : id = 14; break;
            case "中国风" : id = 15; break;
            case "明清" : id = 16; break;
            case "其他" : id = 17; break;
            case "儿童" : id = 18; break;
            case "古典欧洲" : id = 0; break;
            default: id = 17;
        }
        return id;
    }
}
