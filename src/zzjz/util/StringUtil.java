package zzjz.util;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author 郑雲峰
 * @version 2016/6/14 14:26
 * @ClassName: NumberUtil
 * @Description: String常用方法
 */
public class StringUtil {
    private static final int MAX_RECORDS_LIMIT = 800;

    /**
     *  getInCondition.
     * @param filed filed
     * @param list list
     * @return String
     */
    public static String getInCondition(String filed, List list) {
        StringBuilder result = new StringBuilder(" (");

        int round = (list.size() % MAX_RECORDS_LIMIT) == 0 ?
                (list.size() / MAX_RECORDS_LIMIT) :
                (list.size() / MAX_RECORDS_LIMIT) + 1;

        for (int i = 0; i < round; i++) {
            int fromIndex = i * MAX_RECORDS_LIMIT;
            int toIndex = ((i + 1) * MAX_RECORDS_LIMIT) > list.size() ?
                    list.size() : ((i + 1) * MAX_RECORDS_LIMIT);
            List subList = list.subList(fromIndex, toIndex);
            if (0 == i) {
                result.append(filed).append(" in(").append(
                        listConvertToString(subList, ",")).append(") ");
            } else {
                result.append(" or ").append(filed).append(" in(").append(
                        listConvertToString(subList, ",")).append(") ");
            }
        }
        return result.append(")").toString();
    }

    /**
     * @param <T> T
     * @param list 列表
     * @param separator 分隔
     * @return  String
     * @Title: listConvertToString
     * @Description: 将List转换为以指定符号分隔的字符串
     */
    public static<T> String listConvertToString(List<T> list, String separator) {
        StringBuilder result = new StringBuilder("");
        int i = 0;
        if (null == list) {
            return result.toString();
        }
        for (T it : list) {
            if (i > 0) {
                result.append(separator).append(it);
            } else {
                result.append(it);
            }
            i++;
        }
        return result.toString();
    }

    /**
     * 转义下划线和百分号.
     * @param str str
     * @return str
     */
    public static String zhuanyi(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        //转义百分号和下划线
        if (str.contains("_")) {
            str = str.replaceAll("_", "\\\\" + "_");
        }
        if (str.contains("%")) {
            str = str.replaceAll("%", "\\\\" + "%");
        }
        return str;
    }
}
