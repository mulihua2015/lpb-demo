package com.example.demo;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * <strong>功能</strong>
 * <strong>1.中文转化为ping</strong>
 */
public class StringUtil {

    /**
     * 返回首字母
     */
    public static String converPinYinHeadChar(String str) {
        String convert = "";
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.converPinYinHeadChar("朱思道"));
    }
}
