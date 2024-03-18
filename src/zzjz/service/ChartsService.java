package zzjz.service;

import zzjz.bean.ChartCountEntity;


import java.util.List;

/**
 * @author zhengyunfeng
 * @version 2016/6/21 8:43
 * @ClassName: ChartsService
 * @Description: 统计分析接口类
 */
public interface ChartsService {
    /**
     * 根据实体类的parentId,companyId获取需要统计的数据.
     *
     * @param paperId    试卷ID
     * @param userIdList 用户IdList
     * @return 统计数据
     */
    public List<ChartCountEntity> returnChartInfo(String paperId, List<String> userIdList);

    /**
     * 根据单位实体类的companyId取得该单位下每个部门的人员.
     *
     * @param companyId 单位Id
     * @return 统计数据
     */
    public List<String> getUserInfo(String companyId);

    /**
     * 根据单位ID获取部门下用户信息ListUser.
     *
     * @param deptId 部门Id
     * @return 统计数据
     */
    public List<String> getUserListByDeptId(long deptId);

    /**
     * 根据单位ID获取部门下用户信息ListUser.
     *
     * @param deptIds 部门Ids
     * @return 统计数据
     */
    public List<String> getUserListByDeptIds(String deptIds);

}
