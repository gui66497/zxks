package zzjz.service;

import org.springframework.dao.DataAccessException;
import zzjz.bean.TestInfo;
import zzjz.bean.TestResultEntity;
import zzjz.bean.PagingEntity;
import zzjz.bean.UserScoreEntity;
import java.util.List;

/**
 * @author 梅宏振
 * @version 2015年6月16日 下午7:32:29
 * @ClassName: TestResultService
 * @Description: 考试成绩管理接口
 */
public interface TestResultService {

    /**
     * 获取考试成绩列表.
     *
     * @param pagingEntity 分页实体类
     * @param testInfo     考试信息实体
     * @return 查询结果
     * @throws DataAccessException 异常
     */
    List<TestResultEntity> getTestReusltList(TestInfo testInfo,
                                             PagingEntity pagingEntity) throws DataAccessException;

    /**
     * 获取考试成绩总数.
     *
     * @param testInfo     考试信息实体
     * @param pagingEntity 分页实体类
     * @return int类型的值
     * @throws DataAccessException 异常
     */
    int getTestReusltCount(TestInfo testInfo, PagingEntity pagingEntity) throws DataAccessException;

    /**
     * 获取指定某次考试的学生成绩列表.
     *
     * @param id           Id
     * @param pagingEntity 分页实体类
     * @return 查询结果
     * @throws DataAccessException 异常
     */
    List<UserScoreEntity> getTestUserScoreList(long id,
                                               PagingEntity pagingEntity) throws DataAccessException;

    /**
     * 获取指定某次考试的学生成绩总数.
     *
     * @param id           Id
     * @param pagingEntity 分页实体类
     * @return int类型的值
     * @throws DataAccessException 异常
     */
    int getTestUserScoreCount(long id, PagingEntity pagingEntity) throws DataAccessException;

    /**
     * 更新指定考试的参考人数+1.
     *
     * @param id Id
     * @return true or false
     */
    boolean updateTestUserNum(long id);

}
