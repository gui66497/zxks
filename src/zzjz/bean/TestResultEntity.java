package zzjz.bean;

import java.util.Date;

/**
 * @author 梅宏振
 * @version 2015年6月16日 下午7:32:29
 * @ClassName: TestResultEntity
 * @Description: 考试成绩实体类
 */
public class TestResultEntity {

    //考试ID
    private long testInfoId;

    //试卷ID
    private int testPaperId;

    //试卷名称
    private String testPaperName;

    //出卷模式
    private int testPaperModel;

    //试卷分类ID
    private int testPaperCategoryId;

    //试卷分类名称
    private String testPaperCategoryName;

    //总分
    private int totalScore;

    //平均分
    private double avgScore;

    //考试时长
    private int totalTime;

    //考试开始试卷
    private Date startTime;

    //考试结束试卷
    private Date endTime;

    //考试应参加人数
    private int totalUserNum;

    //考试实际参加人数
    private int testUserNum;

    /**
     * 获取考试ID.
     *
     * @return long
     */
    public long getTestInfoId() {
        return this.testInfoId;
    }

    /**
     * 设置考试ID.
     *
     * @param testInfoId 考试ID
     */
    public void setTestInfoId(long testInfoId) {
        this.testInfoId = testInfoId;
    }

    /**
     * 获取试卷ID.
     *
     * @return int
     */
    public int getTestPaperId() {
        return this.testPaperId;
    }

    /**
     * 设置试卷ID.
     *
     * @param testPaperId 试卷ID
     */
    public void setTestPaperId(int testPaperId) {
        this.testPaperId = testPaperId;
    }

    /**
     * 获取试卷名称.
     *
     * @return String
     */
    public String getTestPaperName() {
        return this.testPaperName;
    }

    /**
     * 设置试卷名称.
     *
     * @param testPaperName 试卷名称
     */
    public void setTestPaperName(String testPaperName) {
        this.testPaperName = testPaperName;
    }

    /**
     * 获取出卷模式.
     *
     * @return int
     */
    public int getTestPaperModel() {
        return this.testPaperModel;
    }

    /**
     * 设置出卷模式.
     *
     * @param testPaperModel 出卷模式
     */
    public void setTestPaperModel(int testPaperModel) {
        this.testPaperModel = testPaperModel;
    }

    /**
     * 获取试卷分类ID.
     *
     * @return int
     */
    public int getTestPaperCategoryId() {
        return this.testPaperCategoryId;
    }

    /**
     * 设置试卷分类ID.
     *
     * @param testPaperCategoryId 试卷分类ID
     */
    public void setTestPaperCategoryId(int testPaperCategoryId) {
        this.testPaperCategoryId = testPaperCategoryId;
    }

    /**
     * 获取试卷分类名称.
     *
     * @return String
     */
    public String getTestPaperCategoryName() {
        return this.testPaperCategoryName;
    }

    /**
     * 设置试卷分类名称.
     *
     * @param testPaperCategoryName 试卷分类名称
     */
    public void setTestPaperCategoryName(String testPaperCategoryName) {
        this.testPaperCategoryName = testPaperCategoryName;
    }

    /**
     * 获取总分.
     *
     * @return int
     */
    public int getTotalScore() {
        return this.totalScore;
    }

    /**
     * 设置总分.
     *
     * @param totalScore 总分
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * 获取平均分.
     *
     * @return double
     */
    public double getAvgScore() {
        return this.avgScore;
    }

    /**
     * 设置平均分.
     *
     * @param avgScore 平均分
     */
    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    /**
     * 获取考试时长.
     *
     * @return int
     */
    public int getTotalTime() {
        return this.totalTime;
    }

    /**
     * 设置考试时长.
     *
     * @param totalTime 考试时长
     */
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * 获取考试开始试卷.
     *
     * @return Date
     */
    public Date getStartTime() {
        if (startTime == null) {
            return null;
        } else {
            return (Date) startTime.clone();
        }
    }

    /**
     * 设置考试开始试卷.
     *
     * @param startTime 考试开始试卷
     */
    public void setStartTime(Date startTime) {
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = (Date) startTime.clone();
        }
    }

    /**
     * 获取考试结束试卷.
     *
     * @return Date
     */
    public Date getEndTime() {
        if (this.endTime == null) {
            return null;
        } else {
            return (Date) endTime.clone();
        }
    }

    /**
     * 设置考试结束试卷.
     *
     * @param endTime 考试结束试卷
     */
    public void setEndTime(Date endTime) {
        if (endTime == null) {
            this.endTime = null;
        } else {
            this.endTime = (Date) endTime.clone();
        }
    }

    /**
     * 获取考试应参加人数.
     *
     * @return int
     */
    public int getTotalUserNum() {
        return this.totalUserNum;
    }

    /**
     * 设置考试应参加人数.
     *
     * @param totalUserNum 考试应参加人数
     */
    public void setTotalUserNum(int totalUserNum) {
        this.totalUserNum = totalUserNum;
    }

    /**
     * 获取考试实际参加人数.
     *
     * @return int
     */
    public int getTestUserNum() {
        return this.testUserNum;
    }

    /**
     * 设置考试实际参加人数.
     *
     * @param testUserNum 考试实际参加人数
     */
    public void setTestUserNum(int testUserNum) {
        this.testUserNum = testUserNum;
    }

}
