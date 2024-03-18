package zzjz.util;

/**
 * @ClassName: Constants
 * @Description: 常量类
 * @author 房桂堂
 * @version 2016/6/8 13:58
 */
public class Constants {

    /**
     * 试卷排序 列index对应的值.
     */
    public enum TestpaperColumn {

        /**
         * 试卷名称.
         */
        TESTPAPER_NAME("1"),

        /**
         * 创建时间.
         */
        CREATE_TIME("5"),

        /**
         * 更新时间.
         */
        UPDATE_TIME("6");

        private final String value;

        TestpaperColumn(String value1) {
            this.value = value1;
        }


        /**
         * 根据index获取英文名.
         * @param val 值
         * @return 值 or null
         */
        public static TestpaperColumn getName(String val) {
            for (TestpaperColumn value : values()) {
                if (val.equals(value.getValue())) {
                    return value;
                }
            }
            return null;
        }

        /**
         * 获取值.
         * @return value
         */
        public String getValue() {
            return value;
        }
    }
}
