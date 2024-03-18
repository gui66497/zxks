package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: Achievement
 * @Description: 成绩实体类
 * @author guitang.fang
 * @version 2016/6/1 8:59
 */
@XmlRootElement
public class Achievement {

    //成绩表ID
    private long achievementId;

    //考试信息ID
    private long testInfoId;

    //参考人员ID
    private long userId;

    //考生个人得分
    private double userMark;

    //创建者ID
    private long creator;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //删除标识,0:未删除,1:已删除
    private int deleteFlag;

    //回答结果信息列表
    private List<ResultInfo> resultList;

    //回答结果信息数组
    private ResultInfo[] results;

    /**
     * 用户回答集合
     */
    List<Answer> answers;

    /**
     *
     * @return answers
     */
    public List<Answer> getAnswers() {
        return answers;
    }

    /**
     *
     * @param answers answers
     */
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    /**
     * 获取成绩表ID.
     * @return long
     */
    public long getAchievementId() {
        return this.achievementId;
    }

    /**
     * 设置成绩表ID.
     * @param achievementId 成绩表ID
     */
    public void setAchievementId(long achievementId) {
        this.achievementId = achievementId;
    }

    /**
     * 获取考试信息ID.
     * @return long
     */
    public long getTestInfoId() {
        return this.testInfoId;
    }

    /**
     * 设置考试信息ID.
     * @param testInfoId 考试信息ID
     */
    public void setTestInfoId(long testInfoId) {
        this.testInfoId = testInfoId;
    }

    /**
     * 获取参考人员ID.
     * @return long
     */
    public long getUserId() {
        return this.userId;
    }

    /**
     * 设置参考人员ID.
     * @param userId 参考人员ID
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * 获取考生个人得分
     * @return userMark
     */
    public double getUserMark() {
        return userMark;
    }

    /**
     * 设置考生个人得分
     * @param userMark userMark
     */
    public void setUserMark(double userMark) {
        this.userMark = userMark;
    }

    /**
     * 获取创建者ID.
     * @return long
     */
    public long getCreator() {
        return this.creator;
    }

    /**
     * 设置创建者ID.
     * @param creator 创建者ID
     */
    public void setCreator(long creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间.
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
     * 获取删除标识,0:未删除,1:已删除.
     * @return int
     */
    public int getDeleteFlag() {
        return this.deleteFlag;
    }

    /**
     * 设置删除标识,0:未删除,1:已删除.
     * @param deleteFlag 删除标识,0:未删除,1:已删除
     */
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * 获取回答结果信息列表.
     * @return List
     */
    public List<ResultInfo> getResultList() {
        return this.resultList;
    }

    /**
     * 设置回答结果信息列表.
     * @param resultList 回答结果信息列表
     */
    public void setResultList(List<ResultInfo> resultList) {
        this.resultList = resultList;
    }

    /**
     * 获取回答结果信息数组.
     * @return ResultInfo[]
     */
    public ResultInfo[] getResults() {
        if (results == null) {
            return null;
        } else {
            return (ResultInfo[]) results.clone();
        }
    }

    /**
     * 设置回答结果信息数组.
     * @param results 回答结果信息数组
     */
    public void setResults(ResultInfo[] results) {
        if (results == null) {
            this.results = null;
        } else {
            this.results = (ResultInfo[]) results.clone();
        }
    }
}
