package zzjz.service;

import org.springframework.dao.DataAccessException;
import zzjz.bean.PagingEntity;
import zzjz.bean.TestPaper;

import java.util.List;

/**
 * @author guitang.fang
 * @version 2016/6/1 9:52
 * @ClassName: TestPaperService
 * @Description: 试卷操作service接口定义
 */
public interface TestPaperService {

    /**
     * 根据试卷实体类的testpaperName,categoryId获取试卷信息.
     *
     * @param testPaper 试卷实体类
     * @return 试卷实体
     */
    TestPaper getTestPaper(TestPaper testPaper);

    /**
     * 根据试卷实体类添加试卷信息.
     *
     * @param testPaper 试卷实体类
     * @return 添加结果
     */
    boolean addTestPaper(TestPaper testPaper);

    /**
     * 根据试卷实体类获取试卷信息.
     *
     * @param categoryId   考试分类categoryId
     * @param pagingEntity 分页实体
     * @return 试卷实体集合
     */
    List<TestPaper> getTestPaperListByCategoryId(long categoryId, PagingEntity pagingEntity);

    /**
     * 获取试卷总数.
     *
     * @param categoryId   分类ID
     * @param pagingEntity 分页实体
     * @return 总数
     */
    int getTestpaperCountByCategory(long categoryId, PagingEntity pagingEntity);

    /**
     * 根据试卷id删除试卷信息.
     *
     * @param id 试卷Id
     * @return 删除结果
     */
    boolean deleteTestPaperById(long id);

    /**
     * 根据试卷name获取试卷.
     *
     * @param testpaperName 试卷name
     * @param categoryId 分类id
     * @return 试卷实体类
     */
    TestPaper getTestPaperByNameAndId(String testpaperName, String categoryId);

    /**
     * 根据试卷name获取试卷.
     *
     * @param testpaperName 试卷name
     * @return 试卷实体类
     */
    TestPaper getTestPaperByName(String testpaperName);

    /**
     * 修改试卷信息.
     *
     * @param testPaper 试卷实体类
     * @return 修改结果
     */
    boolean updateTestPaper(TestPaper testPaper);

    /**
     * 根据id获取试卷信息.
     *
     * @param id 试卷id
     * @return 试卷实体
     */
    TestPaper getTestPaperById(long id);

    /**
     * 根据id集合删除试卷信息.
     *
     * @param ids 试卷id集合
     * @return 删除结果
     */
    boolean batchDeleteTestPaperById(List<Long> ids);

    /**
     * 创建试卷.
     *
     * @param testPaper 试卷实体 包含模块实体
     * @return true or false
     * @throws DataAccessException 异常
     */
    boolean createTestpaper(TestPaper testPaper) throws DataAccessException;

    /**
     * 修改试卷.
     *
     * @param testPaper 试卷实体 包含part信息
     * @return true or false
     */
    boolean updateTestpaperAndPart(TestPaper testPaper);

    /**
     * 根据考试类别查询所有试卷.
     *
     * @param categoryId 试卷分类id
     * @return 查询结果
     */
    List<TestPaper> getTestPaperByCategoryId(long categoryId);

    /**
     * 根据题目id获取关联的试卷.
     * @param itemId 题目id
     * @return 试卷集合
     */
    List<TestPaper> gettestpaperByItemId(Long itemId);
}
