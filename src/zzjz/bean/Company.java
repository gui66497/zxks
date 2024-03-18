package zzjz.bean;

/**
 * @author 蔡小龙
 * @version 2016/6/3 16:03
 * @ClassName: Company
 * @Description: 部门实体类
 */
public class Company {

    //单位Id
    private long companyId;

    //单位名称
    private String companyName;

    //创建者ID
    private long creator;

    //创建时间
    private String createTime;

    //更新时间
    private String updateTime;

    //是否删除
    private int deleteFlag;


    /**
     * 获取单位Id.
     *
     * @return long
     */
    public long getCompanyId() {
        return this.companyId;
    }

    /**
     * 设置单位Id.
     *
     * @param companyId 单位Id
     */
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取单位名称.
     *
     * @return String
     */
    public String getCompanyName() {
        return this.companyName;
    }

    /**
     * 设置单位名称.
     *
     * @param companyName 单位名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取创建者ID.
     *
     * @return long
     */
    public long getCreator() {
        return this.creator;
    }

    /**
     * 设置创建者ID.
     *
     * @param creator 创建者ID
     */
    public void setCreator(long creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间.
     *
     * @return String
     */
    public String getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置创建时间.
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
     * 获取是否删除.
     *
     * @return int
     */
    public int getDeleteFlag() {
        return this.deleteFlag;
    }

    /**
     * 设置是否删除.
     *
     * @param deleteFlag 是否删除
     */
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
