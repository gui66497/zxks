package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhuxiaoxiao
 * @version 2016/5/30 14:51
 * @ClassName: TestInfo
 * @Description: 考试信息
 */
@XmlRootElement
public class TestInfo implements Serializable {

    //考试信息ID
    private long id;

    //试卷ID
    private String testId;

    //试卷名称
    private String testpaperName;

    //试卷类别
    private String testCategory;

    //试卷类别Id
    private long testCategoryId;

    //考生ID列表
    private List<Long> userIdList;

    //考生列表
    private List<User> userList;

    //试卷总分
    private int totalMark;

    //考生分数
    private int userMark;

    //考生总人数
    private int totalUserNo;

    //未参加考试人数
    private int noTestUserNo;

    //参加考试总人数
    private int testUserNo;

    //完成考试人数
    private int completeUserNo;

    //成绩ID
    private String achievementId;

    //考试状态,0:未开考,1:开考中,2：已完成,-1:显示全部
    private int testStatus;

    //考试开始时间
    private String startTime;

    //考试结束时间
    private String endTime;

    //考试时长
    private int testHour;

    //出题方式
    private int testPaperMode;

    //创建者(ID)
    private long creator;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //百分制得分
    private int percentMark;

    //删除标识,0:未删除,1:已删除
    private int deleteFlag;

    //参考人数名称
    private String testpeopleName;

    //参考人的Id
    private String testpeopleId;

    /**
     * 获取考试信息ID.
     *
     * @return long
     */
    public long getId() {
        return this.id;
    }

    /**
     * 设置考试信息ID.
     *
     * @param id 考试信息ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取试卷ID.
     *
     * @return String
     */
    public String getTestId() {
        return this.testId;
    }

    /**
     * 设置试卷ID.
     *
     * @param testId 试卷ID
     */
    public void setTestId(String testId) {
        this.testId = testId;
    }

    /**
     * 获取试卷名称.
     *
     * @return String
     */
    public String getTestpaperName() {
        return this.testpaperName;
    }

    /**
     * 设置试卷名称.
     *
     * @param testpaperName 试卷名称
     */
    public void setTestpaperName(String testpaperName) {
        this.testpaperName = testpaperName;
    }

    /**
     * 获取试卷类别.
     *
     * @return String
     */
    public String getTestCategory() {
        return this.testCategory;
    }

    /**
     * 设置试卷类别.
     *
     * @param testCategory 试卷类别
     */
    public void setTestCategory(String testCategory) {
        this.testCategory = testCategory;
    }

    /**
     * 获取试卷类别Id.
     *
     * @return long
     */
    public long getTestCategoryId() {
        return this.testCategoryId;
    }

    /**
     * 设置试卷类别Id.
     *
     * @param testCategoryId 试卷类别Id
     */
    public void setTestCategoryId(long testCategoryId) {
        this.testCategoryId = testCategoryId;
    }

    /**
     * 获取考生ID列表.
     *
     * @return List
     */
    public List<Long> getUserIdList() {
        return this.userIdList;
    }

    /**
     * 设置考生ID列表.
     *
     * @param userIdList 考生ID列表
     */
    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    /**
     * 获取考生列表.
     *
     * @return List
     */
    public List<User> getUserList() {
        return this.userList;
    }

    /**
     * 设置考生列表.
     *
     * @param userList 考生列表
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    /**
     * 获取试卷总分.
     *
     * @return int
     */
    public int getTotalMark() {
        return this.totalMark;
    }

    /**
     * 设置试卷总分.
     *
     * @param totalMark 试卷总分
     */
    public void setTotalMark(int totalMark) {
        this.totalMark = totalMark;
    }

    /**
     * 获取考生分数.
     *
     * @return int
     */
    public int getUserMark() {
        return this.userMark;
    }

    /**
     * 设置考生分数.
     *
     * @param userMark 考生分数
     */
    public void setUserMark(int userMark) {
        this.userMark = userMark;
    }

    /**
     * 获取考生总人数.
     *
     * @return int
     */
    public int getTotalUserNo() {
        return this.totalUserNo;
    }

    /**
     * 设置考生总人数.
     *
     * @param totalUserNo 考生总人数
     */
    public void setTotalUserNo(int totalUserNo) {
        this.totalUserNo = totalUserNo;
    }

    /**
     * 获取未参加考试人数.
     *
     * @return int
     */
    public int getNoTestUserNo() {
        return this.noTestUserNo;
    }

    /**
     * 设置未参加考试人数.
     *
     * @param noTestUserNo 未参加考试人数
     */
    public void setNoTestUserNo(int noTestUserNo) {
        this.noTestUserNo = noTestUserNo;
    }

    /**
     * 获取参加考试总人数.
     *
     * @return int
     */
    public int getTestUserNo() {
        return this.testUserNo;
    }

