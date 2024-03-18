package zzjz.bean;

/**
 * @author zhengyunfeng
 * @version 2016/6/21 13:30
 * @ClassName: ChartCountEntity
 * @Description: 统计报表实体类
 */
public class ChartCountEntity {

    //人数
    private String personCount;

    //平均数
    private String avgCount;


    /**
     * 获取人数.
     *
     * @return String
     */
    public String getPersonCount() {
        return this.personCount;
    }

    /**
     * 设置人数.
     *
     * @param personCount 人数
     */
    public void setPersonCount(String personCount) {
        this.personCount = personCount;
    }

    /**
     * 获取平均数.
     *
     * @return String
     */
    public String getAvgCount() {
        return this.avgCount;
    }

    /**
     * 设置平均数.
     *
     * @param avgCount 平均数
     */
    public void setAvgCount(String avgCount) {
        this.avgCount = avgCount;
    }
}
