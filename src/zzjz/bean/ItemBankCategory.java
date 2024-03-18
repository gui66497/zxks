package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author guzhenggen
 * @version 2016/5/30 13:40
 * @ClassName: ItemBankCategory
 * @Description: 题库类型实体类
 */
@XmlRootElement
public class ItemBankCategory {

    //题库分类ID
    private long categoryId;

    //题库类型名称
    private String categoryName;

    //父类ID
    private long parentId;

    //创建者(ID)
    private long creator;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //删除标识,0:未删除,1:已删除
    private int deleteFlag;

    //题库数量
    private int counts;

    /**
     * 获取题库分类ID.
     *
     * @return long
     */
    public long getCategoryId() {
        return this.categoryId;
    }

    /**
     * 设置题库分类ID.
     *
     * @param categoryId 题库分类ID
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取题库类型名称.
     *
     * @return String
     */
    public String getCategoryName() {
        return this.categoryName;
    }

    /**
     * 设置题库类型名称.
     *
     * @param categoryName 题库类型名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取父类ID.
     *
     * @return long
     */
    public long getParentId() {
        return this.parentId;
    }

    /**
     * 设置父类ID.
     *
     * @param parentId 父类ID
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
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
     * 获取题库数量.
     * @return int
     */
    public int getCounts() {
        return this.counts;
    }

    /**
     * 设置题库数量.
     * @param counts 题库数量
     */
    public void setCounts(int counts) {
        this.counts = counts;
    }
}
