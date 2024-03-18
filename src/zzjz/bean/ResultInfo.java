package zzjz.bean;

import java.util.Date;

/**
 * @ClassName: Result
 * @Description: 回答结果列表实体类
 * @author guzhenggen
 * @version 2016/6/15 11:17
 */
public class ResultInfo {

    //成绩表ID
    private long achievementId;

    //题目ID
    private long itemId;

    //回答结果
    private String result;

    //得分
    private double score;

    //创建者(ID)
    private long creator;

    //创建时间
    private Date createTime;

    //更新时间
    private String updateTime;

    //删除标识,0:未删除,1:已删除
    private int deleteFlag;

    //题目信息
    private Item item;

    /**
     * 获取成绩表ID.
     *
     * @return long
     */
    public long getAchievementId() {
        return this.achievementId;
    }

    /**
     *
     * @return score
     */
    public double getScore() {
        return score;
    }

    /**
     *
     * @param score score
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * 设置成绩表ID.
     *
     * @param achievementId 成绩表ID
     */
    public void setAchievementId(long achievementId) {
        this.achievementId = achievementId;
    }

    /**
     * 获取题目ID.
     *
     * @return long
     */
    public long getItemId() {
        return this.itemId;
    }

    /**
     * 设置题目ID.
     *
     * @param itemId 题目ID
     */
    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    /**
     * 获取回答结果.
     *
     * @return String
     */
    public String getResult() {
        return this.result;
    }

    /**
     * 设置回答结果.
     *
     * @param result 回答结果
     */
    public void setResult(String result) {
        this.result = result;
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
     * @return String
     */
    public String getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置更新时间.
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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
     * 获取题目信息.
     *
     * @return Item
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * 设置题目信息.
     *
     * @param item 题目信息
     */
    public void setItem(Item item) {
        this.item = item;
    }

}
