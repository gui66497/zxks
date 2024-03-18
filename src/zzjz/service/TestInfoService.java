package zzjz.service;

import zzjz.bean.PagingEntity;
import zzjz.bean.TestInfo;

import java.util.List;

/**
 * @ClassName: TestInfoService
 * @Description: 考试信息接口
 * @author zhuxiaoxiao
 * @version 2016/5/30 15:15
 */
public interface TestInfoService {

    /**
     * 获取考试信息.
     * @param userId 用户ID
     * @param pagingEntity 分页排序
     * @return 查询结果
     */
    public List<TestInfo> getTestInfo(String userId, PagingEntity pagingEntity);

    /**
     * 获取检索考试信息.
     * @param testInfo 试卷实体
     * @param pagingEntity 分页排序
     * @return 查询结果
     */
    public List<TestInfo> getFilteredTestInfo(TestInfo testInfo, PagingEntity pagingEntity);

    /**
     * 获取考试信息.
     * @param id 考试ID
     * @return 查询结果
     */
    public List<TestInfo> getTestInfoById(long id);

    /**
     * 删除考试信息.
     * @param id 考试ID
     * @return true or false
     */
    public boolean deleteTestInfoById(long id);

    /**
     *  根据试卷获取考试信息.
     * @param testInfo 考试实体
     * @return 查询结果
     */
    public TestInfo getTestInfoByTestIdAndTime(TestInfo testInfo);

    /**
     * 获取检索考试信息条数.
     * @param testInfo 试卷实体
     * @param pagingEntity 分页排序
     * @return int类型的值
     */
    public int getFilteredTestInfoCount(TestInfo testInfo, PagingEntity pagingEntity);

    /**
     * 添加考试信息.
     * @param testInfo 试卷实体
     * @return true or false
     */
    public boolean addTestInfo(TestInfo testInfo);

    /**
     * 添加考试信息.
     * @param testInfo 试卷实体
     * @return true or false
     */
    public boolean updateTestInfo(TestInfo testInfo);

    /**
     * 删除考试信息与用户关联信息.
     * @param testInfo 试卷实体
     * @return true or false
     */
    public boolean deleteTestInfoAndUserConnection(TestInfo testInfo);

    /**
     * 添加考试信息与用户关联信息.
     * @param testInfo 试卷实体
     * @return true or false
     */
    public boolean addTestInfoAndUserConnection(TestInfo testInfo);

    /**
     * 获取成绩信息.
     * @param userId 用户ID
     * @param pagingEntity 分页排序
     * @return 查询结果
     */
    public List<TestInfo> getPointInfo(String userId, PagingEntity pagingEntity);

    /**
     * 获取考试信息条数.
     * @param userId 用户ID
     * @param pagingEntity 分页排序
     * @return int类型的值
     */
    public int getTestInfoCount(String userId, PagingEntity pagingEntity);

    /**
     * 获取成绩信息条数.
     * @param userId 用户ID
     * @param pagingEntity 分页排序
     * @return int类型的值
     */


    public int getPointInfoCount(String userId, PagingEntity pagingEntity);

    /**
     * 获取用户考试成绩.
     * @param testInfoId 考试信息Id
     * @param userId 用户Id
     * @return true or false
     */
    public boolean userAchievement(String testInfoId, String userId);

    /**
     * 判断删除考试信息.
     * @param id 考试ID
     * @return  true or false
     */
    public boolean confirmActionById(long id);

    /**
     * 根据试卷id获取所有状态不是未开考的考试.
     * @param testPaperId 试卷id
     * @return 查询结果
     */
    List<TestInfo> getTestedInfoByPaperId(long testPaperId);

    /**
     * 获取参考人数名称.
     * @param testInfoId 考试信息ID
     * @return 查询结果
     */
    List<TestInfo> getTestpeopleNames(String testInfoId);

}
