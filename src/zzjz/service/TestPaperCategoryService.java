package zzjz.service;

import zzjz.bean.TestPaperCategory;
import java.util.List;

/**
 * @ClassName: TestPaperCategoryService
 * @Description: 考试类型操作service接口定义
 * @author guitang.fang
 * @version 2016/5/31 13:52
 */
public interface TestPaperCategoryService {

    /**
     * 根据考试类别实体类的parentId,categoryName获取考试类别信息.
     * @param testPaperCategory 考试类别实体类
     * @return 考试类别
     */
    TestPaperCategory getTestPaperCategory(TestPaperCategory testPaperCategory);

    /**
     * 添加考试类别信息.
     * @param testPaperCategory 考试类别实体类
     * @return 添加结果
     */
    boolean addTestPaperCategory(TestPaperCategory testPaperCategory);

    /**
     * 获取所有考试类别信息.
     * @return 考试类别集合
     */
    List<TestPaperCategory> getTestPaperCategoryList();

    /**
     * 根据类别id获取此类别的所有子类别.
     * @param categoryId 考试类别id
     * @return 考试类别集合
     */
    List<TestPaperCategory> getChildTestPaperCategoryById(long categoryId);

    /**
     * 根据类别id删除该考试类别.
     * @param categoryId 考试类别id
     * @return 删除结果
     */
    boolean deleteTestPaperCategory(long categoryId);

    /**
     * 修改考试类别信息.
     * @param testPaperCategory 考试类别实体类
     * @return 修改结果
     */
    boolean updateTestPaperCategory(TestPaperCategory testPaperCategory);

    /**
     * 根据parentId获取父考试类别.
     * @param parentId 考试类别parentId
     * @return 考试类别
     */
    TestPaperCategory getFatherTestPaperCategory(long parentId);
}