    /**
     * 设置参加考试总人数.
     *
     * @param testUserNo 参加考试总人数
     */
    public void setTestUserNo(int testUserNo) {
        this.testUserNo = testUserNo;
    }

    /**
     * 获取完成考试人数.
     *
     * @return int
     */
    public int getCompleteUserNo() {
        return this.completeUserNo;
    }

    /**
     * 设置完成考试人数.
     *
     * @param completeUserNo 完成考试人数
     */
    public void setCompleteUserNo(int completeUserNo) {
        this.completeUserNo = completeUserNo;
    }

    /**
     * 获取成绩ID.
     *
     * @return String
     */
    public String getAchievementId() {
        return this.achievementId;
    }

    /**
     * 设置成绩ID.
     *
     * @param achievementId 成绩ID
     */
    public void setAchievementId(String achievementId) {
        this.achievementId = achievementId;
    }

    /**
     * 获取考试状态,0:未开考,1:开考中,2：已完成,-1:显示全部.
     *
     * @return int
     */
    public int getTestStatus() {
        return this.testStatus;
    }

    /**
     * 设置考试状态,0:未开考,1:开考中,2：已完成,-1:显示全部.
     *
     * @param testStatus 考试状态,0:未开考,1:开考中,2：已完成,-1:显示全部
     */
    public void setTestStatus(int testStatus) {
        this.testStatus = testStatus;
    }

    /**
     * 获取考试开始时间.
     *
     * @return String
     */
    public String getStartTime() {
        return this.startTime;
    }

    /**
     * 设置考试开始时间.
     *
     * @param startTime 考试开始时间
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取考试结束时间.
     *
     * @return String
     */
    public String getEndTime() {
        return this.endTime;
    }

    /**
     * 设置考试结束时间.
     *
     * @param endTime 考试结束时间
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取考试时长.
     *
     * @return int
     */
    public int getTestHour() {
        return this.testHour;
    }

    /**
     * 设置考试时长.
     *
     * @param testHour 考试时长
     */
    public void setTestHour(int testHour) {
        this.testHour = testHour;
    }

    /**
     * 获取出题方式.
     *
     * @return int
     */
    public int getTestPaperMode() {
        return this.testPaperMode;
    }

    /**
     * 设置出题方式.
     *
     * @param testPaperMode 出题方式
     */
    public void setTestPaperMode(int testPaperMode) {
        this.testPaperMode = testPaperMode;
    }

    /**
     * 获取创建者(ID).
     *
     * @return long
     */
    public long getCreator() {
        return this.creator;
    }

    /**
     * 设置创建者(ID).
     *
     * @param creator 创建者(ID)
     */
    public void setCreator(long creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间.
     *
     * @return Date
     */
    public Date getCreateTime() {
        if (createTime == null) {
            return null;
        } else {
            return (Date) createTime.clone();
        }
    }

    /**
     * 设置创建时间.
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        if (createTime == null) {
            this.createTime = null;
        } else {
            this.createTime = (Date) createTime.clone();
        }
    }

    /**
     * 获取更新时间.
     *
     * @return Date
     */
    public Date getUpdateTime() {
        if (updateTime == null) {
            return null;
        } else {
            return (Date) updateTime.clone();
        }
    }

    /**
     * 设置更新时间.
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        if (updateTime == null) {
            this.updateTime = null;
        } else {
            this.updateTime = (Date) updateTime.clone();
        }
    }

    /**
     * 获取百分制得分.
     *
     * @return int
     */
    public int getPercentMark() {
        return this.percentMark;
    }

    /**
     * 设置百分制得分.
     *
     * @param percentMark 百分制得分
     */
    public void setPercentMark(int percentMark) {
        this.percentMark = percentMark;
    }

    /**
     * 获取删除标识,0:未删除,1:已删除.
     *
     * @return int
     */
    public int getDeleteFlag() {
        return this.deleteFlag;
    }

    /**
     * 设置删除标识,0:未删除,1:已删除.
     *
     * @param deleteFlag 删除标识,0:未删除,1:已删除
     */
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * 获取参考人数名称.
     *
     * @return String
     */
    public String getTestpeopleName() {
        return this.testpeopleName;
    }

    /**
     * 设置参考人数名称.
     *
     * @param testpeopleName 参考人数名称
     */
    public void setTestpeopleName(String testpeopleName) {
        this.testpeopleName = testpeopleName;
    }

    /**
     * 获取参考人的Id.
     *
     * @return String
     */
    public String getTestpeopleId() {
        return this.testpeopleId;
    }

    /**
     * 设置参考人的Id.
     *
     * @param testpeopleId 参考人的Id
     */
    public void setTestpeopleId(String testpeopleId) {
        this.testpeopleId = testpeopleId;
    }

}
