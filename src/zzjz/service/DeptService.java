package zzjz.service;

import zzjz.bean.Dept;


import java.util.List;

/**
 * @ClassName: DeptService
 * @Description: 部门操作service接口定义
 * @author caixiaolong
 * @version 2016/6/6 10:47
 */
public interface DeptService {
    /**
     * 创建新部门.
     * @param dept 部门实体
     * @return true or false
     */
    public boolean addDept(Dept dept);

    /**
     * 根据部门ID删除部门.
     * @param deptId 部门Id
     * @return true or false
     */
    public boolean deleteDeptById(long deptId);

    /**
     * 修改部门.
     * @param dept 部门实体
     * @return true or false
     */
    public boolean updateDept(Dept dept);

    /**
     * 根据部门名获取部门信息.
     * @param dept 部门实体
     * @return 查询结果
     */
    public Dept getDeptByDeptName(Dept dept);

    /**
     * 根据部门Id获取部门信息.
     * @param deptId 部门Id
     * @return 查询结果
     */
    public Dept getDeptByDeptId(long deptId);

    /**
     * 获取部门信息List.
     * @return 查询结果
     */
    public List<Dept> getDeptList();

    /**
     * 根据单位ID获取部门信息List.
     * @param companyId 单位Id
     * @return 查询结果
     */
    public List<Dept> getDeptListByCompanyId(long companyId);

    /**
     * 根据部门Id获取用户个数.
     * @param deptId 部门Id
     * @return 查询结果
     */
    public int getUserCountByDeptId(long deptId);
}
