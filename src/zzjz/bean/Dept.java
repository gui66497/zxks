package zzjz.bean;

/**
 * @author caixiaolong
 * @version 2016/6/6 10:42
 * @ClassName: Dept
 * @Description: 部门实体类
 */
public class Dept {

    //用户Id
    private long deptId;

    //用户名称
    private String deptName;

    //上级部门Id
    private long parentId;

    //所属单位Id
    private long companyId;

    //创建者ID
    private long creator;

    //创建时间
    private String createTime;

    //更新时间
    private String updateTime;

    //是否删除
    private int deleteFlag;


    /**
     * 获取用户Id.
     *
     * @return long
     */
    public long getDeptId() {
        return this.deptId;
    }

    /**
     * 设置用户Id.
     *
     * @param deptId 用户Id
     */
    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取用户名称.
     *
     * @return String
     */
    public String getDeptName() {
        return this.deptName;
    }

    /**
     * 设置用户名称.
     *
     * @param deptName 用户名称
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * 获取上级部门Id.
     *
     * @return long
     */
    public long getParentId() {
        return this.parentId;
    }

    /**
     * 设置上级部门Id.
     *
     * @param parentId 上级部门Id
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取所属单位Id.
     *
     * @return long
     */
    public long getCompanyId() {
        return this.companyId;
    }

    /**
     * 设置所属单位Id.
     *
     * @param companyId 所属单位Id
     */
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取创建者ID..
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
