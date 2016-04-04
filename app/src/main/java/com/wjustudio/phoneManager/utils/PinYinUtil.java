package com.wjustudio.phoneManager.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转拼音,依赖pinyin4j.jar包
 * @author songwenju
 *
 */
public class PinYinUtil {
	//单例
    private static HanyuPinyinOutputFormat format;
    public static String toPinyin(String s) {
        if(format == null) {
            format = new HanyuPinyinOutputFormat();
            //设置为大写
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            //设置为无声调
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        }
        StringBuilder sb = new StringBuilder();
        try {
        	//字符串转为数组
            char[] charArray = s.toCharArray();
            for(int i=0; i<charArray.length; i++) {
                //这里为数组,因为有多音字.
                String[] pinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(charArray[i], format);
                //进行容错处理.当汉字开始为其他字符时.
                if(pinyinStringArray!= null && pinyinStringArray.length>0) {
                    sb.append(pinyinStringArray[0]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
