package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author guzhenggen
 * @version 2016/6/1 15:06
 * @ClassName: ItemBank
 * @Description: 题库基本信息实体类
 */
@XmlRootElement
public class ItemBank {


    //题库信息ID
    private long itemBankId;

    //题库类型名称
    private String itemBankName;

    //题库描述信息
    private String itemBankDescription;

    //分类ID
    private long categoryId;

    //创建者(ID)
    private long creator;

    //创建时间
    private Date createTime;

    //更新时间
    private String updateTime;

    //删除标识,0:未删除,1:已删除
    private int deleteFlag;

    //题目数
    private int itemCount;

    /**
     * 获取题库信息ID.
     *
     * @return long
     */
    public long getItemBankId() {
        return this.itemBankId;
    }

    /**
     * 设置题库信息ID.
     *
     * @param itemBankId 题库信息ID
     */
    public void setItemBankId(long itemBankId) {
        this.itemBankId = itemBankId;
    }

    /**
     * 获取题库类型名称.
     *
     * @return String
     */
    public String getItemBankName() {
        return this.itemBankName;
    }

    /**
     * 设置题库类型名称.
     *
     * @param itemBankName 题库类型名称
     */
    public void setItemBankName(String itemBankName) {
        this.itemBankName = itemBankName;
    }

    /**
     * 获取题库描述信息.
     *
     * @return String
     */
    public String getItemBankDescription() {
        return this.itemBankDescription;
    }

    /**
     * 设置题库描述信息.
     *
     * @param itemBankDescription 题库描述信息
     */
    public void setItemBankDescription(String itemBankDescription) {
        this.itemBankDescription = itemBankDescription;
    }

    /**
     * 获取分类ID.
     *
     * @return long
     */
    public long getCategoryId() {
        return this.categoryId;
    }

    /**
     * 设置分类ID.
     *
     * @param categoryId 分类ID
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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
     * 获取题目数.
     *
     * @return int
     */
    public int getItemCount() {
        return this.itemCount;
    }

    /**
     * 设置题目数.
     *
     * @param itemCount 题目数
     */
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

}
