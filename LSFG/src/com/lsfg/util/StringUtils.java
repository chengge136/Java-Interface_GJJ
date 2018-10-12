package com.lsfg.util;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

public class StringUtils {
	protected StringUtils() {
	}
	
	/**
	 * 查看字符序列是否为空.
	 * @param args
	 * @return
	 */
	public static boolean isEmpty(String args){
		return org.apache.commons.lang3.StringUtils.isEmpty(args);
	}
	
	/**
	 * 查看字符序列是否为空或者有空格.
	 * @param args
	 * @return
	 */
	public static boolean isBlank(String args) {
        return org.apache.commons.lang3.StringUtils.isBlank(args);
    }

	/**
	 * 查看字符序列不为空或没有空格.
	 * @param args
	 * @return
	 */
    public static boolean isNotBlank(String args) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(args);
    }

    /**
     * 将字符串首字符大写.
     * @param args
     * @return
     */
    public static String capitalize(String args) {
        return org.apache.commons.lang3.StringUtils.capitalize(args);
    }

    /**
     * 截取字符串.
     * @param args 要截取的字符串
     * @param offset 开始位置
     * @param limit   截取个数
     * @return
     */
    public static String substring(String args, int offset, int limit) {
        return org.apache.commons.lang3.StringUtils.substring(args, offset, limit);
    }

    /**
     * 截取字符串，获得指定字符之前的部分.
     * @param args 要截取的字符串
     * @param token 指定的字符
     * @return
     */
    public static String substringBefore(String args, String token) {
        return org.apache.commons.lang3.StringUtils.substringBefore(args, token);
    }

    /**
     * 截取字符串，获得指定字符之后的部分.
     * @param args 要截取的字符串
     * @param token 指定的字符
     * @return
     */
    public static String substringAfter(String args, String token) {
        return org.apache.commons.lang3.StringUtils.substringAfter(args, token);
    }

    
    /**
     * 按照指定字符分割字符串.
     * @param args 要分割的字符串
     * @param separator 分隔符
     * @return  字符串数组
     */
    public static String[] splitByWholeSeparator(String args, String separator) {
        return org.apache.commons.lang3.StringUtils.splitByWholeSeparator(args, separator);
    }
    
    /**
     * 将指定list中的元素，按照制定的字符串拼接成字符串.
     * @param list 需要拼接的list
     * @param separator 拼接字符
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static String join(List list, String separator) {
        return org.apache.commons.lang3.StringUtils.join(list, separator);
    }

    public static String escapeHtml(String args) {
        return StringEscapeUtils.escapeHtml4(args);
    }

    public static String unescapeHtml(String args) {
        return StringEscapeUtils.unescapeHtml4(args);
    }

    public static String escapeXml(String args) {
        return StringEscapeUtils.escapeXml11(args);
    }

    public static String unescapeXml(String args) {
        return StringEscapeUtils.unescapeXml(args);
    }

    public static String trim(String args) {
        if (args == null) {
            return args;
        }

        // Unicode Character 'NO-BREAK SPACE' (U+00A0)
        args = args.replace("" + ((char) 160), " ");
        // Unicode Character 'ZERO WIDTH SPACE' (U+200B).
        args = args.replace("" + ((char) 8203), " ");

        args = org.apache.commons.lang3.StringUtils.trim(args);
        args = org.apache.commons.lang3.StringUtils.strip(args, "　");

        return args;
    }
    
    public static String spliceString(String code, String msg){
    	 StringBuilder resultData = new StringBuilder();
    	 resultData.append("{\"+code+\":").append("\""+code+"\""+",") .append("\"msg\":").append("\""+msg+"\"").append("}");
    	return resultData.toString();
    }
}
