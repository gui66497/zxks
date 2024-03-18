package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author guzhenggen
 * @version 2016/6/3 11:22
 * @ClassName: itemOption
 * @Description: 题目选项实体类
 */
@XmlRootElement
public class ItemOption {


    //选项ID
    private long optionId;

    //选项内容
    private String content;

    //题目ID
    private long itemId;

    //创建者(ID)
    private long creator;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //删除标识,0:未删除,1:已删除
    private int deleteFlag;

    /**
     * 获取选项ID.
     *
     * @return long
     */
    public long getOptionId() {
        return this.optionId;
    }

    /**
     * 设置选项ID.
     *
     * @param optionId 选项ID
     */
    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    /**
     * 获取选项内容.
     *
     * @return String
     */
    public String getContent() {
        return this.content;
    }

    /**
     * 设置选项内容.
     *
     * @param content 选项内容
     */
    public void setContent(String content) {
        this.content = content;
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
        if (this.updateTime == null) {
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

}
