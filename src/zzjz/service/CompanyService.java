package zzjz.service;

import zzjz.bean.Company;

import java.util.List;

/**
 * @author caixiaolong
 * @version 2016/6/6 9:12
 * @ClassName: CompanyService
 * @Description: 单位操作service接口定义
 */
public interface CompanyService {
    /**
     * 创建新单位.
     *
     * @param company 单位实体
     * @return true or false
     */
    public boolean addCompany(Company company);

    /**
     * 根据单位ID删除单位.
     *
     * @param companyId 单位Id
     * @return true or false
     */
    public boolean deleteCompanyById(long companyId);

    /**
     * 修改单位.
     *
     * @param company 单位实体
     * @return true or false
     */
    public boolean updateCompany(Company company);

    /**
     * 根据单位名获取单位信息.
     *
     * @param companyName 单位名
     * @return 查询信息
     */
    public Company getCompanyByCompanyName(String companyName);

    /**
     * 根据单位Id获取单位信息.
     *
     * @param companyId 单位Id
     * @return 查询结果
     */
    public Company getCompanyByCompanyId(long companyId);

    /**
     * 获取单位信息List.
     *
     * @return 查询结果
     */
    public List<Company> getCompanyList();

    /**
     * 根据单位Id获取用户个数.
     *
     * @param companyId 单位Id
     * @return 查询结果
     */
    public int getUserCountByCompanyId(long companyId);
}
